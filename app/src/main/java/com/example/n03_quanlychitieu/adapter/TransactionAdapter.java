package com.example.n03_quanlychitieu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n03_quanlychitieu.R;
import com.example.n03_quanlychitieu.model.Transaction;

import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_TRANSACTION = 1;

    public interface ListItem {
        int getType();
    }

    public static class HeaderItem implements ListItem {
        public String dateLabel;
        public double dailyTotal;
        public HeaderItem(String dateLabel, double dailyTotal) {
            this.dateLabel = dateLabel;
            this.dailyTotal = dailyTotal;
        }
        @Override
        public int getType() {
            return TYPE_HEADER;
        }
    }

    public static class TransactionItem implements ListItem {
        public Transaction transaction;
        public TransactionItem(Transaction transaction) {
            this.transaction = transaction;
        }
        @Override
        public int getType() {
            return TYPE_TRANSACTION;
        }
    }

    private List<ListItem> items;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    public TransactionAdapter(List<ListItem> items, OnItemClickListener listener) {
        this.items = items;
        this.listener = listener;
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getType();
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_HEADER) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_day_header, parent, false);
            return new HeaderHolder(view);
        } else {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
            return new TransactionHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ListItem item = items.get(position);
        if (item.getType() == TYPE_HEADER && holder instanceof HeaderHolder) {
            HeaderHolder hHolder = (HeaderHolder) holder;
            HeaderItem header = (HeaderItem) item;
            hHolder.tvDate.setText(header.dateLabel);

            // Format daily total
            if (header.dailyTotal < 0) {
                hHolder.tvTotal.setText(String.format("%,.0fđ", header.dailyTotal));
                hHolder.tvTotal.setTextColor(Color.parseColor("#333333")); // Can use specific color if preferred
            } else {
                hHolder.tvTotal.setText(String.format("+%,.0fđ", header.dailyTotal));
                hHolder.tvTotal.setTextColor(Color.parseColor("#333333"));
            }
        } else if (item.getType() == TYPE_TRANSACTION && holder instanceof TransactionHolder) {
            TransactionHolder tHolder = (TransactionHolder) holder;
            TransactionItem tItem = (TransactionItem) item;
            Transaction t = tItem.transaction;

            tHolder.tvTitle.setText(t.categoryName != null ? t.categoryName : "Không xác định");
            tHolder.tvSubtitle.setText(t.description != null ? t.description : "");

            // Extract time for the subtitle/time view
            String time = "";
            if (t.date != null && t.date.length() >= 16) {
                time = t.date.substring(11, 16); // Extract HH:mm from 2026-04-12T15:30:00
            }
            tHolder.tvTime.setText(time);

            if (t.type.equals("expense")) {
                tHolder.tvAmount.setText(String.format("-%,.0fđ", t.amount));
                tHolder.tvAmount.setTextColor(Color.parseColor("#F44336"));
            } else {
                tHolder.tvAmount.setText(String.format("%,.0fđ", t.amount));
                tHolder.tvAmount.setTextColor(Color.parseColor("#2196F3"));
            }

            // Apply category color to icon
            int colorInt = Color.parseColor("#FF9800"); // fallback
            if (t.categoryColor != null && !t.categoryColor.isEmpty()) {
                try {
                    colorInt = Color.parseColor(t.categoryColor);
                } catch (Exception e) {}
            }
            tHolder.ivIcon.setColorFilter(colorInt);

            if (t.categoryIcon != null && !t.categoryIcon.isEmpty()) {
                Context context = tHolder.itemView.getContext();
                int resId = context.getResources().getIdentifier(t.categoryIcon, "drawable", context.getPackageName());
                if (resId != 0) {
                    tHolder.ivIcon.setImageResource(resId);
                } else {
                    tHolder.ivIcon.setImageResource(R.drawable.ic_default_category);
                }
            } else {
                tHolder.ivIcon.setImageResource(R.drawable.ic_default_category);
            }

            tHolder.itemView.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemClick(t);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    class HeaderHolder extends RecyclerView.ViewHolder {
        TextView tvDate, tvTotal;
        public HeaderHolder(View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tv_date);
            tvTotal = itemView.findViewById(R.id.tv_total);
        }
    }

    class TransactionHolder extends RecyclerView.ViewHolder {
        TextView tvTitle, tvSubtitle, tvAmount, tvTime;
        ImageView ivIcon;
        public TransactionHolder(View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvSubtitle = itemView.findViewById(R.id.tv_subtitle);
            tvAmount = itemView.findViewById(R.id.tv_amount);
            tvTime = itemView.findViewById(R.id.tv_time);
            ivIcon = itemView.findViewById(R.id.iv_category_icon);
        }
    }
}
