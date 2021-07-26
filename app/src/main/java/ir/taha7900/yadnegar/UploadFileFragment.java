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
    private static final int PICK_IMAGE = 1;


    private Memory memory;
    private RecyclerView filesList;
    private MaterialButton addFileButton;
    private MaterialButton doneButton;
    private MainActivity context;

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
        filesList = view.findViewById(R.id.filesList);
        addFileButton = view.findViewById(R.id.addFileButton);
        doneButton = view.findViewById(R.id.doneButton);
        fileAdapter = new FileAdapter(memory.getPostFiles());
        filesList.setAdapter(fileAdapter);
        filesList.setLayoutManager(new LinearLayoutManager(context));
        addFileButton.setOnClickListener(this::addFile);

        return view;
    }

    private void addFile(View view) {

    }

    @Override
    public void onResume() {
        super.onResume();
        fileAdapter.notifyDataSetChanged();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }
}