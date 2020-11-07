package com.example.dotory;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.example.dotory.student.StudentBoardActivity;

public class LoginActivity extends AppCompatActivity {
    Button loginButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        EditText IDText = (EditText)findViewById(R.id.user_e_mail);
        EditText pwText = (EditText)findViewById(R.id.user_password);


        loginButton = (Button)findViewById(R.id.btn_login);
        loginButton.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                    loginButton.setBackgroundColor(Color.argb(255,135, 119, 109));
                } else if(motionEvent.getAction() == MotionEvent.ACTION_UP) {
                    loginButton.setBackgroundColor(Color.argb(255,166, 149, 139));
                }

                return false;
            }
        });


    }

    public void loginOnClick(View view)
    {
        Intent intent = new Intent(this, StudentBoardActivity.class);
        startActivity(intent);
        finish();
    }

    public void JoinOnClick(View view)
    {
        Intent intent = new Intent(this, JoinActivity.class);
        startActivity(intent);
    }

}