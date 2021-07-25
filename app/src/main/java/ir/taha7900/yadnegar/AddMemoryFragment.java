package ir.taha7900.yadnegar;

import android.content.Context;
import android.graphics.Rect;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.TagSelectionAdapter;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;


public class AddMemoryFragment extends Fragment {

    private RecyclerView tagsList;
    private MaterialButton nextButton;
    private TextInputEditText titleInput;
    private TextInputEditText contentInput;
    private ProgressBar tagsProgressBar;
    private TagSelectionAdapter adapter;
    private MainActivity context;
    private ArrayList<Long> selectedTags;
    private Handler handler;

    public AddMemoryFragment() {
        selectedTags = new ArrayList<>();
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

    public static AddMemoryFragment newInstance() {
        return new AddMemoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Tag.getUserTags() == null) {
            Network.getTags(handler);
        }
    }

    private void showTags() {
        tagsProgressBar.setVisibility(View.GONE);
        adapter = new TagSelectionAdapter(Tag.getUserTags(), selectedTags);
        tagsList.setLayoutManager(new GridLayoutManager(context, 4));
        tagsList.addItemDecoration(new SpacesItemDecoration(4));
        tagsList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_memory, container, false);
        tagsList = view.findViewById(R.id.tagsList);
        tagsProgressBar = view.findViewById(R.id.tagsProgressBar);
        contentInput = view.findViewById(R.id.contentInput);
        titleInput = view.findViewById(R.id.titleInput);
        nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this::openStep2);
        if (Tag.getUserTags() == null)
            tagsProgressBar.setVisibility(View.VISIBLE);
        else
            showTags();
        return view;
    }

    private void openStep2(View view) {
        if (checkInputs()) {
            Memory memory = new Memory();
            memory.setTitle(titleInput.getText().toString());
            memory.setText(contentInput.getText().toString());
            memory.setTags(getSelectedTags());
            memory.setPost_files(new ArrayList<>());
            memory.setTaggedPeople(new ArrayList<>());
            context.hideKeyboard();
            context.getSupportFragmentManager().beginTransaction()
                    .addToBackStack("file upload")
                    .replace(R.id.mainFrame, UploadFileFragment.newInstance(memory))
                    .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                    .commit();
        }
    }

    private ArrayList<Tag> getSelectedTags() {
        ArrayList<Tag> result = new ArrayList<>();
        for (Tag userTag : Tag.getUserTags()) {
            if (selectedTags.contains(userTag.getId()))
                result.add(userTag);
        }
        return result;
    }

    private boolean checkInputs() {
        if (titleInput.length() == 0){
            titleInput.setError(getString(R.string.this_field_is_required));
            return false;
        }
        if (contentInput.length() == 0){
            contentInput.setError(getString(R.string.this_field_is_required));
            return false;
        }
        return true;


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
        context.topAppBar.setTitle(getString(R.string.add_memory));
        context.setShowNavigationIcon(false);
        context.setShowBackIcon(true);
    }

    static class SpacesItemDecoration extends RecyclerView.ItemDecoration {
        private final int space;

        public SpacesItemDecoration(int space) {
            this.space = space;
        }

        @Override
        public void getItemOffsets(Rect outRect, View view,
                                   RecyclerView parent, RecyclerView.State state) {
            outRect.left = space;
            outRect.right = space;
            outRect.bottom = space;
            outRect.top = space;
        }
    }
}