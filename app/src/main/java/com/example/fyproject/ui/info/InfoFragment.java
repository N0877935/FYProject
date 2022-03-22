package com.example.fyproject.ui.info;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;




import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.fyproject.R;
import com.example.fyproject.checkerActivity;
import com.example.fyproject.databinding.FragmentInfoBinding;
import com.example.fyproject.emergenciesActivity;
import com.example.fyproject.femaleActivity;
import com.example.fyproject.maleActivity;
import com.example.fyproject.mentalHealthActivity;
import com.example.fyproject.profChatActivity;
import com.example.fyproject.studentChatActivity;

public class InfoFragment extends Fragment {

    private FragmentInfoBinding binding;

    LinearLayout layoutMale, layoutFemale, layoutEmergencies, layoutMHealth, layoutStudentChat, layoutProfChat;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        InfoViewModel infoViewModel =
                new ViewModelProvider(this).get(InfoViewModel.class);
        View view = inflater.inflate(R.layout.fragment_info, container, false);

        layoutMale = view.findViewById(R.id.male);
        layoutMale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), maleActivity.class);
                startActivity(intent);
            }
        });

        layoutFemale = view.findViewById(R.id.female);
        layoutFemale.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), femaleActivity.class);
                startActivity(intent);
            }
        });

        layoutEmergencies = view.findViewById(R.id.emergencies);
        layoutEmergencies.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), emergenciesActivity.class);
                startActivity(intent);
            }
        });

        layoutMHealth = view.findViewById(R.id.mentalHealth);
        layoutMHealth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), mentalHealthActivity.class);
                startActivity(intent);
            }
        });

        layoutStudentChat = view.findViewById(R.id.studentChat);
        layoutStudentChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), studentChatActivity.class);
                startActivity(intent);
            }
        });

        layoutProfChat = view.findViewById(R.id.profChat);
        layoutProfChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), profChatActivity.class);
                startActivity(intent);
            }
        });




        return view;
    }

}