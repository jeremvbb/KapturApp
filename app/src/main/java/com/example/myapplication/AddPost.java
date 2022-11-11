package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class AddPost extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout description,place;
    Button btnLoadImage;
    Button btnPublish;
    ImageView postImage;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseDatabase rootnode;
    FirebaseStorage storage;
    String username ;

    Uri imageUri;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_post);
        btnLoadImage= findViewById(R.id.btnLoader);
        postImage=findViewById(R.id.imagePost);
        place=findViewById(R.id.placelayout);
        description=findViewById(R.id.descriptionlayout);
        rootnode = FirebaseDatabase.getInstance();
        reference= rootnode.getReference();
        storage= FirebaseStorage.getInstance();
       storageReference = storage.getReference();
        username= "new TextView";
        btnLoadImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);
            }
        });

        btnPublish=findViewById(R.id.btnPublish);
        btnPublish.setVisibility(View.INVISIBLE);
        btnPublish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            uploadPicture();
            }
        });
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        reference = rootnode.getReference("Users");
        reference.orderByChild("email").equalTo(currentUser.getEmail()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists()){
                    HashMap<String, Object> dataMap = (HashMap<String, Object>) dataSnapshot.getValue();
                    System.out.println(dataMap);

                    for (String key : dataMap.keySet()) {

                        Object data = dataMap.get(key);
                        System.out.println(key+" "+data);
                        HashMap<String, Object> userData = (HashMap<String, Object>) data;
                        System.out.println( userData.get("lastname").toString()+"\n"+userData.get("email")+"\n"+userData.get("name"));
                        username=(String)(userData.get("name").toString());}


                }}

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        imageUri=data.getData();
       postImage.setImageURI(imageUri);
        btnPublish.setVisibility(View.VISIBLE);
        //uploadPicture();

    }
    private void uploadPicture() {
        final String randomkey= UUID.randomUUID().toString();
        System.out.println(username+ " hgervbzjhvezjh");
        StorageReference imageRef=storageReference.child("image/"+randomkey);

        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
                rootnode = FirebaseDatabase.getInstance();
                reference = rootnode.getReference("Post");
                String uPlace = place.getEditText().getText().toString();
                String uUrl = "image/"+randomkey;
                String uDesc = description.getEditText().getText().toString();
                String uName = username;

                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault());
                String currentDateandTime = sdf.format(new Date());
                Post helperclass= new Post(uName,uUrl,uPlace,uDesc,currentDateandTime);
                reference.child("post "+username+ " "+currentDateandTime).setValue(helperclass);
                Intent switchA= new Intent (AddPost.this, HomePage.class);
                startActivity(switchA);
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image Not Uploaded",Toast.LENGTH_SHORT).show();

            }
        });
    }
    @Override
    public void onBackPressed() {
        System.out.println("back pressed");
        Intent switchA= new Intent (AddPost.this, HomePage.class);
        startActivity(switchA);
        finish();
        super.onBackPressed();
    }
}
