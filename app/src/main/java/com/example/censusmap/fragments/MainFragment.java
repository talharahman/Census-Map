package com.example.censusmap.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.censusmap.R;
import com.example.censusmap.utilities.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public final class MainFragment extends Fragment
        implements OnMapReadyCallback, OnQuerySubmitListener {

    private View rootView;
    private FusedLocationProviderClient flpClient;
    private GoogleMap map;
    private String zipCode;
    private FragmentInterface listener;

    public MainFragment() { }

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

        flpClient = LocationServices.getFusedLocationProviderClient((Activity) rootView.getContext());

        MapView mapView = rootView.findViewById(R.id.main_map);
        if (mapView != null) {
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    private void setButton(String zipCode) {
        Button displayButton = rootView.findViewById(R.id.details_button);
        displayButton.setText("Zip Code: " + zipCode);
        displayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                listener.moveToDetailsScreen(zipCode);
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;

        LatLng defaultLoc = new LatLng(40.7128, -74.0060);
        googleMap.addMarker(new MarkerOptions().position(defaultLoc).title("New York City Hall"));
        CameraPosition NYChall = CameraPosition.builder()
                .target(defaultLoc)
                .zoom(16)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.moveCamera(CameraUpdateFactory.newCameraPosition(NYChall));

        UiSettings uiSettings = map.getUiSettings();
        uiSettings.setZoomControlsEnabled(true);
        uiSettings.setCompassEnabled(true);
        uiSettings.setMyLocationButtonEnabled(true);
        uiSettings.setAllGesturesEnabled(true);

        if (ActivityCompat.checkSelfPermission(rootView.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                ||
                ActivityCompat.checkSelfPermission(rootView.getContext(),
                        android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) rootView.getContext(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION,
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    Constants.REQUEST_LOCATION_PERMISSION);
        } else {
            googleMap.setMyLocationEnabled(true);
            flpClient.getLastLocation().addOnSuccessListener(location -> {
                Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
                List<Address> locationAddresses;

                try {
                    locationAddresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                    Address locationAddress = locationAddresses.get(0);
                    zipCode = locationAddress.getPostalCode();
                //    setText(zipCode);
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
    public void onQuerySubmit(String s) {
        List<Address> searchAddresses;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            searchAddresses = geocoder.getFromLocationName(s, 1);
            if (searchAddresses.isEmpty()) {
                Toast.makeText(getContext(),
                        "Invalid location", Toast.LENGTH_SHORT).show();
            }

            Address searchAddress = searchAddresses.get(0);
            LatLng latLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(s));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            zipCode = searchAddress.getPostalCode();

            setButton(zipCode);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}