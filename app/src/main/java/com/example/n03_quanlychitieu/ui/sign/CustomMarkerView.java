package com.example.n03_quanlychitieu.ui.sign;
import android.content.Context;
import android.widget.TextView;
import com.github.mikephil.charting.components.MarkerView;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.data.PieEntry;
import com.example.n03_quanlychitieu.R;
import java.text.NumberFormat;
import java.util.Locale;
public class CustomMarkerView extends MarkerView {
    private TextView tvContent1;
    private TextView tvContent2;
    private TextView tvContent3;
    private double total;
    public CustomMarkerView(Context context, int layoutResource, double total) {
        super(context, layoutResource);
        tvContent1 = findViewById(R.id.tvContent1);
        tvContent2 = findViewById(R.id.tvContent2);
        tvContent3 = findViewById(R.id.tvContent3);
        this.total = total;
    }
    public void setTotal(double total) {
        this.total = total;
    }
    @Override
    public void refreshContent(Entry e, Highlight highlight) {
        if (e instanceof PieEntry) {
            PieEntry pe = (PieEntry) e;
            String originalName = pe.getLabel();
            if (pe.getData() != null && pe.getData() instanceof String) {
                originalName = (String) pe.getData();
            }
            tvContent1.setText(originalName);
            NumberFormat currencyFormat = NumberFormat.getCurrencyInstance(new Locale("vi", "VN"));
            tvContent2.setText(currencyFormat.format(pe.getValue()));
            double percent = total > 0 ? (pe.getValue() / total * 100) : 0;
            tvContent3.setText(String.format(Locale.US, "%.1f %%", percent));
        }
        super.refreshContent(e, highlight);
    }
    @Override
    public MPPointF getOffset() {
        return new MPPointF(-(getWidth() / 2f), -(getHeight() / 2f));
    }
}
