package com.app.backup.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.backup.R;
import com.app.backup.data.drive.DriveService;
import com.app.backup.data.restore.RestoreManager;
import com.app.backup.databinding.FragmentRestoreBinding;
import com.app.backup.ui.activities.DriveAccountActivity;
import com.app.backup.ui.adapters.BackupRecordAdapter;
import com.app.backup.viewmodel.RestoreViewModel;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayout;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestoreFragment extends Fragment {

    private FragmentRestoreBinding binding;
    private RestoreViewModel viewModel;
    private BackupRecordAdapter localAdapter;
    private DriveBackupAdapter driveAdapter;
    private File selectedFile;

    private final ActivityResultLauncher<String> filePicker = registerForActivityResult(
            new ActivityResultContracts.GetContent(), uri -> {
                if (uri != null) importFile(uri);
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentRestoreBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(RestoreViewModel.class);
        setupUI();
        observeViewModel();
    }

    private void setupUI() {
        binding.tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    binding.layoutLocal.setVisibility(View.VISIBLE);
                    binding.layoutDrive.setVisibility(View.GONE);
                } else {
                    binding.layoutLocal.setVisibility(View.GONE);
                    binding.layoutDrive.setVisibility(View.VISIBLE);
                    viewModel.loadDriveBackups();
                }
            }
            @Override public void onTabUnselected(TabLayout.Tab tab) {}
            @Override public void onTabReselected(TabLayout.Tab tab) {}
        });

        localAdapter = new BackupRecordAdapter(record -> {
            if (record.getFilePath() != null) {
                selectedFile = new File(record.getFilePath());
                viewModel.loadPreview(selectedFile);
            }
        });
        binding.rvLocalBackups.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvLocalBackups.setAdapter(localAdapter);

        // Setup Drive backups adapter
        driveAdapter = new DriveBackupAdapter(driveFile -> {
            // Download from drive, then preview
            File outputFile = new File(requireContext().getCacheDir(),
                    "drive_" + driveFile.fileName);
            viewModel.downloadFromDrive(driveFile.fileId, outputFile);
        });
        binding.rvDriveBackups.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvDriveBackups.setAdapter(driveAdapter);

        binding.btnBrowseFile.setOnClickListener(v -> filePicker.launch("*/*"));
        binding.btnRestore.setOnClickListener(v -> showRestoreOptions());
        binding.btnRefreshDrive.setOnClickListener(v -> viewModel.loadDriveBackups());
    }

    private void importFile(Uri uri) {
        try {
            File temp = new File(requireContext().getCacheDir(), "import_backup.bvault");
            try (InputStream is = requireContext().getContentResolver().openInputStream(uri);
                 FileOutputStream fos = new FileOutputStream(temp)) {
                byte[] buf = new byte[8192];
                int r;
                while ((r = is.read(buf)) != -1) fos.write(buf, 0, r);
            }
            selectedFile = temp;
            viewModel.loadPreview(selectedFile);
        } catch (Exception e) {
            Snackbar.make(requireView(), "Failed to read file: " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
        }
    }

    private void showRestoreOptions() {
        if (selectedFile == null) {
            Snackbar.make(requireView(), "Select a backup first", Snackbar.LENGTH_SHORT).show();
            return;
        }
        String[] opts = {"Contacts & SMS", "Contacts only", "SMS only"};
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("What to Restore")
                .setItems(opts, (d, w) -> {
                    RestoreManager.RestoreOptions options;
                    switch (w) {
                        case 1: options = RestoreManager.RestoreOptions.contactsOnly(); break;
                        case 2: options = RestoreManager.RestoreOptions.smsOnly(); break;
                        default: options = RestoreManager.RestoreOptions.all(); break;
                    }
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Confirm Restore")
                            .setMessage("Duplicates will be skipped. This will add data to your device.")
                            .setPositiveButton("Restore", (d2, w2) -> viewModel.startRestore(selectedFile, options))
                            .setNegativeButton("Cancel", null).show();
                })
                .show();
    }

    private void observeViewModel() {
        viewModel.getLocalBackups().observe(getViewLifecycleOwner(), records -> {
            localAdapter.submitList(records);
            binding.tvNoLocalBackups.setVisibility(
                    records == null || records.isEmpty() ? View.VISIBLE : View.GONE);
        });

        // Observe Drive backups list
        viewModel.getDriveBackups().observe(getViewLifecycleOwner(), files -> {
            driveAdapter.setFiles(files);
            boolean empty = files == null || files.isEmpty();
            binding.tvNoDriveBackups.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvDriveBackups.setVisibility(empty ? View.GONE : View.VISIBLE);
        });

        // Observe Drive loading state
        viewModel.isLoadingDriveBackups().observe(getViewLifecycleOwner(), loading -> {
            binding.progressDriveLoading.setVisibility(loading ? View.VISIBLE : View.GONE);
        });

        // Observe download state
        viewModel.isDownloading().observe(getViewLifecycleOwner(), downloading -> {
            binding.restoreProgressLayout.setVisibility(downloading ? View.VISIBLE : View.GONE);
            if (downloading) {
                binding.tvRestoreStatus.setText("Downloading from Drive...");
            }
        });

        viewModel.getDownloadProgress().observe(getViewLifecycleOwner(), p ->  {
            if (Boolean.TRUE.equals(viewModel.isDownloading().getValue())) {
                binding.progressBarRestore.setProgress(p);
            }
        });

        viewModel.isLoadingPreview().observe(getViewLifecycleOwner(), l ->
                binding.previewProgress.setVisibility(l ? View.VISIBLE : View.GONE));

        viewModel.getBackupPreview().observe(getViewLifecycleOwner(), preview -> {
            if (preview == null) return;
            binding.cardPreview.setVisibility(View.VISIBLE);
            binding.tvPreviewContacts.setText("Contacts: " + preview.totalContacts);
            binding.tvPreviewSms.setText("SMS: " + preview.totalSms);
            if (preview.metadata != null) {
                binding.tvPreviewDate.setText("Date: " + new SimpleDateFormat("MMM dd yyyy", Locale.getDefault())
                        .format(new Date(preview.metadata.getBackupDate())));
                binding.tvPreviewDevice.setText("From: " + preview.metadata.getDeviceModel());
            }
            binding.btnRestore.setEnabled(true);
        });

        viewModel.isRestoring().observe(getViewLifecycleOwner(), restoring -> {
            binding.restoreProgressLayout.setVisibility(restoring ? View.VISIBLE : View.GONE);
            binding.btnRestore.setEnabled(!restoring);
        });

        viewModel.getRestoreProgress().observe(getViewLifecycleOwner(), p ->
                binding.progressBarRestore.setProgress(p));

        viewModel.getRestoreMessage().observe(getViewLifecycleOwner(), msg -> {
            if (msg != null) binding.tvRestoreStatus.setText(msg);
        });

        viewModel.getRestoreEvent().observe(getViewLifecycleOwner(), event -> {
            if (event == null) return;
            switch (event.type) {
                case RESTORE_SUCCESS:
                    new MaterialAlertDialogBuilder(requireContext())
                            .setTitle("Restore Complete ✓")
                            .setMessage("Your data has been restored.")
                            .setPositiveButton("Done", null).show();
                    break;
                case RESTORE_FAILED:
                    Snackbar.make(requireView(), "Restore failed: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
                case DRIVE_NOT_CONNECTED:
                    Snackbar.make(requireView(), "Connect Google Drive first", Snackbar.LENGTH_SHORT)
                            .setAction("Connect", v -> startActivity(
                                    new Intent(requireContext(), DriveAccountActivity.class))).show();
                    break;
                case DOWNLOAD_SUCCESS:
                    selectedFile = new File(event.message);
                    viewModel.loadPreview(selectedFile);
                    Snackbar.make(requireView(), "Downloaded! Tap Restore to proceed.", Snackbar.LENGTH_SHORT).show();
                    break;
                case DOWNLOAD_FAILED:
                    Snackbar.make(requireView(), "Download failed: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
                case DRIVE_ERROR:
                    Snackbar.make(requireView(), "Drive error: " + event.message, Snackbar.LENGTH_SHORT).show();
                    break;
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * Simple adapter for displaying Drive backup files in a RecyclerView
     */
    static class DriveBackupAdapter extends RecyclerView.Adapter<DriveBackupAdapter.ViewHolder> {

        interface OnDriveFileClickListener {
            void onFileClick(DriveService.DriveBackupFile file);
        }

        private final List<DriveService.DriveBackupFile> files = new ArrayList<>();
        private final OnDriveFileClickListener listener;

        DriveBackupAdapter(OnDriveFileClickListener listener) {
            this.listener = listener;
        }

        void setFiles(List<DriveService.DriveBackupFile> newFiles) {
            files.clear();
            if (newFiles != null) files.addAll(newFiles);
            notifyDataSetChanged();
        }

        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            MaterialCardView card = new MaterialCardView(parent.getContext());
            card.setLayoutParams(new RecyclerView.LayoutParams(
                    RecyclerView.LayoutParams.MATCH_PARENT,
                    RecyclerView.LayoutParams.WRAP_CONTENT));
            card.setCardElevation(0);
            card.setStrokeWidth(2);
            card.setContentPadding(32, 24, 32, 24);
            card.setUseCompatPadding(true);
            card.setRadius(32);
            card.setClickable(true);
            card.setFocusable(true);

            // Add TextViews for file name, size, and date
            android.widget.LinearLayout layout = new android.widget.LinearLayout(parent.getContext());
            layout.setOrientation(android.widget.LinearLayout.VERTICAL);

            TextView tvName = new TextView(parent.getContext());
            tvName.setId(android.R.id.text1);
            tvName.setTextSize(16);
            layout.addView(tvName);

            TextView tvDetails = new TextView(parent.getContext());
            tvDetails.setId(android.R.id.text2);
            tvDetails.setTextSize(12);
            layout.addView(tvDetails);

            card.addView(layout);
            return new ViewHolder(card);
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
            DriveService.DriveBackupFile file = files.get(position);
            holder.tvName.setText(file.fileName);

            String size = formatFileSize(file.fileSizeBytes);
            String date = file.createdTime > 0
                    ? new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(new Date(file.createdTime))
                    : "Unknown date";
            holder.tvDetails.setText(size + " • " + date);

            holder.itemView.setOnClickListener(v -> listener.onFileClick(file));
        }

        @Override
        public int getItemCount() { return files.size(); }

        private static String formatFileSize(long bytes) {
            if (bytes < 1024) return bytes + " B";
            if (bytes < 1024 * 1024) return String.format(Locale.getDefault(), "%.1f KB", bytes / 1024.0);
            if (bytes < 1024 * 1024 * 1024) return String.format(Locale.getDefault(), "%.1f MB", bytes / (1024.0 * 1024));
            return String.format(Locale.getDefault(), "%.2f GB", bytes / (1024.0 * 1024 * 1024));
        }

        static class ViewHolder extends RecyclerView.ViewHolder {
            final TextView tvName;
            final TextView tvDetails;

            ViewHolder(@NonNull View itemView) {
                super(itemView);
                tvName = itemView.findViewById(android.R.id.text1);
                tvDetails = itemView.findViewById(android.R.id.text2);
            }
        }
    }
}
