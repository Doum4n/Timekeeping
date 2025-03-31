package com.example.timekeeping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.R;

import java.util.List;

public class ScheduleCalendarAdapter extends BaseAdapter {

    private Context context;
    private List<String> days;
    private int selectedPosition;

    public ScheduleCalendarAdapter(Context context, List<String> days) {
        this.context = context;
        this.days = days;
    }


    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int position) {
        return days.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.item_schedule_day, parent, false);
        }

        TextView tvDay = convertView.findViewById(R.id.ScheduleDay);
        String day = days.get(position);

        // Đặt style khác nhau cho ngày trong tuần và ngày tháng
        if (position < 7) { // Các ô đầu tiên là tên ngày trong tuần
            tvDay.setText(day);
            tvDay.setTextColor(context.getResources().getColor(android.R.color.darker_gray));
            convertView.setBackground(null);
        } else {
            tvDay.setText(day);
            if (day.isEmpty()) {
                tvDay.setVisibility(View.INVISIBLE);
            } else {
                tvDay.setVisibility(View.VISIBLE);
                // Highlight ngày được chọn
                convertView.setSelected(position == selectedPosition);
            }
        }

        convertView.setOnClickListener(v -> Toast.makeText(this.context, "Clicked on " + day, Toast.LENGTH_SHORT).show());

        return convertView;
    }

    public void setSelectedPosition(int position) {
        if (position >= 7) { // Chỉ cho phép chọn các ô ngày, không phải tên ngày
            selectedPosition = position;
            notifyDataSetChanged();
        }
    }
}
