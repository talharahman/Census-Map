package com.example.censusmap.view;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.censusmap.R;
import com.example.censusmap.recyclerview.DataAdapter;
import com.example.censusmap.model.CensusModel;
import com.example.censusmap.repositiory.DataPresenter;
import com.example.censusmap.repositiory.OnQuerySubmitListener;
import com.example.censusmap.utilities.Constants;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.bottomsheet.BottomSheetBehavior;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class MainDataFragment extends Fragment implements OnMapReadyCallback, OnQuerySubmitListener {

    private View rootView;
    private FusedLocationProviderClient flpClient;
    private GoogleMap map;
    private String zipCode;
    private DataAdapter adapter;

    public MainDataFragment() { }

    public static MainDataFragment newInstance() {
        return new MainDataFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_main, container, false);
        setBottomSheet();
        return rootView;
    }

    private void setBottomSheet() {
        View bottomSheet = rootView.findViewById(R.id.bottom_sheet);
        BottomSheetBehavior sheetBehavior = BottomSheetBehavior.from(bottomSheet);

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View view, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_EXPANDED:
                        fetchCensus(zipCode);
                        break;
                    case BottomSheetBehavior.STATE_COLLAPSED:
                    case BottomSheetBehavior.STATE_HIDDEN:
                    case BottomSheetBehavior.STATE_DRAGGING:
                    case BottomSheetBehavior.STATE_HALF_EXPANDED:
                    case BottomSheetBehavior.STATE_SETTLING:
                        setDisplayText(zipCode);
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View view, float v) { }
        });
    }

    private void fetchCensus(String zipCode) {
        DataPresenter presenter = new DataPresenter(this);
        presenter.getData(zipCode);

        setView();
    }

    private void setView() {
        RecyclerView recyclerView = rootView.findViewById(R.id.census_recyclerview);
        adapter = new DataAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(rootView.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false));
    }

    private void setDisplayText(String zipCode) {
        TextView displayText = rootView.findViewById(R.id.zip_code_textview);
        displayText.setText("Zip Code: " + zipCode);
    }

    public void updateUI(CensusModel model) {
        adapter.passModel(model);
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


    // TODO: Can this be moved off the UI ?
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

        if (ActivityCompat.checkSelfPermission(rootView.getContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ActivityCompat.checkSelfPermission(rootView.getContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions((Activity) rootView.getContext(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION},
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
                    setDisplayText(zipCode);

                    Log.d(Constants.TAG, locationAddress.getPostalCode());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            flpClient.getLastLocation().addOnFailureListener(e -> Log.d(Constants.TAG, e.getMessage()));
        }
    }

    @Override
    public void onQuerySubmit(String query) {
        List<Address> searchAddresses;
        Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());

        try {
            searchAddresses = geocoder.getFromLocationName(query, 1);
            if (searchAddresses.isEmpty()) {
                Toast.makeText(getContext(),
                        "Invalid location", Toast.LENGTH_SHORT).show();
            }

            Address searchAddress = searchAddresses.get(0);
            LatLng latLng = new LatLng(searchAddress.getLatitude(), searchAddress.getLongitude());
            map.addMarker(new MarkerOptions().position(latLng).title(query));
            map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
            zipCode = searchAddress.getPostalCode();
            setDisplayText(zipCode);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
