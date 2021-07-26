package ir.taha7900.yadnegar;


import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.FileAdapter;
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
    private static final int PICK_FILE = 2;
    private Handler handler;

    private static ArrayList<Uri> filesSelected = new ArrayList<>();


    public UploadFileFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.POST_FILE_SUCCESSFUL:
                        uploadFiles();
                        break;
                    case MsgCode.CREATE_POST_SUCCESSFUL:
                        memory = Memory.getUserMemories().get(Memory.getUserMemories().size() - 1);
                        memory.setPost_files(new ArrayList<>());
                        uploadFiles();
                        break;
                }
            }
        };
    }

    private void uploadFiles() {
        if (filesSelected.size() == 0) {
            goToHomePage();
            return;
        }
        Uri uri = filesSelected.get(0);
        File file;
        try {
            InputStream input = getActivity().getContentResolver().openInputStream(uri);
            file = new File(getActivity().getFilesDir(), getNameFromURI(getActivity().getContentResolver(), uri));
            try (OutputStream output = new FileOutputStream(file)) {
                byte[] buffer = new byte[16 * 1024]; // or other buffer size
                int read;

                while ((read = input.read(buffer)) != -1) {
                    try {
                        output.write(buffer, 0, read);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                output.flush();
                input.close();
            } catch (IOException e) {
                e.printStackTrace();
                Log.w("file" , e.getMessage());
                goToHomePage();
                return;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.w("file" , e.getMessage());
            goToHomePage();
            return;
        }
        filesSelected.remove(uri);
        Network.addFileToPost(handler, memory, file);
    }


    private void goToHomePage() {
        context.showLoading(false);
        context.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrame, HomeFragment.newInstance(HomeFragment.TYPE_TOP_MEMORIES), "homeFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
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

        doneButton.setOnClickListener(view1 -> uploadMemory());
        addFileButton.setOnClickListener(view1 -> openFilePicker());

        return view;
    }

    private void uploadMemory() {
        Network.createPost(handler, memory.extractMemoryData());
        context.showLoading(true);
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
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_FILE) {
            Uri uri = data.getData();
            filesSelected.add(uri);
            memory.addPostFile(getNameFromURI(getActivity().getContentResolver(), uri));
            fileAdapter.notifyDataSetChanged();

        }
    }

    public static String getNameFromURI(ContentResolver resolver, Uri contentUri) {
        Cursor returnCursor = resolver.query(contentUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }

}