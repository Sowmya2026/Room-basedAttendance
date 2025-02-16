package com.example.roombasedattendance;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class AbsenteeStudentsAdapter extends RecyclerView.Adapter<AbsenteeStudentsAdapter.AbsenteeStudentViewHolder> {

    private List<Student> absenteeStudentsList;

    public AbsenteeStudentsAdapter(List<Student> absenteeStudentsList) {
        this.absenteeStudentsList = absenteeStudentsList;
    }

    @NonNull
    @Override
    public AbsenteeStudentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate the item layout for each absentee student
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_absentee_student, parent, false);
        return new AbsenteeStudentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AbsenteeStudentViewHolder holder, int position) {
        // Bind the absentee student data to the view holder
        Student student = absenteeStudentsList.get(position);

        // Format: Room Number - Name
        String formattedText = student.getRoomNumber() + " - " + student.getName();
        holder.tvRoomNumberName.setText(formattedText);
    }

    @Override
    public int getItemCount() {
        return absenteeStudentsList.size();
    }

    // ViewHolder class to represent each item in the RecyclerView
    public static class AbsenteeStudentViewHolder extends RecyclerView.ViewHolder {

        TextView tvRoomNumberName;

        public AbsenteeStudentViewHolder(View itemView) {
            super(itemView);
            // Initialize UI components
            tvRoomNumberName = itemView.findViewById(R.id.tv_room_number_name);
        }
    }
}
