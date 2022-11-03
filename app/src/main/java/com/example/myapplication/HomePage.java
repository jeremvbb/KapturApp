package com.example.myapplication;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

import java.util.ArrayList;
import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase rootnode;
    FirebaseStorage storage;
    DatabaseReference reference;
    private ArrayList<PostModal> instaModalArrayList;
    private Handler mHandler;
    private ProgressBar progressBar;
    String media_url="" ;
    TextView test;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        // Create a storage reference from our app
        storage= FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

// Create a reference with an initial file path and name
        StorageReference pathReference = storageRef.child("image/");
        mAuth = FirebaseAuth.getInstance();
        //test=findViewById(R.id.textViewtest);
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
                        String media_type = "media_type";
                        String permalink = "permalink";
                        instaModalArrayList = new ArrayList<>();
                        String username = "username";
                        String caption;
                        String timestamp = "timestamp";
                        for (String key : dataMap.keySet()) {

                            Object data = dataMap.get(key);
                            System.out.println(key + " " + data);
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;
                            System.out.println(userData.get("url"));
                            caption = String.valueOf(userData.get("description"));
                            StorageReference imgRef= storageRef.child(String.valueOf(userData.get("url")));
                            Task<Uri> uri=imgRef.getDownloadUrl();
                            media_url=uri.toString();
                            System.out.println(media_url);
                            String finalCaption = caption;

                            imgRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    // Download URL

                                    media_url = uri.toString();
                                    System.out.println(media_url);
                                    PostModal instaModal = new PostModal(id, media_type, permalink, media_url, username, finalCaption, timestamp);

                                    // below line is use to add modal
                                    // class to our array list.
                                    instaModalArrayList.add(instaModal);
                                    instaModalArrayList.add(instaModal);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception exception) {
                                    // Errors
                                }

                            });
                            //media_url = imgRef.getDownloadUrl().toString();


                        }


                        //test.setText( userData.get("lastname").toString()+"\n"+userData.get("email")+"\n"+userData.get("name"));
                    }
                    recyclerView();
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }});
            /*reference.orderByChild("email").equalTo(currentUser.getEmail()).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    if (dataSnapshot.exists()){
                        HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                        System.out.println(dataMap);

                        for (String key : dataMap.keySet()) {

                            Object data = dataMap.get(key);
                            System.out.println(key+" "+data);
                            HashMap<String, Object> userData = (HashMap<String, Object>) data;}
                            //test.setText( userData.get("lastname").toString()+"\n"+userData.get("email")+"\n"+userData.get("name"));

                            /*try{
                                HashMap<String, Object> userData = (HashMap<String, Object>) data;

                                User mUser = new User((String) userData.get("name"), (int) (long) userData.get("age"));
                                addTextToView(mUser.getName() + " - " + Integer.toString(mUser.getAge()));

                            }catch (ClassCastException cce){

// If the object can’t be casted into HashMap, it means that it is of type String.

                                try{

                                    String mString = String.valueOf(dataMap.get(key));
                                    addTextToView(mString);

                                }catch (ClassCastException cce2){

                                }
                            }

                        } mHandler.post(new Runnable() {
                        @Override
                        public void run() { String id = "id";
                            String media_type ="media_type";
                            String permalink = "permalink";
                            String media_url = "https://img.itinari.com/pages/images/original/fd3a2c1b-bccc-4273-845e-026f17130454-istock-cover-sorincolac.jpg?ch=DPR&dpr=2.625&w=1600&s=43b902e2cebacd3eb8a9cc7ce45b3894" +
                                    "";
                            String username = "username";
                            String caption = "caption";
                            String timestamp = "timestamp";

                            // below line is to add a constant author image URL to our recycler view.
                            //String author_url = "https://instagram.fnag5-1.fna.fbcdn.net/v/t51.2885-19/s320x320/75595203_828043414317991_4596848371003555840_n.jpg?_nc_ht=instagram.fnag5-1.fna.fbcdn.net&_nc_ohc=WzA_n4sdoQIAX9B5HWJ&tp=1&oh=05546141f5e40a8f02525b497745a3f2&oe=6031653B";
                            //int likesCount = 100 + (i * 10);

                            // below line is use to add data to our modal class.
                            PostModal instaModal = new PostModal(id, media_type, permalink, media_url, username, caption, timestamp);
                            instaModalArrayList = new ArrayList<>();
                            // below line is use to add modal
                            // class to our array list.
                            instaModalArrayList.add(instaModal);
                            instaModalArrayList.add(instaModal);
                            System.out.println(instaModalArrayList.get(0));
                            // below line we are creating an adapter class and adding our array list in it.
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
                        });
                        //System.out.println(dataMap.get("lastname").toString());
                        //test.setText( dataMap.get("lastname").toString());
                    }
                }

               /* @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });*/
        }
    }

    public void recyclerView(){
        mHandler.postDelayed(new Runnable() {

            @Override
            public void run() {

                System.out.println("teettetetettete "+media_url);
                // below line is to add a constant author image URL to our recycler view.
                //String author_url = "https://instagram.fnag5-1.fna.fbcdn.net/v/t51.2885-19/s320x320/75595203_828043414317991_4596848371003555840_n.jpg?_nc_ht=instagram.fnag5-1.fna.fbcdn.net&_nc_ohc=WzA_n4sdoQIAX9B5HWJ&tp=1&oh=05546141f5e40a8f02525b497745a3f2&oe=6031653B";
                //int likesCount = 100 + (i * 10);

                // below line is use to add data to our modal class.

                // System.out.println(instaModalArrayList.get(0));
                // below line we are creating an adapter class and adding our array list in it.
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
        },1000);
    }
}
