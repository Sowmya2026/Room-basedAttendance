package com.example.roombasedattendance;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StdeventActivity extends AppCompatActivity {

    private RecyclerView rvHostelEvents;
    private HostelEventAdapter adapter;
    private List<HostelEvent> eventList;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studevent);

        rvHostelEvents = findViewById(R.id.rv_hostel_events);
        rvHostelEvents.setLayoutManager(new LinearLayoutManager(this));

        eventList = new ArrayList<>();
        adapter = new HostelEventAdapter(eventList);
        rvHostelEvents.setAdapter(adapter);

        // Update database path to "authorityLogin/hostelevents"
        mDatabase = FirebaseDatabase.getInstance().getReference("authorityLogin").child("hostelevents");

        fetchEvents();
    }

    private void fetchEvents() {
        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                eventList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    HostelEvent event = dataSnapshot.getValue(HostelEvent.class);
                    eventList.add(event);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(StdeventActivity.this, "Failed to load events", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
