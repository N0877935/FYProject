package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class maleActivity extends AppCompatActivity {

    WebView maleView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_male);

        maleView = findViewById(R.id.webView1);
        maleView.setWebViewClient(new WebViewClient());
        maleView.loadUrl("https://www.nhs.uk/common-health-questions/mens-health/");
    }

    @Override
    public void onBackPressed() {
        if (maleView.canGoBack()) {
            maleView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}