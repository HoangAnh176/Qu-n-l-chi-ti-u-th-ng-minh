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

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionHolder> {

    private List<Transaction> transactions;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Transaction transaction);
    }

    public TransactionAdapter(List<Transaction> transactions, OnItemClickListener listener) {
        this.transactions = transactions;
        this.listener = listener;
    }

    @NonNull
    @Override
    public TransactionHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionHolder holder, int position) {
        Transaction t = transactions.get(position);
        holder.tvTitle.setText(t.categoryName != null ? t.categoryName : "Không xác định");
        holder.tvSubtitle.setText(t.description);

        String formattedDate = t.date;
        if (formattedDate != null) {
            formattedDate = formattedDate.replace("T", " ");
        }
        holder.tvTime.setText(formattedDate);

        if (t.type.equals("expense")) {
            holder.tvAmount.setText(String.format("-%,.0fđ", t.amount));
            holder.tvAmount.setTextColor(Color.parseColor("#F44336"));
        } else {
            holder.tvAmount.setText(String.format("%,.0fđ", t.amount));
            holder.tvAmount.setTextColor(Color.parseColor("#2196F3"));
        }
        
        if (t.categoryIcon != null && !t.categoryIcon.isEmpty()) {
            Context context = holder.itemView.getContext();
            int resId = context.getResources().getIdentifier(t.categoryIcon, "drawable", context.getPackageName());
            if (resId != 0) {
                holder.ivIcon.setImageResource(resId);
            } else {
                holder.ivIcon.setImageResource(R.drawable.ic_default_category);
            }
        } else {
            holder.ivIcon.setImageResource(R.drawable.ic_default_category);
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(t);
            }
        });
    }

    @Override
    public int getItemCount() {
        return transactions.size();
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
