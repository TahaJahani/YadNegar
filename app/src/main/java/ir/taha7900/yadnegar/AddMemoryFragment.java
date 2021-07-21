package ir.taha7900.yadnegar;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


public class AddMemoryFragment extends Fragment {

    public AddMemoryFragment() {
        // Required empty public constructor
    }

    public static AddMemoryFragment newInstance(String param1, String param2) {
        return new AddMemoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_add_memory, container, false);
    }
}