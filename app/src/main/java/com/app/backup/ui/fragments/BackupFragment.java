package com.app.backup.ui.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.app.backup.data.backup.BackupManager;
import com.app.backup.databinding.FragmentBackupBinding;
import com.app.backup.ui.activities.DriveAccountActivity;
import com.app.backup.viewmodel.BackupViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BackupFragment extends Fragment {

    private FragmentBackupBinding binding;
    private BackupViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentBackupBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BackupViewModel.class);
        setupUI();
        observeViewModel();
        updateDriveStatus();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDriveStatus();
    }

    private void updateDriveStatus() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            binding.tvDriveSubtitle.setText("Connected: " + account.getEmail());
            // Auto-enable the drive toggle if connected
            binding.switchUploadDrive.setChecked(true);
        } else {
            binding.tvDriveSubtitle.setText("Requires Google account");
            binding.switchUploadDrive.setChecked(false);
        }
    }

    private void setupUI() {
        binding.btnBackupNow.setOnClickListener(v -> startBackup());
        binding.btnScheduleBackup.setOnClickListener(v -> showScheduleDialog());
        binding.switchUploadDrive.setOnCheckedChangeListener((btn, checked) -> {
            if (checked) {
                // Remind user to connect Drive
                Snackbar.make(requireView(), "Make sure Google Drive is connected", Snackbar.LENGTH_SHORT)
                        .setAction("Connect", sv ->
                                startActivity(new Intent(requireContext(), DriveAccountActivity.class)))
                        .show();
            }
        });
    }

    private void startBackup() {
        boolean contacts = binding.chipContacts.isChecked();
        boolean sms = binding.chipSms.isChecked();

        if (!contacts && !sms) {
            Snackbar.make(requireView(), "Select at least one type to back up", Snackbar.LENGTH_SHORT).show();
            return;
        }

        BackupManager.BackupOptions opts = new BackupManager.BackupOptions();
        opts.includeContacts = contacts;
        opts.includeSms = sms;
        opts.encrypt = binding.switchEncrypt.isChecked();
        opts.uploadToDrive = binding.switchUploadDrive.isChecked();

        viewModel.startImmediateBackup(opts);
    }

    private void showScheduleDialog() {
        String[] labels = {"Every 6 hours", "Every 12 hours", "Daily", "Weekly"};
        int[] hours = {6, 12, 24, 168};
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Auto Backup Frequency")
                .setItems(labels, (d, w) -> {
                    viewModel.setupAutoBackup(hours[w]);
                    Snackbar.make(requireView(), "Auto backup set: " + labels[w], Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Disable Auto Backup", (d, w) -> {
                    viewModel.cancelAutoBackup();
                    Snackbar.make(requireView(), "Auto backup disabled", Snackbar.LENGTH_SHORT).show();
                })
                .show();
    }

    private void observeViewModel() {
        viewModel.isBackingUp().observe(getViewLifecycleOwner(), backing -> {
            binding.progressBar.setVisibility(backing ? View.VISIBLE : View.GONE);
            binding.tvStatus.setVisibility(backing ? View.VISIBLE : View.GONE);
            binding.tvProgress.setVisibility(backing ? View.VISIBLE : View.GONE);
            binding.btnBackupNow.setEnabled(!backing);
            binding.btnBackupNow.setText(backing ? "Backing up…" : "Back Up Now");
        });

        viewModel.getBackupProgress().observe(getViewLifecycleOwner(), p -> {
            binding.progressBar.setProgress(p);
            binding.tvProgress.setText(p + "%");
        });

        viewModel.getBackupStage().observe(getViewLifecycleOwner(), stage -> {
            if (stage != null) binding.tvStatus.setText(stageLabel(stage));
        });

        viewModel.getBackupEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;
            switch (event.type) {
                case BACKUP_SUCCESS:
                    Snackbar.make(requireView(), "✓ Backup created successfully!", Snackbar.LENGTH_SHORT).show();
                    break;
                case BACKUP_FAILED:
                    Snackbar.make(requireView(), "Backup failed: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
                case UPLOAD_SUCCESS:
                    Snackbar.make(requireView(), "Uploaded to Google Drive!", Snackbar.LENGTH_SHORT).show();
                    break;
                case UPLOAD_FAILED:
                    Snackbar.make(requireView(), "Drive upload failed: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
                case DRIVE_NOT_CONNECTED:
                    Snackbar.make(requireView(), "Connect Google Drive first", Snackbar.LENGTH_SHORT)
                            .setAction("Connect", v -> startActivity(
                                    new Intent(requireContext(), DriveAccountActivity.class)))
                            .show();
                    break;
            }
        });
    }

    private String stageLabel(String stage) {
        switch (stage) {
            case BackupManager.BackupResult.STAGE_READING_CONTACTS: return "Reading contacts…";
            case BackupManager.BackupResult.STAGE_READING_SMS: return "Reading SMS…";
            case BackupManager.BackupResult.STAGE_PACKAGING: return "Packaging data…";
            case BackupManager.BackupResult.STAGE_COMPRESSING: return "Compressing…";
            case BackupManager.BackupResult.STAGE_FINALIZING: return "Encrypting & finalizing…";
            case BackupManager.BackupResult.STAGE_DONE: return "Complete!";
            default: return "Processing…";
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
