package com.example.fyproject.ui.mood;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Geocoder;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fyproject.Coordinates;
import com.example.fyproject.MapsActivity;
import com.example.fyproject.R;
import com.example.fyproject.gpsActivity;
import com.example.fyproject.MyApp;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

public class MoodFragment extends Fragment{


    FirebaseDatabase rootNode;
    private DatabaseReference reference;
    private boolean firstTimeUsed;
    private String firstTimeUsedKey="FIRST_TIME";
    private String sharedPreferencesKey = "MY_PREF";
    private String buttonClickedKey = "BUTTON_CLICKED";
    SharedPreferences sp;
    private long savedDate=0;

    public static final int DEFAULT_UPDATE_INTERVAL = 30;
    public static final int FAST_UPDATE_INTERVAL = 5;
    public static final int PERMISSION_FINE_LOCATION = 99;
    TextView Latitude, Longitude,
            Accuracy, Sensor, Address, wayPointCount;

    SwitchCompat gps, locationUpdates;

    boolean updateOn = false;


    // Google API to access location services.
    FusedLocationProviderClient fusedLocationProviderClient;

    LocationRequest locationRequest;
    LocationCallback locationCallBack;
    Location currentLocation;

    List<Location> moodLocationsList;

    ImageButton btnGreat;
    TextView great, good, average, bad, awful, displayDate;
    Button btnWaypoint, btnShowWaypoint;
    String moodGreat, moodAverage, moodGood, moodBad, moodAwful;
    Button moodLocations;

