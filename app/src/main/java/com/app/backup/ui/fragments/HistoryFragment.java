package com.app.backup.ui.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.app.backup.databinding.FragmentHistoryBinding;
import com.app.backup.ui.adapters.BackupHistoryAdapter;
import com.app.backup.viewmodel.BackupViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;

import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class HistoryFragment extends Fragment {

    private FragmentHistoryBinding binding;
    private BackupViewModel viewModel;
    private BackupHistoryAdapter adapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentHistoryBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(BackupViewModel.class);

        adapter = new BackupHistoryAdapter(
                record -> shareFile(record.getFilePath()),
                record -> confirmDelete(record)
        );
        binding.rvHistory.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvHistory.setAdapter(adapter);

        viewModel.getAllBackups().observe(getViewLifecycleOwner(), records -> {
            adapter.submitList(records);
            boolean empty = records == null || records.isEmpty();
            binding.tvEmpty.setVisibility(empty ? View.VISIBLE : View.GONE);
            binding.rvHistory.setVisibility(empty ? View.GONE : View.VISIBLE);
        });
    }

    private void shareFile(String path) {
        if (path == null) { Snackbar.make(requireView(), "No file found", Snackbar.LENGTH_SHORT).show(); return; }
        try {
            Uri uri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName() + ".fileprovider", new File(path));
            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("application/octet-stream");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(intent, "Export Backup"));
        } catch (Exception e) {
            Snackbar.make(requireView(), "Cannot share file", Snackbar.LENGTH_SHORT).show();
        }
    }

    private void confirmDelete(com.app.backup.data.database.entity.BackupRecord record) {
        new MaterialAlertDialogBuilder(requireContext())
                .setTitle("Delete Backup")
                .setMessage("Delete this backup? This cannot be undone.")
                .setPositiveButton("Delete", (d, w) -> {
                    viewModel.deleteBackup(record);
                    Snackbar.make(requireView(), "Backup deleted", Snackbar.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null).show();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
