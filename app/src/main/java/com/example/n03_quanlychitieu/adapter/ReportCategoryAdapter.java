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
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
public class ReportCategoryAdapter extends RecyclerView.Adapter<ReportCategoryAdapter.ViewHolder> {
    private List<CategoryReportItem> list;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(CategoryReportItem item);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ReportCategoryAdapter(Context context, List<CategoryReportItem> list) {
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_report_category, parent, false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        CategoryReportItem item = list.get(position);
        holder.tvName.setText(item.name);
        NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
        holder.tvAmount.setText(currencyFormat.format(item.amount));
        holder.tvPercent.setText(String.format(Locale.US, "%.1f %%", item.percent));
        try {
            holder.ivIcon.setColorFilter(Color.parseColor(item.color));
        } catch (Exception e) {}

        if (item.icon != null && !item.icon.isEmpty()) {
            int resId = context.getResources().getIdentifier(item.icon, "drawable", context.getPackageName());
            if (resId != 0) {
                holder.ivIcon.setImageResource(resId);
            }
        }

        holder.itemView.setOnClickListener(v -> {
            if (listener != null) {
                listener.onItemClick(item);
            }
        });
    }
    @Override
    public int getItemCount() {
        return list == null ? 0 : list.size();
    }
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivIcon;
        TextView tvName, tvAmount, tvPercent;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivIcon = itemView.findViewById(R.id.iv_cat_icon);
            tvName = itemView.findViewById(R.id.tv_cat_name);
            tvAmount = itemView.findViewById(R.id.tv_cat_amount);
            tvPercent = itemView.findViewById(R.id.tv_cat_percent);
        }
    }
    public static class CategoryReportItem {
        public String categoryId;
        public String name;
        public double amount;
        public double percent;
        public String color;
        public String icon;

        public CategoryReportItem(String categoryId, String name, double amount, double percent, String color, String icon) {
            this.categoryId = categoryId;
            this.name = name;
            this.amount = amount;
            this.percent = percent;
            this.color = color;
            this.icon = icon;
        }
    }
}
