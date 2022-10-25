package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    TextInputLayout name,lastname,email,password;
    Button registerBtn;
    Button test;
    FirebaseDatabase rootnode;
    FirebaseStorage storage;
    StorageReference storageReference;
    DatabaseReference reference;
    ImageView rImage;
    Uri imageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.firstnamelayout);
        rImage= findViewById(R.id.rImage);
        lastname = findViewById(R.id.lastnamelayout);
        email = findViewById(R.id.emaillayout);
        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        password = findViewById(R.id.passwordlayout);
        registerBtn = findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Toast toast=Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT);
                toast.show();*/
                String uName = name.getEditText().getText().toString();
                String uLastName = lastname.getEditText().getText().toString();
                String uEmail = email.getEditText().getText().toString();
                String uPassword = password.getEditText().getText().toString();

                rootnode = FirebaseDatabase.getInstance();
                reference = rootnode.getReference("Users");
                User helperClass = new User(uName, uLastName, uEmail, uPassword);
                reference.orderByChild("name").equalTo(uName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if (dataSnapshot.exists()) {
                                    Toast.makeText(getApplicationContext(), "account already exists", Toast.LENGTH_SHORT).show();

                                } else {
                                    reference.child(uName).setValue(helperClass);
                                    Toast.makeText(getApplicationContext(), "account registered", Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
               /* if(reference.child(uName)!=null)
                {
                    reference.child(uName).setValue(helperClass);
                    toast=Toast.makeText(getApplicationContext(),"account registered",Toast.LENGTH_SHORT);
                }
                else{
                    toast=Toast.makeText(getApplicationContext(),"account already exists",Toast.LENGTH_SHORT);
                }
                toast.show();*/

            }
        });
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("Image");
        test= findViewById(R.id.buttontest);
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(intent, 3);

                /*
                });

              /*  reference.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(
                                    @NonNull DataSnapshot dataSnapshot) {
                                // getting a DataSnapshot for the
                                // location at the specified relative
                                // path and getting in the link variable
                                String link = dataSnapshot.getValue(
                                        String.class);

                                // loading that data into rImage
                                // variable which is ImageView
                                Picasso.get().load(link).into(rImage);
                                Toast
                                        .makeText(MainActivity.this,
                                                " Loading Image",
                                                Toast.LENGTH_SHORT).show();
                            }

                            // this will called when any problem
                            // occurs in getting data
                            @Override
                            public void onCancelled(
                                    @NonNull DatabaseError databaseError) {
                                // we are showing that error message in
                                // toast
                                Toast
                                        .makeText(MainActivity.this,
                                                "Error Loading Image",
                                                Toast.LENGTH_SHORT).show();

                            }
                        });*/
            }
        });

        // Adding listener for a single change
        // in the data at this location.
        // this listener will triggered once
        // with the value of the data at the location

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        imageUri=data.getData();
        rImage.setImageURI(imageUri);
        uploadPicture();

    }

    private void uploadPicture() {
        final String randomkey= UUID.randomUUID().toString();
        StorageReference imageRef=storageReference.child("image/"+randomkey);
        imageRef.putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(getApplicationContext(),"Image Uploaded",Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Image Not Uploaded",Toast.LENGTH_SHORT).show();

            }
        });
    }
}