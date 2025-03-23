package com.example.timekeeping.ui.home;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.timekeeping.databinding.ActivityAddGroupBinding;
import com.example.timekeeping.models.Group;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class AddGroupActivity extends AppCompatActivity {

    private ActivityAddGroupBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityAddGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        db = FirebaseFirestore.getInstance();

        binding.btnSave.setOnClickListener(v -> {
            String groupName = binding.txtNewGroupName.getText().toString();
            String payDateStr = binding.txtPaydate.getText().toString();

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
            try {
                Date payDate = dateFormat.parse(payDateStr);
                if (!groupName.isEmpty() && payDate != null) {
                    saveGroupToFirebase(groupName, payDate);
                }
            } catch (ParseException e) {
                Toast.makeText(this, "Ngày không hợp lệ!", Toast.LENGTH_SHORT).show();
            }
        });

        binding.btnPayDate.setOnClickListener(v -> showDatePickerDialog());
    }

    private void saveGroupToFirebase(String groupName, Date payDate) {
        db.collection("groups")
                .document()
                .set(new Group(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid(), groupName, payDate, List.of()))
                .addOnSuccessListener(v -> Toast.makeText(AddGroupActivity.this, "Nhóm đã được lưu thành công", Toast.LENGTH_SHORT).show());
    }

    private void showDatePickerDialog() {
        Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    String selectedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                    binding.txtPaydate.setText(selectedDate);
                },
                year, month, day
        );

        datePickerDialog.show();
    }
}