package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

public class emergenciesActivity extends AppCompatActivity {

    WebView emergenciesView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergencies);

        emergenciesView = findViewById(R.id.webView1);
        emergenciesView.setWebViewClient(new WebViewClient());
        emergenciesView.loadUrl("https://www.nhs.uk/nhs-services/urgent-and-emergency-care-services/");
}

    @Override
    public void onBackPressed() {
        if (emergenciesView.canGoBack()) {
            emergenciesView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}