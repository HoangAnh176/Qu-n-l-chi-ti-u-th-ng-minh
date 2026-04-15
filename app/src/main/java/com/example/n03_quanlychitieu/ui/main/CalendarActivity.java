package com.example.n03_quanlychitieu.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n03_quanlychitieu.R;
import com.example.n03_quanlychitieu.adapter.CalendarGridAdapter;
import com.example.n03_quanlychitieu.adapter.TransactionAdapter;
import com.example.n03_quanlychitieu.db.DatabaseHelper;
import com.example.n03_quanlychitieu.model.Transaction;
import com.example.n03_quanlychitieu.ui.expense.UpdateExpenseActivity;
import com.example.n03_quanlychitieu.ui.income.UpdateIncomeActivity;
import com.example.n03_quanlychitieu.ui.user.UserProfileActivity;
import com.example.n03_quanlychitieu.utils.AuthenticationManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class CalendarActivity extends AppCompatActivity {
    private BottomNavigationView bottomNav;
    private TextView tvTitle, tvMonthYear, tvTotalIncome, tvTotalExpense, tvTotalAll;
    private ImageView ivPrevMonth, ivNextMonth;
    private GridView gvCalendar;
    private RecyclerView rvTransactions;
    private Calendar currentCalendar;
    private DatabaseHelper dbHelper;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        dbHelper = new DatabaseHelper(this);
        userId = AuthenticationManager.getInstance(this).getCurrentUser().getUser_id();
        currentCalendar = Calendar.getInstance();

        initViews();
        setupListeners();
        loadMonthData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (dbHelper != null && userId != null) {
            loadMonthData();
        }
    }

    private void initViews() {
        bottomNav = findViewById(R.id.bottom_nav);
        tvTitle = findViewById(R.id.tv_title);

        tvMonthYear = findViewById(R.id.tv_month_year);
        ivPrevMonth = findViewById(R.id.iv_prev_month);
        ivNextMonth = findViewById(R.id.iv_next_month);
        gvCalendar = findViewById(R.id.gv_calendar);
        rvTransactions = findViewById(R.id.rv_transactions);
        tvTotalIncome = findViewById(R.id.tv_total_income);
        tvTotalExpense = findViewById(R.id.tv_total_expense);
        tvTotalAll = findViewById(R.id.tv_total_all);

        rvTransactions.setLayoutManager(new LinearLayoutManager(this));

        bottomNav.setSelectedItemId(R.id.nav_calendar);
        bottomNav.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_input) {
                startActivity(new Intent(CalendarActivity.this, MainActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (itemId == R.id.nav_more) {
                startActivity(new Intent(CalendarActivity.this, UserProfileActivity.class));
                overridePendingTransition(0, 0);
                finish();
                return true;
            }
            return true;
        });
    }

    private void setupListeners() {
        ivPrevMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            loadMonthData();
        });
        ivNextMonth.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            loadMonthData();
        });
    }

    private void loadMonthData() {
        SimpleDateFormat sdfInfo = new SimpleDateFormat("MM/yyyy", Locale.getDefault());
        tvMonthYear.setText(sdfInfo.format(currentCalendar.getTime()));

        Calendar cal = (Calendar) currentCalendar.clone();
        cal.set(Calendar.DAY_OF_MONTH, 1);
        int daysInMonth = cal.getActualMaximum(Calendar.DAY_OF_MONTH);

        // Setup dates start and end strings
        String startOfMonth = String.format(Locale.getDefault(), "%04d-%02d-%02dT00:00:00",
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, 1);
        String endOfMonth = String.format(Locale.getDefault(), "%04d-%02d-%02dT23:59:59",
            cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, daysInMonth);

        List<Transaction> monthTransactions = dbHelper.getTransactionsByDateRange(userId, startOfMonth, endOfMonth);

        // Prepopulate empty cells for start of month offset (Monday = first)
        int dayOfWeek = cal.get(Calendar.DAY_OF_WEEK); // 1 = Sun, 2 = Mon
        int offset = dayOfWeek == Calendar.SUNDAY ? 6 : dayOfWeek - 2;

        List<String> days = new ArrayList<>();
        List<Double> sums = new ArrayList<>();
        for (int i = 0; i < offset; i++) {
            days.add("");
            sums.add(0.0);
        }

        double totalIncome = 0;
        double totalExpense = 0;

        for (Transaction t : monthTransactions) {
            if (t.type.equals("income")) {
                totalIncome += t.amount;
            } else {
                totalExpense += t.amount;
            }
        }

        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
            double daySum = 0;

            // Calculate day sum
            for (Transaction t : monthTransactions) {
                // assume date format is yyyy-MM-dd
                if (t.date.length() >= 10) {
                    try {
                        int tDay = Integer.parseInt(t.date.substring(8, 10));
                        if (tDay == i) {
                            if (t.type.equals("income")) {
                                daySum += t.amount;
                            } else {
                                daySum -= t.amount;
                            }
                        }
                    } catch (Exception ignored) {}
                }
            }
            sums.add(daySum);
        }

        gvCalendar.setAdapter(new CalendarGridAdapter(this, days, sums));
        rvTransactions.setAdapter(new TransactionAdapter(monthTransactions, transaction -> {
            if (transaction.type.equals("expense")) {
                Intent intent = new Intent(CalendarActivity.this, UpdateExpenseActivity.class);
                intent.putExtra("expenseID", transaction.id);
                intent.putExtra("date", transaction.date);
                intent.putExtra("amount", String.valueOf(transaction.amount));
                intent.putExtra("categoryId", transaction.categoryId);
                intent.putExtra("description", transaction.description);
                if (transaction.budgetId != null) {
                    intent.putExtra("budget", transaction.budgetId);
                }
                startActivity(intent);
            } else if (transaction.type.equals("income")) {
                Intent intent = new Intent(CalendarActivity.this, UpdateIncomeActivity.class);
                intent.putExtra("userId", userId);
                intent.putExtra("incomeId", transaction.id);
                intent.putExtra("categoryId", transaction.categoryId);
                intent.putExtra("amount", String.valueOf(transaction.amount));
                intent.putExtra("source", transaction.description);
                intent.putExtra("date", transaction.date);
                startActivity(intent);
            }
        }));

        tvTotalIncome.setText(String.format("+%,.0fđ", totalIncome));
        tvTotalExpense.setText(String.format("-%,.0fđ", totalExpense));
        tvTotalAll.setText(String.format("%,.0fđ", totalIncome - totalExpense));
    }
}
