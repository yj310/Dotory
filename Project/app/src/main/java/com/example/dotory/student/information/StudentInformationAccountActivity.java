package com.example.dotory.student.information;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import com.example.dotory.LoginActivity;
import com.example.dotory.R;

public class StudentInformationAccountActivity extends AppCompatActivity {


    //private String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_information_account);

        //Intent intent = getIntent();
        //email = intent.getStringExtra("email");


        findViewById(R.id.logout_btn).setOnClickListener(onClickListener);
        findViewById(R.id.withdrawal_btn).setOnClickListener(onClickListener);
        findViewById(R.id.go_back_button).setOnClickListener(onClickListener);

    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Intent intent;
            switch(view.getId()){
                case R.id.logout_btn:
                    /*intent = new Intent(StudentInformationAccountActivity.this, LoginActivity.class);
                    startActivity(intent);
                    overridePendingTransition(0, 0);
                    ActivityCompat.finishAffinity(StudentInformationAccountActivity.this);*/
                    /*intent = new Intent(StudentInformationAccountActivity.this, LoginActivity.class);

                    startActivity(intent);


                    finish();*/

                    intent = new Intent(StudentInformationAccountActivity.this, LoginActivity.class);
                    if (Build.VERSION.SDK_INT > Build.VERSION_CODES.GINGERBREAD_MR1) {
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    } else {
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    startActivity(intent);


                    break;
                case R.id.withdrawal_btn:
                    //finish();
                    break;
                case R.id.go_back_button:
                    finish();
                    break;
            }
        }
    };

}