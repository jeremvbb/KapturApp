package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase rootnode;
    FirebaseStorage storage;
    DatabaseReference reference;
    private Button disconnect;
    private ArrayList<PostModal> instaModalArrayList;
    private Handler mHandler;
    private ProgressBar progressBar;
    String media_url="" ;
    TextView test;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_homepage);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("KapturApp");
        setSupportActionBar(toolbar);
        // Create a storage reference from our app
        storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        disconnect=findViewById(R.id.btnDisconnected);
        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchA= new Intent (HomePage.this, AddPost.class);
                startActivity(switchA);
                finish();

            }
        });
// Create a reference with an initial file path and name

        mAuth = FirebaseAuth.getInstance();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        mHandler = new Handler(Looper.getMainLooper());
        progressBar = findViewById(R.id.idLoadingPB);
        if (currentUser == null) {
            // No user is signed in
        } else {
            // User logged in
            System.out.println( currentUser.getEmail());

            rootnode = FirebaseDatabase.getInstance();
            reference = rootnode.getReference("Post");
            reference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.exists()){
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        System.out.println(dataMap);
                        String id = "id";
                        String place = "";
                        String permalink = "permalink";
                        instaModalArrayList = new ArrayList<>();
                        String username = "username";
                        String caption;
                        String date;
                        String timestamp = "timestamp";
                        for (String key : dataMap.keySet()) {

                            Object data = dataMap.get(key);
                            System.out.println(key + " " + data);
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            System.out.println(userData.get("url"));
                            username = String.valueOf(userData.get("name"));
                            caption = String.valueOf(userData.get("description"));
                            place = String.valueOf(userData.get("place"));
                            date = String.valueOf(userData.get("date"));
                            StorageReference imgRef= storageRef.child(String.valueOf(userData.get("url")));
                            Task<Uri> uri=imgRef.getDownloadUrl();
                            media_url=uri.toString();
                            System.out.println(media_url);
                            String finalCaption = caption;
                            String finalUsername= username;
                            String finalPlace= place;
                            String finalDate= date;
                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Download URL

                                    media_url = uri.toString();
                                    System.out.println(media_url);
                                    PostModal instaModal = new PostModal(id, finalPlace, permalink, media_url, finalUsername, finalCaption, finalDate);

                                    // below line is use to add modal
                                    // class to our array list.

                                    instaModalArrayList.add(instaModal);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Errors
                                }

                            });



                        }



                    }
                    recyclerView();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }});

        }
    }

    public void recyclerView(){
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                System.out.println("teettetetettete "+media_url);


                Collections.sort(instaModalArrayList, new Comparator<PostModal>() {
                    public int compare(PostModal v1, PostModal v2) {
                        try {
                            System.out.println(v1.getTimestamp()+" "+v2.getTimestamp()+"     "+v1.getTimestamp().compareTo(v2.getTimestamp()));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        try {
                            return v1.getTimestamp().compareTo(v2.getTimestamp());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        return 1;
                    }
                });
                Collections.reverse(instaModalArrayList);
                PostAdapter adapter = new PostAdapter(instaModalArrayList, HomePage.this);
                RecyclerView instRV = findViewById(R.id.idRVInstaFeeds);

                // below line is for setting linear layout manager to our recycler view.
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomePage.this, RecyclerView.VERTICAL, false);

                // below line is to set layout manager to our recycler view.
                instRV.setLayoutManager(linearLayoutManager);

                // below line is to set adapter
                // to our recycler view.
                instRV.setAdapter(adapter);
                progressBar.setVisibility(View.GONE);}
        },2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.home_menu,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.profileitem:
                Intent i = new Intent(this,Profile.class);
                this.startActivity(i);
                return true;

            case R.id.placesitem:
                 i = new Intent(this,Places.class);
                this.startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
