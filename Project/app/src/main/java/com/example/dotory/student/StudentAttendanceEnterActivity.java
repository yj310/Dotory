package com.example.dotory.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotory.R;
import com.example.dotory.manager.ManagerAttendanceEnterActivity;
import com.example.dotory.student.information.StudentInformationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class StudentAttendanceEnterActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private TextView text_date;
    private TextView text_enter_time;
    private String first_time;
    private String second_time;
    private StudentEnterState studentEnterState;

    private String email;
    private String key = "";
    private String state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_attendance_enter);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        text_date = findViewById(R.id.text_date);
        text_enter_time = findViewById(R.id.text_enter_time);
        findViewById(R.id.enter_btn).setOnClickListener(onClickListener);


        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
        String today = formatter.format(now);
        text_date.setText(today);

        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("attendance/enter");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                first_time = dataSnapshot.child("time").child("first").getValue().toString();
                second_time = dataSnapshot.child("time").child("second").getValue().toString();
                text_enter_time.setText(first_time + "      " + second_time);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // get my key
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("StudentUser");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentUser studentUser = snapshot.getValue(StudentUser.class);
                    if(studentUser.getEmail().equals(email)){
                        key = studentUser.getRoom() + email.split("@")[0];
                        // get my state
                        database = FirebaseDatabase.getInstance();
                        databaseReference = database.getReference("attendance/enter/student");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // 파이어베이스의 데이터를 받아오는 곳
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                                    if(snapshot.getKey().toString().equals(key))
                                    {
                                        studentEnterState = snapshot.getValue(StudentEnterState.class);
                                        state = studentEnterState.getState();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(StudentAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });

                        break;
                    }
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch(view.getId()){
                case R.id.enter_btn:
                    enter();
                    break;

            }
        }
    };


    public void enter()
    {
        database = FirebaseDatabase.getInstance();
        databaseReference = database.getReference("attendance/enter");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String date = dataSnapshot.child("date").getValue().toString();

                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String today = formatter.format(now);

                if(date.equals(today))
                {

                    if(first_time.equals("00:00"))
                    {
                        Toast.makeText(StudentAttendanceEnterActivity.this, "입소 시간이 정해지지 않았습니다.", Toast.LENGTH_SHORT).show();
                        // 입소 시간이 정해지지 않았습니다.
                    }
                    else{
                        formatter = new SimpleDateFormat("hh:mm");
                        String now_time = formatter.format(now);
                        if(Integer.parseInt(first_time.split(":")[0]) <= Integer.parseInt(now_time.split(":")[0])
                                &&Integer.parseInt(second_time.split(":")[0]) > Integer.parseInt(now_time.split(":")[0]))
                        {
                            if(state.equals("입소중"))
                            {
                                Toast.makeText(StudentAttendanceEnterActivity.this, "입소 가능", Toast.LENGTH_SHORT).show();
                                studentEnterState.setState("입소완료");
                                databaseReference = database.getReference("attendance/enter/student");
                                databaseReference.child(key).setValue(studentEnterState);

                            } else
                            {
                                Toast.makeText(StudentAttendanceEnterActivity.this, "입소가 불가능합니다.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            // 입소 불가
                            if(Integer.parseInt(first_time.split(":")[0]) > Integer.parseInt(now_time.split(":")[0]))
                            {
                                Toast.makeText(StudentAttendanceEnterActivity.this, "아직 입소시간이 되지 않았습니다.", Toast.LENGTH_SHORT).show();
                                // 아직 입소시간이 되지 않았습니다.
                            }
                            else if(Integer.parseInt(second_time.split(":")[0]) <= Integer.parseInt(now_time.split(":")[0]))
                            {
                                Toast.makeText(StudentAttendanceEnterActivity.this, "입소 시간이 지났습니다.\n지각처리", Toast.LENGTH_SHORT).show();
                                // 입소 시간이 지났습니다.
                                // 지각처리
                            }
                        }
                    }

                } else {
                    Toast.makeText(StudentAttendanceEnterActivity.this, "입소 시간이 정해지지 않았습니다.", Toast.LENGTH_SHORT).show();
                    // 입소 시간이 정해지지 않았습니다.
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentAttendanceEnterActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });


    }

    public void menuBoardOnClick(View view)
    {
        Intent intent = new Intent(this, StudentBoardActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void menuAttendanceOnClick(View view)
    {
        Intent intent = new Intent(this, StudentAttendanceStateActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void menuPointOnClick(View view)
    {
        Intent intent = new Intent(this, StudentPointActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void menuStayOnClick(View view)
    {
        Intent intent = new Intent(this, StudentStayChoiceActivity.class);
        intent.putExtra("email", email);
        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void menuInformationOnClick(View view)
    {
        Intent intent = new Intent(this, StudentInformationActivity.class);
        intent.putExtra("email", email);

        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }

    public void attendanceEnterOnClick(View view)
    {
        Intent intent = new Intent(this, StudentAttendanceEnterActivity.class);
        intent.putExtra("email", email);

        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
    public void attendanceGoOutOnClick(View view)
    {
        Intent intent = new Intent(this, StudentAttendanceGoOutActivity.class);
        intent.putExtra("email", email);

        startActivity(intent);
        overridePendingTransition(0, 0);
        finish();
    }
}