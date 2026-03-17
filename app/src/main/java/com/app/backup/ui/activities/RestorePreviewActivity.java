package com.app.backup.ui.activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.backup.databinding.ActivityRestorePreviewBinding;
import com.app.backup.viewmodel.RestoreViewModel;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class RestorePreviewActivity extends AppCompatActivity {

    public static final String EXTRA_FILE_PATH = "extra_file_path";

    private ActivityRestorePreviewBinding binding;
    private RestoreViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRestorePreviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Backup Preview");
        }

        viewModel = new ViewModelProvider(this).get(RestoreViewModel.class);
        observeViewModel();

        String filePath = getIntent().getStringExtra(EXTRA_FILE_PATH);
        if (filePath != null) {
            viewModel.loadPreview(new File(filePath));
        }
    }

    private void observeViewModel() {
        viewModel.isLoadingPreview().observe(this, loading ->
                binding.progressPreview.setVisibility(loading ? View.VISIBLE : View.GONE));

        viewModel.getBackupPreview().observe(this, preview -> {
            if (preview == null) return;
            binding.tvContactCount.setText(String.valueOf(preview.totalContacts));
            binding.tvSmsCount.setText(String.valueOf(preview.totalSms));

            if (preview.metadata != null) {
                binding.tvBackupDate.setText(new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                        .format(new Date(preview.metadata.getBackupDate())));
                binding.tvDeviceName.setText(preview.metadata.getDeviceModel());
                binding.tvAppVersion.setText("v" + preview.metadata.getAppVersion());
                binding.tvEncrypted.setText(preview.metadata.isEncrypted() ? "Yes (AES-256)" : "No");
            }

            // Show sample contacts
            if (!preview.contactsPreview.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Math.min(5, preview.contactsPreview.size()); i++) {
                    sb.append("• ").append(preview.contactsPreview.get(i).getDisplayName()).append("\n");
                }
                if (preview.totalContacts > 5) sb.append("  ...and ").append(preview.totalContacts - 5).append(" more");
                binding.tvContactsSample.setText(sb.toString());
            }

            // Show sample SMS
            if (!preview.smsPreview.isEmpty()) {
                StringBuilder sb = new StringBuilder();
                for (int i = 0; i < Math.min(5, preview.smsPreview.size()); i++) {
                    String addr = preview.smsPreview.get(i).getAddress();
                    sb.append("• ").append(addr != null ? addr : "Unknown").append("\n");
                }
                if (preview.totalSms > 5) sb.append("  ...and ").append(preview.totalSms - 5).append(" more");
                binding.tvSmsSample.setText(sb.toString());
            }

            binding.contentLayout.setVisibility(View.VISIBLE);
        });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
