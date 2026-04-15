package com.example.n03_quanlychitieu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n03_quanlychitieu.R;
import com.example.n03_quanlychitieu.model.Categories;

import java.util.List;

public class CategoryGridAdapter extends RecyclerView.Adapter<CategoryGridAdapter.ViewHolder> {

    private Context context;
    private List<Categories> list;
    private int selectedPos = -1;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onCategoryClick(Categories category);
        void onEditClick();
    }

    public CategoryGridAdapter(Context context, List<Categories> list, OnItemClickListener listener) {
        this.context = context;
        this.list = list;
        this.listener = listener;
    }

    public void setList(List<Categories> list) {
        this.list = list;
        this.selectedPos = -1;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_category_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (position == list.size()) {
            // Edit button
            holder.tvName.setText("Chỉnh sửa >");
            holder.ivIcon.setVisibility(View.GONE); // Or set to an edit icon
            holder.llRoot.setBackgroundResource(R.drawable.bg_category_item_unselected);
            holder.llRoot.setOnClickListener(v -> listener.onEditClick());
            return;
        }

        Categories category = list.get(position);
        holder.tvName.setText(category.getName());
        holder.ivIcon.setVisibility(View.VISIBLE);

        int iconRes = context.getResources().getIdentifier(category.getIcon(), "drawable", context.getPackageName());
        if (iconRes != 0) holder.ivIcon.setImageResource(iconRes);
        else holder.ivIcon.setImageResource(R.drawable.iccoin); // fallback

        try {
            holder.ivIcon.setColorFilter(Color.parseColor(category.getColor()));
        } catch (Exception e) {
            holder.ivIcon.setColorFilter(Color.GRAY);
        }

        if (position == selectedPos) {
            holder.llRoot.setBackgroundResource(R.drawable.bg_category_item_selected);
        } else {
            holder.llRoot.setBackgroundResource(R.drawable.bg_category_item_unselected);
        }

        holder.llRoot.setOnClickListener(v -> {
            int oldPos = selectedPos;
            selectedPos = holder.getAdapterPosition();
            notifyItemChanged(oldPos);
            notifyItemChanged(selectedPos);
            listener.onCategoryClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return list == null ? 1 : list.size() + 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout llRoot;
        ImageView ivIcon;
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            llRoot = itemView.findViewById(R.id.ll_root);
            ivIcon = itemView.findViewById(R.id.iv_icon);
            tvName = itemView.findViewById(R.id.tv_name);
        }
    }
}

