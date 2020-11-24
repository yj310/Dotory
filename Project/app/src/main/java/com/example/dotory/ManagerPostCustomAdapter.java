package com.example.dotory;

import android.content.Context;
import android.content.Intent;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.dotory.manager.ManagerBoardActivity;
import com.example.dotory.manager.ManagerPostModifyActivity;
import com.example.dotory.manager.ManagerPostUploadActivity;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Dictionary;


public class ManagerPostCustomAdapter extends RecyclerView.Adapter<ManagerPostCustomAdapter.PostCustomViewHolder> {

    private FirebaseDatabase database;
    private DatabaseReference databaseReference;
    private ArrayList<Post> arrayList;
    private Context context;


    public ManagerPostCustomAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public PostCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.manager_list_post, parent, false);
        PostCustomViewHolder holder = new PostCustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostCustomViewHolder holder, int position) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date now = new Date();
        String today = formatter.format(now);
        if(arrayList.get(position).getDate().equals(today))
        {
            holder.tv_date.setText("오늘   "+ arrayList.get(position).getTime().substring(0, 5));
        } else
        {
            holder.tv_date.setText(arrayList.get(position).getDate());
        }

        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_content.setText(arrayList.get(position).getContent());
        holder.id = arrayList.get(position).getDate().replaceAll("-", "") + arrayList.get(position).getTime().replaceAll(":", "");


        if(arrayList.get(position).getImg_url().length() > 0)
        {
            FirebaseStorage fs = FirebaseStorage.getInstance();
            StorageReference imagesRef = fs.getReference().child("postImages/"+arrayList.get(position).getImg_url());
            Glide.with(holder.itemView)
                    .load(imagesRef)
                    .into(holder.iv_post_img);
        }
    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostCustomViewHolder extends RecyclerView.ViewHolder{

        TextView tv_title;
        TextView tv_date;
        TextView tv_content;
        String id;
        LinearLayout post_menu_button;
        ImageView iv_post_img;

        public PostCustomViewHolder(@NonNull final View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.text_title);
            this.tv_date = itemView.findViewById(R.id.text_date);
            this.tv_content = itemView.findViewById(R.id.text_content);
            this.post_menu_button = itemView.findViewById(R.id.post_menu_button);
            this.iv_post_img = itemView.findViewById(R.id.post_img);
            this.post_menu_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu popup = new PopupMenu(context, v);
                    popup.getMenuInflater().inflate(R.menu.manager_post_menu, popup.getMenu());

                    popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getTitle().equals("수정")){
                                Intent intent = new Intent(context, ManagerPostModifyActivity.class);
                                intent.putExtra("id", id);
                                context.startActivity(intent);
                            } else if(item.getTitle().equals("삭제")){

                                database = FirebaseDatabase.getInstance();
                                databaseReference = database.getReference("post/" + id);
                                databaseReference.addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        Post post = dataSnapshot.getValue(Post.class);
                                        post.setVisible(false);
                                        databaseReference.setValue(post);
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                        Toast.makeText(context, "데이터베이스 오류\n" + databaseError.toString(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                                Toast.makeText(context.getApplicationContext(), "삭제", Toast.LENGTH_SHORT).show();
                            }

                            return false;
                        }
                    });
                    popup.show();

                }
            });
        }
/*
        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

            MenuItem Edit = contextMenu.add(Menu.NONE, 1001, 1, "편집");
            MenuItem Delete = contextMenu.add(Menu.NONE, 1002, 2, "삭제");
            Edit.setOnMenuItemClickListener(onEditMenu);
            Delete.setOnMenuItemClickListener(onEditMenu);
        }

        private final MenuItem.OnMenuItemClickListener onEditMenu = new MenuItem.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case 1001:
                        Toast.makeText(context, "편집", Toast.LENGTH_SHORT).show();
                        break;

                    case 1002:

                        Toast.makeText(context, "삭제", Toast.LENGTH_SHORT).show();
                        break;

                }
                return true;
            }
        };*/
    }


}
