package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

public class moodLogActivity extends AppCompatActivity {

    TextView t1;
    String mood;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_log);

        t1 = findViewById(R.id.loggedMood1);

        SharedPreferences sp = getApplicationContext().getSharedPreferences("pref",
                Context.MODE_PRIVATE);
        mood = sp.getString("great", "");

        t1.setText(mood);
    }
}