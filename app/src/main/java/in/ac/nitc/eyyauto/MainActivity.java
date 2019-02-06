package in.ac.nitc.eyyauto;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.ErrorCodes;
import com.firebase.ui.auth.IdpResponse;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    private static int RC_SIGN_IN = 123;

    private FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (isPhoneRegistered()) {
            Intent intent = new Intent(this, GetName.class);
            startActivity(intent);
            finish();
        }
        else {
            signIn();
        }
    }

    private boolean isPhoneRegistered() {
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if (mUser != null) {
            if(mUser.getPhoneNumber() != null) {
                return true;
            } else {
                return false;
            }
        }
        return false;
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
                Intent intent = new Intent(this, GetName.class);
                startActivity(intent);
                finish();
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
}
