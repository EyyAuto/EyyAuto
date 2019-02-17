package in.ac.nitc.eyyauto;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;

import in.ac.nitc.eyyauto.handlers.Event;
import in.ac.nitc.eyyauto.handlers.UserHandler;
import in.ac.nitc.eyyauto.models.User;

public class GetName extends AppCompatActivity {

    private FirebaseUser mUser;
    private static UserHandler mUserHandler = new UserHandler();

    private Button mConfirm;
    private EditText mNameField;

    private String mUserId;
    private String mName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_name);

        mUser = FirebaseAuth.getInstance().getCurrentUser();
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
        }
    }

    private void populateFields() {
        mUserId = mUser.getUid();
        mUserHandler.readOnce(mUserId, new Event<User>() {
            @Override
            public void onReceive(User data) {
                if (data != null) {
                    mNameField.setText(data.getName());
                }
            }

            @Override
            public void onFailed(DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInformation() {
        mName = mNameField.getText().toString();

        if (mName.isEmpty()) {
            Toast.makeText(GetName.this, R.string.registration_error, Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(GetName.this, "Registered successfully", Toast.LENGTH_SHORT).show();
    }
}

