package com.example.fyproject.ui.health;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fyproject.FireStoreLogIn;
import com.example.fyproject.FireStoreSignUp;
import com.example.fyproject.LoginActivity;
import com.example.fyproject.MainActivity;
import com.example.fyproject.R;
import com.example.fyproject.RegistrationActivity;
import com.example.fyproject.checkerActivity;
import com.example.fyproject.databinding.FragmentHealthBinding;
import com.example.fyproject.profChatActivity;
import com.example.fyproject.studentChatActivity;
import com.example.fyproject.ui.info.InfoFragment;


public class HealthFragment extends Fragment {
    LinearLayout healthLayout1, healthLayout2, healthLayout3;

    public HealthFragment(){

    }

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_health, container, false);

        healthLayout1 = view.findViewById(R.id.healthLayout1);
        healthLayout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), checkerActivity.class);
                startActivity(in);
            }
        });

        healthLayout2 = view.findViewById(R.id.healthLayout2);
        healthLayout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), FireStoreLogIn.class);
                startActivity(in);
            }
        });

        healthLayout3 = view.findViewById(R.id.healthLayout3);
        healthLayout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(getActivity(), profChatActivity.class);
                startActivity(in);
            }
        });

        return view;
    }


}