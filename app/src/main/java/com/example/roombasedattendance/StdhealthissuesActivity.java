package com.example.roombasedattendance;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ProgressBar;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class StdhealthissuesActivity extends AppCompatActivity {

    private TextInputEditText etHealthIssue, etRoomNumber, etRegNumber, etStudentName;
    private TextInputLayout tilHealthIssue, tilRoomNumber, tilRegNumber, tilStudentName;
    private CircularProgressIndicator progressBar;
    private DatabaseReference database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdhealthissues);

        // Initialize Firebase database reference under "authorityLogin/health_issues"
        database = FirebaseDatabase.getInstance().getReference("authorityLogin").child("health_issues");

        // Initialize Views
        tilHealthIssue = findViewById(R.id.til_health_issue);
        tilRoomNumber = findViewById(R.id.til_health_room_number);
        tilRegNumber = findViewById(R.id.til_health_reg_number);
        tilStudentName = findViewById(R.id.til_health_student_name);

        etHealthIssue = findViewById(R.id.et_health_issue);
        etRoomNumber = findViewById(R.id.et_health_room_number);
        etRegNumber = findViewById(R.id.et_health_reg_number);
        etStudentName = findViewById(R.id.et_health_student_name);

        progressBar = findViewById(R.id.progress_bar2);

        // Initialize submit button and set click listener
        MaterialButton btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(this::submitHealthIssue);
    }

    // Method to handle health issue submission
    public void submitHealthIssue(View view) {
        String healthIssue = etHealthIssue.getText().toString().trim();
        String roomNumber = etRoomNumber.getText().toString().trim();
        String regNumber = etRegNumber.getText().toString().trim();
        String studentName = etStudentName.getText().toString().trim();

        // Check if all fields are filled
        if (healthIssue.isEmpty() || roomNumber.isEmpty() || regNumber.isEmpty() || studentName.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show progress bar while submitting
        progressBar.setVisibility(View.VISIBLE);

        // Generate a new unique ID for the health issue
        String id = database.push().getKey();
        HealthIssue newIssue = new HealthIssue(healthIssue, roomNumber, regNumber, studentName);

        // Push data to Firebase
        if (id != null) {
            database.child(id).setValue(newIssue).addOnCompleteListener(task -> {
                // Hide the progress bar once submission is complete
                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    Toast.makeText(this, "Health Issue Submitted.", Toast.LENGTH_SHORT).show();
                    etHealthIssue.setText(""); // Clear the input fields after successful submission
                    etRoomNumber.setText("");
                    etRegNumber.setText("");
                    etStudentName.setText("");
                } else {
                    Toast.makeText(this, "Failed to submit health issue. Please try again.", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
