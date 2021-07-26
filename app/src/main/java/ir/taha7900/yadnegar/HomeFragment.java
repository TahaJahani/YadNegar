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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import ir.taha7900.yadnegar.Adapters.MemoryAdapter;
import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.Utils.MsgCode;
import ir.taha7900.yadnegar.Utils.Network;


public class HomeFragment extends Fragment {

    private MainActivity context;
    private RecyclerView memoriesList;
    private FloatingActionButton addMemoryButton;
    private MemoryAdapter adapter;
    private ArrayList<Memory> memories;

    private Handler handler;

    public HomeFragment() {
        handler = new Handler(Looper.getMainLooper()) {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case MsgCode.MEMORY_DATA_READY:
                        memories = (ArrayList<Memory>) msg.obj;
                        showTopMemories();
                        break;
                    case MsgCode.MEMORY_ERROR:
                        //TODO: show error
                }
            }
        };
    }

    private void showTopMemories() {
        adapter = new MemoryAdapter(memories);
        memoriesList.setLayoutManager(new LinearLayoutManager(context));
        memoriesList.setAdapter(adapter);
        context.showLoading(false);
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context.showLoading(true);
        Network.getTopMemories(handler);
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
        name.setText(current.getFirst_name());
        username.setText(current.getUsername());
        if (adapter != null)
            adapter.notifyDataSetChanged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        memoriesList = view.findViewById(R.id.memoriesList);
        addMemoryButton = view.findViewById(R.id.addMemoryButton);
        addMemoryButton.setOnClickListener(this::openAddMemoryFragment);
        if (memories != null)
            showTopMemories();
        return view;
    }

    private void openAddMemoryFragment(View view) {
        context.getSupportFragmentManager().beginTransaction()
                .addToBackStack("addMemory")
                .replace(R.id.mainFrame, AddMemoryFragment.newInstance(null , HomeFragment.this))
                .commit();
    }
}