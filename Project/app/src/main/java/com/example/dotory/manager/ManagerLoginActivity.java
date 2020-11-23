package com.example.dotory.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dotory.JoinActivity;
import com.example.dotory.LoginActivity;
import com.example.dotory.Post;
import com.example.dotory.R;
import com.example.dotory.student.StudentBoardActivity;
import com.example.dotory.student.StudentUser;
import com.example.dotory.student.information.StudentInformationAccountActivity;
import com.example.dotory.student.information.StudentInformationActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ManagerLoginActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_login);


        findViewById(R.id.login_btn).setOnClickListener(onClickListener);



    }


    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.login_btn:
                    login();
                    break;
            }
        }
    };


    private void login() {

        final String password = ((EditText) findViewById(R.id.input_password)).getText().toString();


        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("ManagerUser");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳

                if(password.length() > 0) {
                    if (password.equals(dataSnapshot.child("password").getValue().toString())) {
                        Intent intent = new Intent(ManagerLoginActivity.this, ManagerBoardActivity.class);
                        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        } else {
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        }
                        startActivity(intent);
                    } else {
                        Toast.makeText(ManagerLoginActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ManagerLoginActivity.this, "비밀번호를 입력해주십시오.", Toast.LENGTH_SHORT).show();
                }

            };



            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(ManagerLoginActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}