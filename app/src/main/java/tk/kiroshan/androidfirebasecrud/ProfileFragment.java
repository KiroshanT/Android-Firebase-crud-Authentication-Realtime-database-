package tk.kiroshan.androidfirebasecrud;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    View myView, customPos;
    TextView username, email;
    EditText address, about;
    Button button;
    private FirebaseAuth mAuth;
    private DatabaseReference dR;
    private StorageReference storageReference;
    private FirebaseStorage storage;
    String _usermname, _email;
    private ProgressDialog progressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final String uid = getArguments().getString("uid");
        myView = inflater.inflate(R.layout.profile_layout, container, false);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
//hh
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        customPos = (View) myView.findViewById(R.id.customSnac);
        username = (TextView) myView.findViewById(R.id.username);
        email = (TextView) myView.findViewById(R.id.email);
        address = (EditText) myView.findViewById(R.id.address);
        about = (EditText) myView.findViewById(R.id.about);
        button = (Button) myView.findViewById(R.id.btn_update);

        mAuth = FirebaseAuth.getInstance();

        dR = FirebaseDatabase.getInstance().getReference("User");
        final Query query = dR.orderByChild("uid").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for (DataSnapshot userSnapshot: dataSnapshot.getChildren()) {
                    User user = userSnapshot.getValue(User.class);
                    assert user != null;
                    System.out.println(" About==" + user.getAbout());
                    username.setText(user.getUsername());
                    email.setText(user.getEmail());
                    address.setText(user.getAddress());
                    about.setText(user.getAbout());
                    _usermname = user.getUsername();
                    _email = user.getEmail();
                }
                progressDialog.dismiss();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                            getActivity().getApplicationContext().INPUT_METHOD_SERVICE);

                    imm.hideSoftInputFromWindow(Objects.requireNonNull(getActivity().getCurrentFocus()).getWindowToken(), 0);

                }catch(Exception e){
                    e.printStackTrace();
                }

                progressDialog.show();
                String _address = address.getText().toString();
                String _about = about.getText().toString();

                assert uid != null;
                dR = FirebaseDatabase.getInstance().getReference("User").child(uid);
                User user = new User(uid, _usermname, _email, _address, _about);
                dR.setValue(user);
                storageReference = storage.getReference();
                progressDialog.dismiss();
                snacBar("User details Updated!", R.color.colorPrimary, android.R.color.white);
            }
        });

        return myView;
    }

    public void snacBar(String text, int bColor, int tColor) {
        final Snackbar snac = Snackbar.make(customPos, text, Snackbar.LENGTH_LONG);
        snac.getView().setBackgroundColor(ContextCompat.getColor(getActivity(), bColor));
        snac.setActionTextColor(getResources().getColor(tColor));
        View view = snac.getView();
        TextView setTextGravity = (TextView) view.findViewById(android.support.design.R.id.snackbar_text);
        setTextGravity.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
        snac.show();
    }

}
