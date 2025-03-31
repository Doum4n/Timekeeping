package com.example.timekeeping;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.Menu;
import android.widget.GridView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.timekeeping.adapters.CalendarAdapter;
import com.example.timekeeping.adapters.ChooseShiftAdapter;
import com.example.timekeeping.adapters.ScheduleCalendarAdapter;
import com.example.timekeeping.base.BaseActivity;
import com.example.timekeeping.databinding.ActivityScheduleBinding;
import com.example.timekeeping.models.Shift;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ScheduleActivity extends BaseActivity {

    private ActivityScheduleBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Calendar calendar;
    private GridView gridViewDays;
    private ScheduleCalendarAdapter ScheduleCalendarAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityScheduleBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar,"Lịch làm việc");
        gridViewDays = findViewById(R.id.gridViewDays);

        initCalendar();
        initShift();
        updateCalendar();
    }

    private void initCalendar() {
        gridViewDays = findViewById(R.id.gridViewDays);

        calendar = Calendar.getInstance();
        updateCalendar();

        ScheduleCalenderView customCalenderView = (ScheduleCalenderView) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_calendar);

//        if (customCalenderView != null) {
//            customCalenderView.setOnMonthChangeListener(month -> {
//                calendar.set(Calendar.MONTH, month - 1);
//                updateCalendar();
//            });
//        }

        // Xử lý click chọn ngày
        gridViewDays.setOnItemClickListener((parent, view, position, id) -> ScheduleCalendarAdapter.setSelectedPosition(position));
    }

    private void initShift() {
        String groupId = getIntent().getStringExtra("groupId");
        List<Shift> shiftList = new ArrayList<>();

        db.collection("shifts").whereEqualTo("groupId", groupId).get().addOnSuccessListener(queryDocumentSnapshots -> {
            if (queryDocumentSnapshots.isEmpty()) {

            } else {
                for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                    Shift shift = documentSnapshot.toObject(Shift.class);
                    shiftList.add(shift);
                }

                binding.recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
                ChooseShiftAdapter chooseShiftAdapter = new ChooseShiftAdapter(shiftList);
                binding.recyclerView.setAdapter(chooseShiftAdapter);
            }
        }).addOnFailureListener(e -> Toast.makeText(this, "Lấy dữ liệu thất bại", Toast.LENGTH_SHORT).show());
    }

    private void updateCalendar() {
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

        ScheduleCalendarAdapter calendarAdapter = new ScheduleCalendarAdapter(this, days);
        gridViewDays.setAdapter(calendarAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_done, menu);
        return super.onCreateOptionsMenu(menu);
    }
}