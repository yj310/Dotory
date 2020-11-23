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
    private DatabaseReference databaseReference;
    private DatabaseReference databaseReference2;
    private DatabaseReference databaseReference3;
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

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("attendance/enter");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String date = dataSnapshot.child("date").getValue().toString();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                Date now = new Date();
                String today = formatter.format(now);
                if(!date.equals(today))
                {
                    databaseReference = database.getReference("attendance/enter");
                    databaseReference.child("date").setValue(today);
                    databaseReference.child("time").child("first").setValue("00:00");
                    databaseReference.child("time").child("second").setValue("00:00");
                    first_time = "00:00";
                    second_time = "00:00";
                    first_time_hour.setText("00");
                    first_time_minute.setText("00");
                    second_time_hour.setText("00");
                    second_time_minute.setText("00");
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



                formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
                today = formatter.format(now);
                text_date.setText(today);

                if(first_time.equals("00:00"))
                {
                    databaseReference2 = database.getReference("attendance/enter/student");
                    databaseReference3 = database.getReference("StudentUser");

                    databaseReference3.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            // 파이어베이스의 데이터를 받아오는 곳
                            for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                studentUser = snapshot.getValue(StudentUser.class);
                                studentEnterState = new StudentEnterState(studentUser.getName(), studentUser.getRoom(), "", "");
                                databaseReference2.child(studentUser.getRoom() + studentUser.getEmail().split("@")[0]).setValue(studentEnterState);


                            }
                        };



                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            // DB를 가져오던 중 에러 발생 시
                            Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                else
                {
                    formatter = new SimpleDateFormat("hh:mm");
                    int now_hour = Integer.parseInt(formatter.format(now).split(":")[0]);
                    if(Integer.parseInt(first_time.split(":")[0]) <= now_hour
                        && Integer.parseInt(second_time.split(":")[0]) > now_hour)
                    {
                        databaseReference2 = database.getReference("attendance/enter/student");

                        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // 파이어베이스의 데이터를 받아오는 곳
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    studentEnterState = snapshot.getValue(StudentEnterState.class);
                                    if(studentEnterState.getState().length() <= 0)
                                    {
                                        studentEnterState.setState("입소중");
                                    }
                                    databaseReference2.child(snapshot.getKey()).setValue(studentEnterState);

                                }
                            };

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else if(Integer.parseInt(second_time.split(":")[0]) <= now_hour)
                    {
                        databaseReference2 = database.getReference("attendance/enter/student");

                        databaseReference2.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // 파이어베이스의 데이터를 받아오는 곳
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    studentEnterState = snapshot.getValue(StudentEnterState.class);
                                    if(studentEnterState.getState().equals("입소중"))
                                    {
                                        studentEnterState.setState("지각");
                                    }
                                    databaseReference2.child(snapshot.getKey()).setValue(studentEnterState);

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


        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("attendance/enter/student");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentEnterState studentEnterState = snapshot.getValue(StudentEnterState.class);
                    arrayList.add(studentEnterState);


                }
                //Collections.reverse(arrayList);
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
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("attendance/enter/time");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                databaseReference.child("first").setValue(first_time_hour.getText().toString() + ":" + first_time_minute.getText().toString());
                databaseReference.child("second").setValue(second_time_hour.getText().toString() + ":" + second_time_minute.getText().toString());

                Toast.makeText(ManagerAttendanceEnterActivity.this, "설정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(ManagerAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
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