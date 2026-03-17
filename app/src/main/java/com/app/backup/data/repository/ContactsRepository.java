package com.app.backup.data.repository;

import android.content.ContentProviderOperation;
import android.content.ContentResolver;
import android.content.Context;
import android.content.OperationApplicationException;
import android.database.Cursor;
import android.os.RemoteException;
import android.provider.ContactsContract;
import android.util.Log;

import com.app.backup.data.model.ContactModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class ContactsRepository {

    private static final String TAG = "ContactsRepository";
    private static final int BATCH_SIZE = 100;

    private final Context context;

    @Inject
    public ContactsRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Read all contacts from device in batches
     */
    public Observable<List<ContactModel>> readContacts() {
        return Observable.<List<ContactModel>>fromCallable(this::fetchContacts)
                .subscribeOn(Schedulers.io());
    }

    /**
     * Get total contact count
     */
    public int getContactCount() {
        ContentResolver resolver = context.getContentResolver();
        try (Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI, null, null, null, null)) {
            return cursor != null ? cursor.getCount() : 0;
        } catch (Exception e) {
            Log.e(TAG, "Error getting contact count", e);
            return 0;
        }
    }

    private List<ContactModel> fetchContacts() {
        List<ContactModel> contacts = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();

        String[] projection = {
                ContactsContract.Contacts._ID,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY,
                ContactsContract.Contacts.HAS_PHONE_NUMBER
        };

        try (Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                projection,
                null, null,
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC")) {

            if (cursor == null) return contacts;

            int idIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts._ID);
            int nameIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY);
            int hasPhoneIdx = cursor.getColumnIndexOrThrow(ContactsContract.Contacts.HAS_PHONE_NUMBER);

            while (cursor.moveToNext()) {
                String id = cursor.getString(idIdx);
                String name = cursor.getString(nameIdx);
                int hasPhone = cursor.getInt(hasPhoneIdx);

                ContactModel contact = new ContactModel(id, name);

                // Fetch phone numbers
                if (hasPhone > 0) {
                    contact.setPhones(fetchPhoneNumbers(resolver, id));
                }

                // Fetch emails
                contact.setEmails(fetchEmails(resolver, id));

                // Fetch organization
                fetchOrganization(resolver, id, contact);

                // Fetch notes
                contact.setNotes(fetchNotes(resolver, id));

                contacts.add(contact);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching contacts", e);
        }
        return contacts;
    }

    private List<ContactModel.PhoneNumber> fetchPhoneNumbers(ContentResolver resolver, String contactId) {
        List<ContactModel.PhoneNumber> phones = new ArrayList<>();
        try (Cursor phoneCursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.NUMBER,
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.LABEL
                },
                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                new String[]{contactId},
                null)) {

            if (phoneCursor != null) {
                int numIdx = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.NUMBER);
                int typeIdx = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.TYPE);
                int labelIdx = phoneCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Phone.LABEL);
                while (phoneCursor.moveToNext()) {
                    phones.add(new ContactModel.PhoneNumber(
                            phoneCursor.getString(numIdx),
                            phoneCursor.getInt(typeIdx),
                            phoneCursor.getString(labelIdx)));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching phone numbers for " + contactId, e);
        }
        return phones;
    }

    private List<ContactModel.EmailAddress> fetchEmails(ContentResolver resolver, String contactId) {
        List<ContactModel.EmailAddress> emails = new ArrayList<>();
        try (Cursor emailCursor = resolver.query(
                ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Email.ADDRESS,
                        ContactsContract.CommonDataKinds.Email.TYPE,
                        ContactsContract.CommonDataKinds.Email.LABEL
                },
                ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                new String[]{contactId},
                null)) {

            if (emailCursor != null) {
                int addrIdx = emailCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.ADDRESS);
                int typeIdx = emailCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.TYPE);
                int labelIdx = emailCursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.Email.LABEL);
                while (emailCursor.moveToNext()) {
                    emails.add(new ContactModel.EmailAddress(
                            emailCursor.getString(addrIdx),
                            emailCursor.getInt(typeIdx),
                            emailCursor.getString(labelIdx)));
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching emails for " + contactId, e);
        }
        return emails;
    }

    private void fetchOrganization(ContentResolver resolver, String contactId, ContactModel contact) {
        try (Cursor orgCursor = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Organization.COMPANY,
                        ContactsContract.CommonDataKinds.Organization.TITLE
                },
                ContactsContract.Data.CONTACT_ID + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + " = ?",
                new String[]{contactId, ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE},
                null)) {

            if (orgCursor != null && orgCursor.moveToFirst()) {
                contact.setOrganization(orgCursor.getString(0));
                contact.setTitle(orgCursor.getString(1));
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching organization", e);
        }
    }

    private String fetchNotes(ContentResolver resolver, String contactId) {
        try (Cursor noteCursor = resolver.query(
                ContactsContract.Data.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Note.NOTE},
                ContactsContract.Data.CONTACT_ID + " = ? AND " +
                        ContactsContract.Data.MIMETYPE + " = ?",
                new String[]{contactId, ContactsContract.CommonDataKinds.Note.CONTENT_ITEM_TYPE},
                null)) {

            if (noteCursor != null && noteCursor.moveToFirst()) {
                return noteCursor.getString(0);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching notes", e);
        }
        return null;
    }

    /**
     * Restore contacts from backup list
     */
    public Observable<RestoreResult> restoreContacts(List<ContactModel> contacts, boolean skipDuplicates) {
        return Observable.<RestoreResult>fromCallable(() -> {
            int restored = 0;
            int skipped = 0;
            int failed = 0;

            for (ContactModel contact : contacts) {
                try {
                    if (skipDuplicates && contactExists(contact.getDisplayName())) {
                        skipped++;
                        continue;
                    }
                    insertContact(contact);
                    restored++;
                } catch (Exception e) {
                    Log.e(TAG, "Failed to restore contact: " + contact.getDisplayName(), e);
                    failed++;
                }
            }
            return new RestoreResult(restored, skipped, failed);
        }).subscribeOn(Schedulers.io());
    }

    private boolean contactExists(String name) {
        if (name == null) return false;
        ContentResolver resolver = context.getContentResolver();
        try (Cursor cursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts._ID},
                ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " = ?",
                new String[]{name},
                null)) {
            return cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private void insertContact(ContactModel contact) throws RemoteException, OperationApplicationException {
        ArrayList<ContentProviderOperation> ops = new ArrayList<>();

        int rawContactInsertIndex = ops.size();
        ops.add(ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                .build());

        // Display name
        if (contact.getDisplayName() != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                            contact.getDisplayName())
                    .build());
        }

        // Phone numbers
        if (contact.getPhones() != null) {
            for (ContactModel.PhoneNumber phone : contact.getPhones()) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, phone.getNumber())
                        .withValue(ContactsContract.CommonDataKinds.Phone.TYPE, phone.getType())
                        .withValue(ContactsContract.CommonDataKinds.Phone.LABEL, phone.getLabel())
                        .build());
            }
        }

        // Emails
        if (contact.getEmails() != null) {
            for (ContactModel.EmailAddress email : contact.getEmails()) {
                ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                        .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                        .withValue(ContactsContract.Data.MIMETYPE,
                                ContactsContract.CommonDataKinds.Email.CONTENT_ITEM_TYPE)
                        .withValue(ContactsContract.CommonDataKinds.Email.ADDRESS, email.getAddress())
                        .withValue(ContactsContract.CommonDataKinds.Email.TYPE, email.getType())
                        .withValue(ContactsContract.CommonDataKinds.Email.LABEL, email.getLabel())
                        .build());
            }
        }

        // Organization
        if (contact.getOrganization() != null || contact.getTitle() != null) {
            ops.add(ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(ContactsContract.Data.RAW_CONTACT_ID, rawContactInsertIndex)
                    .withValue(ContactsContract.Data.MIMETYPE,
                            ContactsContract.CommonDataKinds.Organization.CONTENT_ITEM_TYPE)
                    .withValue(ContactsContract.CommonDataKinds.Organization.COMPANY, contact.getOrganization())
                    .withValue(ContactsContract.CommonDataKinds.Organization.TITLE, contact.getTitle())
                    .build());
        }

        context.getContentResolver().applyBatch(ContactsContract.AUTHORITY, ops);
    }

    // Result class
    public static class RestoreResult {
        public final int restored;
        public final int skipped;
        public final int failed;

        public RestoreResult(int restored, int skipped, int failed) {
            this.restored = restored;
            this.skipped = skipped;
            this.failed = failed;
        }
    }
}