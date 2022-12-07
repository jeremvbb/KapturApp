package com.example.myapplication;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.FileProvider;

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
import com.google.firebase.database.core.Constants;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.UUID;

public class AddPost extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputLayout description,place;
    Button btnLoadImage;
    Button btnLoadCamera;
    Button btnPublish;
    Bitmap imageLoader;

    String imageFilePath;
    ImageView postImage;
    DatabaseReference reference;
    StorageReference storageReference;
    FirebaseDatabase rootnode;
    FirebaseStorage storage;
    String username ;

    private int NOTIF_ID=001;
    private final int IMAGE_PIC_REQUEST=3;
    private final int CAMERA_PIC_REQUEST=4;

    Uri imageUri;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            requestPermissions(new String[] {WRITE_EXTERNAL_STORAGE,READ_EXTERNAL_STORAGE},1);
        }
        setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_post);
        btnLoadImage= findViewById(R.id.btnLoader);
        btnLoadCamera= findViewById(R.id.btnLoaderCamera);
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
                startActivityForResult(intent, IMAGE_PIC_REQUEST);
            }
        });

        btnLoadCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


               dispatchTakePictureIntent();


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
        if (requestCode == CAMERA_PIC_REQUEST) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            // Set the image in imageview for display
            postImage.setImageBitmap(photo);
            String filename = "pippo.png";
            File sd = Environment.getExternalStorageDirectory();
            File dest = new File(sd, filename);

            Bitmap bitmap = (Bitmap)data.getExtras().get("data");
            String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(root);
            myDir.mkdirs();
            String fname = "Image-" + "test"+ ".jpg";
            File file = new File(myDir, fname);
            if (file.exists()) file.delete();
            Log.i("LOAD", root + fname);
            try {
                FileOutputStream out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
                out.flush();
                out.close();

            } catch (Exception e) {
                e.printStackTrace();
            }
            imageUri=Uri.fromFile(file);
            postImage.setImageURI(imageUri);
        }else{
        imageUri=data.getData();
       postImage.setImageURI(imageUri);
        }
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
                    sendNotification();
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
    public void sendNotification()
    {

        NotificationManager mNotificationManager;

        NotificationCompat.Builder mBuilder =
                new NotificationCompat.Builder(this, "notify_001");
        mBuilder.setSmallIcon(R.drawable.ic_notif);
        mBuilder.setContentTitle("Kaptur App ");
        mBuilder.setContentText("content published");
        mBuilder.setPriority(Notification.PRIORITY_MAX);


        mNotificationManager =
                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//canal de notification
// === Removed some obsoletes
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)
        {
            String channelId = "Your_channel_id";
            NotificationChannel channel = new NotificationChannel(
                    channelId,
                    "Channel human readable title",
                    NotificationManager.IMPORTANCE_HIGH);
            mNotificationManager.createNotificationChannel(channel);
            mBuilder.setChannelId(channelId);
        }
        mNotificationManager.notify(NOTIF_ID, mBuilder.build());
    }
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        imageFilePath = image.getAbsolutePath();
        return image;
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(takePictureIntent, CAMERA_PIC_REQUEST);
    }
}
