package com.example.roombasedattendance;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class StdmessfedbkActivity extends AppCompatActivity {

    private TextView tvMessFeedbackTitle;
    private TextInputEditText etMessFeedback, etEventDate;
    private MaterialButton btnSubmitFeedback;
    private ProgressBar progressBar;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdmessfedbk);

        // Initialize views
        tvMessFeedbackTitle = findViewById(R.id.tv_mess_feedback_title);
        etMessFeedback = findViewById(R.id.et_mess_feedback);
        etEventDate = findViewById(R.id.et_event_date);
        btnSubmitFeedback = findViewById(R.id.btn_schedule_event);
        progressBar = findViewById(R.id.progress_bar4);

        // Initialize Firebase Database reference
        databaseReference = FirebaseDatabase.getInstance().getReference("authorityLogin").child("mess_feedback");

        // Show DatePicker when clicking on the event date field
        etEventDate.setOnClickListener(v -> showDatePickerDialog());

        // Set the Submit Feedback button click listener
        btnSubmitFeedback.setOnClickListener(v -> submitFeedback());
    }

    // Function to display DatePickerDialog
    private void showDatePickerDialog() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                StdmessfedbkActivity.this,
                (view, selectedYear, selectedMonth, selectedDay) -> {
                    // Set the selected date in the Event Date field
                    etEventDate.setText(String.format("%02d/%02d/%d", selectedDay, selectedMonth + 1, selectedYear));
                },
                year, month, day
        );

        datePickerDialog.show();
    }

    // Function to handle feedback submission
    private void submitFeedback() {
        String feedback = etMessFeedback.getText().toString().trim();
        String eventDate = etEventDate.getText().toString().trim();

        // Check if feedback or event date is empty
        if (TextUtils.isEmpty(feedback) || TextUtils.isEmpty(eventDate)) {
            Toast.makeText(StdmessfedbkActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
        } else {
            // Show progress bar while submitting feedback
            progressBar.setVisibility(View.VISIBLE);

            String timestamp = String.valueOf(System.currentTimeMillis());

            // Create a new MessFeedback object
            MessFeedback messFeedback = new MessFeedback(feedback, eventDate, timestamp);

            // Push the feedback to Firebase
            databaseReference.push().setValue(messFeedback).addOnCompleteListener(task -> {
                // Hide progress bar after submission
                progressBar.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    Toast.makeText(StdmessfedbkActivity.this, "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                    etMessFeedback.setText("");  // Clear the feedback input field
                    etEventDate.setText("");    // Clear the event date field
                } else {
                    Toast.makeText(StdmessfedbkActivity.this, "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
