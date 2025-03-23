package com.example.timekeeping.models;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Timekeeping {
    private String userId;
    private String groupId;
    private List<Shift> shifts = new ArrayList<>();
    private Date inTime;
    private Date outTime;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public Date getInTime() {
        return inTime;
    }

    public void setInTime(Date inTime) {
        this.inTime = inTime;
    }

    public Date getOutTime() {
        return outTime;
    }

    public void setOutTime(Date outTime) {
        this.outTime = outTime;
    }

    public Timekeeping() {}

    public Timekeeping(String userId, String groupId, List<Shift> shifts, Date inTime, Date outTime) {
        this.userId = userId;
        this.groupId = groupId;
        this.shifts = shifts;
        this.inTime = inTime;
        this.outTime = outTime;
    }


    public List<Shift> getShifts() {
        return shifts;
    }

    public void setShifts(List<Shift> shifts) {
        this.shifts = shifts;
    }
}
