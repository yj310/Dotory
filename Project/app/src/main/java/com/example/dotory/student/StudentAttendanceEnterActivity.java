package com.example.dotory.student;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dotory.R;
import com.example.dotory.student.information.StudentInformationActivity;

public class StudentAttendanceEnterActivity extends AppCompatActivity {

    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_attendance_enter);

        Intent intent = getIntent();
        email = intent.getStringExtra("email");

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