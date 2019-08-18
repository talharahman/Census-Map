package com.example.censusmap.repositiory;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.censusmap.fragments.DataFragment;
import com.example.censusmap.model.CensusModel;
import com.example.censusmap.utilities.Constants;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;

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
                .subscribe(new Consumer<CensusModel>() {
                               @Override
                               public void accept(CensusModel model) throws Exception {
                                   fragment.updateUI(model);
                               }
                           },
                        throwable -> Log.d(Constants.TAG, "onFailure: " + throwable));
    }
}
