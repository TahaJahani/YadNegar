package ir.taha7900.yadnegar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.UserAdapters.UserAdapter;
import ir.taha7900.yadnegar.Models.User;


public class SearchUserFragment extends Fragment {

    public static final String TYPE_FOLLOW = "follow";
    public static final String TYPE_TAG = "tag";

    private ArrayList<Long> selectedUsers;
    private ArrayList<User> allUsers;
    private String type;
    private UserAdapter adapter;

    public SearchUserFragment() {

    }


    public static SearchUserFragment newInstance(ArrayList<Long> users, String type) {
        SearchUserFragment fragment = new SearchUserFragment();
        fragment.selectedUsers = users;
        fragment.type = type;
        return fragment;
    }

    public static SearchUserFragment newInstance() {
        return new SearchUserFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_search_user, container, false);
    }
}