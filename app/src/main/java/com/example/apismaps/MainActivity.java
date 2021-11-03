package com.example.apismaps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

public class MainActivity extends FragmentActivity {

    // Bascom Hall
    private final LatLng mDestinationLatLng = new LatLng(43.075106,-89.40359751);
    private GoogleMap mMap;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION= 12; // could be any number
    // Milestone 2
    private FusedLocationProviderClient mFusedLocationProviderClient; // save the instance

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain a FusedLocationProviderClient
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            googleMap.addMarker(new MarkerOptions().position(mDestinationLatLng).title("Destination"));
        });
        displayMyLocation();
    }

    private void displayMyLocation(){
        // check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
        // if not, ask for it
        if(permission == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        // if permission granted, display marker at current location
        else{
            Log.e("here","KINDA GOOD");
            mFusedLocationProviderClient.getLastLocation().addOnCompleteListener(this, task -> {
                Location mLastKnownLocation = task.getResult();
                if(task.isSuccessful() && mLastKnownLocation != null){
                    Log.e("here","WHERE YOU NEED TO BE");
                    mMap.addPolyline(new PolylineOptions().add(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude()), mDestinationLatLng));
                    mMap.addMarker(new MarkerOptions().position(new LatLng(mLastKnownLocation.getLatitude(),mLastKnownLocation.getLongitude())).title("MyLocation"));
                }
            });
        }
    }
}