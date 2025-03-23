package com.example.timekeeping.ui.group;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timekeeping.R;
import com.example.timekeeping.databinding.ActivityAddShiftBinding;
import com.example.timekeeping.databinding.ActivityGroupBinding;
import com.example.timekeeping.models.Shift;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.List;

public class AddShiftActivity extends AppCompatActivity {

    private ActivityAddShiftBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddShiftBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String groupId = getIntent().getStringExtra("groupId");

        binding.btnSaveShift.setOnClickListener(v -> {
            String shiftName = binding.txtShiftName.getText().toString();

            db.collection("shifts")
                    .add(new Shift(shiftName, binding.txtInTime.getText().toString(), binding.txtOutTime.getText().toString(), List.of(), groupId))
                    .addOnSuccessListener(documentReference -> {
                        Toast.makeText(AddShiftActivity.this, "Ca công việc đã được thêm thành công", Toast.LENGTH_SHORT).show();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(AddShiftActivity.this, "Lỗi khi thêm ca công việc", Toast.LENGTH_SHORT).show();
                    });
        });

        binding.txtInTime.setOnClickListener(v -> {
            showTimePicker(time -> binding.txtInTime.setText(time));
        });

        binding.txtOutTime.setOnClickListener(v -> {
            showTimePicker(time -> binding.txtOutTime.setText(time));
        });
    }

    private void showTimePicker(TimePickerCallback callback) {
        Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, (view, hourOfDay, minuteOfHour) -> {
            // Xử lý thời gian đã chọn
            time = hourOfDay + ":" + String.format("%02d", minuteOfHour);
            callback.onTimeSelected(time);
        }, hour, minute, true);

        timePickerDialog.show();
    }

    public interface TimePickerCallback {
        void onTimeSelected(final String time);
    }
}