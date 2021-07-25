package ir.taha7900.yadnegar.Adapters.UserAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;

public class UserTagAdapter extends UserAdapter {

    public UserTagAdapter(ArrayList<User> allUsers, ArrayList<User> selectedUsers) {
        super(allUsers, selectedUsers);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_user_tag, parent, false);
        return new TagViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        User user = allUsers.get(position);
        boolean isSelected = selectedUsers.contains(user);
        ((TagViewHolder)holder).tickImage.setVisibility(isSelected ? View.VISIBLE : View.GONE);
        ((TagViewHolder)holder).parent.setOnClickListener(view -> {
            if (isSelected)
                selectedUsers.remove(user);
            else
                selectedUsers.add(user);
            notifyDataSetChanged();
        });
    }

    static class TagViewHolder extends ViewHolder {
        ImageView tickImage;
        View parent;

        public TagViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.usernameText);
            nameText = itemView.findViewById(R.id.nameText);
            tickImage = itemView.findViewById(R.id.tickImage);
            parent = itemView.findViewById(R.id.mainLayout);
        }
    }
}
