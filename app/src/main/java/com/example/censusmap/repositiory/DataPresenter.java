package com.example.censusmap.repositiory;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.censusmap.view.MainDataFragment;
import com.example.censusmap.model.CensusModel;
import com.example.censusmap.utilities.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

public class DataPresenter {

    private MainDataFragment fragment;

    public DataPresenter(MainDataFragment fragment) {
        this.fragment = fragment;
    }

    @SuppressLint("CheckResult")
    public void getData(String zipCode){
        DataRepository repository = new DataRepository();
        repository.networkCall(zipCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(model -> fragment.updateUI(model),
                        throwable -> Log.d(Constants.TAG, "onFailure: " + throwable));
    }
}
