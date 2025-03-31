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

import com.example.timekeeping.EmployeeDetail;
import com.example.timekeeping.R;
import com.example.timekeeping.GrantPermissionsActivity;
import com.example.timekeeping.models.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeAdapter extends RecyclerView.Adapter<EmployeeAdapter.viewHolder> {

    private List<Employee> employees = new ArrayList<>();
    private Context context;
    private int tab;
    public EmployeeAdapter(List<Employee> employees, int tab){
        this.employees = employees;
        this.tab = tab;
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

        switch (tab){
            case 0:
                holder.btnAction.setText("Xem chi tiết");
                holder.btnAction.setOnClickListener(v -> {
                    Intent intent = new Intent(context, EmployeeDetail.class);
                    intent.putExtra("employeeId", employee.getId());
                    context.startActivity(intent);
                });
                break;
            case 1:
                holder.btnAction.setText("Liên kết");
                holder.btnAction.setOnClickListener(v -> {
                    Intent intent = new Intent(context, GrantPermissionsActivity.class);
                    intent.putExtra("employeeId", employee.getId());
                    context.startActivity(intent);
                });
                break;
        }
    }

    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView txtEmployeeName;
        Button btnAction;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtEmployeeName = itemView.findViewById(R.id.txtEmployeeName);
            btnAction = itemView.findViewById(R.id.btn_action);
        }
    }
}
