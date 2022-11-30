package com.example.myapplication;

import android.Manifest;
import android.content.Intent;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Places extends AppCompatActivity {
    MapView mapView;
    GoogleMap map;
    FrameLayout fl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_places);
        Toolbar ab = (Toolbar) findViewById(R.id.toolbarplaces);
        ab.setTitle("KapturApp");
        setSupportActionBar(ab);
        TextView title= findViewById(R.id.toolbarTitle);
        OkHttpClient client = new OkHttpClient();


            View child = ab.getChildAt(0);

            if(child instanceof TextView ) {
                child.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent
                                = new Intent(Places.this,
                                HomePage.class);
                        startActivity(intent);
                        finish();
                    }
                });
            }
        Request request = new Request.Builder()
                .url("https://api.opentripmap.com/0.1/en/places/radius?radius=5000&lon=4.34878&lat=50.85045&apikey=5ae2e3f221c38a28845f05b6a9026a2313e3d20199335f536fdd9616")
                .get()
                .build();
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                String mMessage = e.getMessage().toString();
                Log.w("failure Response", mMessage);
                //call.cancel();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                String mMessage  =response.body().string();

                Log.e("test", mMessage);
                JSONObject obj = null;
                try {
                    obj = new JSONObject(mMessage);
                    String results= obj.getString("features");

                    if (results.equals("0")){
                       // isgame=false;
                    }

                    else {
                       System.out.println("zzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzzz");


                      /*  JSONArray matchs = obj.getJSONArray("response");
                        for (int i = 0; i < matchs.length(); i++) {
                            JSONObject game = matchs.getJSONObject(i);
                            JSONObject teams = (JSONObject) game.get("teams");
                            JSONObject status = (JSONObject) game.get("status");
                            String statustostring=status.getString("long");
                            JSONObject visitors = (JSONObject) teams.get("visitors");
                            JSONObject home = (JSONObject) teams.get("home");
                            String visitorname = visitors.getString("name");
                            String visitorlogourl = visitors.getString("logo");
                            String homename = home.getString("name");
                            String homelogourl = home.getString("logo");

                            JSONObject scores = (JSONObject) game.get("scores");
                            JSONObject visitorsScores = (JSONObject) scores.get("visitors");
                            JSONObject homeScore = (JSONObject) scores.get("home");
                            String visitorscore = visitorsScores.getString("points");
                            String homescore = homeScore.getString("points");
                            String team1 = visitorname + "\t";
                            String team2 = " " + homename;
                            String gameScores = " " + visitorscore + " - " + " \t" + homescore;*/


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }}});
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);


        Fragment fragmentMap= new MapFragment();
        fl= findViewById(R.id.frame_layout);
        getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragmentMap).commit();
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
