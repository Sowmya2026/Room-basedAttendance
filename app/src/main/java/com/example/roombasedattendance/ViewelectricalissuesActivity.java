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

public class ViewelectricalissuesActivity extends AppCompatActivity {

    private RecyclerView issuesRecyclerView;
    private ElectricalIssueAdapter issueAdapter;
    private List<ElectricalIssue> issueList;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewelectricalissues);

        issuesRecyclerView = findViewById(R.id.lv_electrical_issues);
        issueList = new ArrayList<>();

        // Initialize the Firebase reference to "authorityLogin/electrical_issues"
        databaseReference = FirebaseDatabase.getInstance().getReference("authorityLogin").child("electrical_issues");

        // Set up RecyclerView
        issuesRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        issueAdapter = new ElectricalIssueAdapter(this, issueList);
        issuesRecyclerView.setAdapter(issueAdapter);

        // Fetch issues from Firebase
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                issueList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    ElectricalIssue issue = snapshot.getValue(ElectricalIssue.class);
                    if (issue != null) {
                        issueList.add(issue);
                    }
                }
                issueAdapter.notifyDataSetChanged();  // Refresh RecyclerView
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(ViewelectricalissuesActivity.this, "Error loading issues", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
