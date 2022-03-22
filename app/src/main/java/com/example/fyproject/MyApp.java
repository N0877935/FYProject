package com.example.fyproject;

import android.app.Application;
import android.location.Location;

import java.util.ArrayList;
import java.util.List;

public class MyApp extends Application{

    private static MyApp single;

    private List<Location> moodLocations;

    public List<Location> getMoodLocations() {
        return moodLocations;
    }

    public void setMoodLocations(List<Location> moodLocations) {
        this.moodLocations = moodLocations;
    }

    public MyApp getInstance(){
        return single;
    }

    public void onCreate(){
        super.onCreate();
        single = this;

        moodLocations = new ArrayList<>();
    }
}
