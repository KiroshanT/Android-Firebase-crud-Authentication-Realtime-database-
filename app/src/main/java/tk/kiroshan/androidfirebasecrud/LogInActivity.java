package tk.kiroshan.androidfirebasecrud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.content.ContextCompat;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.inputmethod.InputMethodManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LogInActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    View customPos;
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_log_in);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth= FirebaseAuth.getInstance();

        customPos = (View) findViewById(R.id.customSnac);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);

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

    public void Register(View view) {
        Intent intent = new Intent(LogInActivity.this, SignUpActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void SignIn(View view) {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception ignored) { }

        String email=((EditText)findViewById(R.id.email)).getText().toString();
        String pass=((EditText)findViewById(R.id.password)).getText().toString();

        if (TextUtils.isEmpty(email)) {
            snacBar("Enter your email ID!", R.color.colorPrimary, android.R.color.white);

        } else if (TextUtils.isEmpty(pass)) {
            snacBar("Enter your Password!", R.color.colorPrimary, android.R.color.white);

        } else {
            progressDialog.show();
            mAuth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        final String uid = currentUser.getUid();
                        progressDialog.dismiss();
                        Intent intent = new Intent(LogInActivity.this, MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        intent.putExtra("uid",uid);
                        startActivity(intent);
                        finish();
                    }
                    else{
                        progressDialog.dismiss();
                        snacBar("Enter email and password correctly!", R.color.colorPrimary, android.R.color.white);
                    }
                }
            });
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
