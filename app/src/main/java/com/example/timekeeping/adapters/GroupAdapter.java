package com.example.timekeeping.adapters;

import static android.app.PendingIntent.getActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.MyShiftsActivity;
import com.example.timekeeping.models.Employee;
import com.example.timekeeping.ui.group.EmployeeManagementActivity;
import com.example.timekeeping.ui.group.GroupActivity;
import com.example.timekeeping.R;
import com.example.timekeeping.models.Group;
import com.example.timekeeping.ui.scanner.ScannerActivity;

import java.util.ArrayList;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.viewHolder> {
    private ArrayList<Group> groups;
    private Context context;
    public GroupAdapter(ArrayList<Group> groups) {
        this.groups = groups;
    }

    @NonNull
    @Override
    public GroupAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_group, parent, false);
        return new viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Group group = groups.get(position);

        holder.txtGroupName.setText(groups.get(position).getName());

        holder.cstLayoutGroupItem.setOnClickListener(v -> {
            Intent intent = new Intent(context, GroupActivity.class);
            intent.putExtra("group_id", group.getId());
            context.startActivity(intent);
        });

        //TODO đặt lại tên nút
        holder.btnTimekeeping.setOnClickListener(v -> {
            Intent intent = new Intent(context, MyShiftsActivity.class);
            intent.putExtra("groupId", group.getId());
            context.startActivity(intent);
        });

        holder.btnMember.setOnClickListener(v -> {
            Intent intent = new Intent(context, EmployeeManagementActivity.class);
            intent.putExtra("groupId", group.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return groups.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtGroupName;
        ConstraintLayout cstLayoutGroupItem;
        Button btnTimekeeping;
        Button btnMember;
        public viewHolder(View itemView) {
            super(itemView);
            cstLayoutGroupItem = itemView.findViewById(R.id.cstLayout_group);
            txtGroupName = itemView.findViewById(R.id.txtGroupName);
            btnTimekeeping = itemView.findViewById(R.id.btn_Clock_in);
            btnMember = itemView.findViewById(R.id.btnMembers);
        }
    }
}
