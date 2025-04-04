package com.example.timekeeping.ui.scanner;


import static androidx.core.content.ContextCompat.startActivity;

import android.content.Context;
import android.content.Intent;

import com.example.timekeeping.models.Attendance;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.type.DateTime;

import java.time.Instant;
import java.util.Date;

public class AttendanceHandle {
    private final Context context;
    private final FirebaseDatabase db;
    private final String groupId;
    private final String shiftId;
    private final FirebaseAuth auth;
    public AttendanceHandle(Context context, String groupId, String shiftId){
        this.context = context;
        this.groupId = groupId;
        this.shiftId = shiftId;

        db = FirebaseDatabase.getInstance();
        auth = FirebaseAuth.getInstance();
    }
    public void CheckIn(){

        Attendance attendance = new Attendance(auth.getCurrentUser().getUid(), groupId, shiftId, Date.from(Instant.now()), null);

        DatabaseReference attendanceRef = db.getReference("attendances");
        String AttendanceId = attendanceRef.push().getKey();
        attendanceRef.child(AttendanceId).setValue(attendance)
                .addOnSuccessListener(v -> {

                    Intent intent = new Intent(context, Clock_in_result_Activity.class);
                    intent.putExtra("result", "Chấm công thành công");
                    startActivity(context, intent, null);

                }).addOnFailureListener(v -> {

                });
    }

    public void CheckOut(){
        Attendance attendance = db.getReference("attendances").orderByChild("userId").equalTo(auth.getCurrentUser().getUid()).get().getResult().getValue(Attendance.class);
        assert attendance != null;
        attendance.setOutTime(Date.from(Instant.now()));
        db.getReference("attendances").child(attendance.getUserId()).setValue(attendance);
    }
}
