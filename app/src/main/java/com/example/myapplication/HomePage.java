package com.example.myapplication;

import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.HashMap;

public class HomePage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    FirebaseDatabase rootnode;
    FirebaseStorage storage;
    DatabaseReference reference;
    TextView test;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);
        mAuth = FirebaseAuth.getInstance();
        test=findViewById(R.id.textViewtest);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser == null) {
            // No user is signed in
        } else {
            // User logged in
            System.out.println( currentUser.getEmail());

            rootnode = FirebaseDatabase.getInstance();
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
                            test.setText( userData.get("lastname").toString()+"\n"+userData.get("email")+"\n"+userData.get("name"));
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

                        }*/
                        }
                        //System.out.println(dataMap.get("lastname").toString());
                        //test.setText( dataMap.get("lastname").toString());
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
