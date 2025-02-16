package com.example.roombasedattendance;

public class RoomRegistration {
    private String roomNumber;
    private String roomType;
    private String bedCount;

    public RoomRegistration(String roomNumber, String roomType, String bedCount) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.bedCount = bedCount;
    }

    // Getter and Setter methods
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    public String getBedCount() {
        return bedCount;
    }

    public void setBedCount(String bedCount) {
        this.bedCount = bedCount;
    }
}
