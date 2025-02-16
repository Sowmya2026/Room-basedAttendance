package com.example.roombasedattendance;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentsignupActivity extends AppCompatActivity {
    private TextInputEditText etStudentName, etStudentRegNumber, etStudentPassword;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentsignup);

        // Initialize Firebase Auth and Realtime Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Bind UI components
        etStudentName = findViewById(R.id.et_student_name);
        etStudentRegNumber = findViewById(R.id.et_student_reg_number);
        etStudentPassword = findViewById(R.id.et_student_password);
    }

    // Method to handle registration
    public void studentRegister(View view) {
        String name = etStudentName.getText().toString().trim();
        String regNumber = etStudentRegNumber.getText().toString().trim();
        String password = etStudentPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(regNumber) || TextUtils.isEmpty(password)) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Use registration number as email for Firebase Authentication (e.g., appending domain)
        String email = regNumber + "@vitstudent.ac.in";

        // Register user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, task -> {
                    if (task.isSuccessful()) {
                        // Registration success, store user data in Firebase Realtime Database
                        String userId = mAuth.getCurrentUser().getUid();

                        // Create a User object
                        User newUser = new User(name, regNumber);

                        // Save the user data under the "studentlogin" node using the regNumber as the key
                        mDatabase.child("studentlogin").child(regNumber).setValue(newUser)
                                .addOnCompleteListener(task1 -> {
                                    if (task1.isSuccessful()) {
                                        // Data saved successfully
                                        Toast.makeText(this, "Registration successful", Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(this, StudentloginActivity.class));
                                        finish();
                                    } else {
                                        // Handle error in saving user data
                                        Toast.makeText(this, "Failed to save user data: " + task1.getException().getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                    } else {
                        // Registration failed
                        Toast.makeText(this, "Registration failed: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    // Method to go to login page if the student is already registered
    public void goToStudentLogin(View view) {
        startActivity(new Intent(this, StudentloginActivity.class));
    }

    // User class to represent the user data structure
    public static class User {
        public String name;
        public String regNumber;

        // Default constructor required for calls to DataSnapshot.getValue(User.class)
        public User() {}

        public User(String name, String regNumber) {
            this.name = name;
            this.regNumber = regNumber;
        }
    }
}
