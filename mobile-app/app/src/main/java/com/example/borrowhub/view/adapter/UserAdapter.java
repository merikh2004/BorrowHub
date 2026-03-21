package com.example.borrowhub.view.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.borrowhub.R;
import com.example.borrowhub.data.local.entity.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    public interface UserActionListener {
        void onEditUser(User user);
        void onDeleteUser(User user);
        void onResetPassword(User user);
    }

    private final UserActionListener listener;
    private List<User> users = new ArrayList<>();

    public UserAdapter(UserActionListener listener) {
        this.listener = listener;
    }

    public void setUsers(List<User> users) {
        this.users = users == null ? new ArrayList<>() : new ArrayList<>(users);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_user_row, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        holder.bind(users.get(position), listener);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    static class UserViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvInitial;
        private final TextView tvUserName;
        private final TextView tvUsername;
        private final TextView tvRoleBadge;
        private final ImageButton btnResetPassword;
        private final ImageButton btnEdit;
        private final ImageButton btnDelete;

        UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvInitial = itemView.findViewById(R.id.tvInitial);
            tvUserName = itemView.findViewById(R.id.tvUserName);
            tvUsername = itemView.findViewById(R.id.tvUsername);
            tvRoleBadge = itemView.findViewById(R.id.tvRoleBadge);
            btnResetPassword = itemView.findViewById(R.id.btnResetPassword);
            btnEdit = itemView.findViewById(R.id.btnEdit);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }

        void bind(User user, UserActionListener listener) {
            String name = user.getName() == null ? "" : user.getName();
            String username = user.getUsername() == null ? "" : user.getUsername();
            String role = user.getRole() == null ? "" : user.getRole().trim().toLowerCase(Locale.US);
            boolean isAdmin = "admin".equals(role);
            boolean isProtectedAdmin = "admin".equalsIgnoreCase(username.trim());

            tvUserName.setText(name);
            tvUsername.setText(itemView.getContext().getString(R.string.user_username_format, username));
            tvRoleBadge.setText(isAdmin
                    ? R.string.user_role_admin
                    : R.string.user_role_staff);
            tvRoleBadge.setBackgroundResource(isAdmin
                    ? R.drawable.bg_role_admin_badge
                    : R.drawable.bg_role_staff_badge);
            tvRoleBadge.setTextColor(ContextCompat.getColor(
                    itemView.getContext(),
                    isAdmin ? R.color.user_role_admin_text : R.color.user_role_staff_text
            ));

            tvInitial.setText(name.isEmpty() ? "?" : String.valueOf(name.charAt(0)).toUpperCase(Locale.US));

            btnDelete.setEnabled(!isProtectedAdmin);
            btnDelete.setAlpha(isProtectedAdmin ? 0.4f : 1f);

            btnResetPassword.setOnClickListener(v -> listener.onResetPassword(user));
            btnEdit.setOnClickListener(v -> listener.onEditUser(user));
            btnDelete.setOnClickListener(v -> listener.onDeleteUser(user));
        }
    }
}
