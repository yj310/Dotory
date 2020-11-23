package com.example.dotory.manager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.dotory.Post;
import com.example.dotory.R;
import com.example.dotory.student.StudentUser;
import com.example.dotory.student.information.StudentInformationDetailActivity;
import com.example.dotory.student.information.StudentInformationModifyActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ManagerPostUploadActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private ImageView imageView;
    private EditText et_title;
    private EditText et_content;

    private Uri filePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_post_upload);

        et_title = findViewById(R.id.input_title);
        et_content = findViewById(R.id.input_content);

        imageView = (ImageView)findViewById(R.id.image);


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
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 1);

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                filePath = data.getData();
                try {
                    // 선택한 이미지에서 비트맵 생성
                    InputStream in = getContentResolver().openInputStream(filePath);
                    Bitmap img = BitmapFactory.decodeStream(in);
                    in.close();
                    // 이미지 표시
                    imageView.setImageBitmap(img);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void uploadFile() {
        //업로드할 파일이 있으면 수행
        if (filePath != null) {
            //업로드 진행 Dialog 보이기
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("업로드중...");
            progressDialog.show();

            //storage
            FirebaseStorage storage = FirebaseStorage.getInstance();

            //Unique한 파일명을 만들자.
            SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
            Date now = new Date();
            String filename = formatter.format(now) + ".png";
            //storage 주소와 폴더 파일명을 지정해 준다.
            StorageReference storageRef = storage.getReferenceFromUrl("gs://dotory-f6a74.appspot.com").child("postImages/" + filename);
            //올라가거라...
            storageRef.putFile(filePath)
                    //성공시
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기
                            Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //실패시
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    //진행중
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            @SuppressWarnings("VisibleForTests") //이걸 넣어 줘야 아랫줄에 에러가 사라진다. 넌 누구냐?
                                    double progress = (100 * taskSnapshot.getBytesTransferred()) /  taskSnapshot.getTotalByteCount();
                            //dialog에 진행률을 퍼센트로 출력해 준다
                            progressDialog.setMessage("Uploaded " + ((int) progress) + "% ...");
                        }
                    });
        } else {
            Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
        }
    }

    void postUpload() {

        if(et_title.getText().length() <= 0 || et_content.getText().length() <= 0)
        {
            Toast.makeText(ManagerPostUploadActivity.this, "입력되지 않은 항목이 존재합니다.", Toast.LENGTH_SHORT).show();
            return;
        }


        Toast.makeText(ManagerPostUploadActivity.this, "게시물 업로드중", Toast.LENGTH_SHORT).show();
        try {
            SimpleDateFormat formatID = new SimpleDateFormat("yyyyMMddHHmmss");
            SimpleDateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm:ss");
            Date time = new Date();

            uploadFile();
            Post post;
            if (filePath != null){
                post = new Post(
                        et_title.getText().toString(),
                        et_content.getText().toString(),
                        formatDate.format(time),
                        formatTime.format(time),
                        formatID.format(time) + ".jpg",
                        true
                );
            } else {
                post = new Post(
                        et_title.getText().toString(),
                        et_content.getText().toString(),
                        formatDate.format(time),
                        formatTime.format(time),
                        "",
                        true
                );
            }
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