package com.example.roombasedattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.progressindicator.CircularProgressIndicator;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import io.reactivex.rxjava3.annotations.NonNull;

public class StudentloginActivity extends AppCompatActivity {

    private TextInputEditText etStudentId, etStudentPassword;
    private MaterialButton loginButton;
    private CircularProgressIndicator progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentlogin);

        // Initialize Firebase Auth and Database
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();

        // Initialize UI components
        etStudentId = findViewById(R.id.et_student_id);
        etStudentPassword = findViewById(R.id.et_student_password);
        loginButton = findViewById(R.id.loginstudent);
        progressBar = findViewById(R.id.progressBar);

        // Set click listener for the Login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show progress bar
                progressBar.setVisibility(View.VISIBLE);

                // Retrieve entered student details
                final String studentId = etStudentId.getText().toString().trim();
                final String password = etStudentPassword.getText().toString().trim();

                // Validate inputs
                if (TextUtils.isEmpty(studentId)) {
                    Toast.makeText(StudentloginActivity.this, "Enter Student ID", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }
                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(StudentloginActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // Validate studentId format
                if (!studentId.matches("[a-zA-Z0-9]+")) {
                    Toast.makeText(StudentloginActivity.this, "Invalid Student ID", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                    return;
                }

                // Use the studentId as the email for Firebase authentication
                String email = studentId + "@vitstudent.ac.in";

                // Sign in with Firebase Auth
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                if (task.isSuccessful()) {
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    Toast.makeText(StudentloginActivity.this, "Login successful", Toast.LENGTH_SHORT).show();

                                    if (user != null) {
                                        mDatabase.child("studentlogin").child(studentId)
                                                .addListenerForSingleValueEvent(new ValueEventListener() {
                                                    @Override
                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                        if (dataSnapshot.exists()) {
                                                            String name = dataSnapshot.child("name").getValue(String.class);
                                                            String regNumber = dataSnapshot.child("regNumber").getValue(String.class);

                                                            // Save user data to SharedPreferences
                                                            saveUserSession(studentId, name, regNumber);

                                                            Toast.makeText(StudentloginActivity.this, "Welcome " + name, Toast.LENGTH_SHORT).show();

                                                            // Start the home activity
                                                            Intent intent = new Intent(StudentloginActivity.this, StudenthomeActivity.class);
                                                            intent.putExtra("userName", name);
                                                            intent.putExtra("userRegNumber", regNumber);
                                                            startActivity(intent);
                                                            finish();
                                                        } else {
                                                            Toast.makeText(StudentloginActivity.this, "No data found for the student", Toast.LENGTH_SHORT).show();
                                                        }
                                                    }

                                                    @Override
                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                        Toast.makeText(StudentloginActivity.this, "Error fetching student data", Toast.LENGTH_SHORT).show();
                                                    }
                                                });
                                    }
                                } else {
                                    String errorMessage = task.getException().getMessage();
                                    Toast.makeText(StudentloginActivity.this, "Authentication failed: " + errorMessage, Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });

        // Set click listener for the Sign-Up link
        TextView tvSignUpLink = findViewById(R.id.tv_student_signup_link);
        tvSignUpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StudentloginActivity.this, StudentsignupActivity.class);
                startActivity(intent);
            }
        });
    }

    // Method to save user session data to SharedPreferences
    private void saveUserSession(String studentId, String name, String regNumber) {
        SharedPreferences sharedPreferences = getSharedPreferences("UserSession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("studentId", studentId);
        editor.putString("userName", name);
        editor.putString("userRegNumber", regNumber);
        editor.putBoolean("isLoggedIn", true);  // Store logged-in status
        editor.apply();
    }
}
