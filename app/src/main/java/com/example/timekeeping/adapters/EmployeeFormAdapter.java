package com.example.timekeeping.adapters;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.timekeeping.R;
import com.example.timekeeping.models.Employee;

import java.util.ArrayList;
import java.util.List;

public class EmployeeFormAdapter extends RecyclerView.Adapter<EmployeeFormAdapter.viewHolder> {
    private List<Employee> employees = new ArrayList<>();
    private Context context;

    public EmployeeFormAdapter(List<Employee> employees){
        this.employees = employees;
    }

    @NonNull
    @Override
    public EmployeeFormAdapter.viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View inflate = LayoutInflater.from(context).inflate(R.layout.item_employee_form, parent, false);
        return new EmployeeFormAdapter.viewHolder(inflate);
    }

    @Override
    public void onBindViewHolder(@NonNull EmployeeFormAdapter.viewHolder holder, int position) {
        Employee employee = employees.get(position);

        // Gán dữ liệu có sẵn (nếu có)
        holder.txtEmployeeName.setText(employee.getName());
        holder.txtSalary.setText(employee.getSalary() > 0 ? String.valueOf(employee.getSalary()) : "");

        // Khởi tạo Spinner Adapter (chỉ khởi tạo 1 lần)
        if (holder.spd_role.getAdapter() == null) {
            ArrayAdapter<CharSequence> roleAdapter = ArrayAdapter.createFromResource(
                    context, R.array.role_list, android.R.layout.simple_spinner_item
            );
            roleAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spd_role.setAdapter(roleAdapter);
        }

        if (holder.spd_salary_calculation_method.getAdapter() == null) {
            ArrayAdapter<CharSequence> salaryAdapter = ArrayAdapter.createFromResource(
                    context, R.array.salary_calculation_method_list, android.R.layout.simple_spinner_item
            );
            salaryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            holder.spd_salary_calculation_method.setAdapter(salaryAdapter);
        }

        // Lắng nghe sự kiện nhập liệu để cập nhật Employee
        holder.txtEmployeeName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                employee.setName(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        holder.txtSalary.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                try {
                    employee.setSalary(Integer.parseInt(s.toString()));
                } catch (NumberFormatException e) {
                    employee.setSalary(0);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Lắng nghe sự kiện chọn Spinner
        holder.spd_role.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                employee.setRole(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        holder.spd_salary_calculation_method.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                employee.setMethod(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }


    @Override
    public int getItemCount() {
        return employees.size();
    }

    public class viewHolder extends RecyclerView.ViewHolder {
        TextView textSalary;
        EditText txtEmployeeName;
        EditText txtSalary;
        Spinner spd_role;
        Spinner spd_salary_calculation_method;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            txtEmployeeName = itemView.findViewById(R.id.txtEmployeeName);
            txtSalary = itemView.findViewById(R.id.txtSalary);
            spd_role = itemView.findViewById(R.id.spd_role);
            spd_salary_calculation_method = itemView.findViewById(R.id.spd_salary_calculation_method3);
            textSalary = itemView.findViewById(R.id.textSalary);
        }
    }

    public List<Employee> getEmployees() {
        return employees;
    }
}
