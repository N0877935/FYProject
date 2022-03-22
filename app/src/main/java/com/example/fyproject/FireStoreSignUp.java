package com.example.fyproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.fyproject.databinding.ActivityFireStoreSignUpBinding;
import com.example.fyproject.utilities.Constants;
import com.example.fyproject.utilities.Preferences;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;

public class FireStoreSignUp extends AppCompatActivity {
    private ActivityFireStoreSignUpBinding binding;
    private Preferences preference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFireStoreSignUpBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preference = new Preferences((getApplicationContext()));
        Listeners();
    }

    private void Listeners(){
        binding.accountExists.setOnClickListener(v ->{
            Intent intent = new Intent(getApplicationContext(), FireStoreLogIn.class);
                });
        binding.btnRegister.setOnClickListener(v -> {
            if(isValidSignUp()) {
                signUp();
            }
        });

    }
    
    private void showToast(String message){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private void signUp(){
        loading(true);
        FirebaseFirestore firestore = FirebaseFirestore.getInstance() ;
        HashMap<String, Object> user = new HashMap<>();
        user.put(Constants.KEY_NAME, binding.inputName.getText().toString());
        user.put(Constants.KEY_EMAIL, binding.inputEmail.getText().toString());
        user.put(Constants.KEY_PASSWORD, binding.inputPassword.getText().toString());
        firestore.collection(Constants.KEY_COLLECTION)
                .add(user).addOnSuccessListener(documentReference -> {
                    loading(false);
                    preference.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                    preference.putString(Constants.KEY_USER_IS, documentReference.getId());
                    preference.putString(Constants.KEY_NAME, binding.inputName.getText().toString());
                    Intent intent = new Intent(getApplicationContext(), studentChatActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);

        }).addOnFailureListener(e -> {
            loading(false);
            showToast(e.getMessage());
        });


    }
    private Boolean isValidSignUp(){
        if (binding.inputName.getText().toString().trim().isEmpty()) {
            showToast("Enter Name");
            return false;
        } else if(binding.inputEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Email");
            return false;
        } else if(binding.inputPassword.getText().toString().trim().isEmpty()) {
            showToast("Enter Password");
            return false;
        } else if(binding.inputConfirm.getText().toString().trim().isEmpty()) {
            showToast("Confirm Password");
            return false;
        } else {
            return true;
        }
    }

    private void loading(Boolean isLoading){
        if (isLoading) {
            binding.btnRegister.setVisibility(View.INVISIBLE);
            binding.progressBar.setVisibility(View.VISIBLE);
        } else {
            binding.progressBar.setVisibility(View.INVISIBLE);
            binding.btnRegister.setVisibility(View.VISIBLE);
        }

    }
}