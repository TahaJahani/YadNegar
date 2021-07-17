package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.Utils.Network;


public class HomeFragment extends Fragment {

    private MainActivity context;

    private Handler handler;

    public HomeFragment() {
        // Required empty public constructor
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    // todo
                }
            }
        };
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
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
        context.topAppBar.setTitle(R.string.home);
        context.setShowNavigationIcon(true);
        context.clearTopAppBar();

        View header = context.sideDrawer.getHeaderView(0);
        TextView name = header.findViewById(R.id.nameText);
        TextView username = header.findViewById(R.id.usernameText);
        User current = User.getCurrentUser();
        //TODO: create string resource
        name.setText(current.getFirst_name() + " " +current.getLast_name());
        username.setText(current.getUsername());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home, container, false);
    }
}