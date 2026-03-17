package com.app.backup.data.repository;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Telephony;
import android.util.Log;

import com.app.backup.data.model.SmsModel;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.schedulers.Schedulers;

@Singleton
public class SmsRepository {

    private static final String TAG = "SmsRepository";
    private static final int BATCH_SIZE = 200;
    private static final Uri SMS_URI = Uri.parse("content://sms");

    private final Context context;

    @Inject
    public SmsRepository(Context context) {
        this.context = context.getApplicationContext();
    }

    /**
     * Read all SMS messages in batches (returns Observable emitting batches)
     */
    public Observable<List<SmsModel>> readSmsMessages() {
        return Observable.<List<SmsModel>>fromCallable(this::fetchAllSms)
                .subscribeOn(Schedulers.io());
    }

    /**
     * Read SMS with limit for preview
     */
    public Observable<List<SmsModel>> readSmsPreview(int limit) {
        return Observable.<List<SmsModel>>fromCallable(() -> fetchSmsWithLimit(limit))
                .subscribeOn(Schedulers.io());
    }

    /**
     * Get total SMS count
     */
    public int getSmsCount() {
        ContentResolver resolver = context.getContentResolver();
        try (Cursor cursor = resolver.query(SMS_URI, new String[]{"_id"},
                null, null, null)) {
            return cursor != null ? cursor.getCount() : 0;
        } catch (Exception e) {
            Log.e(TAG, "Error getting SMS count", e);
            return 0;
        }
    }

    private List<SmsModel> fetchAllSms() {
        List<SmsModel> smsList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();

        String[] projection = {
                Telephony.Sms._ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.DATE_SENT,
                Telephony.Sms.TYPE,
                Telephony.Sms.READ,
                Telephony.Sms.THREAD_ID,
                Telephony.Sms.SUBJECT,
                Telephony.Sms.SERVICE_CENTER
        };

        int offset = 0;
        boolean hasMore = true;

        while (hasMore) {
            try (Cursor cursor = resolver.query(
                    SMS_URI,
                    projection,
                    null, null,
                    Telephony.Sms.DATE + " DESC LIMIT " + BATCH_SIZE + " OFFSET " + offset)) {

                if (cursor == null || cursor.getCount() == 0) {
                    hasMore = false;
                    break;
                }

                int idIdx = cursor.getColumnIndex(Telephony.Sms._ID);
                int addressIdx = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
                int bodyIdx = cursor.getColumnIndex(Telephony.Sms.BODY);
                int dateIdx = cursor.getColumnIndex(Telephony.Sms.DATE);
                int dateSentIdx = cursor.getColumnIndex(Telephony.Sms.DATE_SENT);
                int typeIdx = cursor.getColumnIndex(Telephony.Sms.TYPE);
                int readIdx = cursor.getColumnIndex(Telephony.Sms.READ);
                int threadIdx = cursor.getColumnIndex(Telephony.Sms.THREAD_ID);
                int subjectIdx = cursor.getColumnIndex(Telephony.Sms.SUBJECT);
                int scIdx = cursor.getColumnIndex(Telephony.Sms.SERVICE_CENTER);

                while (cursor.moveToNext()) {
                    SmsModel sms = new SmsModel();
                    sms.setId(idIdx >= 0 ? cursor.getString(idIdx) : null);
                    sms.setAddress(addressIdx >= 0 ? cursor.getString(addressIdx) : null);
                    sms.setBody(bodyIdx >= 0 ? cursor.getString(bodyIdx) : null);
                    sms.setDate(dateIdx >= 0 ? cursor.getLong(dateIdx) : 0);
                    sms.setDateSent(dateSentIdx >= 0 ? cursor.getLong(dateSentIdx) : 0);
                    sms.setType(typeIdx >= 0 ? cursor.getInt(typeIdx) : 0);
                    sms.setRead(readIdx >= 0 ? cursor.getInt(readIdx) : 0);
                    sms.setThreadId(threadIdx >= 0 ? cursor.getString(threadIdx) : null);
                    sms.setSubject(subjectIdx >= 0 ? cursor.getString(subjectIdx) : null);
                    sms.setServiceCenter(scIdx >= 0 ? cursor.getString(scIdx) : null);
                    smsList.add(sms);
                }

                if (cursor.getCount() < BATCH_SIZE) {
                    hasMore = false;
                } else {
                    offset += BATCH_SIZE;
                }

            } catch (Exception e) {
                Log.e(TAG, "Error fetching SMS batch at offset " + offset, e);
                hasMore = false;
            }
        }
        return smsList;
    }

