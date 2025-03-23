package com.example.timekeeping.ui.scanner;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.timekeeping.databinding.ActivityClockInResultBinding;

public class Clock_in_result_Activity extends AppCompatActivity {

    private ActivityClockInResultBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityClockInResultBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        String result = getIntent().getStringExtra("result");

        if (result != null) {
            binding.txtResult.setText(result);
        }
    }
}