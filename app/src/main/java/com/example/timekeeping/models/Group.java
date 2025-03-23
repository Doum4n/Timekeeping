package com.example.timekeeping.models;

import com.google.firebase.firestore.Exclude;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Group {
    @Exclude
    private String id;  // Để truy xuất, không cần lưu vào firestore
    private String creatorId; // = AccountId
    private String name;
    private Date Payday;
    private List<Employee> employees = new ArrayList<>();
    public Group() {}

    public Group(String creatorId, String name, Date payday, List<Employee> employees) {
        this.creatorId = creatorId;
        this.name = name;
        Payday = payday;
        this.employees = employees;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getPayday() {
        return Payday;
    }

    public void setPayday(Date payday) {
        Payday = payday;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        if (id != null)
            this.id = id;
    }

    public String getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(String creatorId) {
        this.creatorId = creatorId;
    }
}
