package com.example.censusmap.repositiory;

import android.annotation.SuppressLint;

import com.example.censusmap.fragments.DataFragment;

import io.reactivex.android.schedulers.AndroidSchedulers;

public class DataPresenter {

    private DataFragment fragment;

    public DataPresenter(DataFragment fragment) {
        this.fragment = fragment;
   }

    @SuppressLint("CheckResult")
    public void getData(String zipCode){
        DataRepository repository = new DataRepository();
        repository.networkCall(zipCode)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(censusModel -> fragment.updateUI(censusModel),
                        t -> t.getMessage());
    }

}
