package com.example.fyproject.adapters;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fyproject.UserListener;
import com.example.fyproject.databinding.ContainerUserBinding;
import com.example.fyproject.models.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {

    private final List<User> users;
    private final UserListener userListener;

    public UserAdapter(List<User> users, UserListener userListener) {
        this.users = users;
        this.userListener = userListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ContainerUserBinding containerUserBinding = ContainerUserBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new UserHolder(containerUserBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        holder.userData(users.get(position));
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    class UserHolder extends RecyclerView.ViewHolder {

        ContainerUserBinding binding;

        UserHolder(ContainerUserBinding containerUserBinding) {
            super(containerUserBinding.getRoot());
            binding = containerUserBinding;
        }
        void userData(User user) {
            binding.textName.setText(user.name);
            binding.textEmail.setText(user.email);
            binding.getRoot().setOnClickListener(v -> userListener.userClicked(user));
        }
    }
}
