package com.example.roombasedattendance;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.ListResult;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ViewpntabstActivity extends AppCompatActivity {

    private TextView tvTotalPresentStudents, tvTotalAbsentStudents, tvTotalRegisteredStudents;
    private RecyclerView lvAbsenteeStudents;
    private AbsenteeStudentsAdapter absenteeStudentsAdapter;
    private List<Student> absenteeStudentsList;
    private FirebaseDatabase firebaseDatabase;
    private DatabaseReference attendanceRef;
    private FirebaseStorage storage;

    private int presentCount = 0;
    private int absentCount = 0;
    private int totalRegisteredCount = 0;
    private static final String TAG = "ViewpntabstActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewpntabst);

        // Initialize UI components
        tvTotalPresentStudents = findViewById(R.id.tv_total_present_students);
        tvTotalAbsentStudents = findViewById(R.id.tv_total_absent_students);
        tvTotalRegisteredStudents = findViewById(R.id.tv_total_registered_students);
        lvAbsenteeStudents = findViewById(R.id.lv_absentee_students);

        // Setup RecyclerView for absentee students
        absenteeStudentsList = new ArrayList<>();
        absenteeStudentsAdapter = new AbsenteeStudentsAdapter(absenteeStudentsList);
        lvAbsenteeStudents.setLayoutManager(new LinearLayoutManager(this));
        lvAbsenteeStudents.setAdapter(absenteeStudentsAdapter);

        // Initialize Firebase instances
        firebaseDatabase = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();
        attendanceRef = firebaseDatabase.getReference("Attendance"); // Path to your attendance data

        // Fetch initial counts
        countTotalRegisteredStudents();
        fetchPresentCountForDay();  // Fetch the present count at any time

        // Listen for new registrations (Optional: for real-time updates on total registered count)
        listenForNewRegistrations();
    }

    @Override
    protected void onStart() {
        super.onStart();
        fetchPresentCountForDay(); // Fetch present count when the activity starts
    }

    // Fetch and update present data for the current day at any time
    private void fetchPresentCountForDay() {
        // Get the current date to match the folder in Firebase Storage
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        // Get reference to today's attendance data for present count
        DatabaseReference dateRef = attendanceRef.child(currentDate);

        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Data snapshot received");

                presentCount = 0;  // Reset the present count

                // Iterate over rooms and students
                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot studentSnapshot : roomSnapshot.getChildren()) {
                        String studentName = studentSnapshot.getKey();
                        String roomNumber = roomSnapshot.getKey();
                        String status = studentSnapshot.child("status").getValue(String.class); // Assuming "status" stores "present"/"absent"

                        // Check status and update present count
                        if (status != null && status.equals("present")) {
                            presentCount++;
                        }
                    }
                }

                // Log present count
                Log.d(TAG, "Present Count: " + presentCount);
                tvTotalPresentStudents.setText(String.valueOf(presentCount)); // Update present count
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewpntabstActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch and update absent data for the current day (after the time limit or the next day)
    private void fetchAbsentCountForDay() {
        // Get the current date to match the folder in Firebase Storage
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String currentDate = dateFormat.format(new Date());

        // Get reference to today's attendance data for absent count
        DatabaseReference dateRef = attendanceRef.child(currentDate);

        dateRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Log.d(TAG, "onDataChange: Data snapshot received");

                absentCount = 0;  // Reset the absent count
                absenteeStudentsList.clear();  // Clear the absentee list

                // Iterate over rooms and students
                for (DataSnapshot roomSnapshot : dataSnapshot.getChildren()) {
                    for (DataSnapshot studentSnapshot : roomSnapshot.getChildren()) {
                        String studentName = studentSnapshot.getKey();
                        String roomNumber = roomSnapshot.getKey();
                        String status = studentSnapshot.child("status").getValue(String.class); // Assuming "status" stores "present"/"absent"

                        // Check status and update absent count
                        if (status != null && status.equals("absent")) {
                            absentCount++;
                            absenteeStudentsList.add(new Student(studentName, roomNumber));  // Add absentee students to the list
                        }
                    }
                }

                // Log absent count
                Log.d(TAG, "Absent Count: " + absentCount);
                tvTotalAbsentStudents.setText(String.valueOf(absentCount)); // Update absent count

                // Update RecyclerView for absentee students
                absenteeStudentsAdapter.notifyDataSetChanged();  // Notify the adapter to refresh the RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewpntabstActivity.this, "Error loading data", Toast.LENGTH_SHORT).show();
            }
        });
    }

    // Fetch total registered students count (from Firebase Storage)
    private void countTotalRegisteredStudents() {
        // Specify the path where the images are stored (e.g., "roomsregistered")
        StorageReference roomsRef = storage.getReference().child("roomsregistered");

        // List all rooms in the 'roomsregistered' folder
        roomsRef.listAll().addOnSuccessListener(listResult -> {
            for (StorageReference roomFolder : listResult.getPrefixes()) {
                // List all images (students) within each room folder
                roomFolder.listAll().addOnSuccessListener(roomImagesResult -> {
                    int roomStudentCount = roomImagesResult.getItems().size();
                    totalRegisteredCount += roomStudentCount; // Add count for each room
                    tvTotalRegisteredStudents.setText(String.valueOf(totalRegisteredCount)); // Update registered student count
                }).addOnFailureListener(e -> Log.e(TAG, "Error listing room images: ", e));
            }
        }).addOnFailureListener(e -> {
            Log.e(TAG, "Error listing rooms: ", e);
            Toast.makeText(this, "Error fetching registered students", Toast.LENGTH_SHORT).show();
        });
    }

    private void listenForNewRegistrations() {
        // Firebase Storage doesn't directly support real-time listeners for file uploads.
        // So, we rely on manual updates or Firestore (as an alternative).
        // To track real-time changes, consider using Firestore to store metadata for student records.
    }

    // Update the total registered count (to be called after image upload)
    public void incrementTotalCountAfterRegistration() {
        totalRegisteredCount++; // Increment count by 1
        tvTotalRegisteredStudents.setText(String.valueOf(totalRegisteredCount)); // Update UI
    }
}
