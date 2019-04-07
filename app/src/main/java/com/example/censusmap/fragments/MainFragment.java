package com.example.censusmap.fragments;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.censusmap.R;
import com.example.censusmap.repositiory.FragmentInterface;
import com.example.censusmap.utilities.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public final class MainFragment extends Fragment
        implements OnMapReadyCallback, OnBarQueryListener {

    View rootView;
    FusedLocationProviderClient flpClient;
    MapView mapView;
    GoogleMap map;
    String zipCode;
    FragmentInterface listener;

    public MainFragment() {
    }

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);

        mapView = rootView.findViewById(R.id.main_map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.developer_contact:
                developerInfo().show();
                break;
        }
        return true;
    }

    public AlertDialog developerInfo() {
        AlertDialog.Builder devInfo = new AlertDialog.Builder(rootView.getContext());
        devInfo.setTitle(R.string.developer_info_text)
                .setItems(R.array.developer_contact_text, (dialog, which) -> {
                    switch (which) {
                        case 0:
                            Uri emailUri = Uri.parse(String.valueOf(R.string.e_mail));
                            Intent emailIntent = new Intent(Intent.ACTION_VIEW, emailUri);
                            startActivity(emailIntent);
                            break;
                        case 1:
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(R.string.github_repo))));
                            break;
                        case 2:
                            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(R.string.linkedin_profile))));
                            break;
                    }
                });
        return devInfo.create();
    }


    private void setButton(String zipCode) {
        Button displayButton = rootView.findViewById(R.id.info_button);
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.moveToDetailsScreen(zipCode);
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof FragmentInterface) {
            listener = (FragmentInterface) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement interface");
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        MapsInitializer.initialize(getContext());
        map = googleMap;

        googleMap.addMarker(new MarkerOptions().position(new LatLng(40.7128, -74.0060)).title("New York City Hall"));
        CameraPosition liberty = CameraPosition.builder()
                .target(new LatLng(40.7128, -74.0060))
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(liberty));


        if (ActivityCompat.checkSelfPermission(rootView.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(rootView.getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) rootView.getContext(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, Constants.REQUEST_LOCATION_PERMISSION);
        } else {
            googleMap.setMyLocationEnabled(true);
            flpClient = LocationServices.getFusedLocationProviderClient((Activity) rootView.getContext());
            flpClient.getLastLocation().addOnSuccessListener(location -> {
                Geocoder geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());
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

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }

    @Override
    public void onQuery(String s) {
        List<Address> searchAddresses;
        Geocoder geocoder = new Geocoder(rootView.getContext(), Locale.getDefault());

        try {
            searchAddresses = geocoder.getFromLocationName(s, 1);
            if (searchAddresses.isEmpty()) {
                Toast.makeText(rootView.getContext(),
                        "Invalid location", Toast.LENGTH_SHORT).show();
            }

            // listener.sendData(query)
            Address searchAddress = searchAddresses.get(0);
            LatLng latLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(s));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 10));
            zipCode = searchAddress.getPostalCode();

            setButton(zipCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
