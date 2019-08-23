package com.toxic.salonapp.LodMaps;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.toxic.salonapp.ListSalonActivity;
import com.toxic.salonapp.MapsActivity;
import com.toxic.salonapp.R;
import com.toxic.salonapp.UserActivity;

import java.util.Timer;
import java.util.TimerTask;

public class MapsLoad extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_load);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {

                Intent newPostIntent = new Intent(MapsLoad.this, MapsActivity.class);
                startActivity(newPostIntent);
                finish();

            }
        }, 3000);
    }
}
