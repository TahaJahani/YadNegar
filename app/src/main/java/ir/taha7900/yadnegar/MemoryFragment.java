package ir.taha7900.yadnegar;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MemoryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MemoryFragment extends Fragment implements Toolbar.OnMenuItemClickListener, DialogInterface.OnClickListener {

    private static final String ARG_MEMORY = "memory";

    private Memory memory;
    private MainActivity context;

    public MemoryFragment() {
        // Required empty public constructor
    }

    public static MemoryFragment newInstance() {
        return new MemoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_memory, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        context.topAppBar.setTitle(getString(R.string.memory));
//        if (memory.getUserId() == User.getCurrentUser().getId()) {
//            context.topAppBar.inflateMenu(R.menu.memory_top_menu);
//            context.topAppBar.setOnMenuItemClickListener(this);
//        }
    }

    @Override
    public boolean onMenuItemClick(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.edit) {
            return true;
        }else if (id == R.id.delete) {
            showDeleteAlert();
            return true;
        }
        return false;
    }

    private void showDeleteAlert() {
        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(context);
        builder.setTitle(getString(R.string.alert))
                .setMessage(getString(R.string.delete_message))
                .setPositiveButton(R.string.yes, this)
                .setNegativeButton(R.string.no, this)
                .show();
    }

    @Override
    public void onClick(DialogInterface dialogInterface, int i) {
        if (i == DialogInterface.BUTTON_POSITIVE) {
            //TODO: delete memory
            context.getSupportFragmentManager().popBackStack();
        }
        dialogInterface.dismiss();
    }
}