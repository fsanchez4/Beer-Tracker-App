package com.csc4360.beertracker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Location;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;



public class MapActivity extends AppCompatActivity implements OnMapReadyCallback, NavigationHost {

    private final int REQUEST_LOCATION_PERMISSIONS = 0;
    private GoogleMap mMap;
    private FusedLocationProviderClient mClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private float mZoomLevel = 10;
    private List<MarkerOptions> mBreweryMarkers = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Adding App Bar
        Toolbar appToolbar = findViewById(R.id.appToolbar);
        setSupportActionBar(appToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.map_container, new MapFragment())
                    .commit();
        }

        // Create location request
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(5000);
        mLocationRequest.setFastestInterval(3000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Create location callback
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult != null) {
                    for (Location location : locationResult.getLocations()) {
                        updateMap(location);
                    }
                }
            }
        };

        mClient = LocationServices.getFusedLocationProviderClient(this);

//        Thread getMapData = new Thread(new Runnable(){
//            @Override
//            public void run(){
//                //Pull in list of address from DB
//                String[] addressListFromDB = MainActivity.appDatabase.breweryDao().getAllAddresses();
//
//                //Use geocoder to return list of addressFromGeocoder which contain coordinate information
//                Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);
//
//                //List of type address that is returned from geocoder
//                List<Address> addressFromGeocoder;
//
//                //Temporary variables
//                LatLng tempCoordinates;
//                List<LatLng> coordinatesList = new ArrayList<>();
//
//                //For each address in the DB, get the full geocoder returned address
//                for(int i=0;i<addressListFromDB.length;i++) {
//                    try {
//                        addressFromGeocoder = geocoder.getFromLocationName(addressListFromDB[i], 1);
//                        tempCoordinates = new LatLng(addressFromGeocoder.get(0).getLatitude(),addressFromGeocoder.get(0).getLongitude());
//                        coordinatesList.add(tempCoordinates);
//                        System.out.println("--------------INIT COORDINATE PULL---------------------------" + tempCoordinates);
//
//                    }
//                    catch(Exception e){
//                        System.out.println(e);
//                    }
//                }
//
//                //Create list of markers from brewery coordinates
//                mBreweryMarkers = new ArrayList<>(coordinatesList.size());
//                for(LatLng l: coordinatesList){
//                    mBreweryMarkers.add(new MarkerOptions().title("Fix This Later!").position(l));
//                }
//                for(MarkerOptions m: mBreweryMarkers){
//                    System.out.println("-------------MARKER CHECK COORDINATES------------------" + m.getPosition().toString());
//                }
//            }
//        });
//        getMapData.start();



        //Pull in list of address from DB
        String[] addressListFromDB = MainActivity.appDatabase.breweryDao().getAllAddresses();

        //Use geocoder to return list of addressFromGeocoder which contain coordinate information
        Geocoder geocoder = new Geocoder(getApplicationContext(), Locale.US);

        //List of type address that is returned from geocoder
        List<Address> addressFromGeocoder;

        //Temporary variables
        LatLng tempCoordinates;
        List<LatLng> coordinatesList = new ArrayList<>();

        //For each address in the DB, get the full geocoder returned address
        for(int i=0;i<addressListFromDB.length;i++) {
            try {
                addressFromGeocoder = geocoder.getFromLocationName(addressListFromDB[i], 1);
                tempCoordinates = new LatLng(addressFromGeocoder.get(0).getLatitude(),addressFromGeocoder.get(0).getLongitude());
                coordinatesList.add(tempCoordinates);
                System.out.println("--------------INIT COORDINATE PULL---------------------------" + tempCoordinates);

            }
            catch(Exception e){
                System.out.println(e);
            }
        }

        //Create list of markers from brewery coordinates
        mBreweryMarkers = new ArrayList<>(coordinatesList.size());
        for(LatLng l: coordinatesList){
            mBreweryMarkers.add(new MarkerOptions().title("Fix This Later!").position(l));
        }
        for(MarkerOptions m: mBreweryMarkers){
            System.out.println("-------------MARKER CHECK COORDINATES------------------" + m.getPosition().toString());
        }

//        setBreweryCoordinates(mBreweryMarkers);
    }

    @Override
    public void navigateTo(Fragment fragment, boolean addToBackstack) {
        FragmentTransaction transaction =
                getSupportFragmentManager()
                        .beginTransaction()
                        .replace(R.id.map_container, fragment);

        if (addToBackstack) {
            transaction.addToBackStack(null);
        }

        transaction.commit();
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    private void setBreweryCoordinates(List<MarkerOptions> markersToSet){
        for(MarkerOptions m: markersToSet){
            mMap.addMarker(m);
            System.out.println("00000000000000000000000000000000000000-ADDED COORDINATE: "+ m.getPosition().toString());
        }
    }
    private void updateMap(Location location) {
        // TODO: Show user's location on the map

        // Get current location
        LatLng myLatLng = new LatLng(location.getLatitude(),
                location.getLongitude());

        // Place a marker at the current location
        MarkerOptions myMarker = new MarkerOptions()
                .title("Here we are!")
                .position(myLatLng);

        // Remove previous marker
        mMap.clear();

        // Add new marker
        mMap.addMarker(myMarker);

        // Zoom to previously saved level
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(myLatLng, mZoomLevel);
        mMap.animateCamera(update);

        setBreweryCoordinates(mBreweryMarkers);


        onPause();
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Save zoom level
        mMap.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                CameraPosition cameraPosition = mMap.getCameraPosition();
                mZoomLevel =cameraPosition.zoom;
            }
        });

        // Handle marker click
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(MapActivity.this, "Lat: " + marker.getPosition().latitude +
                        "\nLong: " + marker.getPosition().longitude, Toast.LENGTH_LONG).show();
                return false;
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        mClient.removeLocationUpdates(mLocationCallback);
    }

    @SuppressLint("MissingPermission")
    @Override
    public void onResume() {
        super.onResume();

        if (hasLocationPermission()) {
            mClient.requestLocationUpdates(mLocationRequest, mLocationCallback, null);
        }
    }

    private boolean hasLocationPermission() {

        // Request fine location permission if not already granted
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this ,
                    new String[] { Manifest.permission.ACCESS_FINE_LOCATION },
                    REQUEST_LOCATION_PERMISSIONS);

            return false;
        }

        return true;
    }
}

