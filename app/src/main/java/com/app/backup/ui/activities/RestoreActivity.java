package com.app.backup.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.backup.databinding.ActivityRestoreBinding;
import com.app.backup.data.restore.RestoreManager;
import com.app.backup.ui.adapters.BackupRecordAdapter;
import com.app.backup.viewmodel.RestoreViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestoreActivity extends AppCompatActivity {

    private ActivityRestoreBinding binding;
    private RestoreViewModel viewModel;
    private BackupRecordAdapter localAdapter;
    private File selectedBackupFile;

    private final ActivityResultLauncher<String> filePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.GetContent(),
            uri -> {
                if (uri != null) handlePickedFile(uri);
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestoreBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Restore Backup");
        }

        viewModel = new ViewModelProvider(this).get(RestoreViewModel.class);
        setupUI();
        observeViewModel();
    }

    private void setupUI() {
        // Tab switching: Local vs Drive
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) showLocalBackups();
                else showDriveBackups();
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Local backups recycler
        localAdapter = new BackupRecordAdapter(record -> {
            selectedBackupFile = new File(record.getFilePath());
            viewModel.loadPreview(selectedBackupFile);
        });
        binding.rvLocalBackups.setLayoutManager(new LinearLayoutManager(this));
        binding.rvLocalBackups.setAdapter(localAdapter);

        // Pick file from storage
        binding.btnBrowseFile.setOnClickListener(v ->
                filePickerLauncher.launch("*/*"));

        // Restore button
        binding.btnRestore.setOnClickListener(v -> showRestoreOptionsDialog());

        // Refresh Drive
        binding.btnRefreshDrive.setOnClickListener(v -> viewModel.loadDriveBackups());
    }

    private void showLocalBackups() {
        binding.layoutLocal.setVisibility(View.VISIBLE);
        binding.layoutDrive.setVisibility(View.GONE);
    }

    private void showDriveBackups() {
        binding.layoutLocal.setVisibility(View.GONE);
        binding.layoutDrive.setVisibility(View.VISIBLE);
        viewModel.loadDriveBackups();
    }

    private void handlePickedFile(Uri uri) {
        try {
            File tempFile = new File(getCacheDir(), "selected_backup.bvault");
            try (InputStream is = getContentResolver().openInputStream(uri);
                 FileOutputStream fos = new FileOutputStream(tempFile)) {
                byte[] buf = new byte[8192];
                int read;
                while ((read = is.read(buf)) != -1) fos.write(buf, 0, read);
            }
            selectedBackupFile = tempFile;
            viewModel.loadPreview(selectedBackupFile);
        } catch (Exception e) {
            Snackbar.make(binding.getRoot(), "Failed to read file", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showRestoreOptionsDialog() {
        if (selectedBackupFile == null) {
            Snackbar.make(binding.getRoot(), "Select a backup file first", Snackbar.LENGTH_SHORT).show();
            return;
        }
        String[] options = {"Contacts & SMS", "Contacts only", "SMS only"};
        new MaterialAlertDialogBuilder(this)
                .setTitle("What to Restore")
                .setItems(options, (dialog, which) -> {
                    RestoreManager.RestoreOptions restoreOptions;
                    switch (which) {
                        case 1: restoreOptions = RestoreManager.RestoreOptions.contactsOnly(); break;
                        case 2: restoreOptions = RestoreManager.RestoreOptions.smsOnly(); break;
                        default: restoreOptions = RestoreManager.RestoreOptions.all(); break;
                    }
                    confirmRestore(restoreOptions);
                })
                .show();
    }

    private void confirmRestore(RestoreManager.RestoreOptions options) {
        new MaterialAlertDialogBuilder(this)
                .setTitle("Confirm Restore")
                .setMessage("This will add data to your device. Existing data won't be deleted. Duplicates will be skipped.")
                .setPositiveButton("Restore", (d, w) ->
                        viewModel.startRestore(selectedBackupFile, options))
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void observeViewModel() {
        viewModel.getLocalBackups().observe(this, records -> {
            if (records != null) localAdapter.submitList(records);
            binding.tvNoLocalBackups.setVisibility(
                    records == null || records.isEmpty() ? View.VISIBLE : View.GONE);
        });

        viewModel.getBackupPreview().observe(this, preview -> {
            if (preview == null) return;
            binding.cardPreview.setVisibility(View.VISIBLE);
            binding.tvPreviewContacts.setText("Contacts: " + preview.totalContacts);
            binding.tvPreviewSms.setText("SMS: " + preview.totalSms);
            if (preview.metadata != null) {
                binding.tvPreviewDate.setText("Backup date: " + new java.text.SimpleDateFormat(
                        "MMM dd, yyyy HH:mm", java.util.Locale.getDefault())
                        .format(new java.util.Date(preview.metadata.getBackupDate())));
                binding.tvPreviewDevice.setText("From: " + preview.metadata.getDeviceModel());
            }
            binding.btnRestore.setEnabled(true);
        });

        viewModel.isLoadingPreview().observe(this, loading ->
                binding.previewProgress.setVisibility(loading ? View.VISIBLE : View.GONE));

        viewModel.isRestoring().observe(this, restoring -> {
            binding.restoreProgressLayout.setVisibility(restoring ? View.VISIBLE : View.GONE);
            binding.btnRestore.setEnabled(!restoring);
        });

        viewModel.getRestoreProgress().observe(this, progress ->
                binding.progressBarRestore.setProgress(progress));

        viewModel.getRestoreMessage().observe(this, msg -> {
            if (msg != null) binding.tvRestoreStatus.setText(msg);
        });

        viewModel.getRestoreEvent().observe(this, event -> {
            if (event == null) return;
            switch (event.type) {
                case RESTORE_SUCCESS:
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Restore Complete")
                            .setMessage("Your data has been restored successfully!")
                            .setPositiveButton("Done", (d, w) -> finish())
                            .show();
                    break;
                case RESTORE_FAILED:
                    Snackbar.make(binding.getRoot(),
                            "Restore failed: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
                case DRIVE_NOT_CONNECTED:
                    Snackbar.make(binding.getRoot(), "Connect Google Drive to restore from cloud",
                            Snackbar.LENGTH_SHORT)
                            .setAction("Connect", v -> startActivity(
                                    new Intent(this, DriveAccountActivity.class)))
                            .show();
                    break;
                case DOWNLOAD_SUCCESS:
                    selectedBackupFile = new File(event.message);
                    viewModel.loadPreview(selectedBackupFile);
                    break;
                case DOWNLOAD_FAILED:
                    Snackbar.make(binding.getRoot(),
                            "Download failed: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
