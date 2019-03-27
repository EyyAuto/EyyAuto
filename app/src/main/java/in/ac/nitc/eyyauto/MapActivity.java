package in.ac.nitc.eyyauto;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.RectangularBounds;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import in.ac.nitc.eyyauto.models.User;

import static in.ac.nitc.eyyauto.Constants.INTENT_USER;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {


    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean mLocationPermissionsGranted = false;
    private GoogleMap mMap;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private static final float DEFAULT_ZOOM = 15f;
    private User user = null;
    private LatLng pickUp = new LatLng(11.321458,75.934127);
    private LatLng dropOff  = new LatLng(11.321458,75.934127);
    private Marker pickUpMarker = null;
    private Marker dropOffMarker = null;


    private DrawerLayout drawerLayout;

    private ImageView mGps;


    @Override
    public void onMapReady(GoogleMap googleMap) {
        //Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        if(user!=null)
            Toast.makeText(this, "Signed in  as "+user.getName()+"\n"+user.getContact(), Toast.LENGTH_SHORT).show();
        else
            Toast.makeText(this, "User details are NULL", Toast.LENGTH_SHORT).show();


        Log.d(TAG, "onMapReady: map is ready");
        mMap = googleMap;
        if(AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            MapStyleOptions style = MapStyleOptions.loadRawResourceStyle(this, R.raw.style_json);
            mMap.setMapStyle(style);
        }
        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);
            mMap.getUiSettings().setMyLocationButtonEnabled(false);
            mMap.getUiSettings().setCompassEnabled(true);

        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.AppThemeDark);
        }
        super.onCreate(savedInstanceState);

        user = (User) getIntent().getExtras().get(INTENT_USER);
        mGps = (ImageView) findViewById(R.id.ic_gps);

        setContentView(R.layout.activity_map);
        getLocationPermission();

        setNavDrawer(user);
        searchBarInit();

    }

    private void searchBarInit(){                               //also init the custom gps button
        //final TextView txtVw = findViewById(R.id.placeName);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), getString(R.string.google_maps_key));
        }

        final AutocompleteSupportFragment autocompleteFragmentFrom = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_from);

        autocompleteFragmentFrom.setHint("Pickup Location");
        //TODO make these lat.long constants for NITC
        autocompleteFragmentFrom.setLocationRestriction(RectangularBounds.newInstance(
                new LatLng(11.3215791-.05, 75.9336359-.05),
                new LatLng(11.3215791+.05, 75.9336359+.05)));

        autocompleteFragmentFrom.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));



        autocompleteFragmentFrom.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //txtVw.setText("From: " + place.getLatLng());

                pickUp = place.getLatLng();

                if(pickUpMarker!=null)
                {
                    pickUpMarker.remove();
                    pickUpMarker = null;
                }

                MarkerOptions options = new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getAddress());
                pickUpMarker = mMap.addMarker(options);
            }
            @Override
            public void onError(Status status) {
                //txtVw.setText(status.toString());
                Toast.makeText(MapActivity.this, status.toString(), Toast.LENGTH_SHORT).show();

            }
        });



        AutocompleteSupportFragment autocompleteFragmentTo = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_to);

        autocompleteFragmentTo.setHint("Drop-off Location");
        //TODO make these lat.long constants for NITC
        autocompleteFragmentTo.setLocationRestriction(RectangularBounds.newInstance(
                new LatLng(11.3215791-.15, 75.9336359-.15),
                new LatLng(11.3215791+.15, 75.9336359+.15)));

        autocompleteFragmentTo.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG));
        autocompleteFragmentTo.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                //txtVw.setText(txtVw.getText()+"\nTo:" + place.getLatLng());

                dropOff = place.getLatLng();

                if(dropOffMarker != null)
                {
                    dropOffMarker.remove();
                    dropOffMarker = null;
                }

                MarkerOptions options = new MarkerOptions()
                        .position(place.getLatLng())
                        .title(place.getAddress());
                dropOffMarker = mMap.addMarker(options);


                LatLngBounds.Builder builder = new LatLngBounds.Builder();
                builder.include(pickUp);
                builder.include(dropOff);
                LatLngBounds bounds = builder.build();
                int padding = ((1500 * 10) / 100); // offset from edges of the map in pixels
                CameraUpdate cu = CameraUpdateFactory.newLatLngBounds(bounds,
                        padding);
                mMap.animateCamera(cu);

            }
            @Override
            public void onError(Status status) {
                //txtVw.setText(status.toString());
                Toast.makeText(MapActivity.this, status.toString(), Toast.LENGTH_SHORT).show();

            }
        });





        //initializing the custom gps button
        mGps = (ImageView) findViewById(R.id.ic_gps);
        mGps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "onClick: clicked gps icon");
                getDeviceLocation();

            }
        });

    }

    private void setNavDrawer(User user){
        Log.d(TAG, "setNavDrawer: setting name " + user.getName());
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View header = navigationView.getHeaderView(0);
        TextView nameView = (TextView)header.findViewById(R.id.nav_name);
        nameView.setText(user.getName());


        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(MenuItem menuItem) {
                        drawerLayout.closeDrawers();
                        displayScreen(menuItem.getItemId());
                        return true;
                    }
                });
        ImageButton uiMode = (ImageButton) header.findViewById(R.id.ui_mode);
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

    private void displayScreen(int itemId){
        Log.d(TAG, "displayScreen: new Activity");
        switch (itemId) {
            case R.id.nav_profile:
                Intent i = new Intent(MapActivity.this,ProfileActivity.class);
                i.putExtra(INTENT_USER,user);
                startActivity(i);
                finish();
                break;
            default:
                break;
        }
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

    private void initMap(){
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapActivity.this);
    }

    private void getDeviceLocation(){
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionsGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            if(currentLocation!=null){
                                moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                        DEFAULT_ZOOM);
                            }
                            else{
                                Log.d(TAG, "onComplete: current location is null gps not enabled");
                                Toast.makeText(MapActivity.this, "gps not enabled:unable to get current location", Toast.LENGTH_SHORT).show();
                                moveCamera(new LatLng(11.3215791,75.9336359), 16f);
                            }

                        }else{
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(MapActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage() );
            Toast.makeText(MapActivity.this, "SecurityException: " + e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void moveCamera(LatLng latLng, float zoom){
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude );
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));
    }


    private void getLocationPermission(){
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionsGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for(int i = 0; i < grantResults.length; i++){
                        if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }



    /*private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }*/


    public static void hideSoftKeyboard(Context context, View view) {
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }




}
