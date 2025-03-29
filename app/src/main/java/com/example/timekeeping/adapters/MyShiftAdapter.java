package com.example.timekeeping.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.R;
import com.example.timekeeping.models.Shift;
import com.example.timekeeping.ui.scanner.ScannerActivity;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class MyShiftAdapter extends RecyclerView.Adapter<MyShiftAdapter.viewHolder> {

    private List<Shift> shifts = new ArrayList<>();
    private Context context;
    private String groupId;

    public MyShiftAdapter(List<Shift> shifts, String groupId) {
        this.shifts = shifts;
        this.groupId = groupId;
    }

    @NonNull
    @Override
    public MyShiftAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_my_shift, parent, false);
        return new MyShiftAdapter.viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull MyShiftAdapter.viewHolder holder, int position) {
        Shift shift = shifts.get(position);

        holder.txtShiftName.setText(shift.getName());
        holder.txtTime.setText(String.format("%s - %s", shift.getStartTime(), shift.getEndTime()));

        holder.btnCheckIn.setOnClickListener(v -> {
            Intent intent = new Intent(context, ScannerActivity.class);
            Bundle bundle = new Bundle();
            bundle.putString("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());
            bundle.putString("group_id", groupId);
            bundle.putString("shift_id", shift.getId());
            bundle.putString("type","Timekeeping");
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtShiftName, txtTime, txtDate;
        Button btnCheckIn;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtShiftName = itemView.findViewById(R.id.txtShiftName);
            txtTime = itemView.findViewById(R.id.txtShiftTime);
//            txtDate = itemView.findViewById(R.id.textView33);
            btnCheckIn = itemView.findViewById(R.id.btnCheckIn);
        }
    }
}
