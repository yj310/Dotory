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

public class StudentAttendanceGoOutActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private TextView text_date;
    private TextView text_go_out_time;
    private TextView purpose;
    private TextView place;
    private TextView expected_time;

    private String name;
    private String room;
    private String key = "";
    private String state = "";
    private String today;

    private String first_time;
    private String second_time;

    private String email;

    private StudentGoOutState studentGoOutState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_attendance_go_out);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");


        text_date = findViewById(R.id.text_date);
        text_go_out_time = findViewById(R.id.text_go_out_time);
        purpose = findViewById(R.id.purpose);
        place = findViewById(R.id.place);
        expected_time = findViewById(R.id.expected_time);

        findViewById(R.id.go_out_btn).setOnClickListener(onClickListener);



        final Date now = new Date();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy년 MM월 dd일");
        text_date.setText(formatter.format(now));

        formatter = new SimpleDateFormat("yyyy-MM-dd");
        today = formatter.format(now);


        database = FirebaseDatabase.getInstance();

        // 학생 정보 불러오기
        databaseReference = database.getReference("StudentUser");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    StudentUser studentUser = snapshot.getValue(StudentUser.class);
                    if(studentUser.getEmail().equals(email))
                    {
                        name = studentUser.getName();
                        room = studentUser.getRoom();
                        key = studentUser.getRoom() + email.split("@")[0];

                        // get my state
                        databaseReference = database.getReference("attendance/go_out/"+ today +"student");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if(snapshot.getKey().toString().equals(key))
                                    {
                                        studentGoOutState = snapshot.getValue(StudentGoOutState.class);
                                        state = studentGoOutState.getState();
                                        break;
                                    }
                                }
                            }
                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(StudentAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });
                        break;
                    }

                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

        // 외출 시간 불러오기
        databaseReference = database.getReference("attendance/go_out");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                first_time = dataSnapshot.child("time").child("first").getValue().toString();
                second_time = dataSnapshot.child("time").child("second").getValue().toString();
                text_go_out_time.setText(first_time + "      " + second_time);


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch(view.getId()){
                case R.id.go_out_btn:
                    go_out();
                    break;

            }
        }
    };

    public void go_out()
    {

        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm");
        Date now = new Date();
        String now_time = formatter.format(now);


        databaseReference = database.getReference("attendance/go_out");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String date = dataSnapshot.child("date").getValue().toString();

                Date now = new Date();
                SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
                String today = formatter.format(now);

                if(date.equals(today))
                {

                    if(first_time.equals("00:00")&&second_time.equals("00:00")) {
                        Toast.makeText(StudentAttendanceGoOutActivity.this, "외출 시간이 정해지지 않았습니다.", Toast.LENGTH_SHORT).show();
                        // 입소 시간이 정해지지 않았습니다.
                    } else {
                        formatter = new SimpleDateFormat("HH:mm");
                        String now_time = formatter.format(now);

                        if(state.length() <= 0) {
                            // 현재 시간이 외출시간이라면
                            if((Integer.parseInt(first_time.split(":")[0]) < Integer.parseInt(now_time.split(":")[0])
                                    || (Integer.parseInt(first_time.split(":")[0]) == Integer.parseInt(now_time.split(":")[0])
                                    && Integer.parseInt(first_time.split(":")[1]) <= Integer.parseInt(now_time.split(":")[1])))

                                    && (Integer.parseInt(second_time.split(":")[0]) > Integer.parseInt(now_time.split(":")[0])
                                    || (Integer.parseInt(second_time.split(":")[0]) == Integer.parseInt(now_time.split(":")[0])
                                    && Integer.parseInt(second_time.split(":")[1]) >= Integer.parseInt(now_time.split(":")[1])))) {

                                StudentGoOutState studentGoOutState = new StudentGoOutState(
                                        name,
                                        room,
                                        "외출중",
                                        now_time,
                                        expected_time.getText().toString(),
                                        "",
                                        place.getText().toString(),
                                        purpose.getText().toString()
                                );

                                databaseReference = database.getReference("attendance/go_out/" + today + "/student");
                                databaseReference.child(key).setValue(studentGoOutState);
                                Toast.makeText(StudentAttendanceGoOutActivity.this, "외출 신청이 완료되었습니다.", Toast.LENGTH_SHORT).show();

                            } else {
                                // 외출 시간 아님
                                Toast.makeText(StudentAttendanceGoOutActivity.this, "외출 가능 시간이 아닙니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(StudentAttendanceGoOutActivity.this, "외출이 불가능합니다.", Toast.LENGTH_SHORT).show();
                        }
                    }

                } else {
                    Toast.makeText(StudentAttendanceGoOutActivity.this, "외출 시간이 정해지지 않았습니다.", Toast.LENGTH_SHORT).show();
                }


            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentAttendanceGoOutActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
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