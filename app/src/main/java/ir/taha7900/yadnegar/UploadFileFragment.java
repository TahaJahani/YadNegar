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
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

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
        intent.setType("*/*");
        startActivityForResult(intent, PICK_FILE);

//        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);
//        startActivityForResult(gallery, PICK_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_FILE) {
            File file = null;
            if (data != null) {
                Uri uri = data.getData();
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
                    }
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Network.addFileToPost(handler, memory, file);
                context.showLoading(true);
            }
        }
    }

    public static String getNameFromURI(ContentResolver resolver, Uri contentUri) {
        Cursor returnCursor = resolver.query(contentUri, null, null, null, null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        return returnCursor.getString(nameIndex);
    }

}