package ir.taha7900.yadnegar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import ir.taha7900.yadnegar.Models.User;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout mainFrame;
    public DrawerLayout drawerLayout;
    public MaterialToolbar topAppBar;
    public NavigationView sideDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_main);
        mainFrame = findViewById(R.id.mainFrame);
        drawerLayout = findViewById(R.id.drawerLayout);
        topAppBar = findViewById(R.id.topAppBar);
        sideDrawer = findViewById(R.id.sideNavDrawer);

        View drawerHeader = sideDrawer.getHeaderView(0);
        topAppBar.setNavigationOnClickListener(this);
        sideDrawer.setNavigationItemSelectedListener(this);
        drawerHeader.setOnClickListener(this::profileClicked);

        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainFrame, LoginFragment.newInstance(), "loginFragment")
                .commit();
        setShowNavigationIcon(false);
        topAppBar.setTitle(R.string.login);
    }

    public void setShowNavigationIcon(boolean show) {
        if (show) {
            topAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else {
            topAppBar.setNavigationIcon(null);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }

    }

    @Override
    public void onClick(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void profileClicked(View view) {
        Toast.makeText(this, "Profile Clicked!", Toast.LENGTH_SHORT).show();
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
        getSupportFragmentManager()
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