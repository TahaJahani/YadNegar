package ir.taha7900.yadnegar.Adapters.UserAdapters;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;

public abstract class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    protected ArrayList<User> allUsers;
    protected ArrayList<Long> selectedUsers;

    public UserAdapter(ArrayList<User> allUsers, ArrayList<Long> selectedUsers) {
        this.allUsers = allUsers;
        this.selectedUsers = selectedUsers;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = allUsers.get(position);
        holder.nameText.setText(user.getFirst_name() + " " + user.getLast_name());
        holder.usernameText.setText(user.getUsername());
    }



    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    static abstract class ViewHolder extends RecyclerView.ViewHolder {
        protected MaterialTextView usernameText;
        protected MaterialTextView nameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
