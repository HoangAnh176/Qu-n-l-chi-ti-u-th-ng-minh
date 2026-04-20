package com.example.n03_quanlychitieu.adapter;
import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n03_quanlychitieu.R;
import com.example.n03_quanlychitieu.dao.BudgetDAO;
import com.example.n03_quanlychitieu.db.DatabaseHelper;
import com.example.n03_quanlychitieu.model.Budgets;
import com.example.n03_quanlychitieu.model.Categories;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;
public class BudgetOverviewAdapter extends RecyclerView.Adapter<BudgetOverviewAdapter.BudgetViewHolder> {
    public static class BudgetItem {
        public Categories category; 
        public Budgets budget;
        public boolean isTotal;
        public double spentAmount;
        public BudgetItem(Categories cat, Budgets b, boolean t) { category = cat; budget = b; isTotal = t; spentAmount = 0; }
        public BudgetItem(Categories cat, Budgets b, boolean t, double spentAmount) { category = cat; budget = b; isTotal = t; this.spentAmount = spentAmount; }
    }
    private Context context;
    private List<BudgetItem> items;
    private BudgetDAO budgetDAO;
    private NumberFormat format = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
    public BudgetOverviewAdapter(Context context, List<BudgetItem> items, BudgetDAO budgetDAO) {
        this.context = context;
        this.items = items;
        this.budgetDAO = budgetDAO;
    }
    @NonNull
    @Override
    public BudgetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_budget_overview, parent, false);
        return new BudgetViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull BudgetViewHolder holder, int position) {
        BudgetItem item = items.get(position);
        if (item.isTotal) {
            holder.tvCategoryName.setText("Tổng ngân sách");
        } else if (item.category != null) {
            holder.tvCategoryName.setText(item.category.getName());
        }

        if (item.category != null && item.category.getColor() != null) {
            try {
                int catColor = Color.parseColor(item.category.getColor());
                holder.pbBudget.setProgressTintList(android.content.res.ColorStateList.valueOf(catColor));
            } catch (Exception ignored) {}
        } else {
            holder.pbBudget.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.parseColor("#3498DB")));
        }

        if (item.budget == null) {
            holder.tvBudgetAmount.setText("Ngân sách: Chưa đặt");
            holder.tvSpentAmount.setText("Chi tiêu: " + format.format(item.spentAmount));
            if (item.spentAmount > 0) {
                holder.tvRemaining.setText("Còn lại: -" + format.format(item.spentAmount));
                holder.tvRemaining.setTextColor(Color.RED);
                holder.pbBudget.setProgress(100);
                holder.pbBudget.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.RED));
            } else {
                holder.tvRemaining.setText("Chưa đặt");
                holder.tvRemaining.setTextColor(Color.GRAY);
                holder.pbBudget.setProgress(0);
            }
        } else {
            double totalBudget = item.budget.getAmount();
            double totalSpent = item.spentAmount;
            double remaining = totalBudget - totalSpent;
            holder.tvBudgetAmount.setText("Ngân sách: " + format.format(totalBudget));
            holder.tvSpentAmount.setText("Chi tiêu: " + format.format(totalSpent));
            if (remaining < 0) {
                 holder.tvRemaining.setText("Còn lại: -" + format.format(Math.abs(remaining)));
                 holder.tvRemaining.setTextColor(Color.RED);
                 holder.pbBudget.setProgressTintList(android.content.res.ColorStateList.valueOf(Color.RED));
            } else {
                 holder.tvRemaining.setText("Còn lại: " + format.format(remaining));
                 holder.tvRemaining.setTextColor(Color.BLACK);
            }
            int progress = totalBudget > 0 ? (int) ((totalSpent / totalBudget) * 100) : 0;
            holder.pbBudget.setProgress(Math.min(progress, 100));
        }
        holder.itemView.setOnClickListener(v -> {
            android.content.Intent intent = new android.content.Intent(context, com.example.n03_quanlychitieu.ui.sign.SetBudgets.class);
            if (item.category != null) {
                intent.putExtra("categoryName", item.category.getName());
            } else if (item.isTotal) {
                intent.putExtra("categoryName", "Tổng ngân sách");
            }
            context.startActivity(intent);
        });
    }
    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }
    public static class BudgetViewHolder extends RecyclerView.ViewHolder {
        TextView tvCategoryName, tvRemaining, tvBudgetAmount, tvSpentAmount;
        ProgressBar pbBudget;
        public BudgetViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCategoryName = itemView.findViewById(R.id.tv_category_name);
            tvRemaining = itemView.findViewById(R.id.tv_remaining);
            tvBudgetAmount = itemView.findViewById(R.id.tv_budget_amount);
            tvSpentAmount = itemView.findViewById(R.id.tv_spent_amount);
            pbBudget = itemView.findViewById(R.id.pb_budget);
        }
    }
}
