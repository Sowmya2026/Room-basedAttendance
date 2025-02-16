package com.example.roombasedattendance;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ViewhealthissuesActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private HealthIssueAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewhealthissues);

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.lv_health_issues);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Firebase reference to the health issues node under "authorityLogin"
        DatabaseReference database = FirebaseDatabase.getInstance().getReference("authorityLogin").child("health_issues");

        // FirebaseRecyclerOptions for binding Firebase data to the RecyclerView
        FirebaseRecyclerOptions<HealthIssue> options = new FirebaseRecyclerOptions.Builder<HealthIssue>()
                .setQuery(database, HealthIssue.class)
                .build();

        // Create the adapter instance using the options
        adapter = new HealthIssueAdapter(options);

        // Set the adapter to the RecyclerView
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Start listening to Firebase data changes
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        // Stop listening to Firebase data changes
        adapter.stopListening();
    }
}

