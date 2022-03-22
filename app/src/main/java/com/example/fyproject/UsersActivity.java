package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.example.fyproject.adapters.UserAdapter;
import com.example.fyproject.databinding.ActivityUsersBinding;
import com.example.fyproject.models.User;
import com.example.fyproject.utilities.ChatActivitiy;
import com.example.fyproject.utilities.Constants;
import com.example.fyproject.utilities.Preferences;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;

public class UsersActivity extends AppCompatActivity implements UserListener{

    private ActivityUsersBinding binding;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityUsersBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = new Preferences(getApplicationContext());
        Listener();
        getUser();
    }


    private void Listener(){
        binding.back.setOnClickListener(v -> onBackPressed());
    }

    private void getUser(){
        loading(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(Constants.KEY_COLLECTION)
                .get().addOnCompleteListener(task -> {
                    loading(false);
                    String userId = preferences.getString(Constants.KEY_USER_IS);
                    if(task.isSuccessful() && task.getResult() != null) {
                        List<User> users = new ArrayList<>();
                        for(QueryDocumentSnapshot queryDocumentSnapshot : task.getResult()){
                            if(userId.equals(queryDocumentSnapshot.getId())) {
                                continue;
                            }
                            User user = new User();
                            user.name = queryDocumentSnapshot.getString(Constants.KEY_NAME);
                            user.email = queryDocumentSnapshot.getString(Constants.KEY_EMAIL);
                            user.token = queryDocumentSnapshot.getString(Constants.KEY_FCM_TOKEN);
                            user.id = queryDocumentSnapshot.getId();
                            users.add(user);
                        }
                            if(users.size() > 0) {
                                UserAdapter userAdapter = new UserAdapter(users, this);
                                binding.usersRecylerView.setAdapter(userAdapter);
                                binding.usersRecylerView.setVisibility(View.VISIBLE);
                            } else {
                                errorMessage();
                            }
                    } else {
                        errorMessage();
                    }
        });
    }

    private void errorMessage(){
        binding.errorMessage.setText(String.format("%s", "User Unavailable"));
        binding.errorMessage.setVisibility(View.VISIBLE);
    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void userClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivitiy.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);
        finish();
    }
}