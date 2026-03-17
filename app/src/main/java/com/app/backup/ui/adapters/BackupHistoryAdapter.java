package com.app.backup.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.app.backup.data.database.entity.BackupRecord;
import com.app.backup.databinding.ItemBackupHistoryBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BackupHistoryAdapter extends ListAdapter<BackupRecord, BackupHistoryAdapter.ViewHolder> {

    public interface OnShareClickListener { void onShare(BackupRecord record); }
    public interface OnDeleteClickListener { void onDelete(BackupRecord record); }

    private final OnShareClickListener shareListener;
    private final OnDeleteClickListener deleteListener;

    public BackupHistoryAdapter(OnShareClickListener shareListener,
                                 OnDeleteClickListener deleteListener) {
        super(DIFF_CALLBACK);
        this.shareListener = shareListener;
        this.deleteListener = deleteListener;
    }

    private static final DiffUtil.ItemCallback<BackupRecord> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<BackupRecord>() {
                @Override
                public boolean areItemsTheSame(@NonNull BackupRecord o, @NonNull BackupRecord n) {
                    return o.getBackupId().equals(n.getBackupId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull BackupRecord o, @NonNull BackupRecord n) {
                    return o.getStatus().equals(n.getStatus()) && o.getFileSizeBytes() == n.getFileSizeBytes();
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBackupHistoryBinding binding = ItemBackupHistoryBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), shareListener, deleteListener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBackupHistoryBinding binding;

        ViewHolder(ItemBackupHistoryBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(BackupRecord record, OnShareClickListener shareListener,
                  OnDeleteClickListener deleteListener) {
            String date = record.getBackupDateDisplay() != null
                    ? record.getBackupDateDisplay()
                    : new SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())
                            .format(new Date(record.getCreatedAt()));
            binding.tvDate.setText(date);
            binding.tvContacts.setText(record.getContactsCount() + " contacts");
            binding.tvSms.setText(record.getSmsCount() + " SMS");
            binding.tvType.setText(BackupRecord.TYPE_DRIVE.equals(record.getBackupType())
                    ? "Google Drive" : "Local");
            binding.tvSize.setText(formatSize(record.getFileSizeBytes()));
            binding.tvStatus.setText(record.getStatus().toUpperCase());
            binding.tvEncrypted.setText(record.isEncrypted() ? "Encrypted" : "");
            binding.tvDevice.setText(record.getDeviceName() != null ? record.getDeviceName() : "");

            binding.btnShare.setOnClickListener(v -> shareListener.onShare(record));
            binding.btnDelete.setOnClickListener(v -> deleteListener.onDelete(record));
        }

        private String formatSize(long bytes) {
            if (bytes < 1024) return bytes + " B";
            if (bytes < 1024 * 1024) return String.format(Locale.getDefault(), "%.1f KB", bytes / 1024.0);
            return String.format(Locale.getDefault(), "%.1f MB", bytes / (1024.0 * 1024));
        }
    }
}
