package com.example.dotory.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dotory.R;

public class StudentDemeritActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_demerit);
    }

    public void menuBoardOnClick(View view)
    {
        Intent intent = new Intent(this, StudentBoardActivity.class);
        startActivity(intent);
        finish();
    }
    public void menuAttendanceOnClick(View view)
    {
        Intent intent = new Intent(this, StudentAttendanceOvernightActivity.class);
        startActivity(intent);
        finish();
    }
    public void menuDemeritOnClick(View view)
    {
        Intent intent = new Intent(this, StudentDemeritActivity.class);
        startActivity(intent);
        finish();
    }
    public void menuStayOnClick(View view)
    {
        Intent intent = new Intent(this, StudentStayChoiceActivity.class);
        startActivity(intent);
        finish();
    }
    public void menuInformationOnClick(View view)
    {
        Intent intent = new Intent(this, StudentInformationActivity.class);
        startActivity(intent);
        finish();
    }
}