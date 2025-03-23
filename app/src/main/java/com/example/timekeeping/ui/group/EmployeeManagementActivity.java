package com.example.timekeeping.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.R;
import com.example.timekeeping.adapters.EmployeeAdapter;
import com.example.timekeeping.databinding.ActivityEmployeeManagementBinding;
import com.example.timekeeping.models.Employee;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class EmployeeManagementActivity extends AppCompatActivity {

    private ActivityEmployeeManagementBinding binding;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        groupId = getIntent().getStringExtra("groupId");

        setSupportActionBar(binding.toolbar);

        initList();
    }

    private void initList() {
        List<Employee> employees = new ArrayList<>();

        db.collection("employees").whereArrayContains("groups", groupId).get()
                .addOnSuccessListener(v -> {
                    for (DocumentSnapshot document : v.getDocuments()) {
                        Employee employee = document.toObject(Employee.class);
                        employees.add(employee);
                    }

                    binding.rswEmployeeList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    RecyclerView.Adapter<EmployeeAdapter.viewHolder> adapter = new EmployeeAdapter(employees);
                    binding.rswEmployeeList.setAdapter(adapter);
                }).addOnFailureListener(e -> Log.w("Firestore", "Lỗi khi lấy danh sách nhân viên!", e));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(EmployeeManagementActivity.this, AddEmployeeActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initList();
    }
}