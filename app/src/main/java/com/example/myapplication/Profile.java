package com.example.myapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class Profile extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout description,place;
    Button btndisconnect;
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
        setContentView(R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        btndisconnect=findViewById(R.id.btnSignOut);
        btndisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
                FirebaseAuth.getInstance().signOut();
                FirebaseUser mFireUser= mAuth.getCurrentUser();
                System.out.println(mFireUser);
                if(mFireUser==null){
                    Intent intent
                            = new Intent(Profile.this,
                            SignIn.class);
                    startActivity(intent);
                    finish();
                }
            }
        });
    }
}
