package com.app.backup.ui.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.backup.databinding.ActivityBackupHistoryBinding;
import com.app.backup.ui.adapters.BackupHistoryAdapter;
import com.app.backup.viewmodel.BackupViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class BackupHistoryActivity extends AppCompatActivity {

    private ActivityBackupHistoryBinding binding;
    private BackupViewModel viewModel;
    private BackupHistoryAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBackupHistoryBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setTitle("Backup History");
        }

        viewModel = new ViewModelProvider(this).get(BackupViewModel.class);
        setupRecyclerView();
        observeViewModel();
    }

    private void setupRecyclerView() {
        adapter = new BackupHistoryAdapter(
                record -> {
                    // Share backup file
                    if (record.getFilePath() != null) {
                        shareBackupFile(record.getFilePath());
                    }
                },
                record -> {
                    // Delete backup
                    new MaterialAlertDialogBuilder(this)
                            .setTitle("Delete Backup")
                            .setMessage("Delete this backup? This cannot be undone.")
                            .setPositiveButton("Delete", (d, w) -> {
                                viewModel.deleteBackup(record);
                                Snackbar.make(binding.getRoot(),
                                        "Backup deleted", Snackbar.LENGTH_SHORT).show();
                            })
                            .setNegativeButton("Cancel", null)
                            .show();
                }
        );
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(this));
        binding.rvHistory.setAdapter(adapter);
    }

    private void shareBackupFile(String filePath) {
        try {
            File file = new File(filePath);
            Uri uri = FileProvider.getUriForFile(this,
                    getPackageName() + ".fileprovider", file);
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("application/octet-stream");
            shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "Export Backup"));
        } catch (Exception e) {
            Snackbar.make(binding.getRoot(), "Cannot share file", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void observeViewModel() {
        viewModel.getAllBackups().observe(this, records -> {
            adapter.submitList(records);
            binding.tvEmpty.setVisibility(
                    records == null || records.isEmpty() ? View.VISIBLE : View.GONE);
            binding.rvHistory.setVisibility(
                    records != null && !records.isEmpty() ? View.VISIBLE : View.GONE);
        });
    }

    @Override
    public boolean onSupportNavigateUp() { onBackPressed(); return true; }
}