    private List<SmsModel> fetchSmsWithLimit(int limit) {
        List<SmsModel> smsList = new ArrayList<>();
        ContentResolver resolver = context.getContentResolver();

        try (Cursor cursor = resolver.query(
                SMS_URI, null, null, null,
                Telephony.Sms.DATE + " DESC LIMIT " + limit)) {

            if (cursor == null) return smsList;

            int idIdx = cursor.getColumnIndex(Telephony.Sms._ID);
            int addressIdx = cursor.getColumnIndex(Telephony.Sms.ADDRESS);
            int bodyIdx = cursor.getColumnIndex(Telephony.Sms.BODY);
            int dateIdx = cursor.getColumnIndex(Telephony.Sms.DATE);
            int typeIdx = cursor.getColumnIndex(Telephony.Sms.TYPE);
            int readIdx = cursor.getColumnIndex(Telephony.Sms.READ);

            while (cursor.moveToNext()) {
                SmsModel sms = new SmsModel();
                sms.setId(idIdx >= 0 ? cursor.getString(idIdx) : null);
                sms.setAddress(addressIdx >= 0 ? cursor.getString(addressIdx) : null);
                sms.setBody(bodyIdx >= 0 ? cursor.getString(bodyIdx) : null);
                sms.setDate(dateIdx >= 0 ? cursor.getLong(dateIdx) : 0);
                sms.setType(typeIdx >= 0 ? cursor.getInt(typeIdx) : 0);
                sms.setRead(readIdx >= 0 ? cursor.getInt(readIdx) : 0);
                smsList.add(sms);
            }
        } catch (Exception e) {
            Log.e(TAG, "Error fetching SMS preview", e);
        }
        return smsList;
    }

    /**
     * Restore SMS messages from backup
     */
    public Observable<ContactsRepository.RestoreResult> restoreSms(List<SmsModel> smsList, boolean skipDuplicates) {
        return Observable.<ContactsRepository.RestoreResult>fromCallable(() -> {
            int restored = 0;
            int skipped = 0;
            int failed = 0;

            ContentResolver resolver = context.getContentResolver();

            for (SmsModel sms : smsList) {
                try {
                    if (skipDuplicates && smsExists(sms)) {
                        skipped++;
                        continue;
                    }
                    ContentValues values = buildSmsContentValues(sms);
                    resolver.insert(SMS_URI, values);
                    restored++;
                } catch (Exception e) {
                    Log.e(TAG, "Failed to restore SMS", e);
                    failed++;
                }
            }
            return new ContactsRepository.RestoreResult(restored, skipped, failed);
        }).subscribeOn(Schedulers.io());
    }

    private boolean smsExists(SmsModel sms) {
        if (sms.getAddress() == null || sms.getDate() == 0) return false;
        ContentResolver resolver = context.getContentResolver();
        try (Cursor cursor = resolver.query(SMS_URI,
                new String[]{"_id"},
                Telephony.Sms.ADDRESS + " = ? AND " + Telephony.Sms.DATE + " = ?",
                new String[]{sms.getAddress(), String.valueOf(sms.getDate())},
                null)) {
            return cursor != null && cursor.getCount() > 0;
        } catch (Exception e) {
            return false;
        }
    }

    private ContentValues buildSmsContentValues(SmsModel sms) {
        ContentValues values = new ContentValues();
        values.put(Telephony.Sms.ADDRESS, sms.getAddress());
        values.put(Telephony.Sms.BODY, sms.getBody());
        values.put(Telephony.Sms.DATE, sms.getDate());
        values.put(Telephony.Sms.DATE_SENT, sms.getDateSent());
        values.put(Telephony.Sms.TYPE, sms.getType());
        values.put(Telephony.Sms.READ, sms.getRead());
        values.put(Telephony.Sms.THREAD_ID, sms.getThreadId());
        values.put(Telephony.Sms.SUBJECT, sms.getSubject());
        values.put(Telephony.Sms.SERVICE_CENTER, sms.getServiceCenter());
        return values;
    }
}