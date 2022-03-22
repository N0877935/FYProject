package com.example.fyproject.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyproject.databinding.ReceivedMessageContainerBinding;
import com.example.fyproject.databinding.SentMessageContainerBinding;
import com.example.fyproject.models.Message;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final List<Message> messages;
    private final String senderId;

    public static final int VIEW_TYPE_SENT = 1;
    public static final int VIEW_TYPE_RECEIVED = 2;

    public ChatAdapter(List<Message> messages, String senderId) {
        this.messages = messages;
        this.senderId = senderId;

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == VIEW_TYPE_SENT) {
            return new SentMessageViewHolder(SentMessageContainerBinding.inflate(LayoutInflater.from(
                    parent.getContext()), parent, false));
        } else {
            return new ReceivedMessageViewHolder(ReceivedMessageContainerBinding.inflate(LayoutInflater.from(
                    parent.getContext()), parent, false)
            );
        }
    }

// could be issue
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_SENT) {
            ((SentMessageViewHolder) holder).setData(messages.get(position));
        } else {
            ((ReceivedMessageViewHolder)holder).setData(messages.get(position));
        }
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (messages.get(position).senderId.equals(senderId)) {
            return VIEW_TYPE_SENT;
        } else {
            return VIEW_TYPE_RECEIVED;
        }
    }
    static class SentMessageViewHolder extends RecyclerView.ViewHolder {

        private final SentMessageContainerBinding binding;

        SentMessageViewHolder (SentMessageContainerBinding sentMessageContainerBinding){
            super(sentMessageContainerBinding.getRoot());
            binding = sentMessageContainerBinding;
        }

        void setData(Message message) {
            binding.textMessage.setText(message.message);
            binding.dateTime.setText(message.time);
        }
    }

    static class ReceivedMessageViewHolder extends RecyclerView.ViewHolder {
        private final ReceivedMessageContainerBinding binding;

        ReceivedMessageViewHolder(ReceivedMessageContainerBinding receivedMessageContainerBinding){
            super(receivedMessageContainerBinding.getRoot());
            binding = receivedMessageContainerBinding;
        }

        void setData(Message message){
            binding.messageText.setText(message.message);
            binding.dateTime.setText(message.time);
        }
    }
}
