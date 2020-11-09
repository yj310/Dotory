package com.example.dotory;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class PostCustomAdapter extends RecyclerView.Adapter<PostCustomAdapter.PostCustomViewHolder> {

    private ArrayList<Post> arrayList;
    private Context context;

    public PostCustomAdapter(ArrayList<Post> arrayList, Context context) {
        this.arrayList = arrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public PostCustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_post, parent, false);
        PostCustomViewHolder holder = new PostCustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PostCustomViewHolder holder, int position) {

        holder.tv_title.setText(arrayList.get(position).getTitle());
        holder.tv_date.setText(arrayList.get(position).getDate());
        holder.tv_content.setText(arrayList.get(position).getContent());

    }

    @Override
    public int getItemCount() {
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class PostCustomViewHolder extends RecyclerView.ViewHolder {

        TextView tv_title;
        TextView tv_date;
        TextView tv_content;

        public PostCustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.tv_title = itemView.findViewById(R.id.text_title);
            this.tv_date = itemView.findViewById(R.id.text_date);
            this.tv_content = itemView.findViewById(R.id.text_content);
        }
    }
}
