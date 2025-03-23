package com.example.timekeeping.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.R;
import com.example.timekeeping.GrantPermissionsActivity;
import com.example.timekeeping.models.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.viewHolder> {

    private List<Employee> employees = new ArrayList<>();
    private Context context;

    public EmployeeAdapter(List<Employee> employees){
        this.employees = employees;
    }

    @NonNull
    @Override
    public EmployeeAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_employee, parent, false);
        return new EmployeeAdapter.viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeAdapter.viewHolder holder, int position) {
        Employee employee = employees.get(position);

        holder.txtEmployeeName.setText(employee.getName());

        holder.btnGrantPermissions.setOnClickListener(v -> {
            Intent intent = new Intent(context, GrantPermissionsActivity.class);
            intent.putExtra("employeeId", employee.getId());
            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtEmployeeName;
        Button btnGrantPermissions;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtEmployeeName = itemView.findViewById(R.id.txtEmployeeName);
            btnGrantPermissions = itemView.findViewById(R.id.btn_grant_permissions);
        }
    }
}
