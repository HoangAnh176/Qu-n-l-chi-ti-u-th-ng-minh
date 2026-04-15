package com.example.n03_quanlychitieu.ui.category;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.n03_quanlychitieu.R;
import yuku.ambilwarna.AmbilWarnaDialog;

// The entire class logic is replaced below
public class EditCategoryActivity extends DialogFragment {

    private EditText edtTenDM;
    private CheckBox cbThu, cbChi;
    private Button btnSave;
    private ImageView btnBack;
    private TextView tvTitle;
    private RecyclerView recyclerIcons, recyclerColors;

    private String id;
    private int selectedColor = Color.parseColor("#F44336");
    private int selectedIconResId = R.drawable.ic_outline_cart;
    private String selectedType = "expense";

    private IconAdapter iconAdapter;
    private ColorAdapter colorAdapter;

    private int[] iconListExpense = {
            R.drawable.ic_outline_cart, R.drawable.ic_outline_fastfood, R.drawable.ic_outline_cafe, R.drawable.ic_outline_cake,
            R.drawable.ic_outline_restaurant, R.drawable.ic_outline_car, R.drawable.ic_outline_flight, R.drawable.ic_outline_bike,
            R.drawable.ic_outline_boat, R.drawable.ic_outline_movie, R.drawable.ic_outline_gamepad, R.drawable.ic_outline_palette,
            R.drawable.ic_outline_pets, R.drawable.ic_outline_hospital, R.drawable.ic_outline_gym, R.drawable.ic_outline_school,
            R.drawable.ic_outline_book, R.drawable.ic_outline_work, R.drawable.ic_outline_clothes, R.drawable.ic_outline_home,
            R.drawable.ic_outline_build, R.drawable.ic_outline_print, R.drawable.ic_outline_wallet, R.drawable.ic_outline_card,
            R.drawable.ic_outline_market, R.drawable.ic_outline_smartphone, R.drawable.ic_outline_device, R.drawable.ic_outline_gift,
            R.drawable.ic_outline_florist, R.drawable.ic_outline_star,

            // New 10 icons
            R.drawable.ic_outline_wifi, R.drawable.ic_outline_water_drop, R.drawable.ic_outline_local_gas_station, R.drawable.ic_outline_brush,
            R.drawable.ic_outline_shopping_bag, R.drawable.ic_outline_face, R.drawable.ic_outline_receipt, R.drawable.ic_outline_directions_bus,
            R.drawable.ic_outline_medical_services, R.drawable.ic_outline_music_note
    };

    private int[] iconListIncome = {
            R.drawable.ic_outline_account_balance, R.drawable.ic_outline_savings,
            R.drawable.ic_outline_monetization_on, R.drawable.ic_outline_paid,
            R.drawable.ic_outline_attach_money, R.drawable.ic_outline_trending_up,
            R.drawable.ic_outline_currency_exchange, R.drawable.ic_outline_local_atm,
            R.drawable.ic_outline_business_center, R.drawable.ic_outline_add_card,
            R.drawable.ic_outline_real_estate_agent, R.drawable.ic_outline_handshake,
            R.drawable.ic_outline_stars, R.drawable.ic_outline_emoji_events,
            R.drawable.ic_outline_card_giftcard, R.drawable.ic_outline_wallet
    };

    private int[] currentIconList;

    private String[] colorList = {
            "#F44336", "#E91E63", "#9C27B0", "#673AB7",
            "#3F51B5", "#2196F3", "#03A9F4", "#00BCD4",
            "#009688", "#4CAF50", "#8BC34A", "#CDDC39",
            "#FFEB3B", "#FFC107", "#FF9800", "#FF5722",
            "#795548", "#9E9E9E", "#607D8B", "CUSTOM"
    };

