package com.example.fyproject.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyproject.ConversationListener;
import com.example.fyproject.databinding.ContainerRecentConvoBinding;
import com.example.fyproject.models.Message;
import com.example.fyproject.models.User;

import java.util.List;

public class ConversationAdapter extends  RecyclerView.Adapter<ConversationAdapter.ConversationViewHolder> {

    private final List<Message> messages;
    private final ConversationListener conversationListener;

    public ConversationAdapter(List<Message> messages, ConversationListener conversationListener) {

        this.messages = messages;
        this.conversationListener = conversationListener;
    }

    @NonNull
    @Override
    public ConversationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ConversationViewHolder(
                ContainerRecentConvoBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ConversationViewHolder holder, int position) {
        holder.setData(messages.get(position));

    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    class ConversationViewHolder extends RecyclerView.ViewHolder {

        ContainerRecentConvoBinding binding;

        ConversationViewHolder(ContainerRecentConvoBinding containerRecentConvoBinding) {
            super(containerRecentConvoBinding.getRoot());
            binding = containerRecentConvoBinding;
        }

        void setData (Message message) {
            binding.textName.setText(message.conversationName);
            binding.textRecent.setText(message.message);
            binding.getRoot().setOnClickListener(v -> {
                User user = new User();
                user.id = message.conversationID;
                user.name = message.conversationName;
                conversationListener.conversionClicked(user);
            });
        }
    }


}
