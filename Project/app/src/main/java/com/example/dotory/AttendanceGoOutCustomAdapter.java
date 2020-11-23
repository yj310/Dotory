package com.example.dotory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotory.student.StudentEnterState;
import com.example.dotory.student.StudentGoOutState;

import java.util.ArrayList;


public class AttendanceGoOutCustomAdapter extends RecyclerView.Adapter<AttendanceGoOutCustomAdapter.PostCustomViewHolder> {

    private ArrayList<StudentGoOutState> arrayList;
    private Context context;

    public AttendanceGoOutCustomAdapter(ArrayList<StudentGoOutState> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.attendance_go_out_list, parent, false);
        PostCustomViewHolder holder = new PostCustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostCustomViewHolder holder, int position) {

        holder.tv_room.setText(arrayList.get(position).getRoom());
        holder.tv_name.setText(arrayList.get(position).getName());
        holder.tv_state.setText(arrayList.get(position).getState());
        holder.tv_go_out_time.setText(arrayList.get(position).getGo_out_time());
        holder.tv_expected_time.setText(arrayList.get(position).getExpected_time());
        holder.tv_enter_time.setText(arrayList.get(position).getEnter_time());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostCustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_room;
        TextView tv_state;
        TextView tv_go_out_time;
        TextView tv_expected_time;
        TextView tv_enter_time;








        public PostCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_room = itemView.findViewById(R.id.room);
            this.tv_name = itemView.findViewById(R.id.name);
            this.tv_state = itemView.findViewById(R.id.state);
            this.tv_go_out_time = itemView.findViewById(R.id.go_out_time);
            this.tv_expected_time = itemView.findViewById(R.id.expected_time);
            this.tv_enter_time = itemView.findViewById(R.id.enter_time);

        }
    }
}
