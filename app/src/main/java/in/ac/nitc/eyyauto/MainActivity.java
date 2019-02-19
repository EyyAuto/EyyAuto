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

import java.util.Arrays;

import in.ac.nitc.eyyauto.handlers.UserHandler;
import in.ac.nitc.eyyauto.models.User;

public class MainActivity extends AppCompatActivity {

    private static int RC_SIGN_IN = 123;
    private FirebaseUser mUser;
    private UserHandler mUserHandler;

    private Button mConfirm;
    private EditText mNameField;
    private String mUserId;
    private String mName;
    private Boolean hasPhoneNumber;
    private User mUserObject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // set view according to need
        hasPhoneNumber = getIntent().getBooleanExtra("hasPhoneNumber", false);
        mUserObject = (User)getIntent().getSerializableExtra("userObject");
        if(hasPhoneNumber == false) {
            setContentView(R.layout.activity_main);
            signIn();
        } else {
            setDetailsView();
        }
    }

    private void signIn() {
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
        mUserHandler = new UserHandler();
        mConfirm = findViewById(R.id.confirm);
        mNameField = findViewById(R.id.name);

        if (mUser != null) {
            populateFields();
            mConfirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    saveUserInformation();
                }
            });
        } else {
            Log.d("NULL_error", "mUser is null in details page.");
        }
    }

    private void populateFields() {
        mUserId = mUser.getUid();
        mNameField.setText(mUserObject.getName());
    }

    private void saveUserInformation() {
        mName = mNameField.getText().toString();

        if (mName.isEmpty()) {
            Toast.makeText(this, R.string.registration_error, Toast.LENGTH_SHORT).show();
            return;
        }
        mUserHandler.putValue(mUserId, new User(mName, mUser.getPhoneNumber()));

        Toast.makeText(this, "Registered successfully", Toast.LENGTH_SHORT).show();
    }

}
