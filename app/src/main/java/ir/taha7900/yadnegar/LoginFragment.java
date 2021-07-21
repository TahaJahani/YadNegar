package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Objects;

import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;

public class LoginFragment extends Fragment {

    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private MainActivity context;

    private final Handler handler;

    public LoginFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.LOGIN_SUCCESSFUL:
                        context.showLoading(false);
                        loginSuccessful();
                        break;
                    case MsgCode.LOGIN_FAILED:
                        context.showLoading(false);
                        if (msg.obj == null){
                            msg.obj = getString(R.string.login_failed);
                        }
                        loginFailed(msg.obj);
                        break;
                }
            }
        };
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
        MaterialButton loginButton = view.findViewById(R.id.loginButton);
        MaterialButton registerButton = view.findViewById(R.id.registerButton);
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


    public void loginClicked(View view) {
        context.hideKeyboard();
        if (usernameInput.getText() == null) {
            usernameInput.setError(getString(R.string.this_field_is_required));
            return;
        }
        if (passwordInput.getText() == null){
            passwordInput.setError(getString(R.string.this_field_is_required));
            return;
        }
        String username = usernameInput.getText().toString();
        String password = passwordInput.getText().toString();
        context.showLoading(true);
        Network.sendLoginRequest(username, password, handler);
    }

    private void loginSuccessful() {
        context.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrame, HomeFragment.newInstance(), "homeFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void loginFailed(Object error) {
        Snackbar.make(Objects.requireNonNull(getView()), String.valueOf(error), Snackbar.LENGTH_LONG).show();
    }
}