# BackupVault — Android Contacts & SMS Backup App

A production-grade Android application for backing up and restoring Contacts and SMS messages, with optional Google Drive cloud sync. Built entirely in Java following modern Android architecture best practices.

---

## Architecture

```
MVVM + Repository Pattern + Hilt DI + Room + WorkManager + RxJava3
```

```
com.app.backup/
├── BackupApplication.java          # Hilt + NotificationChannels
├── di/
│   └── AppModule.java              # Hilt singleton providers
├── data/
│   ├── model/
│   │   ├── ContactModel.java       # Contact POJO (Gson-serializable)
│   │   ├── SmsModel.java           # SMS POJO (Gson-serializable)
│   │   └── BackupMetadata.java     # Backup metadata POJO
│   ├── database/
│   │   ├── AppDatabase.java        # Room database
│   │   ├── dao/BackupDao.java      # DAO for backup records
│   │   └── entity/BackupRecord.java # Room entity
│   ├── repository/
│   │   ├── ContactsRepository.java # Reads/writes device contacts
│   │   ├── SmsRepository.java      # Reads/writes device SMS
│   │   └── BackupRepository.java   # Backup record CRUD
│   ├── backup/
│   │   └── BackupManager.java      # Core backup orchestrator
│   ├── restore/
│   │   └── RestoreManager.java     # Restore + preview logic
│   └── drive/
│       └── DriveService.java       # Google Drive upload/download/list
├── ui/
│   ├── activities/
│   │   ├── MainActivity.java       # Host + BottomNav
│   │   ├── BackupActivity.java     # Backup UI (standalone)
│   │   ├── RestoreActivity.java    # Restore UI (standalone)
│   │   ├── DriveAccountActivity.java # Google Sign-In / Drive management
│   │   ├── SettingsActivity.java   # PreferenceFragment host
│   │   ├── BackupHistoryActivity.java
│   │   └── RestorePreviewActivity.java
│   ├── fragments/
│   │   ├── DashboardFragment.java  # Home screen
│   │   ├── BackupFragment.java     # Bottom-nav backup tab
│   │   ├── RestoreFragment.java    # Bottom-nav restore tab
│   │   ├── HistoryFragment.java    # Bottom-nav history tab
│   │   └── SettingsFragment.java   # Bottom-nav settings tab
│   └── adapters/
│       ├── BackupRecordAdapter.java   # RecyclerView for restore list
│       └── BackupHistoryAdapter.java  # RecyclerView for history
├── viewmodel/
│   ├── DashboardViewModel.java
│   ├── BackupViewModel.java
│   ├── RestoreViewModel.java
│   └── DriveViewModel.java
├── workers/
│   ├── BackupWorker.java           # Hilt-injected WorkManager worker
│   └── BootReceiver.java           # Reschedules on boot/update
└── utils/
    ├── PermissionsUtil.java         # Runtime permissions helper
    └── security/
        └── EncryptionUtil.java      # AES-256-GCM via Android Keystore
```

---

## Backup File Format

Each backup is stored as an **AES-256-GCM encrypted** `.bvault` file.  
The decrypted content is a ZIP archive containing:

```
backup_YYYYMMDD_HHmmss_XXXXXXXX.bvault
└── (decrypted ZIP)
    ├── contacts.json   — array of ContactModel
    ├── sms.json        — array of SmsModel
    └── metadata.json   — BackupMetadata
```

`metadata.json` schema:
```json
{
  "backup_id": "uuid",
  "backup_date": 1710000000000,
  "device_name": "generic_x86",
  "device_model": "Pixel 8 Pro",
  "android_version": "14",
  "contacts_count": 1234,
  "sms_count": 5678,
  "app_version": "1.0.0",
  "encrypted": true,
  "compression_level": 6,
  "checksum": "sha256hex"
}
```

---

## Setup Instructions

### 1. Clone and Open in Android Studio
```
File → Open → select BackupApp folder
```

### 2. Google Services Setup (for Drive feature)
1. Go to [Firebase Console](https://console.firebase.google.com)
2. Create a project → Add Android app → package: `com.app.backup`
3. Download `google-services.json` → place in `app/` directory
4. Enable **Google Sign-In** in Firebase Authentication
5. Enable **Google Drive API** at [Google Cloud Console](https://console.cloud.google.com)
6. Create OAuth 2.0 Android credentials with your debug SHA-1:
   ```bash
   keytool -list -v -keystore ~/.android/debug.keystore -alias androiddebugkey -storepass android
   ```

### 3. Build
```bash
./gradlew assembleDebug
```

### 4. Run
```bash
./gradlew installDebug
```

---

## Permissions

| Permission | Required For | Android Version |
|---|---|---|
| `READ_CONTACTS` | Reading contacts for backup | All |
| `WRITE_CONTACTS` | Restoring contacts | All |
| `READ_SMS` | Reading SMS for backup | All |
| `RECEIVE_SMS` | SMS restore validation | All |
| `READ_EXTERNAL_STORAGE` | Browsing backup files | ≤ API 32 |
| `WRITE_EXTERNAL_STORAGE` | Saving backups | ≤ API 28 |
| `READ_MEDIA_IMAGES` | File access | API 33+ |
| `POST_NOTIFICATIONS` | Progress/reminder notifications | API 33+ |
| `FOREGROUND_SERVICE` | Background backup | All |
| `INTERNET` | Google Drive sync | All |

---

## Key Technical Decisions

| Area | Choice | Reason |
|---|---|---|
| **Language** | Java | Requested by spec |
| **DI** | Hilt | Compile-time safety, WorkManager integration |
| **Async** | RxJava 3 | Composable I/O chains, easy error handling |
| **Encryption** | AES-256-GCM + Android Keystore | Hardware-backed key storage |
| **Background** | WorkManager | Battery-aware, survives process death |
| **Database** | Room | Type-safe queries, LiveData integration |
| **Drive API** | Google Drive REST v3 | Full control, supports app-folder scope |

---

## Security Notes

- **Keys** are stored in the Android Keystore — never on disk or in SharedPreferences.
- **Encryption** uses AES-256 in GCM mode with a random IV prepended to each file.
- **Checksums** (SHA-256) are stored in backup records for integrity verification.
- **Drive OAuth** uses the `DRIVE_FILE` scope (app-folder only — least privilege).
- **ProGuard** strips log statements in release builds.

---

## Google Play Compliance

Per [Play policy for SMS and Call Log permissions](https://support.google.com/googleplay/android-developer/answer/10208820):

- This app qualifies as a **backup utility** — one of the permitted use cases.
- A **Privacy Policy** link is included in Settings.
- Permissions are requested **contextually** with clear rationale dialogs.
- The app **never uploads** SMS data anywhere except optionally to the user's own Google Drive folder.

---

## Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests
./gradlew connectedAndroidTest
```

---

## Minimum SDK

- **Min SDK:** API 26 (Android 8.0)
- **Target SDK:** API 34 (Android 14)
- **Compile SDK:** API 34
