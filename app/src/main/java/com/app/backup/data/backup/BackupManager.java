package com.app.backup.data.backup;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import com.app.backup.BuildConfig;
import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.database.entity.BackupRecord;
import com.app.backup.data.model.BackupMetadata;
import com.app.backup.data.model.ContactModel;
import com.app.backup.data.model.SmsModel;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.utils.security.EncryptionUtil;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class BackupManager {

    private static final String TAG = "BackupManager";
    private static final String CONTACTS_FILE = "contacts.json";
    private static final String SMS_FILE = "sms.json";
    private static final String METADATA_FILE = "metadata.json";
    private static final String BACKUP_DIR = "backups";

    private final Context context;
    private final ContactsRepository contactsRepository;
    private final SmsRepository smsRepository;
    private final EncryptionUtil encryptionUtil;
    private final AppDatabase database;
    private final Gson gson;

    @Inject
    public BackupManager(Context context,
                         ContactsRepository contactsRepository,
                         SmsRepository smsRepository,
                         EncryptionUtil encryptionUtil,
                         AppDatabase database) {
        this.context = context.getApplicationContext();
        this.contactsRepository = contactsRepository;
        this.smsRepository = smsRepository;
        this.encryptionUtil = encryptionUtil;
        this.database = database;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
    }

    /**
     * Perform a full backup (contacts + SMS)
     */
    public Observable<BackupResult> createBackup(BackupOptions options) {
        return Observable.<BackupResult>create(emitter -> {
            String backupId = UUID.randomUUID().toString();
            BackupRecord record = new BackupRecord(backupId);
            record.setStatus(BackupRecord.STATUS_IN_PROGRESS);
            record.setBackupType(BackupRecord.TYPE_LOCAL);
            record.setIncludesContacts(options.includeContacts);
            record.setIncludesSms(options.includeSms);
            record.setEncrypted(options.encrypt);
            database.backupDao().insertBackup(record);

            try {
                emitter.onNext(new BackupResult(backupId, BackupResult.STAGE_READING_CONTACTS, 0, null));

                List<ContactModel> contacts = null;
                List<SmsModel> smsList = null;

                if (options.includeContacts) {
                    contacts = contactsRepository.readContacts().blockingFirst();
                    emitter.onNext(new BackupResult(backupId, BackupResult.STAGE_READING_SMS, 25, null));
                }

                if (options.includeSms) {
                    smsList = smsRepository.readSmsMessages().blockingFirst();
                    emitter.onNext(new BackupResult(backupId, BackupResult.STAGE_PACKAGING, 50, null));
                }

                // Create temp directory
                File tempDir = createTempDir(backupId);
                File contactsFile = null;
                File smsFile = null;

                int contactCount = 0;
                int smsCount = 0;

                // Write contacts JSON
                if (contacts != null) {
                    contactsFile = new File(tempDir, CONTACTS_FILE);
                    writeJson(contactsFile, contacts);
                    contactCount = contacts.size();
                }

                // Write SMS JSON
                if (smsList != null) {
                    smsFile = new File(tempDir, SMS_FILE);
                    writeJson(smsFile, smsList);
                    smsCount = smsList.size();
                }

                // Build and write metadata
                BackupMetadata metadata = buildMetadata(backupId, contactCount, smsCount);
                File metadataFile = new File(tempDir, METADATA_FILE);
                writeJson(metadataFile, metadata);

                emitter.onNext(new BackupResult(backupId, BackupResult.STAGE_COMPRESSING, 70, null));

                // Zip everything
                File zipFile = createZipFile(backupId, tempDir, contactsFile, smsFile, metadataFile);

                // Encrypt if required
                File finalFile;
                if (options.encrypt) {
                    finalFile = encryptBackup(zipFile, backupId);
                    zipFile.delete();
                } else {
                    finalFile = zipFile;
                }

                emitter.onNext(new BackupResult(backupId, BackupResult.STAGE_FINALIZING, 90, null));

                // Compute checksum
                String checksum = encryptionUtil.computeChecksum(finalFile);

                // Update record
                record.setStatus(BackupRecord.STATUS_COMPLETED);
                record.setFilePath(finalFile.getAbsolutePath());
                record.setFileSizeBytes(finalFile.length());
                record.setContactsCount(contactCount);
                record.setSmsCount(smsCount);
                record.setChecksum(checksum);
                record.setDeviceName(Build.MODEL);
                record.setAppVersion(BuildConfig.VERSION_NAME);
                record.setBackupDateDisplay(new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(new Date()));
                database.backupDao().updateBackup(record);

                // Cleanup temp
                cleanupTempDir(tempDir);

                emitter.onNext(new BackupResult(backupId, BackupResult.STAGE_DONE, 100, finalFile.getAbsolutePath()));
                emitter.onComplete();

            } catch (Exception e) {
                Log.e(TAG, "Backup failed", e);
                record.setStatus(BackupRecord.STATUS_FAILED);
                record.setErrorMessage(e.getMessage());
                database.backupDao().updateBackup(record);
                emitter.onError(e);
            }
        }).subscribeOn(Schedulers.io());
    }

    private File createTempDir(String backupId) {
        File tempDir = new File(context.getCacheDir(), "backup_temp_" + backupId);
        tempDir.mkdirs();
        return tempDir;
    }

    private void writeJson(File file, Object data) throws IOException {
        try (FileWriter writer = new FileWriter(file)) {
            gson.toJson(data, writer);
        }
    }

    private BackupMetadata buildMetadata(String backupId, int contactCount, int smsCount) {
        BackupMetadata metadata = new BackupMetadata();
        metadata.setBackupId(backupId);
        metadata.setBackupDate(System.currentTimeMillis());
        metadata.setDeviceName(Build.DEVICE);
        metadata.setDeviceModel(Build.MODEL);
        metadata.setAndroidVersion(Build.VERSION.RELEASE);
        metadata.setContactsCount(contactCount);
        metadata.setSmsCount(smsCount);
        metadata.setAppVersion(BuildConfig.VERSION_NAME);
        metadata.setEncrypted(true);
        metadata.setCompressionLevel(6);
        return metadata;
    }

    private File createZipFile(String backupId, File tempDir,
                               File contactsFile, File smsFile, File metadataFile) throws IOException {
        File backupsDir = getBackupsDirectory();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File zipFile = new File(backupsDir, "backup_" + timestamp + "_" + backupId.substring(0, 8) + ".zip");

        try (ZipOutputStream zos = new ZipOutputStream(new FileOutputStream(zipFile))) {
            zos.setLevel(6); // Compression level 6 (balanced)

            if (contactsFile != null && contactsFile.exists()) {
                addToZip(zos, contactsFile, CONTACTS_FILE);
            }
            if (smsFile != null && smsFile.exists()) {
                addToZip(zos, smsFile, SMS_FILE);
            }
            if (metadataFile != null && metadataFile.exists()) {
                addToZip(zos, metadataFile, METADATA_FILE);
            }
        }
        return zipFile;
    }

    private void addToZip(ZipOutputStream zos, File file, String entryName) throws IOException {
        ZipEntry entry = new ZipEntry(entryName);
        zos.putNextEntry(entry);
        java.nio.file.Files.copy(file.toPath(), zos);
        zos.closeEntry();
    }

    private File encryptBackup(File zipFile, String backupId) throws Exception {
        File backupsDir = getBackupsDirectory();
        String timestamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        File encryptedFile = new File(backupsDir, "backup_" + timestamp + "_" + backupId.substring(0, 8) + ".bvault");
        encryptionUtil.encryptFile(zipFile, encryptedFile);
        return encryptedFile;
    }

    public File getBackupsDirectory() {
        File dir = new File(context.getFilesDir(), BACKUP_DIR);
        if (!dir.exists()) dir.mkdirs();
        return dir;
    }

    private void cleanupTempDir(File dir) {
        if (dir != null && dir.exists()) {
            for (File f : dir.listFiles() != null ? dir.listFiles() : new File[0]) {
                f.delete();
            }
            dir.delete();
        }
    }

    // Options class
    public static class BackupOptions {
        public boolean includeContacts = true;
        public boolean includeSms = true;
        public boolean encrypt = true;
        public boolean uploadToDrive = false;

        public static BackupOptions fullBackup() {
            BackupOptions opts = new BackupOptions();
            opts.includeContacts = true;
            opts.includeSms = true;
            opts.encrypt = true;
            return opts;
        }

        public static BackupOptions contactsOnly() {
            BackupOptions opts = new BackupOptions();
            opts.includeContacts = true;
            opts.includeSms = false;
            opts.encrypt = true;
            return opts;
        }

        public static BackupOptions smsOnly() {
            BackupOptions opts = new BackupOptions();
            opts.includeContacts = false;
            opts.includeSms = true;
            opts.encrypt = true;
            return opts;
        }
    }

    // Progress result class
    public static class BackupResult {
        public static final String STAGE_READING_CONTACTS = "reading_contacts";
        public static final String STAGE_READING_SMS = "reading_sms";
        public static final String STAGE_PACKAGING = "packaging";
        public static final String STAGE_COMPRESSING = "compressing";
        public static final String STAGE_FINALIZING = "finalizing";
        public static final String STAGE_DONE = "done";

        public final String backupId;
        public final String stage;
        public final int progress;
        public final String filePath;

        public BackupResult(String backupId, String stage, int progress, String filePath) {
            this.backupId = backupId;
            this.stage = stage;
            this.progress = progress;
            this.filePath = filePath;
        }

        public boolean isDone() {
            return STAGE_DONE.equals(stage);
        }
    }
}