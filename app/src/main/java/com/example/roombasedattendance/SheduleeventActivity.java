package com.example.roombasedattendance;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import java.util.Calendar;

public class SheduleeventActivity extends AppCompatActivity {

    private TextInputEditText etEventName, etEventDate, etEventTime, etEventVenue, etEventDetails;
    private DatabaseReference roomRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheduleevent);

        // Initialize input fields
        etEventName = findViewById(R.id.et_event_name);
        etEventDate = findViewById(R.id.et_event_date);
        etEventTime = findViewById(R.id.et_event_time);
        etEventVenue = findViewById(R.id.et_event_venue);
        etEventDetails = findViewById(R.id.et_event_details);

        // Initialize the roomRef to point to authorityLogin > hostelevents in Firebase
        roomRef = FirebaseDatabase.getInstance().getReference("authorityLogin").child("hostelevents");

        // Set up DatePicker for Event Date
        etEventDate.setOnClickListener(v -> showDatePicker());

        // Set up TimePicker for Event Time
        etEventTime.setOnClickListener(v -> showTimePicker());

        // Set up the Schedule Event button
        findViewById(R.id.btn_schedule_event).setOnClickListener(v -> scheduleEvent());
    }

    // Show Date Picker dialog
    private void showDatePicker() {
        final Calendar calendar = Calendar.getInstance();
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, selectedYear, monthOfYear, dayOfMonth) ->
                        etEventDate.setText(String.format("%02d/%02d/%04d", dayOfMonth, monthOfYear + 1, selectedYear)),
                year, month, day);
        datePickerDialog.show();
    }

    // Show Time Picker dialog
    private void showTimePicker() {
        final Calendar calendar = Calendar.getInstance();
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) ->
                        etEventTime.setText(String.format("%02d:%02d", hourOfDay, minute1)),
                hour, minute, true);
        timePickerDialog.show();
    }

    private void scheduleEvent() {
        String eventName = etEventName.getText().toString().trim();
        String eventDate = etEventDate.getText().toString().trim();
        String eventTime = etEventTime.getText().toString().trim();
        String eventVenue = etEventVenue.getText().toString().trim();
        String eventDetails = etEventDetails.getText().toString().trim();

        if (TextUtils.isEmpty(eventName) || TextUtils.isEmpty(eventDate) || TextUtils.isEmpty(eventTime)
                || TextUtils.isEmpty(eventVenue) || TextUtils.isEmpty(eventDetails)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate unique event ID and create event object
        String eventId = roomRef.push().getKey();
        HostelEvent event = new HostelEvent(eventId, eventName, eventDate, eventTime, eventVenue, eventDetails);

        // Save event in Firebase
        roomRef.child(eventId).setValue(event).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(this, "Event Scheduled", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Error scheduling event", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
