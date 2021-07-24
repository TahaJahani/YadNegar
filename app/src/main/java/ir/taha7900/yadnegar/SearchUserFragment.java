package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Objects;

import ir.taha7900.yadnegar.Adapters.UserAdapters.UserAdapter;
import ir.taha7900.yadnegar.Adapters.UserAdapters.UserFollowAdapter;
import ir.taha7900.yadnegar.Adapters.UserAdapters.UserTagAdapter;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;


public class SearchUserFragment extends Fragment {

    public static final String TYPE_FOLLOW = "follow";
    public static final String TYPE_TAG = "tag";

    private ArrayList<Long> selectedUsers;
    private ArrayList<User> allUsers = new ArrayList<>();
    private String type;
    private UserAdapter adapter;

    private TextInputEditText searchInput;
    private ImageButton searchButton;
    private RecyclerView usersList;
    private MainActivity context;
    private Handler handler;

    public SearchUserFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.GET_USERS_SUCCESSFUL:
                        showUsers();
                        break;
                }
            }
        };
    }

    private void showUsers() {
        context.showLoading(false);
        allUsers.clear();
        allUsers.addAll(User.getAllUsers());
        adapter.notifyDataSetChanged();
    }


    public static SearchUserFragment newInstance(ArrayList<Long> users, String type) {
        SearchUserFragment fragment = new SearchUserFragment();
        fragment.selectedUsers = users;
        fragment.type = type;
        fragment.adapter = fragment.type.equals(TYPE_FOLLOW) ?
                new UserFollowAdapter(fragment.allUsers, fragment.selectedUsers)
                : new UserTagAdapter(fragment.allUsers, fragment.selectedUsers);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_user, container, false);
        usersList = view.findViewById(R.id.usersList);
        searchButton = view.findViewById(R.id.searchButton);
        searchInput = view.findViewById(R.id.searchInput);
        searchButton.setOnClickListener(this::searchUser);
        usersList.setAdapter(adapter);
        usersList.setLayoutManager(new LinearLayoutManager(context));
        return view;
    }

    private void searchUser(View view) {
        if (searchInput.length() == 0)
            return;
        String toSearch = Objects.requireNonNull(searchInput.getText()).toString();
        context.showLoading(true);
        Network.searchUsername(toSearch, handler);
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
        context.topAppBar.setTitle(getString(R.string.follow));
    }
}