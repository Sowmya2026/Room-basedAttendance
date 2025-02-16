package com.example.roombasedattendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


public class StdattendanceActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 101;

    private TextInputEditText attendanceRoomNumber;
    private TextInputEditText attendanceStudentName;
    private Button captureAttendanceButton;
    private FirebaseStorage storage;
    private FirebaseDatabase database;
    private DatabaseReference attendanceDatabaseRef;

    // Register ActivityResultLauncher for camera
    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    Bitmap capturedImage = (Bitmap) result.getData().getExtras().get("data");
                    markAttendanceBasedOnNameAndRoom(capturedImage);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stdattendance);

        // Initialize UI elements
        attendanceRoomNumber = findViewById(R.id.attendanceRoomNumber);
        attendanceStudentName = findViewById(R.id.attendacestudnetname);
        captureAttendanceButton = findViewById(R.id.captureAttendanceButton);

        // Initialize Firebase Storage and Database
        storage = FirebaseStorage.getInstance();
        database = FirebaseDatabase.getInstance();
        attendanceDatabaseRef = database.getReference("Attendance");

        // Set up capture button with time check
        captureAttendanceButton.setOnClickListener(v -> checkTimeAndCapture());

        // Handle camera permission
        checkCameraPermission();
    }

    private void checkTimeAndCapture() {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        if (currentHour >= 8 && currentHour <= 19) {
            captureAttendanceImage();
        } else {
            Toast.makeText(this, "Attendance can only be marked between 8 AM and 7 PM.", Toast.LENGTH_SHORT).show();
        }
    }

    private void captureAttendanceImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(cameraIntent);
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    private void markAttendanceBasedOnNameAndRoom(Bitmap capturedImage) {
        String roomNumber = attendanceRoomNumber.getText().toString().trim();
        String studentName = attendanceStudentName.getText().toString().trim();

        if (roomNumber.isEmpty() || studentName.isEmpty()) {
            Toast.makeText(this, "Please enter both room number and student name", Toast.LENGTH_SHORT).show();
            return;
        }

        // Path to check if the student is registered in the roomsregistered path
        StorageReference roomsRegisteredRef = storage.getReference().child("roomsregistered/" + roomNumber + "/" + studentName + ".jpg");

        // Check if the student is registered in roomsregistered path
        roomsRegisteredRef.getMetadata().addOnSuccessListener(metadata -> {
            // Student is registered, proceed to check attendance
            checkAttendanceForRegisteredStudent(roomNumber, studentName, capturedImage);
        }).addOnFailureListener(e -> {
            // Student is not registered in roomsregistered, no need to mark absent
            Toast.makeText(StdattendanceActivity.this, "Student is not registered for this room", Toast.LENGTH_SHORT).show();
        });
    }
    private void checkAttendanceForRegisteredStudent(String roomNumber, String studentName, Bitmap capturedImage) {
        // Get current date in format (yyyy-MM-dd)
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        // Path to check attendance for this student today
        DatabaseReference attendanceRef = attendanceDatabaseRef.child(currentDate)
                .child(roomNumber)
                .child(studentName);

        // Check if the student has already marked attendance for today
        attendanceRef.child("status").get().addOnSuccessListener(snapshot -> {
            if (snapshot.exists()) {
                String status = snapshot.getValue(String.class);
                if (status != null && status.equals("present")) {
                    // If status is 'present', show a message that attendance is already marked
                    Toast.makeText(StdattendanceActivity.this, "Already marked present", Toast.LENGTH_SHORT).show();
                } else {
                    // If not marked present, check attendance and mark as absent or present
                    markAttendanceAsAbsentIfOutsideTimeLimit(roomNumber, studentName, capturedImage);
                }
            } else {
                // If student has not marked attendance, proceed to mark attendance
                markAttendanceAsAbsentIfOutsideTimeLimit(roomNumber, studentName, capturedImage);
            }
        }).addOnFailureListener(e -> {
            // Handle any failure in reading data from the database
            Toast.makeText(StdattendanceActivity.this, "Error checking attendance: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        });
    }


    private void markAttendanceAsAbsentIfOutsideTimeLimit(String roomNumber, String studentName, Bitmap capturedImage) {
        Calendar calendar = Calendar.getInstance();
        int currentHour = calendar.get(Calendar.HOUR_OF_DAY);

        // If the student hasn't marked attendance and the time is within the allowed limit
        if (currentHour < 8 || currentHour > 19) {
            // Mark as absent if the student didn't mark attendance within the allowed time
            markAttendance(roomNumber, studentName, "absent", capturedImage);
        } else {
            // Otherwise, they will be marked as present
            markAttendance(roomNumber, studentName, "present", capturedImage);
        }
    }

    private void markAttendance(String roomNumber, String studentName, String status, Bitmap capturedImage) {
        // Get the current date (e.g., "2024-11-15")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        // Path to save attendance image with current date
        StorageReference imageRef = storage.getReference().child("Attendance/" + currentDate + "/" + roomNumber + "/" + studentName + "/" + status + ".jpg");

        // You can either skip uploading the image or upload the captured image to Firebase
        if (capturedImage != null) {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
            byte[] byteArray = byteArrayOutputStream.toByteArray();

            imageRef.putBytes(byteArray).addOnSuccessListener(taskSnapshot -> {
                // Save attendance status in Firebase Realtime Database
                saveAttendanceToDatabase(currentDate, roomNumber, studentName, status);
                Toast.makeText(this, "Attendance marked as " + status + " for " + studentName, Toast.LENGTH_SHORT).show();
            }).addOnFailureListener(e -> {
                Toast.makeText(this, "Error saving image: " + e.getMessage(), Toast.LENGTH_SHORT).show();
            });
        } else {
            Toast.makeText(this, "Captured image is null, attendance could not be marked", Toast.LENGTH_SHORT).show();
        }
    }

    private void saveAttendanceToDatabase(String currentDate, String roomNumber, String studentName, String status) {
        DatabaseReference dateRef = attendanceDatabaseRef.child(currentDate);
        DatabaseReference roomRef = dateRef.child(roomNumber);
        DatabaseReference studentRef = roomRef.child(studentName);

        // Save the status (present or absent) in the database
        studentRef.child("status").setValue(status).addOnSuccessListener(aVoid -> {
            Toast.makeText(StdattendanceActivity.this, "Attendance status saved successfully!", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            Toast.makeText(StdattendanceActivity.this, "Failed to save attendance status", Toast.LENGTH_SHORT).show();
        });
    }

    private void checkCameraPermission() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
