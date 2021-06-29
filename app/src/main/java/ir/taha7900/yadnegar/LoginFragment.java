package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.lang.ref.WeakReference;
import java.time.Instant;
import java.util.Date;
import java.util.Objects;

import ir.taha7900.yadnegar.Models.User;

public class LoginFragment extends Fragment implements View.OnClickListener {

    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private MaterialButton loginButton;
    private MaterialButton registerButton;
    private MainActivity context;

    public LoginFragment() {
        // Required empty public constructor
    }

    public static LoginFragment newInstance() {
        return new LoginFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_login, container, false);
        usernameInput = view.findViewById(R.id.usernameInput);
        passwordInput = view.findViewById(R.id.passwordInput);
        loginButton = view.findViewById(R.id.loginButton);
        registerButton = view.findViewById(R.id.registerButton);
        loginButton.setOnClickListener(this::loginClicked);
        registerButton.setOnClickListener(this::registerClicked);
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = (MainActivity) context;
    }

    @Override
    public void onResume() {
        super.onResume();
        context.topAppBar.setTitle(R.string.login);
        context.setShowNavigationIcon(false);
        context.clearTopAppBar();
    }

    @Override
    public void onClick(View view) {

    }

    public void registerClicked(View view) {
        String username = null;
        if (usernameInput.length() != 0)
            username = Objects.requireNonNull(usernameInput.getText()).toString();
        context.getSupportFragmentManager().beginTransaction()
                .addToBackStack("loginFragment")
                .replace(R.id.mainFrame, RegisterFragment.newInstance(username), "registerFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void loginClicked(View view) {
        //TODO: completed, and remove line before function
        User.setCurrentUser(new User(1, "Taha Jahani",
                "Taha7900", Date.from(Instant.now()), "09367642209"));
        context.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrame, HomeFragment.newInstance(), "homeFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }
}