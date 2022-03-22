package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

//show saved locations list
public class gpsActivity extends AppCompatActivity {

    ListView savedLocations;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gps);

        savedLocations = findViewById(R.id.lv_wayPoints);

        MyApp myApp = (MyApp) getApplicationContext();
        List<Location> moodLocations = myApp.getMoodLocations();

        savedLocations.setAdapter(new ArrayAdapter<Location>(this, android.R.layout.simple_list_item_1,
                moodLocations));
    }
}