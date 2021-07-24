package ir.taha7900.yadnegar.Adapters.UserAdapters;

import android.content.DialogInterface;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import ir.taha7900.yadnegar.MainActivity;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.R;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;

public class UserFollowAdapter extends UserAdapter {

    private MainActivity context;
    private Handler handler;
    private DialogInterface tempDialog;
    private FollowViewHolder tempHolder;

    public UserFollowAdapter(ArrayList<User> allUsers, ArrayList<Long> selectedUsers) {
        super(allUsers, selectedUsers);
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.SEND_FRIEND_REQUEST_SUCCESSFUL:
                        tempHolder.followButton.setVisibility(View.GONE);
                        tempDialog.cancel();
                        tempDialog = null;
                        tempHolder = null;
                        break;
                }
            }
        };
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.context = (MainActivity) parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.row_user_follow, parent, false);
        return new FollowViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        User user = allUsers.get(position);
        boolean hasFollowed = selectedUsers.contains(user.getId());
        ((FollowViewHolder)holder).followButton.setVisibility(hasFollowed ? View.GONE : View.VISIBLE);
        ((FollowViewHolder) holder).followButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tempHolder = (FollowViewHolder) holder;
                showFollowDialog(user);
            }
        });
    }

    private void showFollowDialog(User user) {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context)
                .setTitle("Follow User?")
                .setPositiveButton(context.getString(R.string.yes), (dialogInterface, i) -> {
                    tempDialog = dialogInterface;
                    Network.sendFriendRequest(user.getId(), handler);
                })
                .setNegativeButton(context.getString(R.string.no), (dialogInterface, i) -> dialogInterface.dismiss());
        builder.show();
    }

    static class FollowViewHolder extends ViewHolder {
        MaterialButton followButton;

        public FollowViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.usernameText);
            nameText = itemView.findViewById(R.id.nameText);
            followButton = itemView.findViewById(R.id.followButton);
        }
    }
}
