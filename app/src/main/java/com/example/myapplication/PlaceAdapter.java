package com.example.myapplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;



import java.util.ArrayList;


public class PlaceAdapter extends RecyclerView.Adapter<PlaceAdapter.ViewHolder> {

    private ArrayList<String> instaModalArrayList;
    private ArrayList<String> descArrayList;
    private Context context;

    public PlaceAdapter(ArrayList<String> namePlaceArrayList,ArrayList<String> descPlaceArrayList, Context context) {
        this.instaModalArrayList = namePlaceArrayList;
        this.descArrayList = descPlaceArrayList;
        this.context = context;

    }

    @NonNull
    @Override
    public PlaceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.placeview, parent, false);
        return new PlaceAdapter.ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull PlaceAdapter.ViewHolder holder, int position) {
        String modal = instaModalArrayList.get(position);
        String desc = descArrayList.get(position);
        holder.placeName.setText(modal);
        holder.descPlace.setText(desc);
        System.out.println(modal+" bejhzkebrfkbarkfbrtt");

    }

    @Override
    public int getItemCount() {
        return instaModalArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        //CircleImageView authorIV;
        private TextView placeName;
        private TextView descPlace;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            //authorIV = itemView.findViewById(R.id.idCVAuthor);
            placeName = itemView.findViewById(R.id.placeNametext);
            descPlace = itemView.findViewById(R.id.placeNamedescription);

        }
    }
}
