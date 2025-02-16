package com.example.roombasedattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

public class StudenthomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studenthome); // Ensure XML layout file is named activity_studenthome.xml

        // Check if user is logged in
        checkUserLogin();
    }

    // Method to check if the user is logged in
    private void checkUserLogin() {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        // If not logged in, redirect to login activity
        if (!isLoggedIn) {
            Intent intent = new Intent(StudenthomeActivity.this, StudentloginActivity.class);
            startActivity(intent);
            finish();  // Close the current activity to prevent back navigation
        }
    }

    // Open Student Attendance Activity
    public void openStudentAttendance(View view) {
        Intent intent = new Intent(StudenthomeActivity.this, StdattendanceActivity.class);
        startActivity(intent);
    }

    // Open Hostel Events Activity
    public void openHostelEvents(View view) {
        Intent intent = new Intent(StudenthomeActivity.this, StdeventActivity.class);
        startActivity(intent);
    }

    // Open Mess Feedback Activity
    public void openMessFeedback(View view) {
        Intent intent = new Intent(StudenthomeActivity.this, StdmessfedbkActivity.class);
        startActivity(intent);
    }

    // Open Electrical Issues Reporting Activity
    public void openElectricalIssues(View view) {
        Intent intent = new Intent(StudenthomeActivity.this, StdelectricalissuesActivity.class);
        startActivity(intent);
    }

    // Open Health Issues Reporting Activity
    public void openHealthIssues(View view) {
        Intent intent = new Intent(StudenthomeActivity.this, StdhealthissuesActivity.class);
        startActivity(intent);
    }

    // Logout functionality
    public void onLogoutClick(View view) {
        // Clear user session or authentication tokens (if needed)
        clearUserSession();

        // Navigate to the login page
        Intent intent = new Intent(StudenthomeActivity.this, StudentloginActivity.class);
        startActivity(intent);
        finish();  // Close the current activity so that the user cannot navigate back to it
    }

    // Clear user session (SharedPreferences or any session management)
    private void clearUserSession() {
        SharedPreferences preferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Clear all session data
        editor.apply();  // Save changes
    }
}
