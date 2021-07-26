package ir.taha7900.yadnegar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.navigation.NavigationView;

import java.util.Objects;

import ir.taha7900.yadnegar.Models.Memory;
import ir.taha7900.yadnegar.Models.User;
import ir.taha7900.yadnegar.Utils.AndroidUtilities;
import ir.taha7900.yadnegar.Utils.Network;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, NavigationView.OnNavigationItemSelectedListener {

    private FrameLayout mainFrame;
    public DrawerLayout drawerLayout;
    private ProgressBar progressBar;
    public MaterialToolbar topAppBar;
    public NavigationView sideDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        AndroidUtilities.setDensityFromContext(this);
        super.onCreate(savedInstanceState);
        Objects.requireNonNull(getSupportActionBar()).hide();
        setContentView(R.layout.activity_main);
        mainFrame = findViewById(R.id.mainFrame);
        progressBar = findViewById(R.id.progressBar);
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
        topAppBar.setTitleTextColor(Color.WHITE);
        topAppBar.setBackgroundColor(Color.rgb(231, 133, 54));
        topAppBar.setTitle(R.string.login);
    }

    public void setShowNavigationIcon(boolean show) {
        if (show) {
            topAppBar.setNavigationIcon(R.drawable.ic_baseline_menu_24);
            topAppBar.setNavigationOnClickListener(this);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }else {
            topAppBar.setNavigationIcon(null);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    public void setShowBackIcon(boolean show) {
        if (show) {
            topAppBar.setNavigationIcon(R.drawable.ic_baseline_arrow_back_ios_24);
            drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
            topAppBar.setNavigationOnClickListener(view -> {
                getSupportFragmentManager().popBackStack();
                hideKeyboard();
            });
        }
    }


    public void clearTopAppBar() {
        if (topAppBar.getMenu() != null) {
            topAppBar.getMenu().clear();
            topAppBar.setOnMenuItemClickListener(null);
        }
    }

    @Override
    public void onClick(View view) {
        drawerLayout.openDrawer(GravityCompat.START);
    }

    public void profileClicked(View view) {
        Network.logout();
        getSupportFragmentManager().beginTransaction()
                .addToBackStack("profileFragment")
                .replace(R.id.mainFrame, ProfileFragment.newInstance(), "profileFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).commit();
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
        else if (clickedId == R.id.tags) {
            tagsClicked();
        }
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
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.mainFrame, HomeFragment.newInstance(), "homeFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    private void memoriesClicked() {

    }

    private void settingClicked() {

    }

    private void tagsClicked() {
        getSupportFragmentManager()
                .beginTransaction()
                .addToBackStack(null)
                .replace(R.id.mainFrame, TagsFragment.newInstance(), "tagFragment")
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .commit();
    }

    public void showLoading(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
        progressBar.bringToFront();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Activity.INPUT_METHOD_SERVICE);
        View view = getCurrentFocus();
        if (view == null) {
            view = new View(this);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

}