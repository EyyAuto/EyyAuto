package in.ac.nitc.eyyauto;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;

import in.ac.nitc.eyyauto.handlers.Event;
import in.ac.nitc.eyyauto.handlers.UserHandler;
import in.ac.nitc.eyyauto.models.User;

public class SplashActivity extends AppCompatActivity {

    private FirebaseUser mUser;
    String mUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Remove the Title Bar
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        redirectUser();
    }

    private void redirectUser() {
        if (mUser == null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            mUserId = mUser.getUid();
            new UserHandler().readOnce(mUserId, new Event<User>() {
                @Override
                public void onReceive(User data) {
                    if (data != null) {
                        Toast.makeText(SplashActivity.this, "Registration was already done", Toast.LENGTH_LONG).show();
                    } else {
                        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                        intent.putExtra("hasPhoneNumber", true);
                        intent.putExtra("userObject", data);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onFailed(DatabaseError databaseError) {

                }
            });
        }
    }
}
