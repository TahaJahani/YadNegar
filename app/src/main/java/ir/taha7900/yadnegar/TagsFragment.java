package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;

import com.google.android.material.textfield.TextInputEditText;

import ir.taha7900.yadnegar.Adapters.TagAdapter;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;

public class TagsFragment extends Fragment {

    private MainActivity context;
    private ImageButton addTagButton;
    private TextInputEditText labelInput;
    private RecyclerView tagList;
    private TagAdapter adapter;
    private ProgressBar tagsProgressBar;
    private Handler handler;

    public TagsFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.TAG_DATA_READY:
                        showTags();
                }
            }
        };
    }

    private void showTags() {
        tagsProgressBar.setVisibility(View.GONE);
        adapter = new TagAdapter(Tag.getUserTags());
        tagList.setLayoutManager(new GridLayoutManager(context,4));
        tagList.setAdapter(adapter);
    }

    public static TagsFragment newInstance(String param1, String param2) {
        return new TagsFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Tag.getUserTags() == null) {
            Network.getTags(handler);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);
        addTagButton = view.findViewById(R.id.addTagButton);
        addTagButton.setOnClickListener(this::addTag);
        labelInput = view.findViewById(R.id.labelInput);
        tagList = view.findViewById(R.id.tagsList);
        tagsProgressBar = view.findViewById(R.id.tagsProgressBar);
        if (Tag.getUserTags() == null)
            tagsProgressBar.setVisibility(View.VISIBLE);
        else
            showTags();
        return view;
    }

    private void addTag(View view) {
        if (labelInput.length() == 0){
            labelInput.setError(getString(R.string.this_field_is_required));
            return;
        }
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }
}