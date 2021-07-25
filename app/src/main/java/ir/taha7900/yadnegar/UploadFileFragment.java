package ir.taha7900.yadnegar;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.FileAdapter;
import ir.taha7900.yadnegar.Adapters.UserAdapters.UserAdapter;
import ir.taha7900.yadnegar.Models.Memory;


public class UploadFileFragment extends Fragment {

    private static final String ARG_MEMORY = "memory";

    private Memory memory;
    private RecyclerView usersList;
    private RecyclerView filesList;
    private MaterialButton addUserButton;
    private MaterialButton addFileButton;
    private MainActivity context;

    private UserAdapter userAdapter;
    private FileAdapter fileAdapter;

    public UploadFileFragment() {

    }


    public static UploadFileFragment newInstance(Memory memory) {
        UploadFileFragment fragment = new UploadFileFragment();
        fragment.memory = memory;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            memory = (Memory) getArguments().getSerializable(ARG_MEMORY);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_upload_file, container, false);
        usersList = view.findViewById(R.id.taggedUsersList);
        filesList = view.findViewById(R.id.filesList);
        addUserButton = view.findViewById(R.id.addUserButton);
        addFileButton = view.findViewById(R.id.addFileButton);

        userAdapter = new UserAdapter(memory.getTaggedPeople());
        fileAdapter = new FileAdapter(memory.getPostFiles());//TODO: initialize arraylist

        usersList.setAdapter(userAdapter);
        usersList.setLayoutManager(new LinearLayoutManager(context));
        filesList.setAdapter(fileAdapter);
        filesList.setLayoutManager(new LinearLayoutManager(context));

        addUserButton.setOnClickListener(this::openSelectUserFragment);

        return view;
    }

    private void openSelectUserFragment(View view) {
        context.getSupportFragmentManager().beginTransaction()
                .addToBackStack("select users")
                .replace(R.id.mainFrame, SearchUserFragment.newInstance(memory.getTaggedPeople(), SearchUserFragment.TYPE_TAG))
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .commit();
    }

    @Override
    public void onResume() {
        super.onResume();
        fileAdapter.notifyDataSetChanged();
        userAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }
}