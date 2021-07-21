package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.TagSelectionAdapter;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;


public class SelectTagsFragment extends Fragment {

    private static final String ARG_TAGS = "tags";
    private ArrayList<Long> selectedTags;
    private MaterialButton doneButton;
    private RecyclerView tagsList;
    private TagSelectionAdapter adapter;
    private Handler handler;
    private MainActivity context;

    public SelectTagsFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.TAG_DATA_READY:
                        showTags();
                        break;
                }
            }
        };
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
            selectedTags = (ArrayList<Long>) getArguments().getSerializable(ARG_TAGS);
        }
        if (Tag.getUserTags() == null) {
            context.showLoading(true);
            Network.getTags(handler);
        }
    }

    private void showTags() {
        context.showLoading(false);
        adapter = new TagSelectionAdapter(Tag.getUserTags(), selectedTags);
        tagsList.setLayoutManager(new GridLayoutManager(context, 2));
        tagsList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_select_tags, container, false);
        doneButton = view.findViewById(R.id.doneButton);
        tagsList = view.findViewById(R.id.tagsList);
        showTags();
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }
}