package com.example.dotory.student;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.dotory.Post;
import com.example.dotory.PostCustomAdapter;
import com.example.dotory.R;
import com.example.dotory.student.information.StudentInformationActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class StudentBoardActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<Post> arrayList;
    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private String email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_student_board);


        Intent intent = getIntent();
        email = intent.getStringExtra("email");

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        arrayList = new ArrayList<>();

        database = FirebaseDatabase.getInstance();

        databaseReference = database.getReference("post");
        databaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // 파이어베이스의 데이터를 받아오는 곳
                arrayList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    Post post = snapshot.getValue(Post.class);
                    arrayList.add(post);

                }
                Collections.reverse(arrayList);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // DB를 가져오던 중 에러 발생 시
                Toast.makeText(StudentBoardActivity.this, error.toException().toString(), Toast.LENGTH_SHORT).show();
            }
        });
        adapter = new PostCustomAdapter(arrayList, this);
        recyclerView.setAdapter(adapter);

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
}