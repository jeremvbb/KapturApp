package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;



public class PostAdapter extends RecyclerView.Adapter<PostAdapter.ViewHolder> {

    private ArrayList<PostModal> instaModalArrayList;
    private Context context;

    public PostAdapter(ArrayList<PostModal> instaModalArrayList, Context context) {
        this.instaModalArrayList = instaModalArrayList;
        this.context = context;
        System.out.println("eeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeeee");
    }

    @NonNull
    @Override
    public PostAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.postview, parent, false);
        return new PostAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull PostAdapter.ViewHolder holder, int position) {
        PostModal modal = instaModalArrayList.get(position);
        holder.authorTV.setText(modal.getUsername());
        if (modal.getMedia_type().equals("IMAGE")) {
            Picasso.get().load(modal.getMedia_url()).into(holder.postIV);
        }
        Picasso.get().load(modal.getMedia_url()).into(holder.postIV);
        holder.desctv.setText(modal.getCaption());
        holder.likeTV.setText("" + modal.getLikesCount() + " likes");
        //Picasso.get().load(modal.getAuthor_url()).into(holder.authorIV);
    }

    @Override
    public int getItemCount() {
        return instaModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //CircleImageView authorIV;
        private TextView authorTV;
        private ImageView postIV;
        private TextView likeTV, desctv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //authorIV = itemView.findViewById(R.id.idCVAuthor);
            authorTV = itemView.findViewById(R.id.idTVAuthorName);
            postIV = itemView.findViewById(R.id.idIVPost);
            likeTV = itemView.findViewById(R.id.idTVLikes);
            desctv = itemView.findViewById(R.id.idTVPostDesc);
        }
    }
}
