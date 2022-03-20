package com.example.collegeproject.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.fragment.app.FragmentActivity;

import com.example.collegeproject.R;
import com.example.collegeproject.databinding.ActivityMapsBinding;
import com.github.ag.floatingactionmenu.OptionsFabLayout;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    SharedPreferences sharedPreferences;
    public static final String mypreference = "mypref";
    public static final String LATITUDE = "lat";
    public static final String LONGITUDE = "long";
    double lat, longitude;
    OptionsFabLayout fabWithOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        fabWithOptions = (OptionsFabLayout) findViewById(R.id.map_fab);
        sharedPreferences = getSharedPreferences(mypreference, MODE_PRIVATE);
        lat = Double.parseDouble(sharedPreferences.getString(LATITUDE, ""));
        longitude = Double.parseDouble(sharedPreferences.getString(LONGITUDE, ""));
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        // Add a marker in Sydney and move the camera
        LatLng latlng = new LatLng(lat, longitude);
        mMap.addMarker(new MarkerOptions().position(latlng).title("Marker in Sydney"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, 18), 800, null);
        /*mMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));*/
        try {
            fabWithOptions.setMainFabOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fabWithOptions.isOptionsMenuOpened()) {
                        fabWithOptions.closeOptionsMenu();
                    }
                }
            });

            fabWithOptions.setMiniFabSelectedListener(new OptionsFabLayout.OnMiniFabSelectedListener() {
                @Override
                public void onMiniFabSelected(MenuItem fabItem) {
                    if (fabItem.getItemId() == R.id.normal_map) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                        fabWithOptions.closeOptionsMenu();
                    }
                    if (fabItem.getItemId() == R.id.satellite_map) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
                        fabWithOptions.closeOptionsMenu();
                    }
                    if (fabItem.getItemId() == R.id.terrain_map) {
                        mMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
                        fabWithOptions.closeOptionsMenu();
                    }
                }
            });

        } catch (Exception e) {
            Log.e("ERROR", String.valueOf(e));
        }
    }
}