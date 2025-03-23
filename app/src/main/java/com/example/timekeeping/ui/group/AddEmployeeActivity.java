package com.example.timekeeping.ui.group;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.timekeeping.R;
import com.example.timekeeping.adapters.EmployeeFormAdapter;
import com.example.timekeeping.databinding.ActivityAddEmployeeBinding;
import com.example.timekeeping.models.Employee;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class AddEmployeeActivity extends AppCompatActivity {

    private ActivityAddEmployeeBinding binding;
    private EmployeeFormAdapter employeeAdapter;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddEmployeeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);
        groupId = getIntent().getStringExtra("groupId");

        List<Employee> employees = new ArrayList<>();
        employees.add(new Employee());
        employeeAdapter = new EmployeeFormAdapter(employees);
        binding.rswEmployeeForm.setLayoutManager(new LinearLayoutManager(this));
        binding.rswEmployeeForm.setAdapter(employeeAdapter);

        binding.btnAddNewEmployee.setOnClickListener(v -> {
            // Thêm một nhân viên mới vào danh sách
            Employee newEmployee = new Employee();
            employees.add(newEmployee);

            // Cập nhật RecyclerView
            employeeAdapter.notifyItemInserted(employees.size() - 1);
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_done, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }else if (item.getItemId() == R.id.action_done) {
            List<Employee> employeeList = employeeAdapter.getEmployees();
            employeeList.forEach(employee -> {
                employee.setGroups(List.of(groupId));
                db.collection("employees").document().set(employee);
            });

            getOnBackPressedDispatcher().onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}