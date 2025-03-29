package com.example.timekeeping.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.R;
import com.example.timekeeping.models.Shift;

import java.util.List;

public class ShiftManagerAdapter extends RecyclerView.Adapter<ShiftManagerAdapter.viewHolder> {

    private List<Shift> shifts;
    private Context context;
    public ShiftManagerAdapter(List<Shift> shifts) {
        this.shifts = shifts;
    }

    @NonNull
    @Override
    public ShiftManagerAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_shift_manager, parent, false);
        return new ShiftManagerAdapter.viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ShiftManagerAdapter.viewHolder holder, int position) {
        Shift shift = shifts.get(position);

        holder.txtShiftName.setText(shift.getName());
        holder.txtTime.setText(String.format("%s - %s", shift.getStartTime(), shift.getEndTime()));
    }

    @Override
    public int getItemCount() {
        return shifts.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {

        TextView txtShiftName, txtTime;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtShiftName = itemView.findViewById(R.id.txtShiftName);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
