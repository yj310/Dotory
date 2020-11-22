package com.example.dotory.manager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.dotory.Post;
import com.example.dotory.R;
import com.example.dotory.student.StudentUser;
import com.example.dotory.student.information.StudentInformationDetailActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagerPostModifyActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private EditText et_title;
    private EditText et_content;
    private Post post;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_post_modify);

        et_title = findViewById(R.id.input_title);
        et_content = findViewById(R.id.input_content);
        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");
            Toast.makeText(this, id, Toast.LENGTH_SHORT).show();


            findViewById(R.id.go_back_button).setOnClickListener(onClickListener);
            findViewById(R.id.post_add_image_btn).setOnClickListener(onClickListener);
            findViewById(R.id.post_add_file_btn).setOnClickListener(onClickListener);
            findViewById(R.id.post_modify_btn).setOnClickListener(onClickListener);

            database = FirebaseDatabase.getInstance();
            databaseReference = database.getReference("post/" + id);
            databaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    post = dataSnapshot.getValue(Post.class);
                    et_title.setText(post.getTitle());
                    et_content.setText(post.getContent());
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(ManagerPostModifyActivity.this, "데이터베이스 오류\n" + databaseError.toString(), Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception e) {
            Toast.makeText(this, "오류\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch(view.getId()){
                case R.id.go_back_button:
                    finish();
                    break;
                case R.id.post_add_image_btn:
                    Toast.makeText(ManagerPostModifyActivity.this, "이미지 로드", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.post_add_file_btn:
                    Toast.makeText(ManagerPostModifyActivity.this, "파일 로드", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.post_modify_btn:
                    postModify();
                    break;
            }
        }
    };

    void postModify() {

        if(et_title.getText().length() <= 0 || et_content.getText().length() <= 0)
        {
            Toast.makeText(ManagerPostModifyActivity.this, "입력되지 않은 항목이 존재합니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(ManagerPostModifyActivity.this, "게시물 수정중", Toast.LENGTH_SHORT).show();
        try {

            post.setTitle(et_title.getText().toString());
            post.setContent(et_content.getText().toString());


            databaseReference.setValue(post);
            Toast.makeText(this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
            finish();

        }catch(Exception e){
            Toast.makeText(this, "데이터베이스 오류\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }



    }
}