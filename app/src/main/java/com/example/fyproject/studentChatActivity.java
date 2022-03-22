package com.example.fyproject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.fyproject.adapters.ConversationAdapter;
import com.example.fyproject.databinding.ActivityStudentChatBinding;
import com.example.fyproject.models.Message;
import com.example.fyproject.models.User;
import com.example.fyproject.utilities.ChatActivitiy;
import com.example.fyproject.utilities.Constants;
import com.example.fyproject.utilities.Preferences;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class studentChatActivity extends AppCompatActivity implements ConversationListener {

    private ActivityStudentChatBinding binding;
    private Preferences preferences;
    private List<Message> conversations;
    private ConversationAdapter conversationAdapter;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityStudentChatBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        preferences = new Preferences((getApplicationContext()));
        init();
        userDetails();
        getToken();
        Listeners();
        findConversations();
    }

    private void init() {
        conversations = new ArrayList<>();
        conversationAdapter = new ConversationAdapter(conversations, this);
        binding.ConversationsRecycler.setAdapter(conversationAdapter);
        firestore = FirebaseFirestore.getInstance();
    }

    private void Listeners() {
        binding.imSignOut.setOnClickListener(v -> logOut());
        binding.newChat.setOnClickListener(v ->
                startActivity(new Intent(getApplicationContext(), UsersActivity.class)));

    }

    private void userDetails() {
        binding.textName.setText(preferences.getString(Constants.KEY_NAME));
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void findConversations() {
        firestore.collection(Constants.KEY_CONVERSATIONS).whereEqualTo(Constants.KEY_SENDER_ID, preferences.getString(Constants.KEY_USER_IS))
                .addSnapshotListener(eventListener);
        firestore.collection(Constants.KEY_CONVERSATIONS).whereEqualTo(Constants.KEY_RECEIVED_ID, preferences.getString(Constants.KEY_USER_IS))
                .addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        }
        if (value != null) {
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED) {
                    String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    String receivedId = documentChange.getDocument().getString(Constants.KEY_RECEIVED_ID);
                    Message message = new Message();
                    message.senderId = senderId;
                    message.receiverId = receivedId;
                    if (preferences.getString(Constants.KEY_USER_IS).equals(senderId)) {
                        message.conversationName = documentChange.getDocument().getString(Constants.KEY_RECEIVER_NAME);
                        message.conversationID = documentChange.getDocument().getString(Constants.KEY_RECEIVED_ID);
                    } else {
                        message.conversationName = documentChange.getDocument().getString(Constants.KEY_SENDER_NAME);
                        message.conversationID = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    }
                    message.message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                    message.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    conversations.add(message);
                } else if (documentChange.getType() == DocumentChange.Type.MODIFIED) {
                    for (int i = 0; i < conversations.size(); i++) {
                        String senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                        String receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVED_ID);
                        if (conversations.get(i).receiverId.equals(receiverId)) {
                            conversations.get(i).message = documentChange.getDocument().getString(Constants.KEY_LAST_MESSAGE);
                            conversations.get(i).dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                            break;
                        }
                    }
                }
            }
            Collections.sort(conversations, (object1, object2) -> object2.dateObject.compareTo(object1.dateObject));
            conversationAdapter.notifyDataSetChanged();
            binding.ConversationsRecycler.smoothScrollToPosition(0);
            binding.ConversationsRecycler.setVisibility(View.VISIBLE);
            binding.progressBar.setVisibility(View.GONE);
        }
    };


    private void getToken() {
        FirebaseMessaging.getInstance().getToken().addOnSuccessListener(this::updateToken);
    }

    private void updateToken(String token) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference =
                firestore.collection(Constants.KEY_COLLECTION).document(
                        preferences.getString(Constants.KEY_USER_IS)
                );
        documentReference.update(Constants.KEY_FCM_TOKEN, token).addOnSuccessListener(unused -> showToast("Token updated."))
                .addOnFailureListener(e -> showToast("Cant update token"));
    }

    private void logOut() {
        showToast("Logging Out...");
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        DocumentReference documentReference = firestore.collection(Constants.KEY_COLLECTION).document(
                preferences.getString(Constants.KEY_USER_IS)
        );
        HashMap<String, Object> updates = new HashMap<>();
        updates.put(Constants.KEY_FCM_TOKEN, FieldValue.delete());
        documentReference.update(updates).addOnSuccessListener(unused -> {
            preferences.clear();
            startActivity(new Intent(getApplicationContext(), FireStoreLogIn.class));
            finish();
        })
                .addOnFailureListener(e -> showToast("Cannot sign out, Please try again."));
    }

    public void conversionClicked(User user) {
        Intent intent = new Intent(getApplicationContext(), ChatActivitiy.class);
        intent.putExtra(Constants.KEY_USER, user);
        startActivity(intent);

    }
}