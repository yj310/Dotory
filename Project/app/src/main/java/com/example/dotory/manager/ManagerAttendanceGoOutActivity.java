package com.example.dotory.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dotory.AttendanceEnterCustomAdapter;
import com.example.dotory.AttendanceGoOutCustomAdapter;
import com.example.dotory.R;
import com.example.dotory.student.StudentEnterState;
import com.example.dotory.student.StudentGoOutState;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ManagerAttendanceGoOutActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference_attendanceGoOut;
    private DatabaseReference databaseReference_studentGoOut;
    private DatabaseReference databaseReference_GoOutState;
    private StudentGoOutState studentGoOutState;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentGoOutState> arrayList;

    private EditText first_time_hour;
    private EditText first_time_minute;
    private EditText second_time_hour;
    private EditText second_time_minute;
    private TextView text_date;
    private String today;

    private String first_time;
    private String second_time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_attendance_go_out);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<StudentGoOutState>();

        first_time_hour = findViewById(R.id.first_time_hour);
        first_time_minute = findViewById(R.id.first_time_minute);
        second_time_hour = findViewById(R.id.second_time_hour);
        second_time_minute = findViewById(R.id.second_time_minute);
        text_date = findViewById(R.id.text_date);
        findViewById(R.id.setting_btn).setOnClickListener(onClickListener);

        final Date now = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
        text_date.setText(formatter.format(now));

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(now);


        database = FirebaseDatabase.getInstance();
        databaseReference_attendanceGoOut = database.getReference("attendance/go_out");
        databaseReference_studentGoOut = database.getReference("attendance/go_out/"+today+"/student");
        databaseReference_GoOutState = database.getReference("attendance/go_out/" + today + "/state");

        // 외출 정보 불러오기
        databaseReference_attendanceGoOut.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String date = dataSnapshot.child("date").getValue().toString();

                if(!date.equals(today))
                {
                    databaseReference_attendanceGoOut.child("date").setValue(today);
                    databaseReference_attendanceGoOut.child("time").child("first").setValue("00:00");
                    databaseReference_attendanceGoOut.child("time").child("second").setValue("00:00");
                    first_time = "00:00";
                    second_time = "00:00";
                    first_time_hour.setText("00");
                    first_time_minute.setText("00");
                    second_time_hour.setText("00");
                    second_time_minute.setText("00");

                    databaseReference_GoOutState.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseReference_GoOutState.setValue("before");
                        };
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // DB를 가져오던 중 에러 발생 시
                            Toast.makeText(ManagerAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    first_time = dataSnapshot.child("time").child("first").getValue().toString();
                    second_time = dataSnapshot.child("time").child("second").getValue().toString();
                    first_time_hour.setText(first_time.split(":")[0]);
                    first_time_minute.setText(first_time.split(":")[1]);
                    second_time_hour.setText(second_time.split(":")[0]);
                    second_time_minute.setText(second_time.split(":")[1]);
                }

                if(!first_time.equals("00:00"))
                {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    String now_time = formatter.format(now);

                    // 현재 시간이 외출 시간에 포함이 된다면
                    if((Integer.parseInt(first_time.split(":")[0]) < Integer.parseInt(now_time.split(":")[0])
                            || (Integer.parseInt(first_time.split(":")[0]) == Integer.parseInt(now_time.split(":")[0])
                            && Integer.parseInt(first_time.split(":")[1]) <= Integer.parseInt(now_time.split(":")[1])))

                            && (Integer.parseInt(second_time.split(":")[0]) > Integer.parseInt(now_time.split(":")[0])
                            || (Integer.parseInt(second_time.split(":")[0]) == Integer.parseInt(now_time.split(":")[0])
                            && Integer.parseInt(second_time.split(":")[1]) >= Integer.parseInt(now_time.split(":")[1])))) {

                        databaseReference_GoOutState.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue().toString().equals("before")) {
                                    databaseReference_GoOutState.setValue("ing");
                                }
                            };
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(ManagerAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    // 외출 가능 시간이 지났다면
                    else if(Integer.parseInt(second_time.split(":")[0]) < Integer.parseInt(now_time.split(":")[0])
                            || (Integer.parseInt(second_time.split(":")[0]) == Integer.parseInt(now_time.split(":")[0])
                            && Integer.parseInt(second_time.split(":")[1]) < Integer.parseInt(now_time.split(":")[1])))
                    {
                        databaseReference_GoOutState.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(!dataSnapshot.getValue().toString().equals("after")) {
                                    databaseReference_GoOutState.setValue("after");
                                }
                            };
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(ManagerAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        databaseReference_studentGoOut.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    studentGoOutState = snapshot.getValue(StudentGoOutState.class);
                                    if(studentGoOutState.getState().equals("외출중"))
                                    {
                                        studentGoOutState.setState("외출중(지각)");
                                    }
                                    databaseReference_studentGoOut.child(snapshot.getKey()).setValue(studentGoOutState);

                                }
                            };

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(ManagerAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(ManagerAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        // 학생 외출 정보 리사이클러뷰 set change
        databaseReference_studentGoOut.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentGoOutState studentGoOutState = snapshot.getValue(StudentGoOutState.class);
                    arrayList.add(studentGoOutState);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(ManagerAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }

        });
        adapter = new AttendanceGoOutCustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);



    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch(view.getId()){
                case R.id.setting_btn:
                    settingEnterTime();
                    break;

            }
        }
    };

    public void settingEnterTime()
    {

        if(first_time_hour.getText().toString().length() > 0
                && second_time_hour.getText().toString().length() > 0) {
            if(first_time_minute.getText().toString().length() <= 0 ) {
                first_time_minute.setText("00");
            }
            if(second_time_minute.getText().toString().length() <= 0 ) {
                second_time_minute.setText("00");
            }
            if(Integer.parseInt(first_time_hour.getText().toString()) >= 0
                    && Integer.parseInt(first_time_hour.getText().toString()) < 24
                    && Integer.parseInt(second_time_hour.getText().toString()) >= 0
                    && Integer.parseInt(second_time_hour.getText().toString()) < 24
                    && Integer.parseInt(first_time_minute.getText().toString()) >= 0
                    && Integer.parseInt(first_time_minute.getText().toString()) < 60
                    && Integer.parseInt(second_time_minute.getText().toString()) >= 0
                    && Integer.parseInt(second_time_minute.getText().toString()) < 60) {

                databaseReference_attendanceGoOut.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        databaseReference_attendanceGoOut.child("time").child("first").setValue(first_time_hour.getText().toString() + ":" + first_time_minute.getText().toString());
                        databaseReference_attendanceGoOut.child("time").child("second").setValue(second_time_hour.getText().toString() + ":" + second_time_minute.getText().toString());

                        Toast.makeText(ManagerAttendanceGoOutActivity.this, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // DB를 가져오던 중 에러 발생 시
                        Toast.makeText(ManagerAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                Toast.makeText(this, "잘못된 형식의 시간입니다.", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "시간을 입력해주세요.", Toast.LENGTH_SHORT).show();
        }
    }

    public void menuBoardOnClick(View view)
    {
        Intent intent = new Intent(this, ManagerBoardActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void menuAttendanceOnClick(View view)
    {
        Intent intent = new Intent(this, ManagerAttendanceGoOutActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void menuPointOnClick(View view)
    {
        Intent intent = new Intent(this, ManagerPointActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void menuStayOnClick(View view)
    {
        Intent intent = new Intent(this, ManagerStayChoiceActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void menuInformationOnClick(View view)
    {
        Intent intent = new Intent(this, ManagerInformationActivity.class);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }


    public void attendanceEnterOnClick(View view)
    {
        Intent intent = new Intent(this, ManagerAttendanceEnterActivity.class);

        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void attendanceGoOutOnClick(View view)
    {
        Intent intent = new Intent(this, ManagerAttendanceGoOutActivity.class);

        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}