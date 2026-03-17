package com.app.backup;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import com.app.backup.data.model.BackupMetadata;
import com.app.backup.data.restore.RestoreManager;

import org.junit.Test;

public class RestoreManagerTest {

    @Test
    public void testRestoreOptionsAll() {
        RestoreManager.RestoreOptions opts = RestoreManager.RestoreOptions.all();
        assertTrue(opts.restoreContacts);
        assertTrue(opts.restoreSms);
        assertTrue(opts.skipDuplicates);
    }

    @Test
    public void testRestoreOptionsContactsOnly() {
        RestoreManager.RestoreOptions opts = RestoreManager.RestoreOptions.contactsOnly();
        assertTrue(opts.restoreContacts);
        assertFalse(opts.restoreSms);
    }

    @Test
    public void testRestoreOptionsSmsOnly() {
        RestoreManager.RestoreOptions opts = RestoreManager.RestoreOptions.smsOnly();
        assertFalse(opts.restoreContacts);
        assertTrue(opts.restoreSms);
    }

    @Test
    public void testBackupPreviewInitialization() {
        RestoreManager.BackupPreview preview = new RestoreManager.BackupPreview();
        assertNotNull(preview.contactsPreview);
        assertNotNull(preview.smsPreview);
        assertTrue(preview.contactsPreview.isEmpty());
        assertTrue(preview.smsPreview.isEmpty());
    }

    @Test
    public void testRestoreProgressMessage() {
        RestoreManager.RestoreProgress progress = new RestoreManager.RestoreProgress("Restoring...", 50);
        assertNotNull(progress.message);
        assertTrue(progress.progress == 50);
    }

    @Test
    public void testBackupMetadataFields() {
        BackupMetadata meta = new BackupMetadata();
        meta.setContactsCount(100);
        meta.setSmsCount(500);
        meta.setDeviceModel("Pixel 8");
        meta.setEncrypted(true);
        meta.setAppVersion("1.0.0");
        meta.setBackupDate(System.currentTimeMillis());

        assertTrue(meta.getContactsCount() == 100);
        assertTrue(meta.getSmsCount() == 500);
        assertTrue("Pixel 8".equals(meta.getDeviceModel()));
        assertTrue(meta.isEncrypted());
        assertTrue("1.0.0".equals(meta.getAppVersion()));
        assertTrue(meta.getBackupDate() > 0);
    }
}
