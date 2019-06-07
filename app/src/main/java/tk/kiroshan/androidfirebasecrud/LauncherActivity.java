package tk.kiroshan.androidfirebasecrud;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.content.Intent;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LauncherActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    ImageView launcherImage;
    Animation anim;
    View customPos;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            Thread thread = new Thread() {
                @Override
                public void run() {
                    FirebaseUser currentUser = mAuth.getCurrentUser();
                    if(currentUser!=null){
                        Intent intent = new Intent(LauncherActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("uid",currentUser.getUid());

                        startActivity(intent);
                        finish();
                    }
                    else{
                        Intent intent = new Intent(LauncherActivity.this, SignUpActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                    }

                }
            };thread.start();
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_launcher);
        Objects.requireNonNull(getSupportActionBar()).hide();

        customPos = (View) findViewById(R.id.customSnac);

        launcherImage = (ImageView) findViewById(R.id.launcher);
        anim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.jump_from_down);
        launcherImage.startAnimation(anim);

        mAuth= FirebaseAuth.getInstance();

        handler.postDelayed(runnable, 3000);

    }

    private Boolean exit = false;
    @Override
    public void onBackPressed() {
        if (exit) {
            finish(); // finish activity
            System.exit(0);

        } else {
            snacBar("Press Back again to Exit.", R.color.colorPrimary, android.R.color.white);
            exit = true;
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    exit = false;
                }
            }, 3 * 1000);

        }
    }

    public void snacBar(String text, int bColor, int tColor) {
        final Snackbar snac = Snackbar.make(customPos, text, Snackbar.LENGTH_LONG);
        snac.getView().setBackgroundColor(ContextCompat.getColor(getApplicationContext(), bColor));
        snac.setActionTextColor(getResources().getColor(tColor));
        View view = snac.getView();
        TextView setTextGravity = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        setTextGravity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snac.show();
    }

}
