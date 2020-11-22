package com.example.dotory.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dotory.Post;
import com.example.dotory.R;
import com.example.dotory.student.StudentUser;
import com.example.dotory.student.information.StudentInformationDetailActivity;
import com.example.dotory.student.information.StudentInformationModifyActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagerPostUploadActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private EditText et_title;
    private EditText et_content;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_post_upload);

        et_title = findViewById(R.id.input_title);
        et_content = findViewById(R.id.input_content);



        findViewById(R.id.go_back_button).setOnClickListener(onClickListener);
        findViewById(R.id.post_add_image_btn).setOnClickListener(onClickListener);
        findViewById(R.id.post_add_file_btn).setOnClickListener(onClickListener);
        findViewById(R.id.post_upload_btn).setOnClickListener(onClickListener);
    }
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.go_back_button:
                    finish();
                    break;
                case R.id.post_add_image_btn:
                    Toast.makeText(ManagerPostUploadActivity.this, "이미지 로드", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.post_add_file_btn:
                    Toast.makeText(ManagerPostUploadActivity.this, "파일 로드", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.post_upload_btn:
                    postUpload();
                    break;
            }
        }
    };

    void postUpload() {

        if(et_title.getText().length() <= 0 || et_content.getText().length() <= 0)
        {
            Toast.makeText(ManagerPostUploadActivity.this, "입력되지 않은 항목이 존재합니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(ManagerPostUploadActivity.this, "게시물 업로드중", Toast.LENGTH_SHORT).show();
        try {
            SimpleDateFormat formatID = new SimpleDateFormat ( "yyyyMMddHHmmss");
            SimpleDateFormat formatDate = new SimpleDateFormat ( "yyyy-MM-dd");
            SimpleDateFormat formatTime = new SimpleDateFormat ( "HH:mm:ss");
            Date time = new Date();

            Post post = new Post(
                    et_title.getText().toString(),
                    et_content.getText().toString(),
                    formatDate.format(time),
                    formatTime.format(time),
                    true
            );
            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("post");
            databaseReference.child(formatID.format(time)).setValue(post);
            Toast.makeText(this, "게시가 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();

        }catch(Exception e){
            Toast.makeText(this, "데이터베이스 오류\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }



    }
}