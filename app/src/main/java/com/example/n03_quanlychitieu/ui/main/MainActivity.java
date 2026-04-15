package com.example.n03_quanlychitieu.ui.main;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.app.DatePickerDialog;
import java.util.Calendar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n03_quanlychitieu.R;
import com.example.n03_quanlychitieu.adapter.CategoryGridAdapter;
import com.example.n03_quanlychitieu.db.DatabaseHelper;
import com.example.n03_quanlychitieu.model.Categories;
import com.example.n03_quanlychitieu.model.Users;
import com.example.n03_quanlychitieu.ui.category.AddCategoryActivity;
import com.example.n03_quanlychitieu.ui.sign.LogIn;
import com.example.n03_quanlychitieu.ui.user.UserProfileActivity;
import com.example.n03_quanlychitieu.utils.AuthenticationManager;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class MainActivity extends AppCompatActivity {
    private DatabaseHelper dbHelper;
    private AuthenticationManager auth;
    private String userId;
    private RadioGroup rgType;
    private RadioButton rbExpense, rbIncome;
    private TextView tvDate, tvAmountLabel, tvPrevDate, tvNextDate;
    private EditText etNote, etAmount;
    private RecyclerView rvCategories;
    private Button btnSubmit;
    private BottomNavigationView bottomNav;
    private CategoryGridAdapter categoryAdapter;
    private List<Categories> currentCategories;
    private Categories selectedCategory = null;
    private String currentType = "expense"; // "expense" or "income"
    private Calendar selectedCalendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        auth = AuthenticationManager.getInstance(this);
        dbHelper = new DatabaseHelper(this);
        if (!auth.isUserLoggedIn()) {
            startActivity(new Intent(this, LogIn.class));
            finish();
            return;
        }
        Users currentUser = auth.getCurrentUser();
        userId = currentUser != null ? currentUser.getUser_id() : null;
        initViews();
        setupListeners();
        loadCategories();
    }
    private void initViews() {
        rgType = findViewById(R.id.rg_type);
        rbExpense = findViewById(R.id.rb_expense);
        rbIncome = findViewById(R.id.rb_income);
        tvDate = findViewById(R.id.tv_date);
        tvAmountLabel = findViewById(R.id.tv_amount_label);
        etNote = findViewById(R.id.et_note);
        etAmount = findViewById(R.id.et_amount);
        rvCategories = findViewById(R.id.rv_categories);
        btnSubmit = findViewById(R.id.btn_submit);
        bottomNav = findViewById(R.id.bottom_nav);
        tvPrevDate = findViewById(R.id.tv_prev_date);
        tvNextDate = findViewById(R.id.tv_next_date);

        // Set Current Date
        updateDateDisplay();

        // Setup Recycler
        rvCategories.setLayoutManager(new GridLayoutManager(this, 3));
    }
    private void setupListeners() {
        tvPrevDate.setOnClickListener(v -> {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, -1);
            updateDateDisplay();
        });

        tvNextDate.setOnClickListener(v -> {
            selectedCalendar.add(Calendar.DAY_OF_MONTH, 1);
            updateDateDisplay();
        });

        tvDate.setOnClickListener(v -> {
            new DatePickerDialog(MainActivity.this, (view, year, month, dayOfMonth) -> {
                selectedCalendar.set(Calendar.YEAR, year);
                selectedCalendar.set(Calendar.MONTH, month);
                selectedCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateDateDisplay();
            }, selectedCalendar.get(Calendar.YEAR), selectedCalendar.get(Calendar.MONTH), selectedCalendar.get(Calendar.DAY_OF_MONTH)).show();
        });

        bottomNav.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.nav_calendar) {
                Intent intent = new Intent(MainActivity.this, CalendarActivity.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
                return true;
            } else if (item.getItemId() == R.id.nav_more) {
                Intent intent = new Intent(MainActivity.this, UserProfileActivity.class);
                intent.putExtra("userId", userId);
                startActivity(intent);
                return true;
            }
            return true;
        });

        rgType.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.rb_expense) {
                currentType = "expense";
                tvAmountLabel.setText("Tiền chi");
                btnSubmit.setText("Nhập khoản chi");
            } else {
                currentType = "income";
                tvAmountLabel.setText("Tiền thu");
                btnSubmit.setText("Nhập khoản thu");
            }
            selectedCategory = null;
            loadCategories();
        });
        btnSubmit.setOnClickListener(v -> saveTransaction());
    }

    private void updateDateDisplay() {
        String currentDateStr = new SimpleDateFormat("dd/MM/yyyy (E)", new Locale("vi", "VN")).format(selectedCalendar.getTime());
        tvDate.setText(currentDateStr);
    }

    private void loadCategories() {
        new Thread(() -> {
            currentCategories = dbHelper.getCategoriesByUserIdAndType(userId, currentType);
            new Handler(Looper.getMainLooper()).post(() -> {
                if (categoryAdapter == null) {
                    categoryAdapter = new CategoryGridAdapter(this, currentCategories, new CategoryGridAdapter.OnItemClickListener() {
                        @Override
                        public void onCategoryClick(Categories category) {
                            selectedCategory = category;
                        }
                        @Override
                        public void onEditClick() {
                            Intent intent = new Intent(MainActivity.this, AddCategoryActivity.class);
                            intent.putExtra("type", currentType);
                            intent.putExtra("userId", userId);
                            startActivity(intent);
                        }
                    });
                    rvCategories.setAdapter(categoryAdapter);
                } else {
                    categoryAdapter.setList(currentCategories);
                }
            });
        }).start();
    }
    private void saveTransaction() {
        String amount = etAmount.getText().toString();
        String note = etNote.getText().toString();
        if (amount.isEmpty()) {
            Toast.makeText(this, "Vui lòng nhập số tiền", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCategory == null) {
            Toast.makeText(this, "Vui lòng chọn danh mục", Toast.LENGTH_SHORT).show();
            return;
        }

        String currentDateIso = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).format(selectedCalendar.getTime());

        if (currentType.equals("expense")) {
            dbHelper.addExpenseAsync(userId, amount, selectedCategory.getCategory_id(), note, currentDateIso, null, new DatabaseHelper.SimpleCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "Đã thêm chi tiêu!", Toast.LENGTH_SHORT).show();
                    resetForm();
                }
                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            dbHelper.addIncomeAsync(userId, amount, selectedCategory.getCategory_id(), note, currentDateIso, new DatabaseHelper.SimpleCallback() {
                @Override
                public void onSuccess() {
                    Toast.makeText(MainActivity.this, "Đã thêm thu nhập!", Toast.LENGTH_SHORT).show();
                    resetForm();
                }
                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(MainActivity.this, errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void resetForm() {
        etAmount.setText("");
        etNote.setText("");
        selectedCategory = null;
        selectedCalendar = Calendar.getInstance();
        updateDateDisplay();
        if(categoryAdapter != null) categoryAdapter.notifyDataSetChanged();
    }
}
