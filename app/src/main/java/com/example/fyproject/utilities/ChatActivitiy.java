package com.example.fyproject.utilities;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;


import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import com.example.fyproject.adapters.ChatAdapter;
import com.example.fyproject.databinding.ActivityChatActivitiyBinding;
import com.example.fyproject.models.Message;
import com.example.fyproject.models.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChatActivitiy extends AppCompatActivity {

    private ActivityChatActivitiyBinding binding;
    private User receiverUser;
    private List<Message> messageList;
    private ChatAdapter chatAdapter;
    private Preferences preferences;
    private FirebaseFirestore firestore;
    private String convoID = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChatActivitiyBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        loadDetails();
        listeners();
        init();
        messageListener();
    }

    private void init(){
        preferences = new Preferences(getApplicationContext());
        messageList = new ArrayList<>();
        chatAdapter = new ChatAdapter(messageList, preferences.getString(Constants.KEY_USER_IS));
        binding.chatRecyclerView.setAdapter(chatAdapter);
        firestore = FirebaseFirestore.getInstance();
    }

    //could be problem
    private void sendMessage(){
        HashMap<String, Object> message = new HashMap<>();
        message.put(Constants.KEY_SENDER_ID, preferences.getString(Constants.KEY_USER_IS));
        message.put(Constants.KEY_RECEIVED_ID, receiverUser.id);
        message.put(Constants.KEY_MESSAGE, binding.chatInput.getText().toString());
        message.put(Constants.KEY_TIMESTAMP, new Date());
        firestore.collection(Constants.KEY_COLLECTION_CHAT).add(message);
        if(convoID != null) {
            updateConversation(binding.chatInput.getText().toString());
        } else {
            HashMap<String, Object> conversation = new HashMap<>();
            conversation.put(Constants.KEY_USER_IS, preferences.getString(Constants.KEY_USER_IS));
            conversation.put(Constants.KEY_SENDER_NAME, preferences.getString(Constants.KEY_NAME));
            conversation.put(Constants.KEY_RECEIVED_ID, receiverUser.id);
            conversation.put(Constants.KEY_RECEIVER_IMAGE, receiverUser.name);
            conversation.put(Constants.KEY_LAST_MESSAGE, binding.chatInput.getText().toString());
            conversation.put(Constants.KEY_TIMESTAMP, new Date());
            addConversation(conversation);
        }
        binding.chatInput.setText(null);
    }

    private void messageListener(){
        firestore.collection(Constants.KEY_COLLECTION_CHAT).whereEqualTo(Constants.KEY_SENDER_ID, preferences.getString(Constants.KEY_USER_IS))
                .whereEqualTo(Constants.KEY_RECEIVED_ID, receiverUser.id).addSnapshotListener(eventListener);
        firestore.collection(Constants.KEY_COLLECTION_CHAT).whereEqualTo(Constants.KEY_SENDER_ID, receiverUser.id)
                .whereEqualTo(Constants.KEY_RECEIVED_ID, preferences.getString(Constants.KEY_USER_IS)).addSnapshotListener(eventListener);
    }

    private final EventListener<QuerySnapshot> eventListener = (value, error) -> {
        if (error != null) {
            return;
        } if (value != null) {
            int count = messageList.size();
            for (DocumentChange documentChange : value.getDocumentChanges()) {
                if (documentChange.getType() == DocumentChange.Type.ADDED){
                    Message message = new Message();
                    message.senderId = documentChange.getDocument().getString(Constants.KEY_SENDER_ID);
                    message.receiverId = documentChange.getDocument().getString(Constants.KEY_RECEIVED_ID);
                    message.message = documentChange.getDocument().getString(Constants.KEY_MESSAGE);
                    message.time = getDateTime(documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP));
                    message.dateObject = documentChange.getDocument().getDate(Constants.KEY_TIMESTAMP);
                    messageList.add(message);
                }
            }
            Collections.sort(messageList, (obj1, obj2 )-> obj1.dateObject.compareTo(obj2.dateObject));
            if (count == 0) {
                chatAdapter.notifyDataSetChanged();
            } else {
                chatAdapter.notifyItemRangeInserted(messageList.size(), messageList.size());
                binding.chatRecyclerView.smoothScrollToPosition(messageList.size() - 1);
            }
            binding.chatRecyclerView.setVisibility(View.VISIBLE);
        }
        binding.progressBar.setVisibility(View.GONE);

        if(convoID == null){
            checkConversation();
        }
    };

    private void loadDetails(){
        receiverUser = (User) getIntent().getSerializableExtra(Constants.KEY_USER);
        binding.textName.setText(receiverUser.name);
     }

     private void listeners(){
        binding.imBack.setOnClickListener(v -> onBackPressed());
        binding.sendLayout.setOnClickListener(v -> sendMessage());
     }

     private String getDateTime(Date date) {
        return new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(date);
     }

     private void updateConversation(String message) {
         DocumentReference documentReference = firestore.collection(Constants.KEY_CONVERSATIONS).document(convoID);
         documentReference.update(Constants.KEY_LAST_MESSAGE, message, Constants.KEY_TIMESTAMP, new Date());
     }

     private void addConversation(HashMap<String, Object> conversation) {
        firestore.collection(Constants.KEY_CONVERSATIONS).add(conversation).addOnSuccessListener(
                documentReference -> convoID = documentReference.getId());
     }

     //could be wrong list

     private void checkConversation() {
        if(messageList.size() != 0) {
            checkConversation(preferences.getString(Constants.KEY_USER_IS), receiverUser.id);
            checkConversation(receiverUser.id, preferences.getString(Constants.KEY_USER_IS));
        }
     }


     private void checkConversation(String senderId, String receiverId){
        firestore.collection(Constants.KEY_CONVERSATIONS).whereEqualTo(Constants.KEY_USER_IS, senderId)
                .whereEqualTo(Constants.KEY_RECEIVED_ID, receiverId).get().addOnCompleteListener(onCompleteListener);

     }

     private final OnCompleteListener<QuerySnapshot> onCompleteListener = task -> {
        if(task.isSuccessful() && task.getResult() != null && task.getResult().getDocuments().size() > 0) {
            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
            convoID = documentSnapshot.getId();
        }
     };
}