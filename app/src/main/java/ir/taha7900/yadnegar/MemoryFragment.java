package ir.taha7900.yadnegar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ir.taha7900.yadnegar.Adapters.FileAdapter;
import ir.taha7900.yadnegar.Adapters.TagAdapter;
import ir.taha7900.yadnegar.Adapters.UserAdapters.UserAdapter;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoryFragment extends Fragment implements Toolbar.OnMenuItemClickListener, DialogInterface.OnClickListener {

    private static final String ARG_MEMORY = "memory";

    private Memory memory;
    private MainActivity context;
    private Handler handler;

    private TextView usernameText;
    private TextView nameText;
    private TextView dateText;
    private RecyclerView filesList;
    private RecyclerView tagsList;
    private RecyclerView taggedUsersList;
    private TagAdapter tagAdapter;
    private UserAdapter userAdapter;
    private FileAdapter fileAdapter;

    public MemoryFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.DELETE_POST_SUCCESSFUL:
                        memoryDeleted();
                }
            }
        };
    }

    public static MemoryFragment newInstance(Memory memory) {
        MemoryFragment fragment = new MemoryFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_MEMORY, memory);
        fragment.setArguments(args);
        return  fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            memory = (Memory) getArguments().get(ARG_MEMORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        usernameText = view.findViewById(R.id.usernameText);
        nameText = view.findViewById(R.id.nameText);
        dateText = view.findViewById(R.id.dateText);
        filesList = view.findViewById(R.id.filesList);
        tagsList = view.findViewById(R.id.tagsList);
        taggedUsersList = view.findViewById(R.id.taggedUsersList);

        tagAdapter = new TagAdapter(memory.getTags());
        tagsList.setLayoutManager(new GridLayoutManager(context, 4));
        tagsList.setAdapter(tagAdapter);
        userAdapter = new UserAdapter(memory.getTaggedPeople());
        taggedUsersList.setLayoutManager(new LinearLayoutManager(context));
        taggedUsersList.setAdapter(userAdapter);
        fileAdapter = new FileAdapter(memory.getPostFiles());
        filesList.setLayoutManager(new LinearLayoutManager(context));
        filesList.setAdapter(fileAdapter);

        User creatorUser = memory.getCreatorUser();
        usernameText.setText(creatorUser.getUsername());
        nameText.setText(creatorUser.getFullName());
        dateText.setText(memory.getFormattedCreationDate());

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
        context.setShowNavigationIcon(false);
        context.setShowBackIcon(true);
        context.topAppBar.setTitle(getString(R.string.memory));
        if (memory.getCreatorUser().getId() == User.getCurrentUser().getId()) {
            context.topAppBar.inflateMenu(R.menu.memory_top_menu);
            context.topAppBar.setOnMenuItemClickListener(this);
        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            //TODO
            return true;
        }else if (id == R.id.delete) {
            showDeleteAlert();
            return true;
        }
        return false;
    }

    private void showDeleteAlert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.delete_message))
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == DialogInterface.BUTTON_POSITIVE) {
            Network.deletePost(handler, memory);
            context.showLoading(true);
        }
        dialogInterface.dismiss();
    }

    private void memoryDeleted() {
        context.showLoading(false);
        context.getSupportFragmentManager().popBackStack();
    }
}