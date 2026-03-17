package com.app.backup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.database.AppDatabase;
import com.app.backup.data.database.dao.BackupDao;
import com.app.backup.data.model.ContactModel;
import com.app.backup.data.model.SmsModel;
import com.app.backup.data.repository.ContactsRepository;
import com.app.backup.data.repository.SmsRepository;
import com.app.backup.utils.security.EncryptionUtil;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.core.Observable;

@RunWith(MockitoJUnitRunner.class)
public class BackupManagerTest {

    @Mock
    private ContactsRepository contactsRepository;

    @Mock
    private SmsRepository smsRepository;

    @Mock
    private EncryptionUtil encryptionUtil;

    @Mock
    private AppDatabase database;

    @Mock
    private BackupDao backupDao;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(database.backupDao()).thenReturn(backupDao);
    }

    @Test
    public void testBackupOptionsFullBackup() {
        BackupManager.BackupOptions opts = BackupManager.BackupOptions.fullBackup();
        assertTrue("Full backup should include contacts", opts.includeContacts);
        assertTrue("Full backup should include SMS", opts.includeSms);
        assertTrue("Full backup should be encrypted", opts.encrypt);
    }

    @Test
    public void testBackupOptionsContactsOnly() {
        BackupManager.BackupOptions opts = BackupManager.BackupOptions.contactsOnly();
        assertTrue("Contacts only should include contacts", opts.includeContacts);
        assertFalse("Contacts only should not include SMS", opts.includeSms);
    }

    @Test
    public void testBackupOptionsSmsOnly() {
        BackupManager.BackupOptions opts = BackupManager.BackupOptions.smsOnly();
        assertFalse("SMS only should not include contacts", opts.includeContacts);
        assertTrue("SMS only should include SMS", opts.includeSms);
    }

    @Test
    public void testBackupResultStages() {
        // Verify stage constants are defined
        assertNotNull(BackupManager.BackupResult.STAGE_READING_CONTACTS);
        assertNotNull(BackupManager.BackupResult.STAGE_READING_SMS);
        assertNotNull(BackupManager.BackupResult.STAGE_PACKAGING);
        assertNotNull(BackupManager.BackupResult.STAGE_COMPRESSING);
        assertNotNull(BackupManager.BackupResult.STAGE_FINALIZING);
        assertNotNull(BackupManager.BackupResult.STAGE_DONE);
    }

    @Test
    public void testBackupResultIsDone() {
        BackupManager.BackupResult done = new BackupManager.BackupResult(
                BackupManager.BackupResult.STAGE_DONE, 100, "/path/to/backup");
        assertTrue("Result with STAGE_DONE should return isDone=true", done.isDone());

        BackupManager.BackupResult inProgress = new BackupManager.BackupResult(
                BackupManager.BackupResult.STAGE_COMPRESSING, 70, null);
        assertFalse("Result with non-done stage should return isDone=false", inProgress.isDone());
    }

    @Test
    public void testContactModelCreation() {
        ContactModel contact = new ContactModel("1", "John Doe");
        assertEquals("1", contact.getId());
        assertEquals("John Doe", contact.getDisplayName());

        List<ContactModel.PhoneNumber> phones = new ArrayList<>();
        phones.add(new ContactModel.PhoneNumber("+1234567890", 2, null));
        contact.setPhones(phones);

        assertNotNull(contact.getPhones());
        assertEquals(1, contact.getPhones().size());
        assertEquals("+1234567890", contact.getPhones().get(0).getNumber());
    }

    @Test
    public void testSmsModelTypeLabels() {
        SmsModel sms = new SmsModel();
        sms.setType(SmsModel.TYPE_INBOX);
        assertEquals("Received", sms.getTypeLabel());

        sms.setType(SmsModel.TYPE_SENT);
        assertEquals("Sent", sms.getTypeLabel());

        sms.setType(SmsModel.TYPE_DRAFT);
        assertEquals("Draft", sms.getTypeLabel());
    }

    @Test
    public void testSmsModelFields() {
        SmsModel sms = new SmsModel();
        sms.setAddress("+1234567890");
        sms.setBody("Hello World");
        sms.setDate(1234567890L);
        sms.setType(SmsModel.TYPE_INBOX);
        sms.setRead(1);

        assertEquals("+1234567890", sms.getAddress());
        assertEquals("Hello World", sms.getBody());
        assertEquals(1234567890L, sms.getDate());
        assertEquals(SmsModel.TYPE_INBOX, sms.getType());
        assertEquals(1, sms.getRead());
    }
}
