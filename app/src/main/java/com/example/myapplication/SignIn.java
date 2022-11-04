package com.example.myapplication;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;


public class SignIn extends AppCompatActivity {
    TextView registerPage;
    FirebaseAuth rootnode;
    Button btnLog;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        registerPage= findViewById(R.id.registerText);
        btnLog= findViewById(R.id.btnSignIn);
        rootnode = FirebaseAuth.getInstance();
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TextInputLayout email= findViewById(R.id.emaillayout) ;
                TextInputLayout password = findViewById(R.id.passwordlayout);
               String emailvalue = email.getEditText().getText().toString();
               String passwordvalue = password.getEditText().getText().toString();

                // validations for input email and password
                if (TextUtils.isEmpty(emailvalue)) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter email!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                if (TextUtils.isEmpty(passwordvalue)) {
                    Toast.makeText(getApplicationContext(),
                                    "Please enter password!!",
                                    Toast.LENGTH_LONG)
                            .show();
                    return;
                }

                // signin existing user
                rootnode.signInWithEmailAndPassword(emailvalue, passwordvalue)
                        .addOnCompleteListener(
                                new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(
                                            @NonNull Task<AuthResult> task)
                                    {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(getApplicationContext(),
                                                            "Login successful!!",
                                                            Toast.LENGTH_LONG)
                                                    .show();

                                            // hide the progress bar
                                            //progressBar.setVisibility(View.GONE);

                                            // if sign-in is successful
                                            // intent to home activity
                                            Intent intent
                                                    = new Intent(SignIn.this,
                                                    HomePage.class);
                                            startActivity(intent);
                                            finish();
                                        }

                                        else {

                                            // sign-in failed
                                            Toast.makeText(getApplicationContext(),
                                                            "Login failed!!",
                                                            Toast.LENGTH_LONG)
                                                    .show();

                                            // hide the progress bar
                                            //progressbar.setVisibility(View.GONE);
                                        }
                                    }
                                });
            }
            });
        //rootnode = FirebaseDatabase.getInstance();
        registerPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent switchA= new Intent (SignIn.this, MainActivity.class);
                startActivity(switchA);finish();
            }
        });
    }

}
