package com.example.dotory.manager;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.example.dotory.Post;
import com.example.dotory.R;
import com.example.dotory.student.StudentUser;
import com.example.dotory.student.information.StudentInformationDetailActivity;
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

public class ManagerPostModifyActivity extends AppCompatActivity {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;

    private EditText et_title;
    private EditText et_content;
    private Post post;
    private ImageView imageView;

    private Uri filePath;

    private String id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager_post_modify);

        et_title = findViewById(R.id.input_title);
        et_content = findViewById(R.id.input_content);
        imageView = (ImageView)findViewById(R.id.image);
        try {
            Intent intent = getIntent();
            id = intent.getStringExtra("id");


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

                    if(post.getImg_url().length() > 0) {
                        FirebaseStorage fs = FirebaseStorage.getInstance();
                        StorageReference imagesRef = fs.getReference().child("postImages/" + post.getImg_url());

                        Activity activity = (Activity) ManagerPostModifyActivity.this;
                        if (activity.isFinishing())
                            return;

                        Glide.with(ManagerPostModifyActivity.this)
                                .load(imagesRef)
                                .into(imageView);
                    }

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
                    Intent intent = new Intent();
                    intent.setType("image/*");
                    intent.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(intent, 1);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        try {

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
        } catch(Exception e)
        {
            Toast.makeText(this, "onActivityResult\n"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean uploadFile() {
        try {
            //업로드할 파일이 있으면 수행
            if (filePath != null) {
                //업로드 진행 Dialog 보이기

                final ProgressDialog progressDialog = new ProgressDialog(this);
                progressDialog.setTitle("업로드중...");
                progressDialog.show();

                //storage
                FirebaseStorage storage = FirebaseStorage.getInstance();
                final String filename;
                final String prevfilename = post.getImg_url();
                //Unique한 파일명을 만들자.
                if(post.getImg_url().length() <= 0)
                {
                    filename = post.getDate().replaceAll("-", "") + post.getTime().replaceAll(":", "") + ".jpg";
                } else if(!post.getImg_url().contains("_"))
                {
                    filename = post.getImg_url().substring(0, post.getImg_url().indexOf(".")) + "_0" + ".jpg" ;
                } else
                {
                    filename = post.getImg_url().substring(0, post.getImg_url().indexOf("_") + 1)
                            + Integer.toString((post.getImg_url().charAt(post.getImg_url().indexOf("_") + 1) - '0' + 1)) + ".jpg";
                }
                
                storage.getReference().child("postImages/" + prevfilename).delete().addOnSuccessListener(new OnSuccessListener<Void>() {

                    @Override
                    public void onSuccess(Void aVoid) {
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });

                //storage 주소와 폴더 파일명을 지정해 준다.
                StorageReference storageRef = storage.getReferenceFromUrl("gs://dotory-f6a74.appspot.com").child("postImages/" + filename);
                //올라가거라...
                storageRef.putFile(filePath)
                        //성공시
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                progressDialog.dismiss(); //업로드 진행 Dialog 상자 닫기=
                                Toast.makeText(getApplicationContext(), "업로드 완료!", Toast.LENGTH_SHORT).show();
                                post.setImg_url(filename);
                                databaseReference.setValue(post);
                                Toast.makeText(ManagerPostModifyActivity.this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            }
                        })
                        //실패시
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(getApplicationContext(), "업로드 실패!", Toast.LENGTH_SHORT).show();
                                finish();
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
                return true;
            } else {
                Toast.makeText(getApplicationContext(), "파일을 먼저 선택하세요.", Toast.LENGTH_SHORT).show();
            }
        } catch(Exception e) {
            Toast.makeText(this, "uploadFile()\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }
        return false;
    }

    void postModify() {

        try {
            if (et_title.getText().length() <= 0 || et_content.getText().length() <= 0) {
                Toast.makeText(ManagerPostModifyActivity.this, "입력되지 않은 항목이 존재합니다.", Toast.LENGTH_SHORT).show();
                return;
            }

            try {

                post.setTitle(et_title.getText().toString());
                post.setContent(et_content.getText().toString());

                if(!uploadFile())
                {
                    databaseReference.setValue(post);
                    Toast.makeText(ManagerPostModifyActivity.this, "수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                Toast.makeText(this, "데이터베이스 오류\n" + e.toString(), Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e) {
            Toast.makeText(this, "postModify\n" + e.toString(), Toast.LENGTH_SHORT).show();
        }



    }
}