package com.example.roombasedattendance;

public class HealthIssue {

    // Fields for Health Issue details
    private String healthIssue;
    private String roomNumber;
    private String regNumber;
    private String studentName;

    // Default constructor required for Firebase
    public HealthIssue() {
    }

    // Constructor to initialize HealthIssue object
    public HealthIssue(String healthIssue, String roomNumber, String regNumber, String studentName) {
        this.healthIssue = healthIssue;
        this.roomNumber = roomNumber;
        this.regNumber = regNumber;
        this.studentName = studentName;
    }

    // Getter methods
    public String getHealthIssue() {
        return healthIssue;
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

    // Setter methods (optional, if needed for updating)
    public void setHealthIssue(String healthIssue) {
        this.healthIssue = healthIssue;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }
}
