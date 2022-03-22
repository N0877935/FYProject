package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;


public class checkerActivity extends AppCompatActivity {

    Button submit;
    RadioButton radioBtn1, radioBtn2, radioBtn3, radioBtn4, radioBtn5, radioBtn6, radioBtn7,
            radioBtn8, radioBtn9, radioBtn10, radioBtn11, radioBtn12, radioBtn13, radioBtn14, radioBtn15;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker);

        radioBtn1 = findViewById(R.id.radioBtn1);
        radioBtn2 = findViewById(R.id.radioBtn2);
        radioBtn3 = findViewById(R.id.radioBtn3);
        radioBtn4 = findViewById(R.id.radioBtn4);
        radioBtn5 = findViewById(R.id.radioBtn5);
        radioBtn6 = findViewById(R.id.radioBtn6);
        radioBtn7 = findViewById(R.id.radioBtn7);
        radioBtn8 = findViewById(R.id.radioBtn8);
        radioBtn9 = findViewById(R.id.radioBtn9);
        radioBtn10 = findViewById(R.id.radioBtn10);
        radioBtn11 = findViewById(R.id.radioBtn11);
        radioBtn12 = findViewById(R.id.radioBtn12);
        radioBtn13 = findViewById(R.id.radioBtn13);
        radioBtn14 = findViewById(R.id.radioBtn14);
        radioBtn15 = findViewById(R.id.radioBtn1);

        submit = findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popUpWindow();
            }
        });
    }

    public void popUpWindow() {
        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true;
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        popupWindow.showAtLocation(popupView, Gravity.CENTER, 0, 0);

        popupView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                popupWindow.dismiss();
                return true;
            }
        });
    }
}
