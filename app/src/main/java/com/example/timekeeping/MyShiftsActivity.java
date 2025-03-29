package com.example.timekeeping;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.adapters.MyShiftAdapter;
import com.example.timekeeping.databinding.ActivityMyShiftsBinding;
import com.example.timekeeping.models.Shift;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Filter;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class MyShiftsActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ActivityMyShiftsBinding binding;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMyShiftsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();
        init();
    }

    private void init(){
        List<Shift> shifts = new ArrayList<>();
        db.collection("shifts")
                .whereEqualTo("groupId", getIntent().getStringExtra("groupId"))
                .whereArrayContains("employees", auth.getCurrentUser().getUid()).get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot document : queryDocumentSnapshots.getDocuments()) {
                        Shift shift = document.toObject(Shift.class);
                        assert shift != null;
                        shift.setId(document.getId());
                        shifts.add(shift);
                    }

                    // test
                    binding.rsvHappenedShifts.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
                    RecyclerView.Adapter<MyShiftAdapter.viewHolder> adapter = new MyShiftAdapter(shifts, getIntent().getStringExtra("groupId"));
                    binding.rsvHappenedShifts.setAdapter(adapter);
                }).addOnFailureListener(e -> {

                });
    }
}