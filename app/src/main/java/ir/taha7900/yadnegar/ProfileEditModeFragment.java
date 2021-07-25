package ir.taha7900.yadnegar;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.transition.ChangeBounds;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.HashMap;

import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;

public class ProfileEditModeFragment extends Fragment implements Toolbar.OnMenuItemClickListener, android.widget.Toolbar.OnMenuItemClickListener {

    private TextInputEditText usernameInput;
    private TextInputEditText nameInput;
    private TextInputEditText emailInput;
    private TextInputEditText birthdayInput;
    private TextInputEditText phoneNumberInput;
    private String birthday;

    private MainActivity context;
    private Handler handler;

    public ProfileEditModeFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.EDIT_USER_SUCCESSFUL:
                        context.showLoading(false);
                        context.getSupportFragmentManager().popBackStack();
                }
            }
        };
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
        View view = inflater.inflate(R.layout.fragment_profile_edit_mode, container, false);
        usernameInput = view.findViewById(R.id.usernameInput);
        nameInput = view.findViewById(R.id.nameInput);
        emailInput = view.findViewById(R.id.emailInput);
        birthdayInput = view.findViewById(R.id.birthdayInput);
        phoneNumberInput = view.findViewById(R.id.phoneNumberInput);
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
        context.clearTopAppBar();
        context.topAppBar.setTitle(getString(R.string.edit_profile));
        context.setShowNavigationIcon(false);
        context.setShowBackIcon(true);
        context.topAppBar.inflateMenu(R.menu.profile_edit_top_menu);
        context.topAppBar.setOnMenuItemClickListener(this);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        User current = User.getCurrentUser();
        usernameInput.setText(current.getUsername());
        nameInput.setText(current.getFullName());
        emailInput.setText(current.getEmail());
        birthdayInput.setText(current.getFormattedBirthday());
        phoneNumberInput.setText(current.getPhone_number());
        birthday = current.getFormattedBirthday();

        DatePickerDialog datePickerDialog = new DatePickerDialog(context, (datePicker, i, i1, i2) -> {
            birthday = i + "-" + i1 + "-" + i2;
            birthdayInput.setText(birthday);
        }, 1900, 1, 1);
        TextInputLayout birthdayContainer = view.findViewById(R.id.birthdayContainer);
        birthdayInput.setOnClickListener(view1 -> datePickerDialog.show());
        birthdayContainer.setOnClickListener(view1 -> datePickerDialog.show());
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.submit && checkData()) {
            Network.editUser(handler, extractData());
            context.showLoading(true);
            return true;
        }
        return false;
    }

    private HashMap<String, String> extractData() {
        HashMap<String, String> data = new HashMap<>();
        data.put("username", usernameInput.getText().toString());
        data.put("name", nameInput.getText().toString());
        data.put("email", emailInput.getText().toString());
        data.put("phone_number", phoneNumberInput.getText().toString());
        data.put("birthday_date", birthday);
        System.out.println(birthday);
        return data;
    }

    private boolean checkData() {
        if (usernameInput.getText() == null || usernameInput.length() == 0){
            usernameInput.setError(getString(R.string.this_field_is_required));
            return false;
        }
        if (nameInput.getText() == null || nameInput.length() == 0){
            nameInput.setError(getString(R.string.this_field_is_required));
            return false;
        }
        if (emailInput.getText() == null || emailInput.length() == 0){
            emailInput.setError(getString(R.string.this_field_is_required));
            return false;
        }
        if (birthdayInput.getText() == null || birthdayInput.length() == 0){
            birthdayInput.setError(getString(R.string.this_field_is_required));
            return false;
        }
        if (phoneNumberInput.getText() == null || phoneNumberInput.length() == 0){
            phoneNumberInput.setError(getString(R.string.this_field_is_required));
            return false;
        }
        return true;
    }
}