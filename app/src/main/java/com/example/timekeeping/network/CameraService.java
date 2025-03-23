package com.example.timekeeping.network;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.ImageFormat;
import android.graphics.YuvImage;
import android.media.Image;
import android.util.Log;
import android.widget.Toast;

import androidx.camera.core.*;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.content.ContextCompat;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.barcode.*;
import com.google.mlkit.vision.barcode.common.Barcode;
import com.google.mlkit.vision.common.InputImage;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraService {
    private ProcessCameraProvider cameraProvider;
    private ExecutorService cameraExecutor;
    private final Context context;
    private final BarcodeScanner scanner;

    public interface BarcodeResultCallback {
        void onBarcodeScanned(String result);
    }

    public CameraService(Context context) {
        this.context = context;
        this.cameraExecutor = Executors.newSingleThreadExecutor();

        // Cấu hình barcode scanner
        scanner = BarcodeScanning.getClient(
                new BarcodeScannerOptions.Builder()
                        .setBarcodeFormats(Barcode.FORMAT_QR_CODE)
                        .build()
        );
    }

    public void startCamera(PreviewView previewView, BarcodeResultCallback callback) {
        ListenableFuture<ProcessCameraProvider> cameraProviderFuture =
                ProcessCameraProvider.getInstance(context);

        cameraProviderFuture.addListener(() -> {
            try {
                cameraProvider = cameraProviderFuture.get();
                cameraProvider.unbindAll();

                CameraSelector cameraSelector = new CameraSelector.Builder()
                        .requireLensFacing(CameraSelector.LENS_FACING_BACK)
                        .build();

                Preview preview = new Preview.Builder().build();
                preview.setSurfaceProvider(previewView.getSurfaceProvider());

                ImageAnalysis imageAnalysis = new ImageAnalysis.Builder()
                        .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                        .build();

                imageAnalysis.setAnalyzer(cameraExecutor, imageProxy -> analyzeImage(imageProxy, callback));

                cameraProvider.bindToLifecycle((androidx.lifecycle.LifecycleOwner) context, cameraSelector, preview, imageAnalysis);

            } catch (Exception e) {
                Log.e("CameraX", "Lỗi khởi động camera", e);
            }
        }, ContextCompat.getMainExecutor(context));
    }

    public void stopCamera() {
        if (cameraProvider != null) {
            cameraProvider.unbindAll();
            Log.d("CameraX", "Camera đã dừng");
        }
    }

    private void analyzeImage(ImageProxy imageProxy, BarcodeResultCallback callback) {
        try (imageProxy) {
            Bitmap bitmap = imageProxyToBitmap(imageProxy);
            scanBarcode(bitmap, callback);
        } catch (Exception e) {
            Log.e("CameraX", "Lỗi chuyển đổi ảnh", e);
        }
    }

    private void scanBarcode(Bitmap bitmap, BarcodeResultCallback callback) {
        InputImage image = InputImage.fromBitmap(bitmap, 0);

        scanner.process(image)
                .addOnSuccessListener(barcodes -> {
                    if (!barcodes.isEmpty()) {
                        String result = barcodes.get(0).getRawValue();
                        Log.d("Barcode", "Mã vạch: " + result);
                        callback.onBarcodeScanned(result);
                    } else {
                        Log.d("Barcode", "Không tìm thấy mã vạch");
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("Barcode", "Lỗi nhận diện mã vạch", e);
                    Toast.makeText(context, "Lỗi: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    public Bitmap imageProxyToBitmap(ImageProxy imageProxy) {
        Image image = imageProxy.getImage();
        if (image == null) {
            throw new IllegalStateException("ImageProxy.getImage() trả về null");
        }

        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        buffer.get(bytes);

        YuvImage yuvImage = new YuvImage(bytes, ImageFormat.NV21, imageProxy.getWidth(), imageProxy.getHeight(), null);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        yuvImage.compressToJpeg(new android.graphics.Rect(0, 0, imageProxy.getWidth(), imageProxy.getHeight()), 100, out);
        byte[] jpegBytes = out.toByteArray();

        return android.graphics.BitmapFactory.decodeByteArray(jpegBytes, 0, jpegBytes.length);
    }

    public void shutdown() {
        cameraExecutor.shutdown();
    }
}
