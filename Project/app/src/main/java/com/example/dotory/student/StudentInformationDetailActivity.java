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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class StudentInformationDetailActivity extends AppCompatActivity {


    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StudentUser studentUser;

    private TextView tv_name;
    private TextView tv_room_number;
    private TextView tv_grade_class_number;
    private TextView tv_enterance;
    private TextView tv_birth;
    private TextView tv_email;
    private TextView tv_phone;
    private TextView tv_guardian_phone;
    private TextView tv_address_load;
    private TextView tv_address_detail;

    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_information_detail);



        findViewById(R.id.go_back_button).setOnClickListener(onClickListener);

        tv_name                 = findViewById(R.id.info_name);
        tv_room_number          = findViewById(R.id.info_room_number);
        tv_grade_class_number   = findViewById(R.id.info_grade_class_number);
        tv_enterance            = findViewById(R.id.info_entrance_year);
        tv_birth                = findViewById(R.id.info_birth);
        tv_email                = findViewById(R.id.info_email);
        tv_phone                = findViewById(R.id.info_phone_number);
        tv_guardian_phone       = findViewById(R.id.info_guardian_phone_number);
        tv_address_load         = findViewById(R.id.info_address_load);
        tv_address_detail       = findViewById(R.id.info_address_detail);



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


                        tv_name.setText(studentUser.getName());
                        tv_room_number.setText(studentUser.getRoom() + "호");
                        tv_grade_class_number.setText(studentUser.getGrade() + "학년 " + studentUser.getSchool_class() + "반 " + studentUser.getClass_number() + "번");
                        tv_enterance.setText(studentUser.getEntrance_year() + "년");
                        tv_birth.setText(studentUser.getBirth_year() + "-" + studentUser.getBirth_month() + "-" + studentUser.getBirth_day());
                        tv_email.setText(studentUser.getEmail());
                        tv_phone.setText(studentUser.getPhone());
                        tv_guardian_phone.setText(studentUser.getGuardian_phone());
                        tv_address_load.setText(studentUser.getAddress_load());
                        tv_address_detail.setText(studentUser.getAddress_detail());

                        break;
                    }

                }
            };



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentInformationDetailActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.go_back_button:
                    finish();
                    break;
            }
        }
    };

}