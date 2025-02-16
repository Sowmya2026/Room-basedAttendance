package com.example.roombasedattendance;

public class RoomDetails {

    private String roomNumber;
    private String roomType;
    private String bedCount;
    private int currentStudentCount;

    public RoomDetails(String roomNumber, String roomType, String bedCount) {
        this.roomNumber = roomNumber;
        this.roomType = roomType;
        this.bedCount = bedCount;
        this.currentStudentCount = 0;  // Initially, no students are registered
    }

    // Getter and Setter for roomNumber
    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    // Getter and Setter for roomType
    public String getRoomType() {
        return roomType;
    }

    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }

    // Getter and Setter for bedCount
    public String getBedCount() {
        return bedCount;
    }

    public void setBedCount(String bedCount) {
        this.bedCount = bedCount;
    }

    // Getter and Setter for currentStudentCount
    public int getCurrentStudentCount() {
        return currentStudentCount;
    }

    public void setCurrentStudentCount(int currentStudentCount) {
        this.currentStudentCount = currentStudentCount;
    }

    // Helper function to extract numeric value from the bedCount string
    private int getParsedBedCount() {
        try {
            // Extract numeric part from bedCount (assumes it's like "4 Bed")
            String numericPart = bedCount.replaceAll("[^0-9]", ""); // Remove non-numeric characters
            return Integer.parseInt(numericPart);
        } catch (NumberFormatException e) {
            return 0; // Return 0 if parsing fails
        }
    }

    // Method to increment student count when a new student is registered
    public void incrementStudentCount() {
        int maxBeds = getParsedBedCount();
        if (currentStudentCount < maxBeds) {
            currentStudentCount++;
        }
    }

    // Method to check if the room is full
    public boolean isRoomFull() {
        return currentStudentCount >= getParsedBedCount();
    }
}
