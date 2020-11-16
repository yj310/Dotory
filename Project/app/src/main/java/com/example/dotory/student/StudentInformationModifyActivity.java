package com.example.dotory.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotory.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class StudentInformationModifyActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private StudentUser studentUser;

    private EditText et_name;
    private Spinner sp_room_number;
    private Spinner sp_entrance;
    private Spinner sp_grade;
    private Spinner sp_school_class;
    private Spinner sp_class_number;
    private Spinner sp_birth_year;
    private Spinner sp_birth_month;
    private Spinner sp_birth_day;
    private EditText et_email;
    private EditText et_phone;
    private EditText et_guardian_phone;
    private EditText et_address_load;
    private EditText et_address_detail;

    private String[] list;
    //List<String> listItem = Arrays.asList(list);
    //ArrayList<String> list_arr = new ArrayList<String> (listItem);
    private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_information_modify);

        findViewById(R.id.go_back_button).setOnClickListener(onClickListener);
        findViewById(R.id.modify_btn).setOnClickListener(onClickListener);

        et_name                 = findViewById(R.id.info_name);
        sp_room_number          = findViewById(R.id.spinner_room_number);
        sp_entrance            = findViewById(R.id.spinner_entrance_year);
        sp_grade                = findViewById(R.id.spinner_grade);
        sp_school_class         = findViewById(R.id.spinner_class);
        sp_class_number         = findViewById(R.id.spinner_class_number);
        sp_birth_year           = findViewById(R.id.spinner_birth_year);
        sp_birth_month          = findViewById(R.id.spinner_birth_month);
        sp_birth_day            = findViewById(R.id.spinner_birth_day);
        et_email                = findViewById(R.id.info_email);
        et_phone                = findViewById(R.id.info_phone_number);
        et_guardian_phone       = findViewById(R.id.info_guardian_phone_number);
        et_address_load         = findViewById(R.id.info_address_load);
        et_address_detail       = findViewById(R.id.info_address_detail);


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

                        et_name.setText(studentUser.getName());
                        list = getResources().getStringArray(R.array.array_entrance_year);


                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getEntrance_year())){
                                sp_entrance.setSelection(i);
                                break;
                            }

                        }

                        list = getResources().getStringArray(R.array.array_room_number);
                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getRoom())){
                                sp_room_number.setSelection(i);
                                break;
                            }

                        }

                        list = getResources().getStringArray(R.array.array_room_number);
                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getRoom())){
                                sp_room_number.setSelection(i);
                                break;
                            }

                        }

                        list = getResources().getStringArray(R.array.array_grade);
                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getGrade())){
                                sp_grade.setSelection(i);
                                break;
                            }

                        }

                        list = getResources().getStringArray(R.array.array_class);
                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getSchool_class())){
                                sp_school_class.setSelection(i);
                                break;
                            }

                        }

                        list = getResources().getStringArray(R.array.array_class_number);
                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getClass_number())){
                                sp_class_number.setSelection(i);
                                break;
                            }

                        }

                        list = getResources().getStringArray(R.array.array_birth_year);
                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getBirth_year())){
                                sp_birth_year.setSelection(i);
                                break;
                            }

                        }

                        list = getResources().getStringArray(R.array.array_birth_month);
                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getBirth_month())){
                                sp_birth_month.setSelection(i);
                                break;
                            }

                        }

                        list = getResources().getStringArray(R.array.array_birth_day_31);
                        for (int i = 0; i < list.length; i++) {
                            if(list[i].equals(studentUser.getBirth_day())){
                                sp_birth_day.setSelection(i);
                                break;
                            }

                        }

                        et_email.setText(studentUser.getEmail());
                        et_phone.setText(studentUser.getPhone());
                        et_guardian_phone.setText(studentUser.getGuardian_phone());
                        et_address_load.setText(studentUser.getAddress_load());
                        et_address_detail.setText(studentUser.getAddress_detail());

                        break;
                    }

                }
            };



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentInformationModifyActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
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
                case R.id.modify_btn:
                    if(checkEmptyElement())
                    {
                        // 비밀번호 확인
                        if(checkPassword())
                        {
                            modifyUser();
                        }

                    }

                    break;
            }
        }
    };
    private void modifyUser() {
        StudentUser studentUser = new StudentUser(et_name.getText().toString()
                , sp_room_number.getSelectedItem().toString()
                , sp_entrance.getSelectedItem().toString()
                , et_phone.getText().toString()
                , et_email.getText().toString()
                , et_guardian_phone.getText().toString()
                , et_address_load.getText().toString()
                , et_address_detail.getText().toString()
                , sp_grade.getSelectedItem().toString()
                , sp_school_class.getSelectedItem().toString()
                , sp_class_number.getSelectedItem().toString()
                , sp_birth_year.getSelectedItem().toString()
                , sp_birth_month.getSelectedItem().toString()
                , sp_birth_day.getSelectedItem().toString());
        try {
            databaseReference.child(email.split("@")[0]).setValue(studentUser);
            Toast.makeText(this, "정보가 수정되었습니다.", Toast.LENGTH_SHORT).show();
            finish();

        }catch(Exception e){
            Toast.makeText(this, "데이터베이스 오류\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }

        // 데이터 수정


    }

    private boolean checkEmptyElement() {

        if(et_name.getText().toString().equals("")
                || sp_room_number.getSelectedItem().toString().equals("")
                || sp_entrance.getSelectedItem().toString().equals("")
                || et_phone.getText().toString().equals("")
                || et_email.getText().toString().equals("")
                || et_guardian_phone.getText().toString().equals("")
                || et_address_load.getText().toString().equals("")
                || et_address_detail.getText().toString().equals("")
                || sp_grade.getSelectedItem().toString().equals("")
                || sp_school_class.getSelectedItem().toString().equals("")
                || sp_class_number.getSelectedItem().toString().equals("")
                || sp_birth_year.getSelectedItem().toString().equals("")
                || sp_birth_month.getSelectedItem().toString().equals("")
                || sp_birth_day.getSelectedItem().toString().equals(""))
            return false;

        return true;
    }

    private boolean checkPassword() {


        return true;
    }

}