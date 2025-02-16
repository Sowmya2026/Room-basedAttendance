package com.example.roombasedattendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class HealthIssueAdapter extends FirebaseRecyclerAdapter<HealthIssue, HealthIssueAdapter.HealthIssueViewHolder> {

    public HealthIssueAdapter(@NonNull FirebaseRecyclerOptions<HealthIssue> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull HealthIssueViewHolder holder, int position, @NonNull HealthIssue model) {
        // Bind the health issue details to the view holder
        holder.tvHealthIssue.setText("Health Issue: " + model.getHealthIssue());
        holder.tvRoomNumber.setText("Room Number: " + model.getRoomNumber());
        holder.tvRegNumber.setText("Registration Number: " + model.getRegNumber());
        holder.tvStudentName.setText("Student Name: " + model.getStudentName());
    }

    @NonNull
    @Override
    public HealthIssueViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the health issue card layout
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_health_issue, parent, false);
        return new HealthIssueViewHolder(view);
    }

    public static class HealthIssueViewHolder extends RecyclerView.ViewHolder {
        TextView tvHealthIssue, tvRoomNumber, tvRegNumber, tvStudentName;

        public HealthIssueViewHolder(View itemView) {
            super(itemView);
            tvHealthIssue = itemView.findViewById(R.id.tv_health_issue);
            tvRoomNumber = itemView.findViewById(R.id.tv_room_number);
            tvRegNumber = itemView.findViewById(R.id.tv_reg_number);
            tvStudentName = itemView.findViewById(R.id.tv_student_name);
        }
    }
}
