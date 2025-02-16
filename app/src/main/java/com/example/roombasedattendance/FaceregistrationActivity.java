package com.example.roombasedattendance;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
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
import com.google.android.material.textfield.TextInputEditText;

import java.io.ByteArrayOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class FaceregistrationActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 100;
    private static final String TAG = "FaceregistrationActivity";

    private TextInputEditText roomNumberEditText, studentNameInput;
    private Spinner roomTypeSpinner, bedCountSpinner;
    private ImageView studentImageView;
    private Button registerFaceButton, submitButton;
    private FirebaseDatabase database;
    private FirebaseStorage storage;
    private StorageReference storageRef;
    private int registeredStudentsCount = 0;
    private int maxBedCount = 5; // Default max bed count, adjusted based on bed type
    private String roomId;

    // Register ActivityResultLauncher for camera
    private ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    // Handle image capture
                    Bitmap capturedImage = (Bitmap) result.getData().getExtras().get("data");
                    displayCapturedImage(capturedImage);
                    checkAndUploadImageToFirebaseStorage(capturedImage);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faceregistration);

        // Initialize UI elements
        roomNumberEditText = findViewById(R.id.roomNumber);
        studentNameInput = findViewById(R.id.studentNameInput); // Added input field for student name
        roomTypeSpinner = findViewById(R.id.roomType);
        bedCountSpinner = findViewById(R.id.bedCount);
        studentImageView = findViewById(R.id.studentImage);
        registerFaceButton = findViewById(R.id.registerFaceButton);
        submitButton = findViewById(R.id.submitButton);

        // Initialize Firebase
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        storageRef = storage.getReference();

        // Setup register face button
        registerFaceButton.setOnClickListener(v -> captureFaceImage());

        // Setup submit button
        submitButton.setOnClickListener(v -> submitRoomDetails());

        // Handle camera permission
        checkCameraPermission();

        // Update max bed count when the bed count spinner selection changes
        bedCountSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View view, int position, long id) {
                switch (position) {
                    case 0: maxBedCount = 2; break;  // 2 students max
                    case 1: maxBedCount = 3; break;  // 3 students max
                    case 2: maxBedCount = 4; break;  // 4 students max
                    default: maxBedCount = 5; break; // Default max
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // Handle case when nothing is selected
            }
        });
    }

    private void captureFaceImage() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            // Open the camera
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            cameraLauncher.launch(cameraIntent);
        } else {
            // Request permission if not granted
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, CAMERA_REQUEST_CODE);
        }
    }

    private void displayCapturedImage(Bitmap capturedImage) {
        studentImageView.setImageBitmap(capturedImage);

        // Increment the student count
        registeredStudentsCount++;
        if (registeredStudentsCount >= maxBedCount) {
            registerFaceButton.setVisibility(View.GONE); // Hide button after max students reached
            Toast.makeText(this, "Room is full, cannot register more students.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkAndUploadImageToFirebaseStorage(Bitmap capturedImage) {
        // Convert image to byte array
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        capturedImage.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();

        // Get the room ID from user input
        roomId = roomNumberEditText.getText().toString(); // Use room number as roomId
        String studentName = studentNameInput.getText().toString().trim();

        // Check if roomId or student name is empty
        if (roomId.isEmpty() || studentName.isEmpty()) {
            Toast.makeText(this, "Room ID and Student Name cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }

        // Generate a unique filename for the image using the student's name and room number
        String imageFileName = "roomsregistered/" + roomId + "/" + studentName + ".jpg";

        // Check if the image already exists in Firebase Storage
        StorageReference imageRef = storageRef.child(imageFileName);

        // Check if the image already exists (by checking for the file)
        imageRef.getMetadata().addOnSuccessListener(metadata -> {
            // Image already exists, so the student is already registered
            Toast.makeText(FaceregistrationActivity.this, "Student face already registered in this room", Toast.LENGTH_SHORT).show();
        }).addOnFailureListener(e -> {
            // Image does not exist, proceed with the upload
            uploadImageToFirebaseStorage(byteArray, imageFileName);
        });
    }


    private void uploadImageToFirebaseStorage(byte[] byteArray, String imageFileName) {
        StorageReference imageRef = storageRef.child(imageFileName);

        imageRef.putBytes(byteArray)
                .addOnSuccessListener(taskSnapshot -> {
                    Toast.makeText(FaceregistrationActivity.this, "Student face registered successfully!", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Log error for debugging
                    Log.e(TAG, "Error uploading image: ", e);
                    Toast.makeText(FaceregistrationActivity.this, "Error registering student face: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void submitRoomDetails() {
        String roomNumber = roomNumberEditText.getText().toString().trim();
        String roomType = roomTypeSpinner.getSelectedItem().toString();
        String bedCount = bedCountSpinner.getSelectedItem().toString();

        if (roomNumber.isEmpty()) {
            Toast.makeText(this, "Please enter a room number", Toast.LENGTH_SHORT).show();
            return;
        }

        // Check if the room is already registered in Firebase Realtime Database
        DatabaseReference roomRef = database.getReference("authorityLogin").child("roomRegistration").child(roomNumber);
        roomRef.get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (task.getResult().exists()) {
                    // Room number is already registered
                    Toast.makeText(FaceregistrationActivity.this, "Room number is already registered", Toast.LENGTH_SHORT).show();
                } else {
                    // Room number is not registered, proceed with the registration
                    roomRef.setValue(new RoomDetails(roomNumber, roomType, bedCount));
                    registeredStudentsCount = 0; // Reset student count after room registration
                    Toast.makeText(FaceregistrationActivity.this, "Room registration submitted", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Error in checking the database
                Toast.makeText(FaceregistrationActivity.this, "Error checking room registration", Toast.LENGTH_SHORT).show();
            }
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
                // Permission granted, allow camera usage
                Toast.makeText(this, "Camera permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // Permission denied, show message
                Toast.makeText(this, "Camera permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    // Helper class for room details
    public static class RoomDetails {
        public String roomNumber;
        public String roomType;
        public String bedCount;

        public RoomDetails(String roomNumber, String roomType, String bedCount) {
            this.roomNumber = roomNumber;
            this.roomType = roomType;
            this.bedCount = bedCount;
        }
    }
}
