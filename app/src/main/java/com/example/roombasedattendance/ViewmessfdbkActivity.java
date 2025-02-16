package com.example.roombasedattendance;

import android.os.Bundle;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.List;

public class ViewmessfdbkActivity extends AppCompatActivity {

    private RecyclerView feedbackRecyclerView;
    private MessFeedbackAdapter adapter;
    private List<MessFeedback> feedbackList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmessfdbk); // Use your XML layout here

        // Initialize RecyclerView and List
        feedbackRecyclerView = findViewById(R.id.rv_mess_feedback);
        feedbackList = new ArrayList<>();

        // Initialize Firebase Database reference to the "authorityLogin/mess_feedback" node
        databaseReference = FirebaseDatabase.getInstance().getReference("authorityLogin").child("mess_feedback");

        // Set up RecyclerView
        feedbackRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new MessFeedbackAdapter(feedbackList);
        feedbackRecyclerView.setAdapter(adapter);

        // Fetch feedback data from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                feedbackList.clear(); // Clear existing feedback data

                // Loop through the children of the snapshot and add to the list
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessFeedback feedback = snapshot.getValue(MessFeedback.class);
                    if (feedback != null) {
                        feedbackList.add(feedback); // Add each feedback item
                    }
                }

                // Notify the adapter that data has been updated
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Handle database error
                Toast.makeText(ViewmessfdbkActivity.this, "Error loading feedback", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
