package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class RegisterFragment extends Fragment implements View.OnClickListener {

    private static final String ARG_USERNAME = "USERNAME";
    private String username;
    private MainActivity context;

    private TextInputEditText usernameInput;
    private TextInputEditText passwordInput;
    private TextInputEditText confirmPasswordInput;
    private TextInputEditText nameInput;
    private TextInputEditText birthdayInput;
    private TextInputEditText emailInput;
    private TextInputEditText phoneNumberInput;
    private MaterialButton registerButton;

    public RegisterFragment() {
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
    }

    @Override
    public void onClick(View view) {

    }

    public void registerClicked(View view) {
        if (isInputValid()) {
            //TODO: complete
            context.getSupportFragmentManager().popBackStackImmediate();
        }
    }

    private boolean isInputValid() {
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
        if (!passwordInput.getText().toString().equals(confirmPasswordInput.getText().toString())){
            passwordInput.setError(getString(R.string.passwords_do_not_match));
            return false;
        }
        return true;
    }

    private void setError(TextInputEditText editText) {
        editText.setError(getString(R.string.field_is_required));
    }
}