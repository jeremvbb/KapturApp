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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
//import com.google.firebase.auth.AuthResult;
//import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.AuthResult;
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

import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    TextInputLayout name,lastname,email,password;
    Button registerBtn;
    Button test;
    FirebaseDatabase rootnode;
    FirebaseStorage storage;

    FirebaseAuth mAuth;
    StorageReference storageReference;
    DatabaseReference reference;
    ImageView rImage;
    Uri imageUri;
    TextView connexionLink;
//efzfzeef
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.firstnamelayout);

        lastname = findViewById(R.id.lastnamelayout);
        email = findViewById(R.id.emaillayout);
        storage= FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        mAuth=FirebaseAuth.getInstance();
        password = findViewById(R.id.passwordlayout);
        registerBtn = findViewById(R.id.btnRegister);
        isConnected();
        connexionLink= findViewById(R.id.signIn);
        connexionLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent switchA= new Intent (MainActivity.this, SignIn.class);
                startActivity(switchA);
                finish();
            }
        });
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
                                    mAuth.createUserWithEmailAndPassword(uEmail, uPassword)
                                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                                @Override
                                                public void onComplete(@NonNull Task<AuthResult> task) {
                                                    if (task.isSuccessful()) {
                                                        Toast.makeText(getApplicationContext(), "Registration successful!", Toast.LENGTH_LONG).show();

                                                    }
                                                    else {
                                                        Toast.makeText(getApplicationContext(), "Registration failed! Please try again later", Toast.LENGTH_LONG).show();

                                                    }
                                                }
                                            });
                                    Toast.makeText(getApplicationContext(), "account registered", Toast.LENGTH_SHORT).show();

                                }


                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


            }
        });
        rootnode = FirebaseDatabase.getInstance();
        reference = rootnode.getReference("Image");





    }
    public void isConnected(){
        FirebaseUser mFireUser= mAuth.getCurrentUser();
        if(mFireUser!=null){
            Intent intent
                    = new Intent(MainActivity.this,
                    HomePage.class);
            startActivity(intent);
            finish();
        }
    }
}