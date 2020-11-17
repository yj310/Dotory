package com.example.dotory.student.information;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotory.LoginActivity;
import com.example.dotory.Post;
import com.example.dotory.R;
import com.example.dotory.student.StudentAttendanceStateActivity;
import com.example.dotory.student.StudentBoardActivity;
import com.example.dotory.student.StudentPointActivity;
import com.example.dotory.student.StudentStayChoiceActivity;
import com.example.dotory.student.StudentUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class StudentInformationActivity extends AppCompatActivity {

    private ArrayList<Post> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String email;
    private StudentUser studentUser;
    private TextView tv_info_name;
    private TextView tv_info_room;
    private TextView tv_info_grade_class_number;
    private TextView tv_info_phone;
    private TextView tv_info_email;


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_information);

        ImageView imageView = (ImageView) findViewById(R.id.profile_img);
        tv_info_name = (TextView) findViewById(R.id.info_text_name);
        tv_info_room = (TextView) findViewById(R.id.info_text_room);
        tv_info_grade_class_number = (TextView) findViewById(R.id.info_text_grade_class_number);
        tv_info_phone = (TextView) findViewById(R.id.info_text_phone);
        tv_info_email = (TextView) findViewById(R.id.info_text_email);


        findViewById(R.id.detail_info_btn).setOnClickListener(onClickListener);
        findViewById(R.id.modify_info_btn).setOnClickListener(onClickListener);
        findViewById(R.id.push_btn).setOnClickListener(onClickListener);
        findViewById(R.id.account_btn).setOnClickListener(onClickListener);


        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("StudentUser");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {

                    studentUser = snapshot.getValue(StudentUser.class);

                    if(studentUser.getEmail().equals(email)){


                        tv_info_name.setText(studentUser.getName());
                        tv_info_room.setText(studentUser.getRoom());
                        tv_info_grade_class_number.setText(studentUser.getGrade() + "학년 " + studentUser.getSchool_class() + "반 " + studentUser.getClass_number() + "번");
                        tv_info_phone.setText(studentUser.getPhone());
                        tv_info_email.setText(studentUser.getEmail());

                        break;
                    }

                }
            };



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentInformationActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch(view.getId()){
                case R.id.detail_info_btn:
                    intent = new Intent(StudentInformationActivity.this, StudentInformationDetailActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.modify_info_btn:
                    intent = new Intent(StudentInformationActivity.this, StudentInformationModifyActivity.class);
                    intent.putExtra("email", email);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.push_btn:
                    intent = new Intent(StudentInformationActivity.this, StudentInformationPushActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
                case R.id.account_btn:
                    intent = new Intent(StudentInformationActivity.this, StudentInformationAccountActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    break;
            }
        }
    };
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
}