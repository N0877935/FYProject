package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class mentalHealthActivity extends AppCompatActivity {

    WebView mentalHealthView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mental_health);


    mentalHealthView = findViewById(R.id.webView1);
        mentalHealthView.setWebViewClient(new WebViewClient());
        mentalHealthView.loadUrl("https://www.nhs.uk/mental-health/");
}

    @Override
    public void onBackPressed() {
        if (mentalHealthView.canGoBack()) {
            mentalHealthView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}