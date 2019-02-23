package in.ac.nitc.eyyauto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import java.util.Arrays;

import in.ac.nitc.eyyauto.handlers.Event;
import in.ac.nitc.eyyauto.handlers.UserHandler;
import in.ac.nitc.eyyauto.models.User;

import static in.ac.nitc.eyyauto.Constants.INTENT_HAS_PHONE_NUMBER;
import static in.ac.nitc.eyyauto.Constants.INTENT_USER;

public class MainActivity extends AppCompatActivity {

    private static int RC_SIGN_IN = 123;
    private FirebaseUser mUser;
    private UserHandler mUserHandler;

    private Button mConfirm;
    private EditText mNameField;
    private String mUserId;
    private Boolean hasPhoneNumber;
    private User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set view according to need
        hasPhoneNumber = getIntent().getBooleanExtra(INTENT_HAS_PHONE_NUMBER, false);
        mUserHandler = new UserHandler();
        if(!hasPhoneNumber) {
            setContentView(R.layout.activity_main);
            signIn();
        } else {
            setDetailsView();
        }
    }

    private void signIn() {
        //TODO: Custom theme for FirebaseUI here
        startActivityForResult(
                AuthUI.getInstance()
                        .createSignInIntentBuilder()
                        .setAvailableProviders(Arrays.asList(
                                new AuthUI.IdpConfig.PhoneBuilder().build()
                        )).build(),
                RC_SIGN_IN
        );
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == RC_SIGN_IN) {
            IdpResponse response = IdpResponse.fromResultIntent(data);

            if (resultCode == RESULT_OK) {
                setDetailsView();
            } else {
                if (response == null) {
                    Toast.makeText(this, "Sign in Cancelled", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (response.getError().getErrorCode() == ErrorCodes.NO_NETWORK) {
                    Toast.makeText(this, "Network error", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "Error trying to Sign in", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setDetailsView() {
        setContentView(R.layout.personal_details);
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        mUserId = mUser.getUid();
        mConfirm = findViewById(R.id.confirm);
        mNameField = findViewById(R.id.name);
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
        user = new User(mName, mUser.getPhoneNumber());
        mUserHandler.putValue(mUserId, user);
        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
        // TODO: switch to maps activity here
        finish();
    }
}
