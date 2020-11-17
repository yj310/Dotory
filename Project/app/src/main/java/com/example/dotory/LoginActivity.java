package com.example.dotory;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dotory.manager.ManagerBoardActivity;
import com.example.dotory.manager.ManagerLoginActivity;
import com.example.dotory.student.StudentBoardActivity;
import com.example.dotory.student.StudentUser;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        findViewById(R.id.join_btn).setOnClickListener(onClickListener);
        findViewById(R.id.login_btn).setOnClickListener(onClickListener);
        findViewById(R.id.manager_login_btn).setOnClickListener(onClickListener);

        /*loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    loginButton.setBackgroundColor(Color.argb(255,135, 119, 109));
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    loginButton.setBackgroundColor(Color.argb(255,166, 149, 139));
                }

                return false;
            }
        });*/


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
            Intent intent;
            switch(view.getId()){
                case R.id.join_btn:
                    intent = new Intent(LoginActivity.this, JoinActivity.class);
                    startActivity(intent);
                    break;
                case R.id.login_btn:
                    login();
                    break;
                case R.id.manager_login_btn:
                    intent = new Intent(LoginActivity.this, ManagerLoginActivity.class);
                    startActivity(intent);
                    break;
            }
        }
    };


    private void login() {

        final String email = ((EditText) findViewById(R.id.input_email)).getText().toString();
        final String password = ((EditText) findViewById(R.id.input_password)).getText().toString();

        if (email.length() > 0 && password.length() > 0 ) {

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Toast.makeText(LoginActivity.this, "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();


                                Intent intent = new Intent(LoginActivity.this, StudentBoardActivity.class);
                                intent.putExtra("email", email);



                                startActivity(intent);
                                finish();
                            } else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(LoginActivity.this, task.getException().toString(), Toast.LENGTH_SHORT).show();

                            }

                        }
                    });


        } else {
            Toast.makeText(LoginActivity.this, "입력되지 않은 항목이 존재합니다.", Toast.LENGTH_SHORT).show();

        }

    }

}