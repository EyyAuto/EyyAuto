package in.ac.nitc.eyyauto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import in.ac.nitc.eyyauto.handlers.UserHandler;
import in.ac.nitc.eyyauto.models.User;

import static in.ac.nitc.eyyauto.Constants.INTENT_USER;

public class ProfileActivity extends AppCompatActivity {


    private static final String TAG = "ProfileActivity";
    private UserHandler mUserHandler;
    private User user;

    private FirebaseUser mUser;

    private Button mConfirm;
    private EditText mNameField;
    private String mUserId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        user = (User) getIntent().getExtras().get(INTENT_USER);
        mUserHandler = new UserHandler();
        setDetailsView();
    }

    private void setDetailsView() {
        setContentView(R.layout.activity_profile);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser.getUid();
        mConfirm = findViewById(R.id.confirmUpdate);
        mNameField = findViewById(R.id.nameUpdate);
        mNameField.setText(user.getName());
        mConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveUserInformation();
            }
        });
    }

    private void saveUserInformation() {
        String mName = mNameField.getText().toString();
        if (mName.isEmpty()) {
            Toast.makeText(this, R.string.registration_error, Toast.LENGTH_SHORT).show();
            return;
        }
        user.setName(mName);
        mUserHandler.putValue(mUserId, user);
        Toast.makeText(this, "Updated profile successfully", Toast.LENGTH_SHORT).show();
        // switch to maps activity here
        Intent i = new Intent(ProfileActivity.this, MapActivity.class);
        i.putExtra(INTENT_USER,user);
        startActivity(i);
        finish();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent i = new Intent(ProfileActivity.this, MapActivity.class);
                i.putExtra(INTENT_USER,user);
                startActivity(i);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
