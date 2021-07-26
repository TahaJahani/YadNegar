package ir.taha7900.yadnegar;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.FileAdapter;
import ir.taha7900.yadnegar.Adapters.UserAdapters.UserAdapter;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;


public class UploadFileFragment extends Fragment {

    private Memory memory;
    private RecyclerView filesList;
    private MaterialButton addFileButton;
    private MaterialButton doneButton;
    private MainActivity context;

    private FileAdapter fileAdapter;
    private int filePickerCode = 2;
    private Handler handler;

    public UploadFileFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.POST_FILE_SUCCESSFUL:
                        fileUploaded();
                }
            }
        };
    }

    private void fileUploaded() {
        context.showLoading(false);
        fileAdapter.notifyDataSetChanged();
    }


    public static UploadFileFragment newInstance(Memory memory) {
        UploadFileFragment fragment = new UploadFileFragment();
        fragment.memory = memory;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        addFileButton.setOnClickListener(view1 -> openFilePicker());

        return view;
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

    private void openFilePicker() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, filePickerCode);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == filePickerCode) {
            if (resultCode == Activity.RESULT_OK){
                Uri uri;
                if (data != null){
                    uri = data.getData();
                    File selectedFile = new File(uri.getPath());
                    Network.addFileToPost(handler, memory, selectedFile);
                    context.showLoading(true);
                }
            }else{
                ;//todo something
            }
        }
    }
}