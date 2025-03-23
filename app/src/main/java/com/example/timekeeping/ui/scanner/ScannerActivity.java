package com.example.timekeeping.ui.scanner;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.timekeeping.R;
import com.example.timekeeping.network.CameraService;
import com.example.timekeeping.network.LocationService;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class ScannerActivity extends AppCompatActivity {
    public enum ScannerType {
        Timekeeping,
        JoinWorkGroup
    }

    private static final int CAMERA_REQUEST_CODE = 100;
    private PreviewView previewView;
    private CameraService cameraService;
    private LocationService locationService;
    private ScannerType scannerType;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private Map<ScannerType, Consumer<String>> scannerHandlers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);

        Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        scannerType = ScannerType.valueOf(bundle.getString("type"));

        previewView = findViewById(R.id.previewView);
        Button scanButton = findViewById(R.id.scanButton);

        cameraService = new CameraService(this);

        // Ánh xạ ScannerType với phương thức xử lý tương ứng
        scannerHandlers = new EnumMap<>(ScannerType.class);
        scannerHandlers.put(ScannerType.Timekeeping, this::handleTimekeeping);
        scannerHandlers.put(ScannerType.JoinWorkGroup, this::handleJoinWorkGroup);

        // Kiểm tra quyền camera
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        } else {
            cameraService.startCamera(previewView, this::handleBarcodeResult);
        }

        locationService = new LocationService(this);
        locationService.requestLocationUpdates();

        scanButton.setOnClickListener(v -> cameraService.startCamera(previewView, this::handleBarcodeResult));
    }

    private void handleBarcodeResult(String rawValue) {
        scannerHandlers.getOrDefault(scannerType, value -> {
            Log.e("ScannerActivity", "Loại quét không hợp lệ");
            runOnUiThread(() -> Toast.makeText(this, "Lỗi: Loại quét không hợp lệ!", Toast.LENGTH_SHORT).show());
        }).accept(rawValue);

        cameraService.stopCamera();
    }

    private void handleTimekeeping(String rawValue) {
        Intent intent = new Intent(this, Clock_in_result_Activity.class);
        intent.putExtra("result", "Chấm công thành công");
        startActivity(intent);
    }

    private void handleJoinWorkGroup(String rawValue) {
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        // Tạo hai truy vấn độc lập
        Task<DocumentSnapshot> groupTask = db.collection("groups").document(rawValue).get();
        Task<DocumentSnapshot> employeeTask = db.collection("employees").document(userId).get();

        // Chạy song song hai truy vấn
        Tasks.whenAllSuccess(groupTask, employeeTask)
                .addOnSuccessListener(results -> {
                    DocumentSnapshot groupSnapshot = (DocumentSnapshot) results.get(0);
                    DocumentSnapshot employeeSnapshot = (DocumentSnapshot) results.get(1);

                    // Kiểm tra nhóm có tồn tại không
                    if (!groupSnapshot.exists()) {
                        runOnUiThread(() -> Toast.makeText(this, "Mã QR không hợp lệ!", Toast.LENGTH_SHORT).show());
                        return;
                    }

                    // Kiểm tra nhân viên đã tham gia nhóm chưa
                    List<String> groups = (List<String>) employeeSnapshot.get("groups");
                    if (groups != null && groups.contains(rawValue)) {
                        runOnUiThread(() -> Toast.makeText(this, "Bạn đã tham gia nhóm này!", Toast.LENGTH_SHORT).show());
                    } else {
                        addEmployeeToGroup(userId, rawValue);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Firebase", "Lỗi kiểm tra nhóm", e);
                    runOnUiThread(() -> Toast.makeText(this, "Lỗi kiểm tra nhóm!", Toast.LENGTH_SHORT).show());
                });
    }


    private void addEmployeeToGroup(String userId, String groupId) {
        db.collection("employees")
                .document(userId)
                .update("groups", FieldValue.arrayUnion(groupId))
                .addOnSuccessListener(aVoid -> runOnUiThread(() ->
                        Toast.makeText(this, "Tham gia nhóm thành công!", Toast.LENGTH_SHORT).show()))
                .addOnFailureListener(e -> Log.e("Firebase", "Lỗi cập nhật nhóm", e));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        cameraService.shutdown();
        locationService.stopLocationUpdates();
    }
}
