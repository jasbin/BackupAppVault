package com.app.backup.data.database.entity;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "backup_records")
public class BackupRecord {

    public static final String STATUS_COMPLETED = "completed";
    public static final String STATUS_FAILED = "failed";
    public static final String STATUS_IN_PROGRESS = "in_progress";
    public static final String STATUS_PENDING = "pending";

    public static final String TYPE_LOCAL = "local";
    public static final String TYPE_DRIVE = "drive";

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "backup_id")
    private String backupId;

    @ColumnInfo(name = "created_at")
    private long createdAt;

    @ColumnInfo(name = "backup_date_display")
    private String backupDateDisplay;

    @ColumnInfo(name = "contacts_count")
    private int contactsCount;

    @ColumnInfo(name = "sms_count")
    private int smsCount;

    @ColumnInfo(name = "file_path")
    private String filePath;

    @ColumnInfo(name = "file_size_bytes")
    private long fileSizeBytes;

    @ColumnInfo(name = "status")
    private String status;

    @ColumnInfo(name = "backup_type")
    private String backupType; // local or drive

    @ColumnInfo(name = "drive_file_id")
    private String driveFileId;

    @ColumnInfo(name = "drive_file_name")
    private String driveFileName;

    @ColumnInfo(name = "error_message")
    private String errorMessage;

    @ColumnInfo(name = "device_name")
    private String deviceName;

    @ColumnInfo(name = "app_version")
    private String appVersion;

    @ColumnInfo(name = "checksum")
    private String checksum;

    @ColumnInfo(name = "is_encrypted")
    private boolean isEncrypted;

    @ColumnInfo(name = "includes_contacts")
    private boolean includesContacts;

    @ColumnInfo(name = "includes_sms")
    private boolean includesSms;

    // Constructor
    public BackupRecord(@NonNull String backupId) {
        this.backupId = backupId;
        this.createdAt = System.currentTimeMillis();
    }

    // Getters and Setters
    @NonNull
    public String getBackupId() { return backupId; }
    public void setBackupId(@NonNull String backupId) { this.backupId = backupId; }

    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }

    public String getBackupDateDisplay() { return backupDateDisplay; }
    public void setBackupDateDisplay(String backupDateDisplay) { this.backupDateDisplay = backupDateDisplay; }

    public int getContactsCount() { return contactsCount; }
    public void setContactsCount(int contactsCount) { this.contactsCount = contactsCount; }

    public int getSmsCount() { return smsCount; }
    public void setSmsCount(int smsCount) { this.smsCount = smsCount; }

    public String getFilePath() { return filePath; }
    public void setFilePath(String filePath) { this.filePath = filePath; }

    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getBackupType() { return backupType; }
    public void setBackupType(String backupType) { this.backupType = backupType; }

    public String getDriveFileId() { return driveFileId; }
    public void setDriveFileId(String driveFileId) { this.driveFileId = driveFileId; }

    public String getDriveFileName() { return driveFileName; }
    public void setDriveFileName(String driveFileName) { this.driveFileName = driveFileName; }

    public String getErrorMessage() { return errorMessage; }
    public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getAppVersion() { return appVersion; }
    public void setAppVersion(String appVersion) { this.appVersion = appVersion; }

    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    public boolean isEncrypted() { return isEncrypted; }
    public void setEncrypted(boolean encrypted) { isEncrypted = encrypted; }

    public boolean isIncludesContacts() { return includesContacts; }
    public void setIncludesContacts(boolean includesContacts) { this.includesContacts = includesContacts; }

    public boolean isIncludesSms() { return includesSms; }
    public void setIncludesSms(boolean includesSms) { this.includesSms = includesSms; }
}
