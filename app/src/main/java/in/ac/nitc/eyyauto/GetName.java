package in.ac.nitc.eyyauto;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetName extends AppCompatActivity {

    private FirebaseUser mUser;
    private DatabaseReference mRef;

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
        mRef = FirebaseDatabase.getInstance().getReference().child("UserInfo").child(mUserId).child("Name");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    mNameField.setText(dataSnapshot.getValue().toString());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void saveUserInformation() {
        mName = mNameField.getText().toString();

        if (mName.isEmpty()) {
            Toast.makeText(GetName.this, R.string.registration_error, Toast.LENGTH_SHORT).show();
            return;
        }

        mRef.setValue(mName);

        Toast.makeText(GetName.this, "Registered successfully", Toast.LENGTH_SHORT).show();
    }
}

