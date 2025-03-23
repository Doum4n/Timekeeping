package com.example.timekeeping.models;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Shift {
    private String name;
    private String startTime;
    private String endTime;
    private List<Employee>  employees= new ArrayList<>();
    private String groupId;
    private static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
    public Shift(){}

    public Shift(String name, String startTime, String endTime, List<Employee> employees, String groupId) {
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employees = employees;
        this.groupId = groupId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }


    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }
}
