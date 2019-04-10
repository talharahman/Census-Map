package com.example.censusmap.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.censusmap.R;
import com.example.censusmap.controller.DataAdapter;
import com.example.censusmap.model.CensusModel;
import com.example.censusmap.repositiory.DataPresenter;
import com.example.censusmap.utilities.Constants;

import java.util.ArrayList;

public final class DataFragment extends Fragment {

    public static final String PARAM_KEY = "zip";

    String zipCode;
    View rootView;
    DataAdapter adapter;

    public DataFragment() {}

    public static DataFragment newInstance(String zipCode) {
        DataFragment dataFragment = new DataFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_KEY, zipCode);
        dataFragment.setArguments(args);
        return dataFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            zipCode = getArguments().getString(PARAM_KEY);
        } else {
            Log.i(Constants.TAG, "Zip code is null value");
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.data_fragment, container, false);
        initialize();
        setData();
        return rootView;
    }

    private void setData() {
        DataPresenter presenter = new DataPresenter(this);
        presenter.getData(zipCode);
    }

    private void initialize() {
        RecyclerView recyclerView = rootView.findViewById(R.id.census_recyclerview);
        adapter = new DataAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(
                new LinearLayoutManager(rootView.getContext(),
                        LinearLayoutManager.VERTICAL,
                        false));
    }

    public void updateUI(CensusModel model){
        adapter.passModel(model);
    }

}
