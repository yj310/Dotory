package com.example.dotory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotory.student.StudentAttendanceEnterActivity;
import com.example.dotory.student.StudentEnterState;
import com.example.dotory.student.StudentGoOutState;
import com.example.dotory.student.StudentUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class GoOutInfoPopup extends Activity {

    private TextView student_info;
    private TextView state;
    private TextView go_out_time;
    private TextView expected_time;
    private TextView enter_time;
    private TextView place;
    private TextView purpose;

    private String room;
    private String name;
    private String today;

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_go_out_info_popup);

        student_info = (TextView)findViewById(R.id.student_info);
        state = (TextView)findViewById(R.id.state);
        go_out_time = (TextView)findViewById(R.id.go_out_time);
        expected_time = (TextView)findViewById(R.id.expected_time);
        enter_time = (TextView)findViewById(R.id.enter_time);
        place = (TextView)findViewById(R.id.place);
        purpose = (TextView)findViewById(R.id.purpose);


        //데이터 가져오기
        Intent intent = getIntent();
        room = intent.getStringExtra("room");
        name = intent.getStringExtra("name");

        student_info.setText(room + "호 " + name);

        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(now);


        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("attendance/go_out/" + today + "/student");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentGoOutState studentGoOutState = snapshot.getValue(StudentGoOutState.class);
                    if(studentGoOutState.getRoom().equals(room)
                            && studentGoOutState.getName().equals(name)){
                        state.setText(studentGoOutState.getState());
                        go_out_time.setText(studentGoOutState.getGo_out_time());
                        expected_time.setText(studentGoOutState.getExpected_time());
                        enter_time.setText(studentGoOutState.getEnter_time());
                        place.setText(studentGoOutState.getPlace());
                        purpose.setText(studentGoOutState.getPurpose());
                        break;
                    }
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(GoOutInfoPopup.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });




    }

    //확인 버튼 클릭
    public void mOnClose(View v){

        finish();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //바깥레이어 클릭시 안닫히게
        /*if(event.getAction()==MotionEvent.ACTION_OUTSIDE){
            return false;
        }*/
        return true;
    }

    @Override
    public void onBackPressed() {
        //안드로이드 백버튼 막기
        return;
    }
}

