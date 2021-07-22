package ir.taha7900.yadnegar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class SearchUserFragment extends Fragment {

    private ArrayList<Long> users;

    public SearchUserFragment() {
        // Required empty public constructor
    }


    public static SearchUserFragment newInstance(ArrayList<Long> users) {
        SearchUserFragment fragment = new SearchUserFragment();
        fragment.users = users;
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