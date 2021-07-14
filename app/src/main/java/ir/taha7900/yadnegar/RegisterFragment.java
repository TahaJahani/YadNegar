package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import static ir.taha7900.yadnegar.Utils.MsgCode.*;

import java.util.ArrayList;
import java.util.HashMap;

import ir.taha7900.yadnegar.Utils.Network;

public class RegisterFragment extends Fragment {

    private static final String ARG_USERNAME = "USERNAME";
    private String username;
    private MainActivity context;

    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private TextInputEditText nameInput;
    private TextInputEditText surnameInput;
    private TextInputEditText birthdayInput;
    private TextInputEditText emailInput;
    private TextInputEditText phoneNumberInput;
    private MaterialButton registerButton;

    private Handler handler;

    public RegisterFragment() {
        this.handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case NETWORK_ERROR:
                        context.showLoading(false);
                        showNetworkError();
                        break;
                    case REGISTER_ERROR:
                        context.showLoading(false);
                        showRegisterError((String)msg.obj);
                        break;
                    case REGISTER_SUCCESSFUL:
                        context.showLoading(false);
                        login();
                        break;
                }
            }
        };
    }

    private void login() {
        context.getSupportFragmentManager().beginTransaction()
                .replace(R.id.mainFrame, HomeFragment.newInstance(), "homeFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
//        context.getSupportFragmentManager().popBackStackImmediate();
    }


    public static RegisterFragment newInstance(String username) {
        RegisterFragment fragment = new RegisterFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USERNAME, username);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            username = getArguments().getString(ARG_USERNAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_register, container, false);
        usernameInput = view.findViewById(R.id.usernameInput);
        if (username != null)
            usernameInput.setText(username);
        passwordInput = view.findViewById(R.id.passwordInput);
        confirmPasswordInput = view.findViewById(R.id.confirmPasswordInput);
        nameInput = view.findViewById(R.id.nameInput);
        surnameInput = view.findViewById(R.id.surnameInput);
        birthdayInput = view.findViewById(R.id.birthdayInput);
        emailInput = view.findViewById(R.id.emailInput);
        phoneNumberInput = view.findViewById(R.id.phoneNumberInput);
        registerButton = view.findViewById(R.id.registerButton);
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
        context.topAppBar.setTitle(R.string.register);
        context.setShowNavigationIcon(false);
        context.clearTopAppBar();
    }

    public void registerClicked(View view) {
        context.hideKeyboard();
        if (isInputValid()) {
            context.showLoading(true);
            Network.sendRegisterRequest(collectData(), handler);
        }
    }

    private HashMap<String, String> collectData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", usernameInput.getText().toString());
        data.put("password", passwordInput.getText().toString());
        data.put("first_name", nameInput.getText().toString());
        data.put("last_name", surnameInput.getText().toString());
        if (birthdayInput.getText() != null)
            data.put("birthday_date", birthdayInput.getText().toString());
        data.put("password", passwordInput.getText().toString());
        data.put("phone_number", phoneNumberInput.getText().toString());
        data.put("email", emailInput.getText().toString());
        return data;
    }

    private boolean isInputValid() {
        if (nameInput.length() == 0){
            setError(nameInput);
            return false;
        }
        if (surnameInput.length() == 0){
            setError(surnameInput);
            return false;
        }
        if (usernameInput.length() == 0) {
            setError(usernameInput);
            return false;
        }
        if (passwordInput.length() == 0) {
            setError(passwordInput);
            return false;
        }
        if (confirmPasswordInput.length() == 0) {
            setError(confirmPasswordInput);
            return false;
        }
        if (emailInput.length() == 0) {
            setError(emailInput);
            return false;
        }
        if (phoneNumberInput.length() != 11) {
            setError(phoneNumberInput);
            return false;
        }
        if (!passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString())) {
            passwordInput.setError(getString(R.string.passwords_do_not_match));
            return false;
        }
        return true;
    }

    private void setError(TextInputEditText editText) {
        editText.setError(getString(R.string.field_is_required));
    }

    private void showNetworkError() {
        Snackbar.make(getView(), R.string.connection_error, Snackbar.LENGTH_LONG).show();
    }

    private void showRegisterError(String error) {
        Snackbar.make(getView(), error, Snackbar.LENGTH_LONG).show();
    }
}