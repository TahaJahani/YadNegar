package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.FriendRequestAdapter;
import ir.taha7900.yadnegar.Adapters.MemoryAdapter;
import ir.taha7900.yadnegar.Models.FriendRequest;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;


public class ProfileFragment extends Fragment implements Toolbar.OnMenuItemClickListener, android.widget.Toolbar.OnMenuItemClickListener {

    private MainActivity context;

    private EditText usernameText;
    private ShapeableImageView userProfilePicture;
    private TextView memoriesCountText;
    private TextView likesCountText;
    private TextView commentsCountText;
    private RecyclerView memoriesList;
    private RecyclerView requestsList;
    private LinearLayout requestsLayout;
    private FriendRequestAdapter requestAdapter;
    private MemoryAdapter memoryAdapter;
    private Handler handler;


    public ProfileFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.GET_FRIEND_REQUEST_SUCCESSFUL:
                        showFriendRequests();
                        break;
                    case MsgCode.USER_MEMORY_DATA_READY:
                        showMemories();
                        break;
                }
            }
        };
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (FriendRequest.getUserFriendRequests() == null)
            Network.getFriendRequests(handler);
        if (Memory.getUserMemories() == null)
            Network.getUserMemories(handler);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.clearTopAppBar();
        context.topAppBar.setTitle(R.string.profile);
        context.topAppBar.inflateMenu(R.menu.profile_top_menu);
        context.topAppBar.setOnMenuItemClickListener(this);
    }

    private void showFriendRequests() {
        requestsLayout.setVisibility(View.VISIBLE);
        if (requestAdapter == null) {
            requestAdapter = new FriendRequestAdapter(FriendRequest.getUserFriendRequests());
        }
        requestsList.setAdapter(requestAdapter);
        requestsList.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        if (requestAdapter.getItemCount() == 0) {
            requestsLayout.setVisibility(View.GONE);
            return;
        }
        requestAdapter.notifyDataSetChanged();
    }

    private void showMemories() {
        if (memoryAdapter == null) {
            memoryAdapter = new MemoryAdapter(Memory.getUserMemories());
        }
        memoriesList.setLayoutManager(new LinearLayoutManager(context));
        memoriesList.setAdapter(memoryAdapter);
        memoryAdapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        usernameText = view.findViewById(R.id.usernameText);
        usernameText.setText(User.getCurrentUser().getUsername());
        ViewCompat.setTransitionName(usernameText, "username_Text");
        userProfilePicture = view.findViewById(R.id.userPicture);
        ViewCompat.setTransitionName(userProfilePicture, "user_Picture");
        memoriesCountText = view.findViewById(R.id.memoriesCountText);
        likesCountText = view.findViewById(R.id.likesCountText);
        commentsCountText = view.findViewById(R.id.commentsCountText);
        memoriesList = view.findViewById(R.id.memoriesList);
        requestsList = view.findViewById(R.id.requestsList);
        requestsLayout = view.findViewById(R.id.requestsLayout);
        if (Memory.getUserMemories() != null)
            showMemories();
        if (FriendRequest.getUserFriendRequests() != null)
            showFriendRequests();
        return view;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            openEditMode();
            return true;
        } else if (id == R.id.sendFriendRequest) {
            openSearchUsersFragment();
        }
        return false;
    }

    private void openSearchUsersFragment() {
        context.getSupportFragmentManager().beginTransaction()
                .addToBackStack("search users")
                .replace(R.id.mainFrame, SearchUserFragment.newInstance(new ArrayList<>(), SearchUserFragment.TYPE_FOLLOW))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void openEditMode() {
        context.getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addSharedElement(usernameText, "nameText")
                .addSharedElement(userProfilePicture, "userPicture")
                .replace(R.id.mainFrame, ProfileEditModeFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}