package com.example.roombasedattendance;

public class ElectricalIssue {

    private String issueDescription;
    private String roomNumber;
    private String regNumber;
    private String studentName;
    private String startTime;
    private String endTime;

    public ElectricalIssue() {
        // Default constructor required for Firebase
    }

    public ElectricalIssue(String issueDescription, String roomNumber, String regNumber, String studentName, String startTime, String endTime) {
        this.issueDescription = issueDescription;
        this.roomNumber = roomNumber;
        this.regNumber = regNumber;
        this.studentName = studentName;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public String getIssueDescription() {
        return issueDescription;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public String getStudentName() {
        return studentName;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
