package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.TagAdapter;
import ir.taha7900.yadnegar.Adapters.TagSelectionAdapter;
import ir.taha7900.yadnegar.Adapters.UserAdapters.UserAdapter;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;


public class AddMemoryFragment extends Fragment {

    private static final String TYPE_CREATE = "create";
    private static final String TYPE_EDIT = "edit";

    private RecyclerView tagsList;
    private MaterialButton nextButton;
    private TextInputEditText titleInput;
    private TextInputEditText contentInput;
    private ProgressBar tagsProgressBar;
    private TagSelectionAdapter adapter;
    private RecyclerView usersList;
    private MaterialButton addUserButton;
    private UserAdapter userAdapter;
    private MainActivity context;
    private ArrayList<Long> selectedTags;
    private Handler handler;
    private Memory memory;
    private String type;

    public AddMemoryFragment() {
        selectedTags = new ArrayList<>();
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.TAG_DATA_READY:
                        showTags();
                        break;
                    case MsgCode.EDIT_POST_SUCCESSFUL:
                        //  memoryUploaded();
                        break;
                }
            }
        };
    }

    public static AddMemoryFragment newInstance(Memory memory) {
        AddMemoryFragment fragment = new AddMemoryFragment();
        if (memory == null) {
            memory = new Memory();
            memory.setTaggedPeople(new ArrayList<>());
            memory.setPost_files(new ArrayList<>());
            memory.setTags(new ArrayList<>());
            fragment.type = TYPE_CREATE;
        } else
            fragment.type = TYPE_EDIT;
        fragment.memory = memory;
        return fragment;
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
        tagsList.addItemDecoration(new TagAdapter.SpacesItemDecoration(4));
        tagsList.setAdapter(adapter);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_memory, container, false);
        tagsList = view.findViewById(R.id.tagsList);
        tagsProgressBar = view.findViewById(R.id.tagsProgressBar);
        usersList = view.findViewById(R.id.taggedUsersList);
        addUserButton = view.findViewById(R.id.addUserButton);
        userAdapter = new UserAdapter(memory.getTaggedPeople());
        usersList.setAdapter(userAdapter);
        usersList.setLayoutManager(new LinearLayoutManager(context));
        contentInput = view.findViewById(R.id.contentInput);
        titleInput = view.findViewById(R.id.titleInput);
        nextButton = view.findViewById(R.id.nextButton);
        nextButton.setOnClickListener(this::openStep2);
        addUserButton.setOnClickListener(this::openSelectUserFragment);
        if (Tag.getUserTags() == null)
            tagsProgressBar.setVisibility(View.VISIBLE);
        else
            showTags();

        if (type.equals(TYPE_EDIT)) {
            titleInput.setText(memory.getTitle());
            contentInput.setText(memory.getText());
        }
        return view;
    }

    private void openSelectUserFragment(View view) {
        context.getSupportFragmentManager().beginTransaction()
                .addToBackStack("select users")
                .replace(R.id.mainFrame, SearchUserFragment.newInstance(memory.getTaggedPeople(), SearchUserFragment.TYPE_TAG))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    private void openStep2(View view) {
        if (checkInputs()) {
            memory.setTitle(titleInput.getText().toString());
            memory.setText(contentInput.getText().toString());
            memory.setTags(getSelectedTags());
            context.hideKeyboard();
            if (type.equals(TYPE_CREATE))
                goNextPage();
            else if (type.equals(TYPE_EDIT)) {
                Network.editPost(handler, memory.extractMemoryData(), memory);
                context.showLoading(true);
            }
        }
    }

    private void goNextPage() {
        context.showLoading(false);
        context.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrame, UploadFileFragment.newInstance(memory))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
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
        if (titleInput.length() == 0) {
            titleInput.setError(getString(R.string.this_field_is_required));
            return false;
        }
        if (contentInput.length() == 0) {
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
        userAdapter.notifyDataSetChanged();
    }
}