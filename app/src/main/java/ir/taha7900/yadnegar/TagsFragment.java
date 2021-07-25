package ir.taha7900.yadnegar;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
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

import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import ir.taha7900.yadnegar.Adapters.TagAdapter;
import ir.taha7900.yadnegar.Models.Tag;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;

public class TagsFragment extends Fragment {

    private MainActivity context;
    private ImageButton addTagButton;
    private ImageButton selectColorButton;
    private TextInputEditText labelInput;
    private TextInputLayout labelInputLayout;
    private int selectedColor;
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
                        break;
                    case MsgCode.CREATE_TAG_SUCCESSFUL:
                        adapter.notifyDataSetChanged();
                }
            }
        };
    }

    private void showTags() {
        tagsProgressBar.setVisibility(View.GONE);
        adapter = new TagAdapter(Tag.getUserTags());
        tagList.addItemDecoration(new TagAdapter.SpacesItemDecoration(4));
        tagList.setLayoutManager(new GridLayoutManager(context,4));
        tagList.setAdapter(adapter);
    }

    public static TagsFragment newInstance() {
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
    public void onResume() {
        super.onResume();
        context.clearTopAppBar();
        context.topAppBar.setTitle(getString(R.string.tags));
        context.setShowNavigationIcon(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_tags, container, false);
        addTagButton = view.findViewById(R.id.addTagButton);
        addTagButton.setOnClickListener(this::addTag);
        selectColorButton = view.findViewById(R.id.selectColorButton);
        selectColorButton.setOnClickListener(this::openColorPicker);
        labelInput = view.findViewById(R.id.labelInput);
        labelInputLayout = view.findViewById(R.id.labelInputLayout);
        selectedColor = labelInputLayout.getBoxStrokeColor();
        tagList = view.findViewById(R.id.tagsList);
        tagsProgressBar = view.findViewById(R.id.tagsProgressBar);
        if (Tag.getUserTags() == null)
            tagsProgressBar.setVisibility(View.VISIBLE);
        else
            showTags();
        return view;
    }

    private void openColorPicker(View view) {
        ColorPickerDialogBuilder
                .with(context)
                .setTitle("Choose color")
                .initialColor(selectedColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                .density(12)
                .setPositiveButton("ok", new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        changeSelectedColor(selectedColor);
                    }
                })
                .build()
                .show();
    }

    private void addTag(View view) {
        if (labelInput.length() == 0){
            labelInput.setError(getString(R.string.this_field_is_required));
            return;
        }
        Network.createTag(labelInput.getText().toString(), Integer.toHexString(selectedColor), handler);
    }

    private void changeSelectedColor(int newColor) {
        labelInputLayout.setBoxStrokeColor(newColor);
        System.out.println("SET!");
        selectedColor = newColor;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }
}