package com.app.backup.utils;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

@Singleton
public class PermissionsUtil {

    public static final int REQUEST_CONTACTS = 1001;
    public static final int REQUEST_SMS = 1002;
    public static final int REQUEST_STORAGE = 1003;
    public static final int REQUEST_ALL = 1004;
    public static final int REQUEST_NOTIFICATIONS = 1005;

    @Inject
    public PermissionsUtil() {}

    // --- Permission Arrays ---

    public static String[] getContactsPermissions() {
        return new String[]{
                Manifest.permission.READ_CONTACTS,
                Manifest.permission.WRITE_CONTACTS
        };
    }

    public static String[] getSmsPermissions() {
        return new String[]{
                Manifest.permission.READ_SMS,
                Manifest.permission.RECEIVE_SMS
        };
    }

    public static String[] getStoragePermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.READ_MEDIA_IMAGES};
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            return new String[]{Manifest.permission.READ_EXTERNAL_STORAGE};
        } else {
            return new String[]{
                    Manifest.permission.READ_EXTERNAL_STORAGE,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
            };
        }
    }

    public static String[] getNotificationPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return new String[]{Manifest.permission.POST_NOTIFICATIONS};
        }
        return new String[]{};
    }

    // --- Check Methods ---

    public boolean hasContactsPermissions(Context context) {
        return hasAllPermissions(context, getContactsPermissions());
    }

    public boolean hasSmsPermissions(Context context) {
        return hasAllPermissions(context, getSmsPermissions());
    }

    public boolean hasStoragePermissions(Context context) {
        return hasAllPermissions(context, getStoragePermissions());
    }

    public boolean hasNotificationPermission(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            return ContextCompat.checkSelfPermission(context,
                    Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED;
        }
        return true;
    }

    public boolean hasAllPermissions(Context context, String[] permissions) {
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(context, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public boolean hasReadContactsPermission(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED;
    }

    public boolean hasReadSmsPermission(Context context) {
        return ContextCompat.checkSelfPermission(context,
                Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED;
    }

    // --- Request Methods (Activity) ---

    public void requestContactsPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, getContactsPermissions(), REQUEST_CONTACTS);
    }

    public void requestSmsPermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, getSmsPermissions(), REQUEST_SMS);
    }

    public void requestStoragePermissions(Activity activity) {
        ActivityCompat.requestPermissions(activity, getStoragePermissions(), REQUEST_STORAGE);
    }

    public void requestAllBackupPermissions(Activity activity) {
        List<String> permissions = new ArrayList<>();
        for (String p : getContactsPermissions()) permissions.add(p);
        for (String p : getSmsPermissions()) permissions.add(p);
        for (String p : getStoragePermissions()) permissions.add(p);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            permissions.add(Manifest.permission.POST_NOTIFICATIONS);
        }
        ActivityCompat.requestPermissions(activity,
                permissions.toArray(new String[0]), REQUEST_ALL);
    }

    // --- Request Methods (Fragment) ---

    public void requestContactsPermissions(Fragment fragment) {
        fragment.requestPermissions(getContactsPermissions(), REQUEST_CONTACTS);
    }

    public void requestSmsPermissions(Fragment fragment) {
        fragment.requestPermissions(getSmsPermissions(), REQUEST_SMS);
    }

    // --- Rationale Methods ---

    public boolean shouldShowRationale(Activity activity, String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public boolean shouldShowContactsRationale(Activity activity) {
        return shouldShowRationale(activity, Manifest.permission.READ_CONTACTS);
    }

    public boolean shouldShowSmsRationale(Activity activity) {
        return shouldShowRationale(activity, Manifest.permission.READ_SMS);
    }

    // --- Check Result Helper ---

    public boolean areAllGranted(int[] grantResults) {
        if (grantResults.length == 0) return false;
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) return false;
        }
        return true;
    }

    // --- Settings Intent ---

    public Intent getAppSettingsIntent(Context context) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.fromParts("package", context.getPackageName(), null));
        return intent;
    }

    // --- Missing Permissions ---

    public List<String> getMissingBackupPermissions(Context context) {
        List<String> missing = new ArrayList<>();
        for (String p : getContactsPermissions()) {
            if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED)
                missing.add(p);
        }
        for (String p : getSmsPermissions()) {
            if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED)
                missing.add(p);
        }
        for (String p : getStoragePermissions()) {
            if (ContextCompat.checkSelfPermission(context, p) != PackageManager.PERMISSION_GRANTED)
                missing.add(p);
        }
        return missing;
    }
}
