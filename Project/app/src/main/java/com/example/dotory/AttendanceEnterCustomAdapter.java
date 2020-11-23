package com.example.dotory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dotory.student.StudentEnterState;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;


public class AttendanceEnterCustomAdapter extends RecyclerView.Adapter<AttendanceEnterCustomAdapter.PostCustomViewHolder> {

    private ArrayList<StudentEnterState> arrayList;
    private Context context;

    public AttendanceEnterCustomAdapter(ArrayList<StudentEnterState> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_enter_list, parent, false);
        PostCustomViewHolder holder = new PostCustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostCustomViewHolder holder, int position) {

        holder.tv_room.setText(arrayList.get(position).getRoom());
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_state.setText(arrayList.get(position).getState());
        holder.tv_enter_time.setText(arrayList.get(position).getEnter_time());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostCustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_room;
        TextView tv_name;
        TextView tv_state;
        TextView tv_enter_time;

        public PostCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_room = itemView.findViewById(R.id.room);
            this.tv_name = itemView.findViewById(R.id.name);
            this.tv_state = itemView.findViewById(R.id.state);
            this.tv_enter_time = itemView.findViewById(R.id.enter_time);

        }
    }
}
