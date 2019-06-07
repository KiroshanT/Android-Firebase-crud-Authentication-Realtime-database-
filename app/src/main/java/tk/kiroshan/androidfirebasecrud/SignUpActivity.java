package tk.kiroshan.androidfirebasecrud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;


public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private DatabaseReference dR;

    private View customPos;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_sign_up);
        Objects.requireNonNull(getSupportActionBar()).hide();

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        dR = FirebaseDatabase.getInstance().getReference("User");

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


    public void LogIn(View view) {
        Intent intent = new Intent(SignUpActivity.this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    public void SignUp(View view) {
        try {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            assert imm != null;
            imm.hideSoftInputFromWindow(Objects.requireNonNull(getCurrentFocus()).getWindowToken(), 0);
        } catch (Exception ignored) { }

        final String username = ((EditText)findViewById(R.id.username)).getText().toString();
        final String password = ((EditText)findViewById(R.id.password)).getText().toString();
        final String c_password = ((EditText)findViewById(R.id.c_password)).getText().toString();
        final String email = ((EditText)findViewById(R.id.email)).getText().toString();

        if (TextUtils.isEmpty(username)) {
            snacBar("Enter the username!", R.color.colorPrimary, android.R.color.white);

        } else if (TextUtils.isEmpty(password)) {
            snacBar("Enter the Password!", R.color.colorPrimary, android.R.color.white);

        } else if (TextUtils.isEmpty(c_password)) {
            snacBar("Enter the confirm Password!", R.color.colorPrimary, android.R.color.white);

        } else if (!password.equals(c_password)) {
            snacBar("Confirm password not matched!", R.color.colorPrimary, android.R.color.white);

        } else if (TextUtils.isEmpty(email)) {
            snacBar("Enter your Email ID!", R.color.colorPrimary, android.R.color.white);

        } else {
            progressDialog.show();
            final AuthResult[] result= new AuthResult[1];
            mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()){
                        FirebaseUser currentUser = mAuth.getCurrentUser();
                        final String uid = currentUser.getUid();
                        final Query query = dR.orderByChild("uid").equalTo(uid);
                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot data : dataSnapshot.getChildren()){
                                    query.removeEventListener(this);
                                    return;
                                }
                                query.removeEventListener(this);
                                dR = FirebaseDatabase.getInstance().getReference("User").child(uid);
                                User user = new User(uid, username, email, "Address", "About");
                                dR.setValue(user);
                                storageReference = storage.getReference();
                                progressDialog.dismiss();
                                Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                intent.putExtra("uid",uid);

                                startActivity(intent);
                                finish();

                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });


                    } else {
                        progressDialog.dismiss();
                        snacBar("Unsuccessfull!", R.color.colorPrimary, android.R.color.white);
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
