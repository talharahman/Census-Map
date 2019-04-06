package com.example.censusmap;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Button;
import android.support.v7.widget.SearchView;
import com.example.censusmap.fragments.DataFragment;
import com.example.censusmap.utilities.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Locale;


public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {

    FusedLocationProviderClient flpClient;
    GoogleMap map;
    SearchView searchView;
    String zipCode;
    android.support.v7.widget.Toolbar toolBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.developer_info, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.developer_contact:
             //   developerInfo().show();
                break;
        }
        return true;
    }

   /* public AlertDialog developerInfo() {
        AlertDialog.Builder devInfo = new AlertDialog.Builder(getApplicationContext());
        devInfo.setTitle(R.string.developer_info_text)
                .setItems(R.array.developer_contact_text, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:
                                Uri emailUri = Uri.parse(String.valueOf(R.string.e_mail));
                                Intent emailIntent = new Intent(Intent.ACTION_VIEW, emailUri);
                                startActivity(emailIntent);
                                break;
                            case 1:
                                Uri githubUri = Uri.parse(String.valueOf(R.string.github_repo));
                                Intent githubIntent = new Intent(Intent.ACTION_VIEW, githubUri);
                                startActivity(githubIntent);
                                break;
                            case 2:
                                Uri linkedinUri = Uri.parse(String.valueOf(R.string.linkedin_profile));
                                Intent linkedinIntent = new Intent(Intent.ACTION_VIEW, linkedinUri);
                                startActivity(linkedinIntent);
                                break;
                        }
                    }
                });
        return devInfo.create();
    }*/


    private void initialize() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.main_map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        toolBar = findViewById(R.id.main_toolbar);
        setSupportActionBar(toolBar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeAsUpIndicator(R.drawable.ic_developer_info);
            setSearchView();
        }
    }

    private void setSearchView() {
        searchView = findViewById(R.id.map_search_view);
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                String location = searchView.getQuery().toString();
                List<Address> searchAddresses = null;
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());

                try {
                    searchAddresses = geocoder.getFromLocationName(location, 1);
                    Address searchAddress = searchAddresses.get(0);
                    LatLng latLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());
                    map.addMarker(new MarkerOptions().position(latLng).title(location));
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
                    zipCode = searchAddress.getPostalCode();
                    setButton(zipCode);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(this,
                        android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.REQUEST_LOCATION_PERMISSION);
        } else {
            googleMap.setMyLocationEnabled(true);
            flpClient = LocationServices.getFusedLocationProviderClient(this);
            flpClient.getLastLocation().addOnSuccessListener(location -> {
                Geocoder geocoder = new Geocoder(MainActivity.this, Locale.getDefault());
                List<Address> locationAddresses;

                try {
                    locationAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address locationAddress = locationAddresses.get(0);
                    zipCode = locationAddress.getPostalCode();
                    setButton(zipCode);

                    Log.d(Constants.TAG, locationAddress.getPostalCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            flpClient.getLastLocation().addOnFailureListener(e -> Log.d(Constants.TAG, e.getMessage()));
        }
    }

    private void setButton(String zipCode) {
        Button displayButton = findViewById(R.id.info_button);
        displayButton.setOnClickListener(v -> getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.enter, R.anim.exit, R.anim.pop_enter, R.anim.pop_exit)
                .replace(R.id.main_activity_container, DataFragment.newInstance(zipCode))
                .addToBackStack(null)
                .commit());
    }
}
