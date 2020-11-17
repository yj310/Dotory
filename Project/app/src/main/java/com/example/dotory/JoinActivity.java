package com.example.dotory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.dotory.student.StudentBoardActivity;
import com.example.dotory.student.StudentUser;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class JoinActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    private TextView tv_name;
    private TextView tv_password;
    private TextView tv_password_again;
    private TextView tv_email;
    private TextView tv_phone;
    private TextView tv_guardian_phone;
    private TextView tv_address_load;
    private TextView tv_address_detail;
    private Spinner tv_room;
    private Spinner tv_entrance;
    private Spinner tv_grade;
    private Spinner tv_school_class;
    private Spinner tv_class_number;
    private Spinner tv_birth_year;
    private Spinner tv_birth_month;
    private Spinner tv_birth_day;

    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_join);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        findViewById(R.id.join_btn).setOnClickListener(onClickListener);
        findViewById(R.id.go_back_button).setOnClickListener(onClickListener);

        tv_name = findViewById(R.id.input_name);
        tv_password = findViewById(R.id.input_password);
        tv_password_again = findViewById(R.id.input_password_again);
        tv_email = findViewById(R.id.input_email);
        tv_phone = findViewById(R.id.input_phone_number);
        tv_guardian_phone = findViewById(R.id.input_guardian_phone_number);
        tv_address_load = findViewById(R.id.input_address_load);
        tv_address_detail = findViewById(R.id.input_address_detail);
        tv_room = findViewById(R.id.spinner_room_number);
        tv_entrance = findViewById(R.id.spinner_entrance_year);
        tv_grade = findViewById(R.id.spinner_grade);
        tv_school_class = findViewById(R.id.spinner_class);
        tv_class_number = findViewById(R.id.spinner_class_number);
        tv_birth_year = findViewById(R.id.spinner_birth_year);
        tv_birth_month = findViewById(R.id.spinner_birth_month);
        tv_birth_day = findViewById(R.id.spinner_birth_day);

    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.join_btn:
                    signin();
                    break;
                case R.id.go_back_button:
                    finish();
                    break;
            }
        }
    };

    private void signin() {

        String email = ((EditText)findViewById(R.id.input_email)).getText().toString();
        String password = ((EditText)findViewById(R.id.input_password)).getText().toString();
        String passwordAgain = ((EditText)findViewById(R.id.input_password_again)).getText().toString();

        if(email.length() > 0 && password.length() > 0 && passwordAgain.length() > 0) {
            if (password.equals(passwordAgain)) {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(JoinActivity.this, "회원가입에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                    writeNewUser();
                                    Toast.makeText(JoinActivity.this, "*", Toast.LENGTH_SHORT).show();

                                    FirebaseUser user = mAuth.getCurrentUser();
                                    finish();
                                } else {
                                    if(task.getException() != null){
                                        Toast.makeText(JoinActivity.this, "회원가입에 실패하였습니다.",
                                                Toast.LENGTH_SHORT).show();
                                    }

                                }

                                // ...
                            }
                        });

            } else {
                Toast.makeText(JoinActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();

            }
        } else {
            Toast.makeText(JoinActivity.this, "입력되지 않은 항목이 존재합니다.", Toast.LENGTH_SHORT).show();

        }



    }

    private void writeNewUser() {


        StudentUser studentUser = new StudentUser(tv_name.getText().toString()
                , tv_room.getSelectedItem().toString()
                , tv_entrance.getSelectedItem().toString()
                , tv_phone.getText().toString()
                , tv_email.getText().toString()
                , tv_guardian_phone.getText().toString()
                , tv_address_load.getText().toString()
                , tv_address_detail.getText().toString()
                , tv_grade.getSelectedItem().toString()
                , tv_school_class.getSelectedItem().toString()
                , tv_class_number.getSelectedItem().toString()
                , tv_birth_year.getSelectedItem().toString()
                , tv_birth_month.getSelectedItem().toString()
                , tv_birth_day.getSelectedItem().toString());

        mDatabase.child("StudentUser").child(tv_email.getText().toString().split("@")[0]).setValue(studentUser);

    }

}