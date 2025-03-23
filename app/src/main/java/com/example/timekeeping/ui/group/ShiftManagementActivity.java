package com.example.timekeeping.ui.group;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.R;
import com.example.timekeeping.adapters.ShiftAdapter;
import com.example.timekeeping.databinding.ActivityShiftManagementBinding;
import com.example.timekeeping.models.Shift;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class ShiftManagementActivity extends AppCompatActivity {

    private ActivityShiftManagementBinding binding;
    private String groupId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityShiftManagementBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        groupId = getIntent().getStringExtra("groupId");

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        initList();
    }

    private void initList() {

        List<Shift> shifts = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("shifts").where(Filter.equalTo("groupId", groupId)).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()){
                        Shift shift = document.toObject(Shift.class);
                        shifts.add(shift);
                    }

                    binding.rswListShift.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    RecyclerView.Adapter<ShiftAdapter.viewHolder> adapter = new ShiftAdapter(shifts);
                    binding.rswListShift.setAdapter(adapter);

                }).addOnFailureListener(e -> Log.w("Firestore", "Lỗi khi lấy danh sách ca làm việc!", e));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_add) {
            Intent intent = new Intent(ShiftManagementActivity.this, AddShiftActivity.class);
            intent.putExtra("groupId", groupId);
            startActivity(intent);
            return true;
        }else if (item.getItemId() == R.id.action_settings) {

            return true;
        }else if (item.getItemId() == android.R.id.home) {
            getOnBackPressedDispatcher().onBackPressed();
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