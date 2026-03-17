package com.app.backup.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.app.backup.R;
import com.app.backup.data.backup.BackupManager;
import com.app.backup.data.drive.DriveService;
import com.app.backup.databinding.ActivityBackupBinding;
import com.app.backup.viewmodel.BackupViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BackupActivity extends AppCompatActivity {

    private ActivityBackupBinding binding;
    private BackupViewModel viewModel;

    @Inject
    DriveService driveService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBackupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Create Backup");
        }

        viewModel = new ViewModelProvider(this).get(BackupViewModel.class);
        setupUI();
        observeViewModel();
    }

    private void setupUI() {
        binding.btnBackupNow.setOnClickListener(v -> startBackup());
        binding.btnScheduleBackup.setOnClickListener(v -> showScheduleDialog());

        binding.switchUploadDrive.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked && !driveService.isInitialized()) {
                showDriveConnectionDialog();
            }
        });
    }

    private void showDriveConnectionDialog() {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Connect Google Drive")
                .setMessage("You need to connect your Google account to upload backups to Drive.")
                .setPositiveButton("Connect Now", (dialog, which) -> {
                    startActivity(new Intent(this, DriveAccountActivity.class));
                })
                .setNegativeButton("Cancel", (dialog, which) -> {
                    binding.switchUploadDrive.setChecked(false);
                })
                .setOnCancelListener(dialog -> binding.switchUploadDrive.setChecked(false))
                .show();
    }

    private void startBackup() {
        boolean includeContacts = binding.chipContacts.isChecked();
        boolean includeSms = binding.chipSms.isChecked();
        boolean uploadToDrive = binding.switchUploadDrive.isChecked();

        if (!includeContacts && !includeSms) {
            Snackbar.make(binding.getRoot(), "Select at least one data type", Snackbar.LENGTH_SHORT).show();
            return;
        }

        if (uploadToDrive && !driveService.isInitialized()) {
            showDriveConnectionDialog();
            return;
        }

        BackupManager.BackupOptions options = new BackupManager.BackupOptions();
        options.includeContacts = includeContacts;
        options.includeSms = includeSms;
        options.encrypt = binding.switchEncrypt.isChecked();
        options.uploadToDrive = uploadToDrive;

        viewModel.startImmediateBackup(options);
    }

    private void showScheduleDialog() {
        String[] options = {"Every 6 hours", "Every 12 hours", "Daily", "Weekly"};
        int[] hours = {6, 12, 24, 168};
        new MaterialAlertDialogBuilder(this)
                .setTitle("Auto Backup Frequency")
                .setItems(options, (dialog, which) -> {
                    viewModel.setupAutoBackup(hours[which]);
                    Snackbar.make(binding.getRoot(),
                            "Auto backup set: " + options[which], Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Disable Auto Backup", (d, w) -> {
                    viewModel.cancelAutoBackup();
                    Snackbar.make(binding.getRoot(), "Auto backup disabled", Snackbar.LENGTH_SHORT).show();
                })
                .show();
    }

    private void observeViewModel() {
        viewModel.isBackingUp().observe(this, isBackingUp -> {
            binding.progressBar.setVisibility(isBackingUp ? View.VISIBLE : View.GONE);
            binding.btnBackupNow.setEnabled(!isBackingUp);
            binding.btnBackupNow.setText(isBackingUp ? "Backing up..." : "Back Up Now");
            
            // Show status section if backing up
            if (isBackingUp) {
                binding.tvStatus.getParent().getParent().getClass(); // Placeholder for visibility logic
                // Actually we should toggle the card visibility
                ((View)binding.tvStatus.getParent().getParent()).setVisibility(View.VISIBLE);
            }
        });

        viewModel.getBackupProgress().observe(this, progress -> {
            binding.progressBar.setProgress(progress);
            binding.tvProgress.setText(progress + "%");
        });

        viewModel.getBackupStage().observe(this, stage -> {
            if (stage != null) binding.tvStatus.setText(getStageName(stage));
        });

        viewModel.getBackupEvent().observe(this, event -> {
            if (event == null) return;
            switch (event.type) {
                case BACKUP_SUCCESS:
                    Snackbar.make(binding.getRoot(), "Backup created successfully!", Snackbar.LENGTH_SHORT)
                            .setAction("View", v -> finish()).show();
                    break;
                case BACKUP_FAILED:
                    Snackbar.make(binding.getRoot(),
                            "Backup failed: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
                case BACKUP_QUEUED:
                    Snackbar.make(binding.getRoot(),
                            "Backup queued in background", Snackbar.LENGTH_SHORT).show();
                    break;
                case UPLOAD_SUCCESS:
                    Snackbar.make(binding.getRoot(), "Uploaded to Google Drive!", Snackbar.LENGTH_SHORT).show();
                    break;
                case UPLOAD_FAILED:
                    Snackbar.make(binding.getRoot(),
                            "Drive upload failed: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
                case DRIVE_NOT_CONNECTED:
                    showDriveConnectionDialog();
                    break;
            }
        });
    }

    private String getStageName(String stage) {
        switch (stage) {
            case BackupManager.BackupResult.STAGE_READING_CONTACTS: return "Reading contacts...";
            case BackupManager.BackupResult.STAGE_READING_SMS: return "Reading SMS...";
            case BackupManager.BackupResult.STAGE_PACKAGING: return "Packaging data...";
            case BackupManager.BackupResult.STAGE_COMPRESSING: return "Compressing...";
            case BackupManager.BackupResult.STAGE_FINALIZING: return "Encrypting & finalizing...";
            case BackupManager.BackupResult.STAGE_DONE: return "Complete!";
            default: return "Processing...";
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
