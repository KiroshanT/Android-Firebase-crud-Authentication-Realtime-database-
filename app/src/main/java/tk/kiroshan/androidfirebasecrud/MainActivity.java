package tk.kiroshan.androidfirebasecrud;

import android.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private TextView mTextMessage;
    FragmentManager fragmentManager = getFragmentManager();
    ProfileFragment profileFragment = new ProfileFragment();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.profile:
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, profileFragment).commit();
                    //this.setTitle(Html.fromHtml("<small>Profile</small>"));
                    return true;
                case R.id.settings:
                    fragmentManager.beginTransaction().replace(R.id.mainFrame, new SettingsFragment()).commit();
                    //this.setTitle(Html.fromHtml("<small>Profile</small>"));
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        String uid = getIntent().getExtras().getString("uid");
        System.out.println(uid+" Hello");

        Bundle bundle = new Bundle();

        bundle.putString("uid", uid);

        profileFragment.setArguments(bundle);

        Objects.requireNonNull(getSupportActionBar()).hide();
        BottomNavigationView navView = findViewById(R.id.nav_view);
        mTextMessage = findViewById(R.id.message);
        navView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragmentManager.beginTransaction().replace(R.id.mainFrame, profileFragment).commit();
    }

}