    HashMap<String, String> values = new HashMap<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState ) {
        MoodViewModel moodViewModel =
                new ViewModelProvider(this).get(MoodViewModel.class);

        View view = inflater.inflate(R.layout.fragment_mood, container, false);

//        displayDate = (TextView) view.findViewById(R.id.dateText);
//        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());
//        displayDate.setText(currentDateTimeString);


        sp = getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        savedDate = sp.getLong(buttonClickedKey,0);
        firstTimeUsed = sp.getBoolean(firstTimeUsedKey,true);//default is true if no value is saved
        checkPrefs();

        Latitude = view.findViewById(R.id.latitude);
        Longitude = view.findViewById(R.id.longitude);
        Accuracy = view.findViewById(R.id.accuracy);
        Sensor = view.findViewById(R.id.sensor);
//        Updates = view.findViewById(R.id.updates);
        Address = view.findViewById(R.id.address);
        gps = view.findViewById(R.id.sw_gps);
        wayPointCount = view.findViewById(R.id.tv_counterOfCrumbs);
        locationUpdates = view.findViewById(R.id.sw_locationsupdates);


        locationRequest = LocationRequest.create();
        locationRequest.setInterval(1000 * DEFAULT_UPDATE_INTERVAL);
        locationRequest.setFastestInterval(1000 * FAST_UPDATE_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

        locationCallBack = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);

                Location location = locationResult.getLastLocation();
                updateUI(location);
            }
        };

        gps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gps.isChecked()) {
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    Sensor.setText("Using GPS Sensors");
                } else {
                    locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);
                    Sensor.setText("Using Towers + WiFi");
                }
            }
        });

        locationUpdates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationUpdates.isChecked()) {
                    updateLocation();
                } else {
                    finishUpdating();

                }
            }
        });

        updateGPS();

        great = (TextView) view.findViewById(R.id.moodGreat);
        good = (TextView) view.findViewById(R.id.moodGood);
        average = (TextView) view.findViewById(R.id.moodAverage);
        bad = (TextView) view.findViewById(R.id.moodBad);
        awful = (TextView) view.findViewById(R.id.moodAwful);

        btnGreat = (ImageButton) view.findViewById(R.id.btnGreat);
        btnGreat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView moodTextView =(TextView)view.findViewById(R.id.moodTextView);
                moodTextView.setText("Great");
                moodTextView.setTextColor(Color.parseColor("#00FF00"));

                //put into method
                rootNode = FirebaseDatabase.getInstance();
                reference =  rootNode.getReference("Location");
                String latitude = Latitude.getText().toString();
                String longitude = Longitude.getText().toString();

                Coordinates coordinates = new Coordinates(latitude, longitude);
                reference.child("great").push().setValue(coordinates);

                moodGreat = great.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("great", moodGreat);
                editor.apply();
                Toast.makeText(getContext(), "Mood Saved.", Toast.LENGTH_SHORT).show();
             //   saveClickedTime();
                checkPrefs();
                placePin();
            }
        });

        ImageButton BtnGood = (ImageButton) view.findViewById(R.id.btnGood);
        BtnGood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView moodTextView =(TextView)view.findViewById(R.id.moodTextView);
                moodTextView.setText("Good");
                moodTextView.setTextColor(Color.parseColor("#013220"));

                rootNode = FirebaseDatabase.getInstance();
                reference =  rootNode.getReference("Location");
                String latitude = Latitude.getText().toString();
                String longitude = Longitude.getText().toString();
                Coordinates coordinates = new Coordinates(latitude, longitude);
                reference.child("good").push().setValue(coordinates);

                moodGood = good.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("good", moodGood);
                editor.apply();
                Toast.makeText(getContext(), "Mood Saved.", Toast.LENGTH_SHORT).show();
                //saveClickedTime();
                checkPrefs();
                placePin();

            }
        });

        ImageButton BtnAverage = (ImageButton) view.findViewById(R.id.btnAverage);
        BtnAverage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                TextView moodTextView =(TextView)view.findViewById(R.id.moodTextView);
                moodTextView.setText("Average");
                moodTextView.setTextColor(Color.parseColor("#FFA500"));

                rootNode = FirebaseDatabase.getInstance();
                reference =  rootNode.getReference("Location");
                String latitude = Latitude.getText().toString();
                String longitude = Longitude.getText().toString();
                Coordinates coordinates = new Coordinates(latitude, longitude);
                reference.child("average").push().setValue(coordinates);

                moodAverage = average.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("average", moodAverage);
                editor.apply();
                Toast.makeText(getContext(), "Mood Saved.", Toast.LENGTH_SHORT).show();
                //saveClickedTime();
                checkPrefs();
                //saveClickedTime();
                checkPrefs();
                placePin();
            }
        });

        ImageButton BtnBad = (ImageButton) view.findViewById(R.id.btnBad);
        BtnBad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView moodTextView =(TextView)view.findViewById(R.id.moodTextView);
                moodTextView.setText("Bad");
                moodTextView.setTextColor(Color.parseColor("#8B0000"));

                rootNode = FirebaseDatabase.getInstance();
                reference =  rootNode.getReference("Location");
                String latitude = Latitude.getText().toString();
                String longitude = Longitude.getText().toString();
                Coordinates coordinates = new Coordinates(latitude, longitude);
                reference.child("bad").push().setValue(coordinates);

                moodBad = bad.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("bad", moodBad);
                editor.apply();
                Toast.makeText(getContext(), "Mood Saved.", Toast.LENGTH_SHORT).show();
                //saveClickedTime();
                checkPrefs();
                //saveClickedTime();
                checkPrefs();
                placePin();
            }
        });

        ImageButton BtnAwful = (ImageButton) view.findViewById(R.id.btnAwful);
        BtnAwful.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TextView moodTextView =(TextView)view.findViewById(R.id.moodTextView);
                moodTextView.setText("Awful");
                moodTextView.setTextColor(Color.parseColor("#FF0000"));

                rootNode = FirebaseDatabase.getInstance();
                reference =  rootNode.getReference("Location");
                String latitude = Latitude.getText().toString();
                String longitude = Longitude.getText().toString();
                Coordinates coordinates = new Coordinates(latitude, longitude);
                reference.child("awful").push().setValue(coordinates);

                moodAwful = awful.getText().toString();
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("awful", moodAwful);
                editor.apply();
                Toast.makeText(getContext(), "Mood Saved.", Toast.LENGTH_SHORT).show();
                //saveClickedTime();
                checkPrefs();
               // saveClickedTime();
                checkPrefs();
                placePin();
            }
        });

