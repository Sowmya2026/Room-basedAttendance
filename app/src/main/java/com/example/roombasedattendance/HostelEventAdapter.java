package com.example.roombasedattendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class HostelEventAdapter extends RecyclerView.Adapter<HostelEventAdapter.EventViewHolder> {

    private List<HostelEvent> eventList;

    public HostelEventAdapter(List<HostelEvent> eventList) {
        this.eventList = eventList;
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.event_list, parent, false);
        return new EventViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        HostelEvent event = eventList.get(position);
        holder.tvEventName.setText(event.getEventName());
        holder.tvEventDate.setText("Date: " + event.getEventDate());
        holder.tvEventTime.setText("Time: " + event.getEventTime());
        holder.tvEventVenue.setText("Venue: " + event.getEventVenue());
        holder.tvEventDetails.setText("Details: " + event.getEventDetails());
    }

    @Override
    public int getItemCount() {
        return eventList.size();
    }

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        TextView tvEventName, tvEventDate, tvEventTime, tvEventVenue, tvEventDetails;

        public EventViewHolder(View itemView) {
            super(itemView);
            tvEventName = itemView.findViewById(R.id.tv_event_name);
            tvEventDate = itemView.findViewById(R.id.tv_event_date);
            tvEventTime = itemView.findViewById(R.id.tv_event_time);
            tvEventVenue = itemView.findViewById(R.id.tv_event_venue);
            tvEventDetails = itemView.findViewById(R.id.tv_event_details);
        }
    }
}
