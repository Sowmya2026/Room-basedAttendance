package com.example.roombasedattendance;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.pm.PackageManager;
import android.Manifest;
import android.app.PendingIntent;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import android.widget.Toast;
import androidx.core.app.ActivityCompat;

public class MainActivity extends AppCompatActivity {

    private TextView tvTitle;
    private Button btnStudentLogin;
    private Button btnAuthorityLogin;

    private GeofencingClient geofencingClient;
    private PendingIntent geofencePendingIntent;

    // Constants for geofence
    private static final double LATITUDE = 12.9916;
    private static final double LONGITUDE = 80.2350;
    private static final float GEOFENCE_RADIUS = 100f; // 100 meters
    private static final String GEOFENCE_ID = "VIT_Chennai_Geofence";
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        tvTitle = findViewById(R.id.tv_title);
        btnStudentLogin = findViewById(R.id.btn_student_login);
        btnAuthorityLogin = findViewById(R.id.btn_authority_login);

        // Initialize GeofencingClient
        geofencingClient = LocationServices.getGeofencingClient(this);

        // Set click listeners
        btnStudentLogin.setOnClickListener(view -> {
            Intent studentIntent = new Intent(MainActivity.this, StudentloginActivity.class);
            startActivity(studentIntent);
        });

        btnAuthorityLogin.setOnClickListener(view -> {
            Intent authorityIntent = new Intent(MainActivity.this, AuthorityloginActivity.class);
            startActivity(authorityIntent);
        });

        // Check permissions and set up geofence
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            setUpGeofence();
        }
    }

    private void setUpGeofence() {
        Geofence geofence = new Geofence.Builder()
                .setRequestId(GEOFENCE_ID)
                .setCircularRegion(LATITUDE, LONGITUDE, GEOFENCE_RADIUS)
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();

        GeofencingRequest geofencingRequest = new GeofencingRequest.Builder()
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .addGeofence(geofence)
                .build();

        geofencePendingIntent = getGeofencePendingIntent();

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Location permission is required for geofencing", Toast.LENGTH_SHORT).show();
            return;
        }

        geofencingClient.addGeofences(geofencingRequest, geofencePendingIntent)
                .addOnSuccessListener(this, unused -> Toast.makeText(MainActivity.this, "Geofence added successfully", Toast.LENGTH_SHORT).show())
                .addOnFailureListener(this, e -> Toast.makeText(MainActivity.this, "Failed to add geofence", Toast.LENGTH_SHORT).show());
    }

    private PendingIntent getGeofencePendingIntent() {
        if (geofencePendingIntent != null) {
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        geofencePendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setUpGeofence();
            } else {
                Toast.makeText(this, "Location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
