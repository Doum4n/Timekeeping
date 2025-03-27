package com.example.timekeeping.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timekeeping.CustomCalenderView;
import com.example.timekeeping.R;
import com.example.timekeeping.adapters.CalendarAdapter;
import com.example.timekeeping.base.BaseActivity;
import com.example.timekeeping.databinding.ActivityGroupBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class GroupActivity extends BaseActivity implements CustomCalenderView.OnMonthChangeListener {

    private ActivityGroupBinding binding;


    private GridView gridViewDays;
    private TextView tvMonthYear;
    private CalendarAdapter calendarAdapter;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar("Tên nhóm");

        String groupId = getIntent().getStringExtra("group_id");

        binding.imgBtnShiftManagement.setOnClickListener(v -> {
            Intent intent = new Intent(GroupActivity.this, ShiftManagementActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        });

        binding.imgBtnEmployeeList.setOnClickListener(v -> {
            Intent intent = new Intent(GroupActivity.this, EmployeeManagementActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
        });


        gridViewDays = findViewById(R.id.gridViewDays);
        tvMonthYear = findViewById(R.id.tvMonthYear);

        calendar = Calendar.getInstance();
        updateCalendar();

        CustomCalenderView customCalenderView = (CustomCalenderView) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_calendar);

        if (customCalenderView != null) {
            customCalenderView.setOnMonthChangeListener(month -> {
                calendar.set(Calendar.MONTH, month - 1);
                updateCalendar();
            });
        }

        // Xử lý click chọn ngày
        gridViewDays.setOnItemClickListener((parent, view, position, id) -> calendarAdapter.setSelectedPosition(position));
    }

    private void updateCalendar() {
        SimpleDateFormat monthFormat = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
        tvMonthYear.setText(monthFormat.format(calendar.getTime()));

        List<String> days = new ArrayList<>();

        String[] dayNames = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
        for (String day : dayNames) {
            days.add(day);
        }

        // Xác định ngày đầu tiên của tháng và thêm khoảng trống nếu cần
        Calendar tempCalendar = (Calendar) calendar.clone();
        tempCalendar.set(Calendar.DAY_OF_MONTH, 1);
        int firstDayOfWeek = tempCalendar.get(Calendar.DAY_OF_WEEK);
        // Chuyển từ Calendar.SUNDAY=1 sang Monday=0
        int emptyDays = (firstDayOfWeek + 5) % 7;

        for (int i = 0; i < emptyDays; i++) {
            days.add("");
        }

        // Thêm các ngày trong tháng
        int daysInMonth = tempCalendar.getActualMaximum(Calendar.DAY_OF_MONTH);
        for (int i = 1; i <= daysInMonth; i++) {
            days.add(String.valueOf(i));
        }

        calendarAdapter = new CalendarAdapter(this, days);
        gridViewDays.setAdapter(calendarAdapter);
    }

    @Override
    public void onMonthChanged(int month) {
        calendar.set(Calendar.MONTH, month - 1); // Đặt tháng mới (Calendar.MONTH bắt đầu từ 0)
        updateCalendar();
    }
}