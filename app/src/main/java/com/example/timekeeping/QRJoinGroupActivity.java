package com.example.timekeeping;

import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.timekeeping.databinding.ActivityQrJoinGroupBinding;
import com.example.timekeeping.utils.QRGenerator;

public class QRJoinGroupActivity extends AppCompatActivity {

    private ActivityQrJoinGroupBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityQrJoinGroupBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        String groupId = getIntent().getStringExtra("group_id");
        Bitmap qrCodeBitmap = QRGenerator.generateQRCode(groupId);
        binding.imageView2.setImageBitmap(qrCodeBitmap);
    }
}