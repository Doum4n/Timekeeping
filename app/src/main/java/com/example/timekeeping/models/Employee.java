package com.example.timekeeping.models;

import com.google.firebase.database.Exclude;

import java.util.ArrayList;
import java.util.List;

public class Employee {
    public List<String> getGroups() {
        return groups;
    }

    public void setGroups(List<String> groups) {
        this.groups = groups;
    }

    @Exclude
    private String id; // Dùng khi cấp quyền
    private String name;
    private String method;
    private String position;
    private int salary;
    private String AccountId;
    private List<String> groups = new ArrayList<>();

    public Employee(){}

    public Employee(String name, String method, String position, int salary, String accountId, List<String> groups) {
        this.name = name;
        this.method = method;
        this.position = position;
        this.salary = salary;
        AccountId = accountId;
        this.groups = groups;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getAccountId() {
        return AccountId;
    }

    public void setAccountId(String accountId) {
        AccountId = accountId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
