package com.example.fyproject;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.Preference;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.fyproject.databinding.ActivityFireStoreLogInBinding;
import com.example.fyproject.databinding.ActivityFireStoreSignUpBinding;
import com.example.fyproject.utilities.Constants;
import com.example.fyproject.utilities.Preferences;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.w3c.dom.Document;

public class FireStoreLogIn extends AppCompatActivity {

    private ActivityFireStoreLogInBinding binding;
    private Preferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityFireStoreLogInBinding.inflate(getLayoutInflater());
        preferences = new Preferences(getApplicationContext());
        setContentView(binding.getRoot());
        Listeners();
    }

    private void Listeners() {
        binding.makeOne.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), FireStoreSignUp.class)));
        binding.btnLogin.setOnClickListener(v -> {
        if (isValidLogIn()) {
        }
            signIn();
        });
    }

    private void signIn(){
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        firestore.collection(Constants.KEY_COLLECTION)
                .whereEqualTo(Constants.KEY_EMAIL, binding.inputLoginEmail.getText().toString())
                .whereEqualTo(Constants.KEY_PASSWORD, binding.inputLoginPassword.getText().toString())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult() != null
                    && task.getResult().getDocuments().size() > 0){
                        DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                        preferences.putBoolean(Constants.KEY_IS_SIGNED_IN, true);
                        preferences.putString(Constants.KEY_USER_IS, documentSnapshot.getId());
                        preferences.putString(Constants.KEY_NAME, documentSnapshot.getString(Constants.KEY_NAME));
                        Intent intent = new Intent(getApplicationContext(), studentChatActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }
                });
    }

    public void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private Boolean isValidLogIn() {
        if (binding.inputLoginEmail.getText().toString().trim().isEmpty()) {
            showToast("Enter Valid Email");
            return false;
        } else if (binding.inputLoginPassword.getText().toString().trim().isEmpty()) {
            return false;
        } else {
            return true;
        }
    }
}