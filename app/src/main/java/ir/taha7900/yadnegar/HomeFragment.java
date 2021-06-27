package ir.taha7900.yadnegar;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import ir.taha7900.yadnegar.Models.User;


public class HomeFragment extends Fragment implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private MainActivity context;
    private NavigationView sideNavDrawer;
    private MaterialToolbar topAppBar;
    private DrawerLayout drawerLayout;
    private View drawerHeader;

    public HomeFragment() {
        // Required empty public constructor
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
        ((MainActivity) context).getSupportActionBar().hide();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        topAppBar = view.findViewById(R.id.topAppBar);
        sideNavDrawer = view.findViewById(R.id.sideNavDrawer);
        drawerLayout = view.findViewById(R.id.drawerLayout);
        drawerHeader = sideNavDrawer.getHeaderView(0);
        topAppBar.setNavigationOnClickListener(this);
        sideNavDrawer.setNavigationItemSelectedListener(this);
        drawerHeader.setOnClickListener(this::profileClicked);
        return view;
    }

    @Override
    public void onClick(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void profileClicked(View view) {
        Toast.makeText(context, "Profile Clicked!", Toast.LENGTH_SHORT).show();
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int clickedId = item.getItemId();
        if (clickedId == R.id.home)
            homeClicked();
        else if (clickedId == R.id.memories)
            memoriesClicked();
        else if (clickedId == R.id.setting)
            settingClicked();
        else if (clickedId == R.id.logout)
            logoutClicked();
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logoutClicked() {
        User.logoutCurrentUser();
        context.getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFrame, LoginFragment.newInstance(), "loginFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_CLOSE)
                .commit();
    }

    private void homeClicked() {

    }

    private void memoriesClicked() {

    }

    private void settingClicked() {

    }
}