package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.fyproject.ui.info.InfoFragment;
import com.example.fyproject.utilities.Constants;


public class checkerActivity extends AppCompatActivity {

    Button submit;
    RadioGroup radioGroup;
    CheckBox cb1, cb2, cb3, cb4, cb5, cb6, cb7, cb8, cb9, cb10, cb11, cb12, cb13, cb14;
    TextView symptom1, symptom2, symptom3, symptom4, symptom5, symptom6, symptom7, symptom8, warningText;
    EditText editTextNumber;
    Button profChat, returnHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checker);

        radioGroup = findViewById(R.id.testRadio);
        warningText = findViewById(R.id.warning);
        editTextNumber = findViewById(R.id.editTextNumber);
        String editTextNumberStr = editTextNumber.getText().toString();

        submit = findViewById(R.id.submitBtn);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (radioGroup.getCheckedRadioButtonId() == -1) {
                    warningText.setText("Please enter who you are taking the test for and their age.");
                } else {
                    warningText.setVisibility(View.INVISIBLE);
                    popUpWindow(view);
                }
            }
        });

    }

    public void popUpWindow(View view) {

        cb1 = findViewById(R.id.radioBtn1);
        cb2 = findViewById(R.id.radioBtn2);
        cb3 = findViewById(R.id.radioBtn3);
        cb4 = findViewById(R.id.radioBtn4);
        cb5 = findViewById(R.id.radioBtn5);
        cb6 = findViewById(R.id.radioBtn6);
        cb7 = findViewById(R.id.radioBtn7);
        cb8 = findViewById(R.id.radioBtn8);
        cb9 = findViewById(R.id.radioBtn9);
        cb10 = findViewById(R.id.radioBtn10);
        cb11 = findViewById(R.id.radioBtn11);
        cb12 = findViewById(R.id.radioBtn12);
        cb13 = findViewById(R.id.radioBtn13);
        cb14 = findViewById(R.id.radioBtn14);


        LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        int width = LinearLayout.LayoutParams.MATCH_PARENT;
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

        symptom1 = popupView.findViewById(R.id.ADHD);
        symptom2 = popupView.findViewById(R.id.Anxiety);
        symptom3 = popupView.findViewById(R.id.Depression);
        symptom4 = popupView.findViewById(R.id.Hoarding);
        symptom5 = popupView.findViewById(R.id.PTSD);
        symptom6 = popupView.findViewById(R.id.Schizophrenia);
        symptom7 = popupView.findViewById(R.id.Sleep);
        symptom8 = popupView.findViewById(R.id.Trauma);

        if (radioGroup.getCheckedRadioButtonId() == -1) {
            warningText.setText("Please enter who you are taking the test for and their age.");
        } else {
            warningText.setVisibility(View.INVISIBLE);
        }

        // maintaining focus or impulsive, ADHD
        if (cb1.isChecked() || cb2.isChecked() || cb10.isChecked() || cb12.isChecked()) {
            symptom1.setVisibility(View.VISIBLE);
        } else {
            symptom1.setVisibility(View.INVISIBLE);
        }
        // clutter, Hoarding
        if (cb3.isChecked()) {
            symptom4.setVisibility(View.VISIBLE);
        } else {
            symptom4.setVisibility(View.INVISIBLE);
        }
        // irritable or angry, Depression, Bipolar
        if (cb4.isChecked()) {
            symptom3.setVisibility(View.VISIBLE);
        } else {
            symptom3.setVisibility(View.INVISIBLE);
        }
        // avoids social situations, anxiety
        if (cb5.isChecked() || cb9.isChecked() || cb12.isChecked() || cb13.isChecked()) {
            symptom2.setVisibility(View.VISIBLE);
        } else {
            symptom2.setVisibility(View.INVISIBLE);
        }
        // Experienced trauma, trauma
        if (cb6.isChecked() || cb13.isChecked()) {
            symptom8.setVisibility(View.VISIBLE);
        } else {
            symptom8.setVisibility(View.INVISIBLE);
        }
        //strange mood, schizophrenia
        if (cb7.isChecked() || cb8.isChecked()) {
            symptom6.setVisibility(View.VISIBLE);
            symptom5.setVisibility(View.VISIBLE);
        } else {
            symptom6.setVisibility(View.INVISIBLE);
            symptom5.setVisibility(View.INVISIBLE);
        }
        // troubling sleeping, Sleep
        if (cb11.isChecked()) {
            symptom7.setVisibility(View.VISIBLE);
        } else {
            symptom7.setVisibility(View.INVISIBLE);
        }

        ///////////////////////////////////////////


    }

}
