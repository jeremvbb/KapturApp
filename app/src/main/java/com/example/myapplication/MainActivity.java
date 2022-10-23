package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    TextInputLayout name,lastname,email,password;
    Button registerBtn;
    FirebaseDatabase rootnode;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        name = findViewById(R.id.firstnamelayout);
        lastname= findViewById(R.id.lastnamelayout);
        email = findViewById(R.id.emaillayout);
        password = findViewById(R.id.passwordlayout);
        registerBtn =findViewById(R.id.btnRegister);
        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Toast toast=Toast.makeText(getApplicationContext(),"Hello Javatpoint",Toast.LENGTH_SHORT);
                toast.show();*/
                String uName= name.getEditText().getText().toString();
                String uLastName= lastname.getEditText().getText().toString();
                String uEmail= email.getEditText().getText().toString();
                String uPassword= password.getEditText().getText().toString();

                rootnode = FirebaseDatabase.getInstance();
                reference= rootnode.getReference("Users");
                User helperClass= new User(uName, uLastName, uEmail, uPassword);
                reference.orderByChild("name").equalTo(uName)
                        .addListenerForSingleValueEvent(new ValueEventListener() {

                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {


                                if(dataSnapshot.exists()){
                                    Toast.makeText(getApplicationContext(),"account already exists",Toast.LENGTH_SHORT).show();

                                } else {
                                    reference.child(uName).setValue(helperClass);
                                    Toast.makeText(getApplicationContext(),"account registered",Toast.LENGTH_SHORT).show();

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

    }
}