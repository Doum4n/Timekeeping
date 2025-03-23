package com.example.timekeeping.ui.group;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timekeeping.databinding.ActivityGroupBinding;

public class GroupActivity extends AppCompatActivity {

    private ActivityGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

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
    }
}