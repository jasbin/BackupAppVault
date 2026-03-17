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
import com.app.backup.databinding.FragmentDashboardBinding;
import com.app.backup.ui.activities.BackupHistoryActivity;
import com.app.backup.ui.activities.DriveAccountActivity;
import com.app.backup.viewmodel.DashboardViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    private FragmentDashboardBinding binding;
    private DashboardViewModel viewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(DashboardViewModel.class);
        setupClickListeners();
        observeViewModel();
        viewModel.loadCounts();
        viewModel.loadStorageStats();
        updateDriveCard();
    }

    @Override
    public void onResume() {
        super.onResume();
        updateDriveCard();
    }

    private void updateDriveCard() {
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(requireContext());
        if (account != null) {
            String email = account.getEmail();
            binding.tvDriveStatus.setText("Connected: " + email);
        } else {
            binding.tvDriveStatus.setText("Tap to connect");
        }
    }

    private void setupClickListeners() {
        binding.btnBackupNow.setOnClickListener(v -> showBackupDialog());
        binding.btnViewHistory.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), BackupHistoryActivity.class)));
        binding.cardDrive.setOnClickListener(v ->
                startActivity(new Intent(requireContext(), DriveAccountActivity.class)));
        binding.btnRefreshCounts.setOnClickListener(v -> viewModel.loadCounts());
    }

    private void showBackupDialog() {
        String[] options = {"Contacts & SMS (Full Backup)", "Contacts only", "SMS only"};
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Start Backup")
                .setItems(options, (dialog, which) -> {
                    BackupManager.BackupOptions opts;
                    switch (which) {
                        case 1: opts = BackupManager.BackupOptions.contactsOnly(); break;
                        case 2: opts = BackupManager.BackupOptions.smsOnly(); break;
                        default: opts = BackupManager.BackupOptions.fullBackup(); break;
                    }
                    viewModel.startBackup(opts);
                })
                .show();
    }

    private void observeViewModel() {
        viewModel.getContactCount().observe(getViewLifecycleOwner(), count -> {
            binding.tvContactCount.setText(String.valueOf(count));
        });

        viewModel.getSmsCount().observe(getViewLifecycleOwner(), count -> {
            binding.tvSmsCount.setText(String.valueOf(count));
        });

        viewModel.isLoadingCounts().observe(getViewLifecycleOwner(), loading -> {
            binding.progressCounts.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        viewModel.getLatestBackup().observe(getViewLifecycleOwner(), record -> {
            if (record != null) {
                binding.tvLastBackup.setText(record.getBackupDateDisplay());
                binding.tvLastBackupDetails.setText(
                        record.getContactsCount() + " contacts, " + record.getSmsCount() + " SMS");
                binding.chipBackupStatus.setText("Last: " + record.getStatus());
            } else {
                binding.tvLastBackup.setText("No backups yet");
                binding.tvLastBackupDetails.setText("Tap 'Back Up Now' to get started");
            }
        });

        viewModel.getTotalStorageUsed().observe(getViewLifecycleOwner(), bytes -> {
            binding.tvStorageUsed.setText(formatFileSize(bytes));
        });

        viewModel.isBackingUp().observe(getViewLifecycleOwner(), isBackingUp -> {
            binding.backupProgressCard.setVisibility(isBackingUp ? View.VISIBLE : View.GONE);
            binding.btnBackupNow.setEnabled(!isBackingUp);
        });

        viewModel.getBackupProgress().observe(getViewLifecycleOwner(), progress -> {
            binding.progressBarBackup.setProgress(progress);
        });

        viewModel.getBackupStage().observe(getViewLifecycleOwner(), stage -> {
            if (stage != null) binding.tvBackupStage.setText(stageName(stage));
        });

        viewModel.getBackupResult().observe(getViewLifecycleOwner(), result -> {
            if (result == null) return;
            if (result.success) {
                Snackbar.make(requireView(), "✓ Backup complete!", Snackbar.LENGTH_SHORT)
                        .setAction("History", v ->
                                startActivity(new Intent(requireContext(), BackupHistoryActivity.class)))
                        .show();
                viewModel.loadStorageStats();
            } else {
                Snackbar.make(requireView(), "Backup failed: " + result.errorMessage,
                        Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    private String stageName(String stage) {
        switch (stage) {
            case BackupManager.BackupResult.STAGE_READING_CONTACTS: return "Reading contacts...";
            case BackupManager.BackupResult.STAGE_READING_SMS: return "Reading SMS...";
            case BackupManager.BackupResult.STAGE_PACKAGING: return "Packaging...";
            case BackupManager.BackupResult.STAGE_COMPRESSING: return "Compressing...";
            case BackupManager.BackupResult.STAGE_FINALIZING: return "Encrypting...";
            case BackupManager.BackupResult.STAGE_DONE: return "Done!";
            default: return "Working...";
        }
    }

    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format(Locale.getDefault(), "%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format(Locale.getDefault(), "%.1f MB", bytes / (1024.0 * 1024));
        return String.format(Locale.getDefault(), "%.2f GB", bytes / (1024.0 * 1024 * 1024));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
