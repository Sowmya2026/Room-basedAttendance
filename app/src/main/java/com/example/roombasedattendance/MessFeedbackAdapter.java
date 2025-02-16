package com.example.roombasedattendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.card.MaterialCardView;
import java.util.List;

public class MessFeedbackAdapter extends RecyclerView.Adapter<MessFeedbackAdapter.FeedbackViewHolder> {

    private List<MessFeedback> feedbackList;

    // Constructor
    public MessFeedbackAdapter(List<MessFeedback> feedbackList) {
        this.feedbackList = feedbackList;
    }

    @Override
    public FeedbackViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate the item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_mess_fdbk, parent, false);
        return new FeedbackViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(FeedbackViewHolder holder, int position) {
        // Bind the feedback data to the views
        MessFeedback feedback = feedbackList.get(position);
        holder.tvEventDate.setText("Event Date: " + feedback.getEventDate());
        holder.tvFeedbackDescription.setText("Feedback: " + feedback.getFeedbackDescription());
    }

    @Override
    public int getItemCount() {
        return feedbackList.size(); // Return the total number of feedback items
    }

    // ViewHolder to hold each item view
    public static class FeedbackViewHolder extends RecyclerView.ViewHolder {

        public TextView tvEventDate;
        public TextView tvFeedbackDescription;

        public FeedbackViewHolder(View itemView) {
            super(itemView);

            // Initialize TextViews
            tvEventDate = itemView.findViewById(R.id.tv_event_date);
            tvFeedbackDescription = itemView.findViewById(R.id.tv_feedback_description);
        }
    }
}
