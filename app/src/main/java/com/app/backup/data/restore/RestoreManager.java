package com.app.backup.data.restore;

import android.util.Log;

import com.app.backup.data.model.BackupMetadata;
import com.app.backup.data.model.ContactModel;
import com.app.backup.data.model.SmsModel;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.utils.security.EncryptionUtil;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class RestoreManager {

    private static final String TAG = "RestoreManager";
    private static final String CONTACTS_FILE = "contacts.json";
    private static final String SMS_FILE = "sms.json";
    private static final String METADATA_FILE = "metadata.json";

    private final ContactsRepository contactsRepository;
    private final SmsRepository smsRepository;
    private final EncryptionUtil encryptionUtil;
    private final Gson gson;

    @Inject
    public RestoreManager(ContactsRepository contactsRepository,
                          SmsRepository smsRepository,
                          EncryptionUtil encryptionUtil) {
        this.contactsRepository = contactsRepository;
        this.smsRepository = smsRepository;
        this.encryptionUtil = encryptionUtil;
        this.gson = new Gson();
    }

    /**
     * Preview backup content without restoring
     */
    public Observable<BackupPreview> previewBackup(File backupFile) {
        return Observable.<BackupPreview>fromCallable(() -> {
            File zipFile = prepareZipFile(backupFile);
            try {
                return readBackupPreview(zipFile);
            } finally {
                if (!zipFile.equals(backupFile)) {
                    zipFile.delete(); // clean up temp decrypted file
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Restore contacts and/or SMS from a backup file
     */
    public Observable<RestoreProgress> restoreBackup(File backupFile, RestoreOptions options) {
        return Observable.<RestoreProgress>create(emitter -> {
            File zipFile = null;
            try {
                emitter.onNext(new RestoreProgress("Preparing backup...", 5));
                zipFile = prepareZipFile(backupFile);

                List<ContactModel> contacts = null;
                List<SmsModel> smsList = null;

                // Read contacts from zip
                if (options.restoreContacts) {
                    emitter.onNext(new RestoreProgress("Reading contacts...", 20));
                    contacts = readContactsFromZip(zipFile);
                }

                // Read SMS from zip
                if (options.restoreSms) {
                    emitter.onNext(new RestoreProgress("Reading SMS messages...", 35));
                    smsList = readSmsFromZip(zipFile);
                }

                // Restore contacts
                if (contacts != null && !contacts.isEmpty()) {
                    emitter.onNext(new RestoreProgress("Restoring contacts...", 50));
                    ContactsRepository.RestoreResult result = contactsRepository
                            .restoreContacts(contacts, options.skipDuplicates)
                            .blockingFirst();
                    emitter.onNext(new RestoreProgress(
                            "Contacts: " + result.restored + " restored, " + result.skipped + " skipped",
                            70));
                }

                // Restore SMS
                if (smsList != null && !smsList.isEmpty()) {
                    emitter.onNext(new RestoreProgress("Restoring SMS messages...", 80));
                    ContactsRepository.RestoreResult result = smsRepository
                            .restoreSms(smsList, options.skipDuplicates)
                            .blockingFirst();
                    emitter.onNext(new RestoreProgress(
                            "SMS: " + result.restored + " restored, " + result.skipped + " skipped",
                            95));
                }

                emitter.onNext(new RestoreProgress("Restore complete!", 100));
                emitter.onComplete();

            } catch (Exception e) {
                Log.e(TAG, "Restore failed", e);
                emitter.onError(e);
            } finally {
                if (zipFile != null && !zipFile.equals(backupFile)) {
                    zipFile.delete();
                }
            }
        }).subscribeOn(Schedulers.io());
    }

    /**
     * Decrypt .bvault file if needed, return the zip file
     */
    private File prepareZipFile(File backupFile) throws Exception {
        if (backupFile.getName().endsWith(".bvault")) {
            File tempZip = new File(backupFile.getParent(),
                    backupFile.getName().replace(".bvault", "_temp.zip"));
            boolean success = encryptionUtil.decryptFile(backupFile, tempZip);
            if (!success) throw new Exception("Failed to decrypt backup file");
            return tempZip;
        }
        return backupFile;
    }

    private BackupPreview readBackupPreview(File zipFile) throws IOException {
        BackupPreview preview = new BackupPreview();
        try (ZipFile zip = new ZipFile(zipFile)) {
            // Read metadata
            ZipEntry metaEntry = zip.getEntry(METADATA_FILE);
            if (metaEntry != null) {
                String json = readZipEntry(zip, metaEntry);
                preview.metadata = gson.fromJson(json, BackupMetadata.class);
            }

            // Preview contacts (first 10)
            ZipEntry contactsEntry = zip.getEntry(CONTACTS_FILE);
            if (contactsEntry != null) {
                String json = readZipEntry(zip, contactsEntry);
                Type listType = new TypeToken<List<ContactModel>>(){}.getType();
                List<ContactModel> all = gson.fromJson(json, listType);
                preview.totalContacts = all != null ? all.size() : 0;
                preview.contactsPreview = all != null ? all.subList(0, Math.min(10, all.size())) : new ArrayList<>();
            }

            // Preview SMS (first 10)
            ZipEntry smsEntry = zip.getEntry(SMS_FILE);
            if (smsEntry != null) {
                String json = readZipEntry(zip, smsEntry);
                Type listType = new TypeToken<List<SmsModel>>(){}.getType();
                List<SmsModel> all = gson.fromJson(json, listType);
                preview.totalSms = all != null ? all.size() : 0;
                preview.smsPreview = all != null ? all.subList(0, Math.min(10, all.size())) : new ArrayList<>();
            }
        }
        return preview;
    }

    private List<ContactModel> readContactsFromZip(File zipFile) throws IOException {
        try (ZipFile zip = new ZipFile(zipFile)) {
            ZipEntry entry = zip.getEntry(CONTACTS_FILE);
            if (entry == null) return new ArrayList<>();
            String json = readZipEntry(zip, entry);
            Type listType = new TypeToken<List<ContactModel>>(){}.getType();
            List<ContactModel> result = gson.fromJson(json, listType);
            return result != null ? result : new ArrayList<>();
        }
    }

    private List<SmsModel> readSmsFromZip(File zipFile) throws IOException {
        try (ZipFile zip = new ZipFile(zipFile)) {
            ZipEntry entry = zip.getEntry(SMS_FILE);
            if (entry == null) return new ArrayList<>();
            String json = readZipEntry(zip, entry);
            Type listType = new TypeToken<List<SmsModel>>(){}.getType();
            List<SmsModel> result = gson.fromJson(json, listType);
            return result != null ? result : new ArrayList<>();
        }
    }

    private String readZipEntry(ZipFile zipFile, ZipEntry entry) throws IOException {
        try (InputStream is = zipFile.getInputStream(entry);
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line).append('\n');
            }
            return sb.toString();
        }
    }

    // Data classes
    public static class BackupPreview {
        public BackupMetadata metadata;
        public List<ContactModel> contactsPreview = new ArrayList<>();
        public List<SmsModel> smsPreview = new ArrayList<>();
        public int totalContacts;
        public int totalSms;
    }

    public static class RestoreOptions {
        public boolean restoreContacts = true;
        public boolean restoreSms = true;
        public boolean skipDuplicates = true;

        public static RestoreOptions all() {
            RestoreOptions opts = new RestoreOptions();
            opts.restoreContacts = true;
            opts.restoreSms = true;
            return opts;
        }

        public static RestoreOptions contactsOnly() {
            RestoreOptions opts = new RestoreOptions();
            opts.restoreContacts = true;
            opts.restoreSms = false;
            return opts;
        }

        public static RestoreOptions smsOnly() {
            RestoreOptions opts = new RestoreOptions();
            opts.restoreContacts = false;
            opts.restoreSms = true;
            return opts;
        }
    }

    public static class RestoreProgress {
        public final String message;
        public final int progress;

        public RestoreProgress(String message, int progress) {
            this.message = message;
            this.progress = progress;
        }
    }
}