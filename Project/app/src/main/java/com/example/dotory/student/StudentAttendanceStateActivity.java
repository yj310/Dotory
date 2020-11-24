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

public class StudentAttendanceStateActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private TextView text_state;
    private String email;

    private String key;
    private String name;
    private String go_out_state = "";
    private String enter_state = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_attendance_state);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        text_state = findViewById(R.id.text_state);


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
                        name = studentUser.getName();
                        // get my state
                        databaseReference = database.getReference("attendance/enter/student");
                        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                // 파이어베이스의 데이터를 받아오는 곳
                                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                    if(snapshot.getKey().toString().equals(key)){
                                        StudentEnterState studentEnterState = snapshot.getValue(StudentEnterState.class);
                                        enter_state = studentEnterState.getState();
                                        break;
                                    }
                                }

                                databaseReference = database.getReference("attendance/go_out/student");
                                databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                        // 파이어베이스의 데이터를 받아오는 곳
                                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                                            if(snapshot.getKey().toString().equals(key)){
                                                StudentGoOutState studentGoOutState = snapshot.getValue(StudentGoOutState.class);
                                                go_out_state = studentGoOutState.getState();
                                                break;
                                            }
                                        }
                                        if(enter_state.equals("입소중"))
                                        {
                                            text_state.setText("이연지님은\n현재 입소 중입니다.");
                                        } else if (go_out_state.equals("외출중")){
                                            text_state.setText("이연지님은\n현재 외출 중입니다.");
                                        } else {
                                            text_state.setText(name + "님은\n현재 특별한 이벤트가 없습니다.");
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {
                                        // DB를 가져오던 중 에러 발생 시
                                        Toast.makeText(StudentAttendanceStateActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError error) {
                                // DB를 가져오던 중 에러 발생 시
                                Toast.makeText(StudentAttendanceStateActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
                            }
                        });


                        break;
                    }
                }
            };
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentAttendanceStateActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
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