package in.ac.nitc.eyyauto;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
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

    private DrawerLayout drawerLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppThemeDark);
        }

        super.onCreate(savedInstanceState);

        user = (User) getIntent().getExtras().get(INTENT_USER);
        mUserHandler = new UserHandler();
        setDetailsView();
        setNavDrawer(user);
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
        user = new User(mName, mUser.getPhoneNumber());
        mUserHandler.putValue(mUserId, user);
        Toast.makeText(this, "Updated profile successfully", Toast.LENGTH_SHORT).show();
        // switch to maps activity here
        Intent i = new Intent(ProfileActivity.this, MapActivity.class);
        i.putExtra(INTENT_USER,user);
        startActivity(i);
        finish();
    }


    private void setNavDrawer(User user){
        Log.d(TAG, "setNavDrawer: setting name " + user.getName());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view2);
        View header = navigationView.getHeaderView(0);
        TextView nameView = (TextView)header.findViewById(R.id.nav_name);
        nameView.setText(user.getName());


        drawerLayout = findViewById(R.id.drawer_layout2);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        menuItem.setChecked(true); // set item as selected to persist highlight
                        drawerLayout.closeDrawers();

                        displayMap(menuItem.getItemId());
                        return true;
                    }
                });

        ImageButton uiMode= (ImageButton) header.findViewById(R.id.ui_mode);
        uiMode.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                switch(AppCompatDelegate.getDefaultNightMode()) {
                    // do stuff
                    case AppCompatDelegate.MODE_NIGHT_NO:
                        AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                    case AppCompatDelegate.MODE_NIGHT_YES:
                        AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                        break;
                    default:
                        AppCompatDelegate
                            .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                        break;
                }
                recreate();
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    private void displayMap(int itemId){
        Log.d(TAG, "displayScreen: new Activity");
        switch (itemId) {
            case R.id.nav_profile:
                break;
            case R.id.nav_map:
                Intent i = new Intent(ProfileActivity.this,MapActivity.class);
                i.putExtra(INTENT_USER,user);
                startActivity(i);
                finish();
                break;
        }

    }
}
