package com.example.roombasedattendance;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class StdelectricalissuesActivity extends AppCompatActivity {

    private TextInputEditText etIssue, etRoomNumber, etRegNumber, etStudentName, etStartTime, etEndTime;
    private AppCompatButton btnSubmitIssue;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    private int hour, minute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdelectricalissues);

        // Initialize Firebase
        databaseReference = FirebaseDatabase.getInstance().getReference("authorityLogin").child("electrical_issues");

        // Initialize Views
        etIssue = findViewById(R.id.et_electrical_issue);
        etRoomNumber = findViewById(R.id.et_electrical_room_number);
        etRegNumber = findViewById(R.id.et_electrical_reg_number);
        etStudentName = findViewById(R.id.et_electrical_student_name);
        etStartTime = findViewById(R.id.et_start_time);
        etEndTime = findViewById(R.id.et_end_time);
        btnSubmitIssue = findViewById(R.id.btn_schedule_event);
        progressBar = findViewById(R.id.progress_bar1);

        // Time Picker logic for Start Time
        etStartTime.setOnClickListener(view -> showTimePickerDialog(etStartTime));

        // Time Picker logic for End Time
        etEndTime.setOnClickListener(view -> showTimePickerDialog(etEndTime));

        // Submit Issue logic
        btnSubmitIssue.setOnClickListener(view -> submitIssue());
    }

    // Method to show the TimePickerDialog
    private void showTimePickerDialog(final TextInputEditText editText) {
        final Calendar calendar = Calendar.getInstance();
        hour = calendar.get(Calendar.HOUR_OF_DAY);
        minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(
                StdelectricalissuesActivity.this,
                (view, hourOfDay, minuteOfHour) -> {
                    editText.setText(String.format("%02d:%02d", hourOfDay, minuteOfHour));
                },
                hour, minute, true
        );
        timePickerDialog.show();
    }

    // Method to submit the issue
    private void submitIssue() {
        String issueDescription = etIssue.getText().toString().trim();
        String roomNumber = etRoomNumber.getText().toString().trim();
        String regNumber = etRegNumber.getText().toString().trim();
        String studentName = etStudentName.getText().toString().trim();
        String startTime = etStartTime.getText().toString().trim();
        String endTime = etEndTime.getText().toString().trim();

        // Validate the inputs
        if (issueDescription.isEmpty() || roomNumber.isEmpty() || regNumber.isEmpty() || studentName.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create an ElectricalIssue object
        ElectricalIssue electricalIssue = new ElectricalIssue(issueDescription, roomNumber, regNumber, studentName, startTime, endTime);

        // Show progress bar while uploading
        progressBar.setVisibility(View.VISIBLE);

        // Upload the issue to Firebase
        databaseReference.push().setValue(electricalIssue).addOnCompleteListener(task -> {
            progressBar.setVisibility(View.GONE);
            if (task.isSuccessful()) {
                Toast.makeText(StdelectricalissuesActivity.this, "Issue submitted successfully", Toast.LENGTH_SHORT).show();
                clearFields();
            } else {
                Toast.makeText(StdelectricalissuesActivity.this, "Failed to submit the issue", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Method to clear all fields after submission
    private void clearFields() {
        etIssue.setText("");
        etRoomNumber.setText("");
        etRegNumber.setText("");
        etStudentName.setText("");
        etStartTime.setText("");
        etEndTime.setText("");
    }
}
