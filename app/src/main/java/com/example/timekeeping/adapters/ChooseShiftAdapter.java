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

public class ChooseShiftAdapter extends RecyclerView.Adapter<ChooseShiftAdapter.viewHolder> {
    private List<Shift> shiftList;
    private Context context;

    public ChooseShiftAdapter(List<Shift> shiftList) {
        this.shiftList = shiftList;
    }

    @NonNull
    @Override
    public ChooseShiftAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_choose_shift, parent, false);
        return new ChooseShiftAdapter.viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull ChooseShiftAdapter.viewHolder holder, int position) {
        Shift shift = shiftList.get(position);
        holder.txtShiftName.setText(shift.getName());
        holder.txtTime.setText(String.format("%s - %s", shift.getStartTime(), shift.getEndTime()));
    }

    @Override
    public int getItemCount() {
        return shiftList.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtShiftName;
        TextView txtTime;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtShiftName = itemView.findViewById(R.id.txtShiftName);
            txtTime = itemView.findViewById(R.id.txtTime);
        }
    }
}
