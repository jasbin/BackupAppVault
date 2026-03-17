package com.app.backup.data.drive;

import android.content.Context;
import android.util.Log;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.database.entity.BackupRecord;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.http.FileContent;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class DriveService {

    private static final String TAG = "DriveService";
    private static final String APP_NAME = "BackupVault";
    private static final String MIME_FOLDER = "application/vnd.google-apps.folder";
    private static final String MIME_OCTET = "application/octet-stream";
    private static final String FOLDER_NAME = "BackupVault";

    private final Context context;
    private final AppDatabase database;
    private Drive driveService;
    private GoogleAccountCredential credential;

    @Inject
    public DriveService(Context context, AppDatabase database) {
        this.context = context.getApplicationContext();
        this.database = database;
    }

    /**
     * Initialize Drive service with account
     */
    public void initWithAccount(String accountName) {
        credential = GoogleAccountCredential.usingOAuth2(
                context, Collections.singletonList(DriveScopes.DRIVE_FILE));
        credential.setSelectedAccountName(accountName);

        driveService = new Drive.Builder(
                new NetHttpTransport(),
                GsonFactory.getDefaultInstance(),
                credential)
                .setApplicationName(APP_NAME)
                .build();
    }

    public boolean isInitialized() {
        return driveService != null;
    }

    /**
     * Auto-initialize from saved Google account if not already initialized.
     * Call this before any Drive operation to handle app restart scenarios.
     */
    public void ensureInitialized() {
        if (driveService != null) return;
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(context);
        if (account != null && account.getEmail() != null) {
            initWithAccount(account.getEmail());
        }
    }

    /**
     * Upload a backup file to Google Drive app folder
     */
    public Observable<UploadProgress> uploadBackup(java.io.File localFile, String backupId) {
        return Observable.<UploadProgress>create(emitter -> {
            ensureInitialized();
            if (!isInitialized()) {
                emitter.onError(new IllegalStateException("Drive service not initialized"));
                return;
            }
            try {
                emitter.onNext(new UploadProgress("Preparing upload...", 10));

                // Ensure app folder exists
                String folderId = getOrCreateAppFolder();
                emitter.onNext(new UploadProgress("Uploading to Drive...", 30));

                // Build file metadata
                File fileMetadata = new File();
                fileMetadata.setName(localFile.getName());
                fileMetadata.setParents(Collections.singletonList(folderId));

                // Content
                FileContent content = new FileContent(MIME_OCTET, localFile);

                // Upload
                File uploadedFile = driveService.files()
                        .create(fileMetadata, content)
                        .setFields("id, name, size, createdTime")
                        .execute();

                emitter.onNext(new UploadProgress("Updating records...", 90));

                // Update local record with Drive file ID
                BackupRecord record = database.backupDao().getBackupById(backupId);
                if (record != null) {
                    record.setDriveFileId(uploadedFile.getId());
                    record.setDriveFileName(uploadedFile.getName());
                    record.setBackupType(BackupRecord.TYPE_DRIVE);
                    database.backupDao().updateBackup(record);
                }

                emitter.onNext(new UploadProgress("Upload complete!", 100));
                emitter.onComplete();

            } catch (Exception e) {
                Log.e(TAG, "Upload failed", e);
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * List all backup files on Drive
     */
    public Single<List<DriveBackupFile>> listBackups() {
        return Single.<List<DriveBackupFile>>fromCallable(() -> {
            ensureInitialized();
            if (!isInitialized()) return new ArrayList<>();

            String folderId = getOrCreateAppFolder();
            List<DriveBackupFile> backups = new ArrayList<>();

            FileList result = driveService.files()
                    .list()
                    .setQ("'" + folderId + "' in parents and trashed = false")
                    .setFields("files(id, name, size, createdTime, modifiedTime)")
                    .setOrderBy("createdTime desc")
                    .execute();

            if (result.getFiles() != null) {
                for (File file : result.getFiles()) {
                    DriveBackupFile driveFile = new DriveBackupFile();
                    driveFile.fileId = file.getId();
                    driveFile.fileName = file.getName();
                    driveFile.fileSizeBytes = file.getSize() != null ? file.getSize() : 0;
                    driveFile.createdTime = file.getCreatedTime() != null
                            ? file.getCreatedTime().getValue() : 0;
                    backups.add(driveFile);
                }
            }
            return backups;
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Download a backup file from Drive
     */
    public Observable<DownloadProgress> downloadBackup(String fileId, java.io.File outputFile) {
        return Observable.<DownloadProgress>create(emitter -> {
            ensureInitialized();
            if (!isInitialized()) {
                emitter.onError(new IllegalStateException("Drive service not initialized"));
                return;
            }
            try {
                emitter.onNext(new DownloadProgress("Connecting to Drive...", 10));

                // Get file size for progress
                File fileMeta = driveService.files().get(fileId).setFields("size").execute();
                long totalSize = fileMeta.getSize() != null ? fileMeta.getSize() : 0;

                emitter.onNext(new DownloadProgress("Downloading backup...", 20));

                try (InputStream inputStream = driveService.files()
                        .get(fileId).executeMediaAsInputStream();
                     OutputStream outputStream = new FileOutputStream(outputFile)) {

                    byte[] buffer = new byte[8192];
                    long downloaded = 0;
                    int bytesRead;

                    while ((bytesRead = inputStream.read(buffer)) != -1) {
                        outputStream.write(buffer, 0, bytesRead);
                        downloaded += bytesRead;
                        if (totalSize > 0) {
                            int progress = (int) (20 + (downloaded * 70.0 / totalSize));
                            emitter.onNext(new DownloadProgress("Downloading...", Math.min(progress, 90)));
                        }
                    }
                }

                emitter.onNext(new DownloadProgress("Download complete!", 100));
                emitter.onComplete();

            } catch (Exception e) {
                Log.e(TAG, "Download failed", e);
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Delete a backup file from Drive
     */
    public Completable deleteBackup(String fileId) {
        return Completable.fromAction(() -> {
            ensureInitialized();
            if (!isInitialized()) throw new IllegalStateException("Drive service not initialized");
            driveService.files().delete(fileId).execute();
        }).subscribeOn(Schedulers.io());
    }

    private String getOrCreateAppFolder() throws IOException {
        // Search for existing folder
        FileList result = driveService.files().list()
                .setQ("name = '" + FOLDER_NAME + "' and mimeType = '" + MIME_FOLDER
                        + "' and trashed = false")
                .setFields("files(id)")
                .execute();

        if (result.getFiles() != null && !result.getFiles().isEmpty()) {
            return result.getFiles().get(0).getId();
        }

        // Create folder
        File folderMetadata = new File();
        folderMetadata.setName(FOLDER_NAME);
        folderMetadata.setMimeType(MIME_FOLDER);

        File folder = driveService.files().create(folderMetadata)
                .setFields("id")
                .execute();
        return folder.getId();
    }

    public void signOut() {
        driveService = null;
        credential = null;
    }

    // Data classes
    public static class DriveBackupFile {
        public String fileId;
        public String fileName;
        public long fileSizeBytes;
        public long createdTime;
    }

    public static class UploadProgress {
        public final String message;
        public final int progress;

        public UploadProgress(String message, int progress) {
            this.message = message;
            this.progress = progress;
        }
    }

    public static class DownloadProgress {
        public final String message;
        public final int progress;

        public DownloadProgress(String message, int progress) {
            this.message = message;
            this.progress = progress;
        }
    }
}