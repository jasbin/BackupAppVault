package com.app.backup.ui.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.app.backup.data.database.entity.BackupRecord;
import com.app.backup.databinding.ItemBackupRecordBinding;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class BackupRecordAdapter extends ListAdapter<BackupRecord, BackupRecordAdapter.ViewHolder> {

    public interface OnItemClickListener {
        void onItemClick(BackupRecord record);
    }

    private final OnItemClickListener listener;

    public BackupRecordAdapter(OnItemClickListener listener) {
        super(DIFF_CALLBACK);
        this.listener = listener;
    }

    private static final DiffUtil.ItemCallback<BackupRecord> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<BackupRecord>() {
                @Override
                public boolean areItemsTheSame(@NonNull BackupRecord oldItem, @NonNull BackupRecord newItem) {
                    return oldItem.getBackupId().equals(newItem.getBackupId());
                }
                @Override
                public boolean areContentsTheSame(@NonNull BackupRecord oldItem, @NonNull BackupRecord newItem) {
                    return oldItem.getStatus().equals(newItem.getStatus());
                }
            };

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBackupRecordBinding binding = ItemBackupRecordBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(getItem(position), listener);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final ItemBackupRecordBinding binding;

        ViewHolder(ItemBackupRecordBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(BackupRecord record, OnItemClickListener listener) {
            binding.tvDate.setText(record.getBackupDateDisplay() != null
                    ? record.getBackupDateDisplay()
                    : new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault())
                            .format(new Date(record.getCreatedAt())));

            binding.tvDetails.setText(record.getContactsCount() + " contacts · "
                    + record.getSmsCount() + " SMS");

            binding.tvType.setText(BackupRecord.TYPE_DRIVE.equals(record.getBackupType())
                    ? "☁ Google Drive" : "📱 Local");

            binding.tvStatus.setText(record.getStatus());

            String size = formatSize(record.getFileSizeBytes());
            binding.tvSize.setText(size);

            binding.getRoot().setOnClickListener(v -> listener.onItemClick(record));
        }

        private String formatSize(long bytes) {
            if (bytes < 1024) return bytes + " B";
            if (bytes < 1024 * 1024) return String.format(Locale.getDefault(), "%.1f KB", bytes / 1024.0);
            return String.format(Locale.getDefault(), "%.1f MB", bytes / (1024.0 * 1024));
        }
    }
}
