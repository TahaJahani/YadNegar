package ir.taha7900.yadnegar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SelectTagsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SelectTagsFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_TAGS = "tags";

    // TODO: Rename and change types of parameters
    private ArrayList<Long> tags;

    public SelectTagsFragment() {
        // Required empty public constructor
    }

    public static SelectTagsFragment newInstance(ArrayList<Long> tags) {
        SelectTagsFragment fragment = new SelectTagsFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_TAGS, tags);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            tags = (ArrayList<Long>) getArguments().getSerializable(ARG_TAGS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_select_tags, container, false);
    }
}