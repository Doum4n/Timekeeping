package com.example.timekeeping.models;

import java.util.Date;

public class Attendance {
    private String userId;
    private String groupId;
    private String shiftId;
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

    public Attendance() {}

    public Attendance(String userId, String groupId, String shiftId, Date inTime, Date outTime) {
        this.userId = userId;
        this.groupId = groupId;
        this.shiftId = shiftId;
        this.inTime = inTime;
        this.outTime = outTime;
    }


    public String getShifts() {
        return shiftId;
    }

    public void setShifts(String shiftId) {
        this.shiftId = shiftId;
    }
}
