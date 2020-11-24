package com.example.dotory;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
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
        //holder.tv_go_out_time.setText(arrayList.get(position).getGo_out_time());
        //holder.tv_expected_time.setText(arrayList.get(position).getExpected_time());
        //holder.tv_enter_time.setText(arrayList.get(position).getEnter_time());


    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostCustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_name;
        TextView tv_room;
        TextView tv_state;
        //TextView tv_go_out_time;
        //TextView tv_expected_time;
        //TextView tv_enter_time;


        public PostCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_room = itemView.findViewById(R.id.room);
            this.tv_name = itemView.findViewById(R.id.name);
            this.tv_state = itemView.findViewById(R.id.state);
            //this.tv_go_out_time = itemView.findViewById(R.id.go_out_time);
            //this.tv_expected_time = itemView.findViewById(R.id.expected_time);
            //this.tv_enter_time = itemView.findViewById(R.id.enter_time);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 팝업창 띄우기
                    Intent intent = new Intent(context, GoOutInfoPopup.class);
                    intent.putExtra("room", tv_room.getText());
                    intent.putExtra("name", tv_name.getText());
                    ((Activity) context).startActivity(intent);
                }

            });

        }
/*
        public void modal(final View view, boolean pay) {
            String msg;
            if (pay) {
                msg = "주문을 접수 하시겠습니까?";
            } else {
                msg = "본 주문은 무통장 입금 주문입니다.\n입금 확인이 되었다면 확인 버튼을 눌러주세요.";
            }

            new AlertDialog.Builder(view.getContext())
                    .setTitle("주문 접수 확인")
                    .setMessage(msg)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            Toast.makeText(view.getContext(), "접수 되었습니다.", Toast.LENGTH_SHORT).show();
                            //((Activity) view.getContext()).finish();
                        }
                    }).setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                    Toast.makeText(view.getContext(), "취소 하였습니다.", Toast.LENGTH_SHORT).show();

                }
            }).show();
        }*/

    }


}
