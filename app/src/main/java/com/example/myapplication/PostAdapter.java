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

        Picasso.get().load(modal.getMedia_url()).into(holder.postIV);
        holder.desctv.setText(modal.getCaption());
        holder.placeTv.setText(modal.getPlace());
        holder.placeTv.setTextColor(context.getResources().getColor(R.color.grey));

    }

    @Override
    public int getItemCount() {
        return instaModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //CircleImageView authorIV;
        private TextView authorTV;
        private ImageView postIV;
        private TextView  desctv, placeTv;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            authorTV = itemView.findViewById(R.id.idTVAuthorName);
            postIV = itemView.findViewById(R.id.idIVPost);

            desctv = itemView.findViewById(R.id.idTVPostDesc);
            placeTv= itemView.findViewById(R.id.idTVPlace);
        }
    }
}
