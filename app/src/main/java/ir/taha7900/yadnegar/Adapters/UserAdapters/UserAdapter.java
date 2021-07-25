package ir.taha7900.yadnegar.Adapters.UserAdapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textview.MaterialTextView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ViewHolder> {

    protected ArrayList<User> allUsers;
    protected ArrayList<User> selectedUsers;

    public UserAdapter(ArrayList<User> allUsers, ArrayList<User> selectedUsers) {
        this.allUsers = allUsers;
        this.selectedUsers = selectedUsers;
    }

    public UserAdapter(ArrayList<User> allUsers) {
        this.allUsers = allUsers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_user, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        User user = allUsers.get(position);
        holder.nameText.setText(user.getFullName());
        holder.usernameText.setText(user.getUsername());
    }



    @Override
    public int getItemCount() {
        return allUsers.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        protected MaterialTextView usernameText;
        protected MaterialTextView nameText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.usernameText = itemView.findViewById(R.id.usernameText);
            this.nameText = itemView.findViewById(R.id.nameText);
        }
    }
}