    public static EditCategoryActivity newInstance(String id, String name, String color, String type, String icon) {
        EditCategoryActivity frag = new EditCategoryActivity();
        Bundle args = new Bundle();
        args.putString("id", id);
        args.putString("name", name);
        args.putString("color", color);
        args.putString("type", type);
        args.putString("icon", icon);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Light_NoTitleBar);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_edit_category, container, false);
        khoitao(view);

        if (getArguments() != null) {
            id = getArguments().getString("id");
            String name = getArguments().getString("name", "");
            String color = getArguments().getString("color", "#F44336");
            String type = getArguments().getString("type", "expense");
            String icon = getArguments().getString("icon", "");

            selectedType = (type != null && !type.isEmpty()) ? type : "expense";

            if (id == null || id.isEmpty()) {
                tvTitle.setText("Tạo mới");
                id = null;
            } else {
                tvTitle.setText("Sửa danh mục");
                edtTenDM.setText(name);
                if (!color.isEmpty() && !color.equals("CUSTOM")) {
                    try { selectedColor = Color.parseColor(color); } catch (Exception ignored) {}
                }
            }

            cbThu.setChecked(selectedType.equals("income"));
            cbChi.setChecked(selectedType.equals("expense"));

            currentIconList = selectedType.equals("income") ? iconListIncome : iconListExpense;

            if (id != null && !id.isEmpty() && !icon.isEmpty()) {
                int res = getResources().getIdentifier(icon, "drawable", requireContext().getPackageName());
                if (res != 0) selectedIconResId = res;
                else selectedIconResId = currentIconList[0];
            } else {
                selectedIconResId = currentIconList[0];
            }
        } else {
            currentIconList = iconListExpense;
            selectedIconResId = iconListExpense[0];
        }

        setupArrays();
        setCheckBox();
        setupListeners();
        return view;
    }

    private void khoitao(View view) {
        edtTenDM = view.findViewById(R.id.edtTenDM);
        cbThu = view.findViewById(R.id.cbThu);
        cbChi = view.findViewById(R.id.cbChi);
        btnSave = view.findViewById(R.id.btnSave);
        btnBack = view.findViewById(R.id.btnBack);
        tvTitle = view.findViewById(R.id.tvTitle);
        recyclerIcons = view.findViewById(R.id.recycler_icons);
        recyclerColors = view.findViewById(R.id.recycler_colors);
    }

    private void setCheckBox() {
        cbThu.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbChi.setChecked(false);
                selectedType = "income";
                currentIconList = iconListIncome;
                selectedIconResId = iconListIncome[0];
                if (iconAdapter != null) iconAdapter.notifyDataSetChanged();
            }
        });
        cbChi.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                cbThu.setChecked(false);
                selectedType = "expense";
                currentIconList = iconListExpense;
                selectedIconResId = iconListExpense[0];
                if (iconAdapter != null) iconAdapter.notifyDataSetChanged();
            }
        });
    }

    private void setupArrays() {
        recyclerIcons.setLayoutManager(new GridLayoutManager(requireContext(), 4));
        iconAdapter = new IconAdapter();
        recyclerIcons.setAdapter(iconAdapter);

        recyclerColors.setLayoutManager(new GridLayoutManager(requireContext(), 5));
        colorAdapter = new ColorAdapter();
        recyclerColors.setAdapter(colorAdapter);
    }

    private void setupListeners() {
        btnBack.setOnClickListener(v -> dismiss());

        btnSave.setOnClickListener(v -> {
            String name = edtTenDM.getText().toString().trim();
            String colorHex = String.format("#%06X", (0xFFFFFF & selectedColor));
            String iconName = getResources().getResourceEntryName(selectedIconResId);

            if (name.isEmpty()) {
                edtTenDM.setError("Tên danh mục không được để trống");
                return;
            }
            if (getActivity() instanceof OnCategoryUpdatedListener) {
                ((OnCategoryUpdatedListener) getActivity()).onCategorySaved(id, name, colorHex, selectedType, iconName);
            }
            dismiss();
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null && dialog.getWindow() != null) {
            android.view.Window window = dialog.getWindow();
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            window.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
            window.clearFlags(android.view.WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.addFlags(android.view.WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.WHITE);
            // Use light status bar so icons are dark and visible
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public interface OnCategoryUpdatedListener {
        void onCategorySaved(String id, String name, String color, String type, String icon);
    }

    class IconAdapter extends RecyclerView.Adapter<IconAdapter.Holder> {
        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_icon_sel, parent, false);
            return new Holder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int pos) {
            int icon = currentIconList[pos];
            holder.img.setImageResource(icon);
            GradientDrawable gd = new GradientDrawable();
            gd.setShape(GradientDrawable.RECTANGLE);
            gd.setCornerRadius(24);
            
            if (icon == selectedIconResId) {
                gd.setStroke(4, selectedColor);
                holder.img.setColorFilter(selectedColor);
            } else {
                gd.setStroke(3, Color.parseColor("#E0E0E0"));
                holder.img.setColorFilter(Color.parseColor("#757575"));
            }
            holder.container.setBackground(gd);

            holder.itemView.setOnClickListener(v -> {
                selectedIconResId = icon;
                notifyDataSetChanged();
            });
        }
        @Override public int getItemCount() { return currentIconList.length; }
        class Holder extends RecyclerView.ViewHolder {
            ImageView img;
            View container;
            public Holder(@NonNull View itemView) {
                super(itemView);
                img = itemView.findViewById(R.id.imgIcon);
                container = itemView.findViewById(R.id.llContainer);
            }
        }
    }

    class ColorAdapter extends RecyclerView.Adapter<ColorAdapter.Holder> {
        @NonNull @Override
        public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_color, parent, false);
            return new Holder(v);
        }
        @Override
        public void onBindViewHolder(@NonNull Holder holder, int pos) {
            String colorStr = colorList[pos];
            
            if (colorStr.equals("CUSTOM")) {
                holder.colorView.setBackgroundResource(R.drawable.bg_custom_color);
                
                holder.colorView.setOnClickListener(v -> {
                    new AmbilWarnaDialog(requireContext(), selectedColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                        @Override public void onCancel(AmbilWarnaDialog dialog) {}
                        @Override public void onOk(AmbilWarnaDialog dialog, int clr) {
                            selectedColor = clr;
                            notifyDataSetChanged();
                            iconAdapter.notifyDataSetChanged();
                        }
                    }).show();
                });
            } else {
                int color = Color.parseColor(colorStr);
                GradientDrawable gd = new GradientDrawable();
                gd.setShape(GradientDrawable.RECTANGLE);
                gd.setCornerRadius(16);
                gd.setColor(color);
                
                if (color == selectedColor) {
                    GradientDrawable selectionBg = new GradientDrawable();
                    selectionBg.setShape(GradientDrawable.RECTANGLE);
                    selectionBg.setCornerRadius(20);
                    selectionBg.setStroke(6, color);
                    selectionBg.setColor(Color.WHITE);
                    holder.colorView.setBackground(gd);
                    gd.setStroke(6, Color.parseColor("#333333"));
                } else {
                    holder.colorView.setBackground(gd);
                    gd.setStroke(0, Color.TRANSPARENT);
                }

                holder.colorView.setOnClickListener(v -> {
                    selectedColor = color;
                    notifyDataSetChanged();
                    iconAdapter.notifyDataSetChanged(); // update icon border colors
                });
            }
        }
        @Override public int getItemCount() { return colorList.length; }
        class Holder extends RecyclerView.ViewHolder {
            View colorView;
            public Holder(@NonNull View itemView) { super(itemView); colorView = itemView.findViewById(R.id.colorView); }
        }
    }
}
