package com.example.dotory.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotory.AttendanceEnterCustomAdapter;
import com.example.dotory.ManagerPostCustomAdapter;
import com.example.dotory.R;
import com.example.dotory.student.StudentEnterState;
import com.example.dotory.student.StudentGoOutState;
import com.example.dotory.student.StudentUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;

public class ManagerAttendanceEnterActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference_attendanceEnter;
    private DatabaseReference databaseReference_studentEnter;
    private DatabaseReference databaseReference_enterState;
    private DatabaseReference databaseReference_studentUser;
    private StudentUser studentUser;
    private StudentEnterState studentEnterState;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<StudentEnterState> arrayList;

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
        setContentView(R.layout.activity_manager_attendance_enter);


        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

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
        databaseReference_attendanceEnter = database.getReference("attendance/enter");
        databaseReference_studentEnter = database.getReference("attendance/enter/" + today + "/student");
        databaseReference_enterState = database.getReference("attendance/enter/" + today + "/state");

        // 입소 정보 불러오기
        databaseReference_attendanceEnter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String date = dataSnapshot.child("date").getValue().toString();

                // 입소 시간 로드
                if(!date.equals(today)) {
                    // 날짜 넘기기
                    databaseReference_attendanceEnter.child("date").setValue(today);
                    databaseReference_attendanceEnter.child("time").child("first").setValue("00:00");
                    databaseReference_attendanceEnter.child("time").child("second").setValue("00:00");
                    first_time = "00:00";
                    second_time = "00:00";
                    first_time_hour.setText("00");
                    first_time_minute.setText("00");
                    second_time_hour.setText("00");
                    second_time_minute.setText("00");

                    databaseReference_attendanceEnter.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            databaseReference_enterState.setValue("before");
                        };
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // DB를 가져오던 중 에러 발생 시
                            Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                    // 오늘의 학생 입소정보 리스트 생성
                    databaseReference_studentUser = database.getReference("StudentUser");
                    databaseReference_studentUser.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // 파이어베이스의 데이터를 받아오는 곳
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                studentUser = snapshot.getValue(StudentUser.class);
                                studentEnterState = new StudentEnterState(studentUser.getName(), studentUser.getRoom(), "", "");
                                databaseReference_attendanceEnter.child(today).child("student").child(studentUser.getRoom() + studentUser.getEmail().split("@")[0]).setValue(studentEnterState);
                            }
                        };
                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // DB를 가져오던 중 에러 발생 시
                            Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    first_time = dataSnapshot.child("time").child("first").getValue().toString();
                    second_time = dataSnapshot.child("time").child("second").getValue().toString();
                    first_time_hour.setText(first_time.split(":")[0]);
                    first_time_minute.setText(first_time.split(":")[1]);
                    second_time_hour.setText(second_time.split(":")[0]);
                    second_time_minute.setText(second_time.split(":")[1]);
                }



                if(!first_time.equals("00:00")) {
                    SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
                    String now_time = formatter.format(now);

                    // 현재 시간이 입소 시간에 포함이 된다면
                    if((Integer.parseInt(first_time.split(":")[0]) < Integer.parseInt(now_time.split(":")[0])
                            || (Integer.parseInt(first_time.split(":")[0]) == Integer.parseInt(now_time.split(":")[0])
                            && Integer.parseInt(first_time.split(":")[1]) <= Integer.parseInt(now_time.split(":")[1])))

                            && (Integer.parseInt(second_time.split(":")[0]) > Integer.parseInt(now_time.split(":")[0])
                            || (Integer.parseInt(second_time.split(":")[0]) == Integer.parseInt(now_time.split(":")[0])
                            && Integer.parseInt(second_time.split(":")[1]) >= Integer.parseInt(now_time.split(":")[1])))) {

                        databaseReference_enterState.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                if(dataSnapshot.getValue().toString().equals("before")) {
                                    databaseReference_enterState.setValue("ing");
                                    databaseReference_studentEnter.addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                            // 파이어베이스의 데이터를 받아오는 곳
                                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                                studentEnterState = snapshot.getValue(StudentEnterState.class);
                                                if(studentEnterState.getState().length() <= 0) {
                                                    studentEnterState.setState("입소중");
                                                }
                                                databaseReference_studentEnter.child(snapshot.getKey()).setValue(studentEnterState);

                                            }
                                        };
                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {
                                            // DB를 가져오던 중 에러 발생 시
                                            Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            };
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                    // 입소 시간이 지났다면
                    else if(Integer.parseInt(second_time.split(":")[0]) < Integer.parseInt(now_time.split(":")[0])
                            || (Integer.parseInt(second_time.split(":")[0]) == Integer.parseInt(now_time.split(":")[0])
                            && Integer.parseInt(second_time.split(":")[1]) < Integer.parseInt(now_time.split(":")[1]))) {
                        databaseReference_attendanceEnter.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                if(!dataSnapshot.getValue().toString().equals("after")) {
                                    databaseReference_enterState.setValue("after");
                                }
                            };
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        databaseReference_studentEnter.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    studentEnterState = snapshot.getValue(StudentEnterState.class);
                                    if(studentEnterState.getState().equals("입소중"))
                                    {
                                        studentEnterState.setState("입소중(지각)");
                                    }
                                    databaseReference_studentEnter.child(snapshot.getKey()).setValue(studentEnterState);

                                }
                            };

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });


                    }

                }

            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        // 학생 입소 정보 리사이클러뷰 set change
        databaseReference_studentEnter.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentEnterState studentEnterState = snapshot.getValue(StudentEnterState.class);
                    arrayList.add(studentEnterState);
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new AttendanceEnterCustomAdapter(arrayList, this);
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

                databaseReference_attendanceEnter.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        databaseReference_attendanceEnter.child("time").child("first").setValue(first_time_hour.getText().toString() + ":" + first_time_minute.getText().toString());
                        databaseReference_attendanceEnter.child("time").child("second").setValue(second_time_hour.getText().toString() + ":" + second_time_minute.getText().toString());

                        Toast.makeText(ManagerAttendanceEnterActivity.this, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        // DB를 가져오던 중 에러 발생 시
                        Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
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
        Intent intent = new Intent(this, ManagerAttendanceEnterActivity.class);
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