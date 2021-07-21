package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;


public class AddMemoryFragment extends Fragment {

    private MaterialButton addTagButton;
    private MainActivity context;
    private ArrayList<Long> selectedTags;

    public AddMemoryFragment() {
        selectedTags = new ArrayList<>();
    }

    public static AddMemoryFragment newInstance() {
        return new AddMemoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_memory, container, false);
        addTagButton = view.findViewById(R.id.addTagButton);
        addTagButton.setOnClickListener(this::openTagsFragment);
        return view;
    }



    private void openTagsFragment(View view) {
        SelectTagsFragment fragment = SelectTagsFragment.newInstance(selectedTags);
        context.getSupportFragmentManager().beginTransaction()
                .addToBackStack("tagSelection")
                .replace(R.id.mainFrame, fragment)
                .commit();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }
}