//        Button viewLogBtn = (Button) view.findViewById(R.id.viewLogBtn);
//        viewLogBtn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent in = new Intent(getActivity(), moodLogActivity.class);
//                startActivity(in);
//            }
//        });

        moodLocations = view.findViewById(R.id.btn_showMap);
        moodLocations.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               Intent in = new Intent(getActivity(), MapsActivity.class);
               startActivity(in);
            }
        });
        return view;
    }

    private void finishUpdating() {

       // Updates.setText("Tracking off.");
        Latitude.setText("Tracking off.");
        Longitude.setText("Tracking off");
        gps.setText("Tracking off");
        Address.setText("Tracking off");
        Accuracy.setText("Tracking off");

        Sensor.setText("Sensor off");

        fusedLocationProviderClient.removeLocationUpdates(locationCallBack);
    }

    @SuppressLint("MissingPermission")
    private void updateLocation() {
      //  Updates.setText("Location tracking on.");
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallBack, null);
        updateGPS();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case PERMISSION_FINE_LOCATION:
                if(grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    updateGPS();
                }
                else {
                    Toast.makeText(getContext(), "Permissions required.", Toast.LENGTH_SHORT).show();


                }
                break;
        }
    }

    // doesn't have to go in separate method
    @SuppressLint("MissingPermission")
    private void updateGPS(){
        // get permission for user
        // get current location from client
        //update the UI

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getContext());

        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) ==
                PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener((Activity) getContext(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    //permission granted, put location values to UI
                    updateUI(location);
                    currentLocation = location;

                }
            });
        } else {

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION},
                        PERMISSION_FINE_LOCATION);

            }

        }
    }

    private void updateUI(Location location) {
        // update textViews
        Latitude.setText(String.valueOf(location.getLatitude()));
        Longitude.setText(String.valueOf(location.getLongitude()));
        Accuracy.setText(String.valueOf(location.getAccuracy()));


        Geocoder geocoder = new Geocoder(getContext());

        try {
            List<android.location.Address> addresses = geocoder.getFromLocation(location.getLatitude(),
                    location.getLongitude(), 1);
            Address.setText(addresses.get(0).getAddressLine(0));
        } catch (Exception e) {
            Address.setText("Street name unavailable.");

        }

        MyApp myApp = (MyApp) getActivity().getApplicationContext();
        moodLocationsList = myApp.getMoodLocations();

        wayPointCount.setText(Integer.toString(moodLocationsList.size()));

    }

    private void checkPrefs(){


        if(firstTimeUsed==false){
            if(savedDate>0){

                //create two instances of Calendar and set minute,hour,second and millis
                //to 1, to be sure that the date differs only from day,month and year

                Calendar currentCal = Calendar.getInstance();
                currentCal.set(Calendar.MINUTE,1);
                currentCal.set(Calendar.HOUR,1);
                currentCal.set(Calendar.SECOND,1);
                currentCal.set(Calendar.MILLISECOND,1);

                Calendar savedCal = Calendar.getInstance();
                savedCal.setTimeInMillis(savedDate); //set the time in millis from saved in sharedPrefs
                savedCal.set(Calendar.MINUTE,1);
                savedCal.set(Calendar.HOUR,1);
                savedCal.set(Calendar.SECOND,1);
                savedCal.set(Calendar.MILLISECOND,1);

                if(currentCal.getTime().after(savedCal.getTime())){

                   // btnGreat.setVisibility(View.VISIBLE);
                }else if(currentCal.getTime().equals(savedCal.getTime())){

                     //btnGreat.setVisibility(View.GONE);
                }

            }
        }else{

            //just set the button visible if app is used the first time

           // btnGreat.setVisibility(View.VISIBLE);

        }

    }

    public void placePin(){
        MyApp myApp = (MyApp) getActivity().getApplicationContext();
        moodLocationsList = myApp.getMoodLocations();
        moodLocationsList.add(currentLocation);

    }
}