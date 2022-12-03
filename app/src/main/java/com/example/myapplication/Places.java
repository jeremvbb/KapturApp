package com.example.myapplication;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import android.os.AsyncTask;

public class Places extends AppCompatActivity {
    MapView mapView;
    GoogleMap map;
    FrameLayout fl;
    ArrayList<String> allNames = new ArrayList<String>();
    ArrayList<String> allDesc = new ArrayList<String>();

    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTheme(android.R.style.Theme_Black_NoTitleBar);
        setContentView(R.layout.activity_places);
        Toolbar ab = (Toolbar) findViewById(R.id.toolbarplaces);
        ab.setTitle("KapturApp");
        setSupportActionBar(ab);

        TextView title = findViewById(R.id.toolbarTitle);
        mHandler = new Handler(Looper.getMainLooper());
        OkHttpClient client = new OkHttpClient();
        LocationManager locationManager = (LocationManager)
                this.getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = locationManager.getLastKnownLocation(locationManager
                .getBestProvider(criteria, false));
        double latitude = location.getLatitude();
        double longitude = location.getLongitude();

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
                .url("https://api.opentripmap.com/0.1/en/places/radius?radius=5000&lon="+longitude+"&lat="+latitude+"&apikey=5ae2e3f221c38a28845f05b6a9026a2313e3d20199335f536fdd9616")
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
                AsyncTaskRunner runner = new AsyncTaskRunner();
                runner.execute();
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


                       JSONArray matchs = obj.getJSONArray("features");
                        for (int i = 0; i < 10; i++) {
                            JSONObject game = matchs.getJSONObject(i);
                            JSONObject teams = (JSONObject) game.get("properties");
                            //JSONObject status = (JSONObject) game.get("status");
                            String name=teams.getString("name");
                            String desc=teams.getString("kinds");
                            if (!name.equals("")){
                                allDesc.add(desc);
                                allNames.add(name);
                                System.out.println(name);
                                System.out.println(i);
                            }


                        }


                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }}});


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
    private class AsyncTaskRunner extends AsyncTask<String, String, String> {

        private String resp;


        @Override
        protected String doInBackground(String... strings) {
            return null;
        }
        @Override
        protected void onPostExecute(String result) {
            // execution of result of Long time consuming operation
            ActivityCompat.requestPermissions(Places.this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION},1);
            PlaceAdapter adapter = new PlaceAdapter( allNames,allDesc, Places.this);
            RecyclerView instRV = findViewById(R.id.idRVPlaces);
            System.out.println("ceci est un testtttttttttttttt");
            // below line is for setting linear layout manager to our recycler view.
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(Places.this, RecyclerView.VERTICAL, false);

            // below line is to set layout manager to our recycler view.
            instRV.setLayoutManager(linearLayoutManager);

            // below line is to set adapter
            // to our recycler view.
            instRV.setAdapter(adapter);
            Fragment fragmentMap= new MapFragment();
            fl= findViewById(R.id.frame_layout);
            getSupportFragmentManager().beginTransaction().replace(R.id.frame_layout,fragmentMap).commit();

        }
    }
}
