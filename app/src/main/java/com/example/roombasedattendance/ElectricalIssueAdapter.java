package com.example.roombasedattendance;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ElectricalIssueAdapter extends RecyclerView.Adapter<ElectricalIssueAdapter.ElectricalIssueViewHolder> {

    private Context context;
    private List<ElectricalIssue> issueList;

    // Constructor
    public ElectricalIssueAdapter(Context context, List<ElectricalIssue> issueList) {
        this.context = context;
        this.issueList = issueList;
    }

    @Override
    public ElectricalIssueViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout (the one you provided)
        View view = LayoutInflater.from(context).inflate(R.layout.item_electrical_issue, parent, false);
        return new ElectricalIssueViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ElectricalIssueViewHolder holder, int position) {
        // Get the current issue
        ElectricalIssue issue = issueList.get(position);

        // Set the values from the issue to the TextViews
        holder.tvIssueDescription.setText("Issue Description: " + issue.getIssueDescription());
        holder.tvRoomNumber.setText("Room Number: " + issue.getRoomNumber());
        holder.tvRegNumber.setText("Registration Number: " + issue.getRegNumber());
        holder.tvStudentName.setText("Student: " + issue.getStudentName());
        holder.tvStartTime.setText("Start Time: " + issue.getStartTime());
        holder.tvEndTime.setText("End Time: " + issue.getEndTime());
    }

    @Override
    public int getItemCount() {
        return issueList.size();  // Return the size of the issue list
    }

    // ViewHolder class
    public class ElectricalIssueViewHolder extends RecyclerView.ViewHolder {

        TextView tvIssueDescription, tvRoomNumber, tvRegNumber, tvStudentName, tvStartTime, tvEndTime;

        public ElectricalIssueViewHolder(View itemView) {
            super(itemView);

            // Initialize the views by finding them by ID
            tvIssueDescription = itemView.findViewById(R.id.tvIssueDescription);
            tvRoomNumber = itemView.findViewById(R.id.tvRoomNumber);
            tvRegNumber = itemView.findViewById(R.id.tvRegNumber);
            tvStudentName = itemView.findViewById(R.id.tvStudentName);
            tvStartTime = itemView.findViewById(R.id.tvStartTime);
            tvEndTime = itemView.findViewById(R.id.tvEndTime);
        }
    }
}
