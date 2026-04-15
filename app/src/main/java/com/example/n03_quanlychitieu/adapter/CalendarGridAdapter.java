package com.example.n03_quanlychitieu.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import com.example.n03_quanlychitieu.R;
import java.util.List;

public class CalendarGridAdapter extends BaseAdapter {
    private Context context;
    private List<String> days;
    private List<Double> sums; // 0 if no sum
    private int currentMonth; // to check if it's outside month

    public CalendarGridAdapter(Context context, List<String> days, List<Double> sums) {
        this.context = context;
        this.days = days;
        this.sums = sums;
    }

    @Override public int getCount() { return days.size(); }
    @Override public Object getItem(int position) { return days.get(position); }
    @Override public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_calendar_cell, parent, false);
        }
        TextView tvDay = convertView.findViewById(R.id.tv_cellday);
        TextView tvSum = convertView.findViewById(R.id.tv_cellsum);

        String day = days.get(position);
        tvDay.setText(day);

        if (day.isEmpty()) {
            tvDay.setText("");
            tvSum.setVisibility(View.GONE);
        } else {
            Double sum = sums.get(position);
            if (sum != null && Math.abs(sum) > 0) {
                tvSum.setVisibility(View.VISIBLE);
                tvSum.setText(String.format("%,.0f", Math.abs(sum)));
                if (sum < 0) {
                    tvSum.setTextColor(Color.parseColor("#F44336"));
                } else {
                    tvSum.setTextColor(Color.parseColor("#2196F3"));
                }
            } else {
                tvSum.setVisibility(View.GONE);
            }
        }

        return convertView;
    }
}
