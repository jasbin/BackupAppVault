package com.app.backup.data.model;

import com.google.gson.annotations.SerializedName;

public class BackupMetadata {

    @SerializedName("backup_date")
    private long backupDate;

    @SerializedName("device_name")
    private String deviceName;

    @SerializedName("device_model")
    private String deviceModel;

    @SerializedName("android_version")
    private String androidVersion;

    @SerializedName("contacts_count")
    private int contactsCount;

    @SerializedName("sms_count")
    private int smsCount;

    @SerializedName("app_version")
    private String appVersion;

    @SerializedName("backup_id")
    private String backupId;

    @SerializedName("file_size_bytes")
    private long fileSizeBytes;

    @SerializedName("checksum")
    private String checksum;

    @SerializedName("encrypted")
    private boolean encrypted;

    @SerializedName("compression_level")
    private int compressionLevel;

    // Constructors
    public BackupMetadata() {}

    // Getters and Setters
    public long getBackupDate() { return backupDate; }
    public void setBackupDate(long backupDate) { this.backupDate = backupDate; }

    public String getDeviceName() { return deviceName; }
    public void setDeviceName(String deviceName) { this.deviceName = deviceName; }

    public String getDeviceModel() { return deviceModel; }
    public void setDeviceModel(String deviceModel) { this.deviceModel = deviceModel; }

    public String getAndroidVersion() { return androidVersion; }
    public void setAndroidVersion(String androidVersion) { this.androidVersion = androidVersion; }

    public int getContactsCount() { return contactsCount; }
    public void setContactsCount(int contactsCount) { this.contactsCount = contactsCount; }

    public int getSmsCount() { return smsCount; }
    public void setSmsCount(int smsCount) { this.smsCount = smsCount; }

    public String getAppVersion() { return appVersion; }
    public void setAppVersion(String appVersion) { this.appVersion = appVersion; }

    public String getBackupId() { return backupId; }
    public void setBackupId(String backupId) { this.backupId = backupId; }

    public long getFileSizeBytes() { return fileSizeBytes; }
    public void setFileSizeBytes(long fileSizeBytes) { this.fileSizeBytes = fileSizeBytes; }

    public String getChecksum() { return checksum; }
    public void setChecksum(String checksum) { this.checksum = checksum; }

    public boolean isEncrypted() { return encrypted; }
    public void setEncrypted(boolean encrypted) { this.encrypted = encrypted; }

    public int getCompressionLevel() { return compressionLevel; }
    public void setCompressionLevel(int compressionLevel) { this.compressionLevel = compressionLevel; }
}
