package com.example.borrowhub.view.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.borrowhub.databinding.ItemBorrowRowBinding;
import com.example.borrowhub.viewmodel.TransactionViewModel;

import java.util.ArrayList;
import java.util.List;

public class BorrowItemRowAdapter extends RecyclerView.Adapter<BorrowItemRowAdapter.RowViewHolder> {

    public interface RowListener {
        void onTypeChanged(int position, String type);
        void onNameChanged(int position, String name);
        void onRemove(int position);
    }

    private final List<TransactionViewModel.ItemRow> rows = new ArrayList<>();
    private final RowListener listener;

    public BorrowItemRowAdapter(RowListener listener) {
        this.listener = listener;
    }

    public void updateRows(List<TransactionViewModel.ItemRow> newRows) {
        rows.clear();
        if (newRows != null) {
            for (TransactionViewModel.ItemRow row : newRows) {
                rows.add(new TransactionViewModel.ItemRow(row.type, row.name));
            }
        }
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public RowViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemBorrowRowBinding binding = ItemBorrowRowBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new RowViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull RowViewHolder holder, int position) {
        holder.bind(rows.get(position), position, rows.size());
    }

    @Override
    public int getItemCount() {
        return rows.size();
    }

    class RowViewHolder extends RecyclerView.ViewHolder {
        private final ItemBorrowRowBinding binding;

        RowViewHolder(ItemBorrowRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        void bind(TransactionViewModel.ItemRow row, int position, int totalRows) {
            Context context = binding.getRoot().getContext();

            // Remove old listeners before setting values to avoid re-trigger
            binding.actvItemType.setOnItemClickListener(null);
            binding.actvItemName.setOnItemClickListener(null);

            // Setup type dropdown
            ArrayAdapter<String> typeAdapter = new ArrayAdapter<>(
                    context, android.R.layout.simple_dropdown_item_1line,
                    TransactionViewModel.ITEM_TYPES);
            binding.actvItemType.setAdapter(typeAdapter);
            binding.actvItemType.setText(row.type, false);

            // Setup item name dropdown based on current type
            String[] nameItems = row.type.isEmpty()
                    ? null
                    : TransactionViewModel.ITEMS_BY_TYPE.get(row.type);
            boolean hasType = nameItems != null && nameItems.length > 0;

            if (hasType) {
                ArrayAdapter<String> nameAdapter = new ArrayAdapter<>(
                        context, android.R.layout.simple_dropdown_item_1line, nameItems);
                binding.actvItemName.setAdapter(nameAdapter);
                binding.actvItemName.setText(row.name, false);
                binding.tilItemName.setEnabled(true);
            } else {
                binding.actvItemName.setText("", false);
                binding.actvItemName.setAdapter(null);
                binding.tilItemName.setEnabled(false);
            }

            // Show remove button only when there are multiple rows
            binding.btnRemoveRow.setVisibility(totalRows > 1 ? View.VISIBLE : View.GONE);

            // Re-attach listeners after values are set
            binding.actvItemType.setOnItemClickListener((parent, view, pos, id) -> {
                int adapterPos = getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    listener.onTypeChanged(adapterPos, TransactionViewModel.ITEM_TYPES[pos]);
                }
            });

            binding.actvItemName.setOnItemClickListener((parent, view, pos, id) -> {
                int adapterPos = getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    String selectedName = (String) parent.getItemAtPosition(pos);
                    listener.onNameChanged(adapterPos, selectedName);
                }
            });

            binding.btnRemoveRow.setOnClickListener(v -> {
                int adapterPos = getAdapterPosition();
                if (adapterPos != RecyclerView.NO_POSITION) {
                    listener.onRemove(adapterPos);
                }
            });
        }
    }
}
