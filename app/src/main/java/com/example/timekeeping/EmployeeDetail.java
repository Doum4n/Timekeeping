package com.example.timekeeping;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timekeeping.base.BaseActivity;
import com.example.timekeeping.databinding.ActivityEmployeeDetailBinding;

public class EmployeeDetail extends BaseActivity {

    private ActivityEmployeeDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEmployeeDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setupToolbar(binding.toolbar, "Tên nhân viên - Chức vụ");

        binding.btnViewInfo.setOnClickListener(v -> {
            Intent intent = new Intent(EmployeeDetail.this, EmployeeInformation.class);
            startActivity(intent);
        });
    }
}