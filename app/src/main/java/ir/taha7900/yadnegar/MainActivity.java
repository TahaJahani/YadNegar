package ir.taha7900.yadnegar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private FrameLayout mainFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mainFrame = findViewById(R.id.mainFrame);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.mainFrame, LoginFragment.newInstance(), "loginFragment")
                .commit();
    }
}