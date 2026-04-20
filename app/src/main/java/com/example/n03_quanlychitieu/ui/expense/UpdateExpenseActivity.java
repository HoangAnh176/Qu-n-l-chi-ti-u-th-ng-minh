package com.example.n03_quanlychitieu.ui.expense;
import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.n03_quanlychitieu.R;
import com.example.n03_quanlychitieu.adapter.CategoryGridAdapter;
import com.example.n03_quanlychitieu.db.DatabaseHelper;
import com.example.n03_quanlychitieu.model.Categories;
import com.example.n03_quanlychitieu.utils.AuthenticationManager;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
public class UpdateExpenseActivity extends AppCompatActivity {
    private static final String TAG = "UpdateExpenseActivity";
    private TextView tvDate, tvDelete;
    private EditText etNote, etAmount;
    private RecyclerView rvCategories;
    private Button btnUpdate;
    private View btnBack;
    private DatabaseHelper databaseHelper;
    private String userId, expenseId;
    private int position;
    private CategoryGridAdapter categoryAdapter;
    private List<Categories> currentCategories;
    private Categories selectedCategory = null;
    private Calendar selectedCalendar = Calendar.getInstance();
    private SimpleDateFormat dispFmt = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
    private SimpleDateFormat isoFmt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_expense);
        databaseHelper = new DatabaseHelper(this);
        userId = AuthenticationManager.getInstance(this).getCurrentUser().getUser_id();
        // ng xa
        btnBack = findViewById(R.id.btn_back);
        tvDate = findViewById(R.id.tv_date);
        etNote = findViewById(R.id.et_note);
        etAmount = findViewById(R.id.et_amount);
        rvCategories = findViewById(R.id.rv_categories);
        btnUpdate = findViewById(R.id.btn_update);
        tvDelete = findViewById(R.id.tv_delete);
        rvCategories.setLayoutManager(new GridLayoutManager(this, 3));
        btnBack.setOnClickListener(v -> finish());
        Intent intent = getIntent();
        expenseId = intent.getStringExtra("expenseID");
        String originalDate = intent.getStringExtra("date");
        String amount = intent.getStringExtra("amount");
        String categoryId = intent.getStringExtra("categoryId");
        String description = intent.getStringExtra("description");
        position = intent.getIntExtra("position", -1);
        if (originalDate != null && originalDate.contains("T")) {
            try {
                Date d = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss", Locale.getDefault()).parse(originalDate);
                if (d != null) selectedCalendar.setTime(d);
            } catch (ParseException ignored) {}
        } else if (originalDate != null) {
            try {
                Date d = isoFmt.parse(originalDate);
                if (d != null) selectedCalendar.setTime(d);
            } catch (Exception e1) {
                try {
                    Date d = dispFmt.parse(originalDate);
                    if (d != null) selectedCalendar.setTime(d);
                } catch (Exception ignored) {}
            }
        }
        updateDateDisplay();
        if (amount != null) {
            try {
                double amountValue = Double.parseDouble(amount);
                etAmount.setText(String.format(Locale.getDefault(), "%.0f", amountValue));
            } catch (NumberFormatException e) {
                etAmount.setText("");
            }
        }
        etNote.setText(description);
        loadCategories(categoryId);
        tvDate.setOnClickListener(v -> showDatePicker());
        // Update action
        btnUpdate.setOnClickListener(v -> updateExpense());
        // Delete action
        tvDelete.setOnClickListener(v -> {
            databaseHelper.deleteExpenseAsync(expenseId, userId, new DatabaseHelper.SimpleCallback() {
                @Override
                public void onSuccess() {
                    Intent result = new Intent();
                    result.putExtra("position", position);
                    result.putExtra("deleted", true);
                    setResult(RESULT_OK, result);
                    finish();
                }
                @Override
                public void onError(String errorMessage) {
                    Toast.makeText(UpdateExpenseActivity.this, "Loi: " + errorMessage, Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void updateDateDisplay() {
        tvDate.setText(dispFmt.format(selectedCalendar.getTime()));
    }
    private void showDatePicker() {
        new DatePickerDialog(this,
                (v, y, m, d) -> {
                    selectedCalendar.set(y, m, d);
                    updateDateDisplay();
                },
                selectedCalendar.get(Calendar.YEAR), 
                selectedCalendar.get(Calendar.MONTH), 
                selectedCalendar.get(Calendar.DAY_OF_MONTH)
        ).show();
    }
    private void loadCategories(String selectedCategoryId) {
        new Thread(() -> {
            currentCategories = databaseHelper.getCategoriesByUserIdAndType(userId, "expense");
            new Handler(Looper.getMainLooper()).post(() -> {
                int initialPos = -1;
                for (int i = 0; i < currentCategories.size(); i++) {
                    Categories c = currentCategories.get(i);
                    if (c.getCategory_id().equals(selectedCategoryId)) {
                        selectedCategory = c;
                        initialPos = i;
                        break;
                    }
                }
                categoryAdapter = new CategoryGridAdapter(this, currentCategories, new CategoryGridAdapter.OnItemClickListener() {
                    @Override
                    public void onCategoryClick(Categories category) {
                        selectedCategory = category;
                    }
                    @Override
                    public void onEditClick() {
                    }
                });
                if (initialPos != -1) {
                    categoryAdapter.setSelectedPos(initialPos);
                }
                rvCategories.setAdapter(categoryAdapter);
            });
        }).start();
    }
    private void updateExpense() {
        String newAmount = etAmount.getText().toString().trim().replace(",", "");
        String newDesc = etNote.getText().toString().trim();
        if (newAmount.isEmpty()) {
            Toast.makeText(this, "Vui ḷng nhap so tien", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedCategory == null || "none".equals(selectedCategory.getCategory_id())) {
            Toast.makeText(this, "Vui long chon danh muc", Toast.LENGTH_SHORT).show();
            return;
        }
        try {
            double amtVal = Double.parseDouble(newAmount);
            if (amtVal <= 0) {
                Toast.makeText(this, "So tien phai lon hon 0", Toast.LENGTH_SHORT).show();
                return;
            }
            final String formattedDate = isoFmt.format(selectedCalendar.getTime());
            String newCatId = selectedCategory.getCategory_id();
            btnUpdate.setEnabled(false);
            databaseHelper.updateExpenseAsync(
                    expenseId, userId, newAmount, newCatId, newDesc, formattedDate, null,
                    new DatabaseHelper.SimpleCallback() {
                        @Override
                        public void onSuccess() {
                            Intent result = new Intent();
                            result.putExtra("position", position);
                            result.putExtra("expense_id", expenseId);
                            result.putExtra("date", formattedDate);
                            result.putExtra("amount", newAmount);
                            result.putExtra("category", newCatId);
                            result.putExtra("description", newDesc);
                            setResult(RESULT_OK, result);
                            finish();
                        }
                        @Override
                        public void onError(String errorMessage) {
                            Toast.makeText(UpdateExpenseActivity.this, "Loi: " + errorMessage, Toast.LENGTH_SHORT).show();
                            btnUpdate.setEnabled(true);
                        }
                    }
            );
        } catch (NumberFormatException e) {
            Toast.makeText(this, "So tien khong hop le", Toast.LENGTH_SHORT).show();
        }
    }
}