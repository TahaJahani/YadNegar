package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.transition.FragmentTransitionSupport;

import android.transition.ChangeBounds;
import android.transition.Fade;
import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionInflater;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileEditModeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileEditModeFragment extends Fragment implements Toolbar.OnMenuItemClickListener {

    private TextView nameText;
    private EditText nameInput;
    private EditText emailInput;
    private EditText birthdayInput;
    private EditText phoneNumberInput;

    private MainActivity context;

    public ProfileEditModeFragment() {
        // Required empty public constructor
    }

    public static ProfileEditModeFragment newInstance() {
        return new ProfileEditModeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setSharedElementEnterTransition(new ChangeBounds());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_edit_mode, container, false);
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
        context.topAppBar.setTitle(getString(R.string.edit_profile));
        context.topAppBar.inflateMenu(R.menu.profile_edit_top_menu);
        context.topAppBar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nameText = view.findViewById(R.id.nameText);
        nameInput = view.findViewById(R.id.nameInput);
        emailInput = view.findViewById(R.id.emailInput);
        birthdayInput = view.findViewById(R.id.birthdayInput);
        phoneNumberInput = view.findViewById(R.id.phoneNumberInput);
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.submit) {
            //TODO: submit data
            context.getSupportFragmentManager().popBackStack();
            return true;
        }
        return false;
    }
}