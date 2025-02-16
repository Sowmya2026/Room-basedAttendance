package com.example.roombasedattendance;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.progressindicator.CircularProgressIndicator;

public class AuthorityloginActivity extends AppCompatActivity {

    // Predefined login credentials
    private static final String AUTHORITY_ID = "50321";
    private static final String AUTHORITY_PASSWORD = "authority123";

    private TextInputEditText authorityId, password;
    private CircularProgressIndicator progressBar; // ProgressBar for login process

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authoritylogin); // Ensure this layout file exists

        // Initialize UI components
        authorityId = findViewById(R.id.et_authority_id);
        password = findViewById(R.id.et_authority_password);
        progressBar = findViewById(R.id.progressBar); // Initialize the progress bar
    }

    // Method for handling authority login
    public void hostelAuthorityLogin(View view) {
        String authorityID = authorityId.getText().toString().trim();
        String pass = password.getText().toString().trim();

        // Show progress bar while checking credentials
        progressBar.setVisibility(View.VISIBLE);

        // Simulate network delay or processing
        new android.os.Handler().postDelayed(() -> {
            // Validate credentials
            if (AUTHORITY_ID.equals(authorityID) && AUTHORITY_PASSWORD.equals(pass)) {
                // Successful login
                Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show();

                // Save authority session
                saveAuthoritySession(authorityID);

                // Navigate to the Authority Home Activity
                Intent intent = new Intent(this, AuthorityhomeActivity.class); // Ensure AuthorityhomeActivity exists
                startActivity(intent);
                finish(); // Optional: prevent user from going back to the login screen
            } else if (authorityID.isEmpty() || pass.isEmpty()) {
                // Missing input
                Toast.makeText(this, "Please enter both Authority ID and Password", Toast.LENGTH_SHORT).show();
            } else {
                // Invalid credentials
                Toast.makeText(this, "Invalid Authority ID or Password", Toast.LENGTH_SHORT).show();
            }

            // Hide progress bar after processing
            progressBar.setVisibility(View.GONE);
        }, 2000); // Simulate a delay of 2 seconds (you can adjust this duration)
    }

    // Method to save authority session data to SharedPreferences
    private void saveAuthoritySession(String authorityID) {
        SharedPreferences sharedPreferences = getSharedPreferences("AuthoritySession", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("authorityId", authorityID);
        editor.putBoolean("isAuthorityLoggedIn", true);  // Store logged-in status
        editor.apply();
    }

    // Method to check if authority is already logged in
    @Override
    protected void onStart() {
        super.onStart();
        SharedPreferences sharedPreferences = getSharedPreferences("AuthoritySession", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isAuthorityLoggedIn", false);

        // If the authority is logged in, navigate directly to the home activity
        if (isLoggedIn) {
            Intent intent = new Intent(this, AuthorityhomeActivity.class);
            startActivity(intent);
            finish(); // Prevent going back to the login screen
        }
    }

    // Method to navigate back to the main home page (if needed)
    public void goToHomePage(View view) {
        Intent intent = new Intent(this, AuthorityloginActivity.class); // Replace MainActivity with your actual home activity
        startActivity(intent);
    }
}
