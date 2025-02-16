package com.example.roombasedattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AuthorityhomeActivity extends AppCompatActivity {

    private static final String TAG = "AuthorityhomeActivity"; // Add log tag for debugging

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authorityhome); // Ensure the XML layout filename is correct
    }

    // Method to open the Student Face Registration screen
    public void onStudentFaceRegistrationClick(View view) {
        // Navigate to the Student Face Registration Activity
        Intent intent = new Intent(AuthorityhomeActivity.this, FaceregistrationActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to Student Face Registration", Toast.LENGTH_SHORT).show();
    }

    // Method to open the View Attendance screen
    public void onViewAttendanceClick(View view) {
        // Navigate to the View Attendance Activity
        Intent intent = new Intent(AuthorityhomeActivity.this, ViewpntabstActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to View Attendance", Toast.LENGTH_SHORT).show();
    }

    // Method to open the Schedule Hostel Events screen
    public void onScheduleHostelEventsClick(View view) {
        // Navigate to the Hostel Event Scheduling Activity
        Intent intent = new Intent(AuthorityhomeActivity.this, SheduleeventActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to Schedule Hostel Events", Toast.LENGTH_SHORT).show();
    }

    // Method to open the View Mess Feedback screen
    public void onViewMessFeedbackClick(View view) {
        // Navigate to the View Mess Feedback Activity
        Intent intent = new Intent(AuthorityhomeActivity.this, ViewmessfdbkActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to View Mess Feedback", Toast.LENGTH_SHORT).show();
    }

    // Method to open the View Electrical Issues screen
    public void onViewElectricalIssuesClick(View view) {
        // Navigate to the View Electrical Issues Activity
        Intent intent = new Intent(AuthorityhomeActivity.this, ViewelectricalissuesActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to View Electrical Issues", Toast.LENGTH_SHORT).show();
    }

    // Method to open the View Health Issues screen
    public void onViewHealthIssuesClick(View view) {
        // Navigate to the View Health Issues Activity
        Intent intent = new Intent(AuthorityhomeActivity.this, ViewhealthissuesActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Navigating to View Health Issues", Toast.LENGTH_SHORT).show();
    }

    // Logout method
    public void onLogoutClick(View view) {
        // Log that the logout method is being called
        Log.d(TAG, "onLogoutClick() called");

        // Clear user session or authentication tokens (if needed)
        clearUserSession();

        // Navigate to the login page
        Intent intent = new Intent(AuthorityhomeActivity.this, AuthorityloginActivity.class);
        startActivity(intent);
        finish();  // Close the current activity so that the user cannot navigate back to it

        // Log that the navigation to login is happening
        Log.d(TAG, "Navigating to AuthorityloginActivity");
    }

    // Method to clear user session
    private void clearUserSession() {
        // Use Android's SharedPreferences to clear session data
        SharedPreferences preferences = getSharedPreferences("AuthoritySession", MODE_PRIVATE); // Ensure the key is consistent
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();  // Clear all session data
        editor.apply();  // Save changes
        Log.d(TAG, "User session cleared");
    }
}
