package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class femaleActivity extends AppCompatActivity {

    WebView femaleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_female);
        femaleView = findViewById(R.id.webView1);
        femaleView.setWebViewClient(new WebViewClient());
        femaleView.loadUrl("https://www.nhs.uk/common-health-questions/womens-health/");
    }

    @Override
    public void onBackPressed() {
        if (femaleView.canGoBack()) {
            femaleView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}