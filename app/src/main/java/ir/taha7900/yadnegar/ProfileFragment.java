package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.transition.Slide;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;

import ir.taha7900.yadnegar.Models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment implements Toolbar.OnMenuItemClickListener, android.widget.Toolbar.OnMenuItemClickListener {

    private MainActivity context;

    private EditText usernameText;
    private ShapeableImageView userProfilePicture;
    private TextView memoriesCountText;
    private TextView likesCountText;
    private TextView commentsCountText;
    private RecyclerView memoriesList;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
        context.topAppBar.setTitle(R.string.profile);
        context.topAppBar.inflateMenu(R.menu.profile_top_menu);
        context.topAppBar.setOnMenuItemClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        usernameText = view.findViewById(R.id.usernameText);
        usernameText.setText(User.getCurrentUser().getUsername());
        ViewCompat.setTransitionName(usernameText, "username_Text");
        userProfilePicture = view.findViewById(R.id.userPicture);
        ViewCompat.setTransitionName(userProfilePicture, "user_Picture");
        memoriesCountText = view.findViewById(R.id.memoriesCountText);
        likesCountText = view.findViewById(R.id.likesCountText);
        commentsCountText = view.findViewById(R.id.commentsCountText);
        memoriesList = view.findViewById(R.id.memoriesList);
        return view;
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            openEditMode();
            return true;
        }
        return false;
    }

    private void openEditMode() {
        context.getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .addSharedElement(usernameText, "nameText")
                .addSharedElement(userProfilePicture, "userPicture")
                .replace(R.id.mainFrame, ProfileEditModeFragment.newInstance())
                .addToBackStack(null)
                .commit();
    }
}