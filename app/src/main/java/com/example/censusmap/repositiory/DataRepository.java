package com.example.censusmap.repositiory;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.censusmap.model.CensusModel;
import com.example.censusmap.network.CensusService;
import com.example.censusmap.network.RetrofitSingleton;
import com.example.censusmap.utilities.Constants;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

final class DataRepository {

    private final CensusService service = RetrofitSingleton
            .getInstance()
            .create(CensusService.class);

    @SuppressLint("CheckResult")
    Single<CensusModel> networkCall(final String zipCode) {
        return service.getData(Constants.TOTAL_POPULATION +
                                Constants.COMMA +
                                Constants.FOREIGN_BORN +
                                Constants.COMMA +
                                Constants.MINORS +
                                Constants.COMMA +
                                Constants.SENIORS +
                                Constants.NAME_KEY,
                        Constants.ZIP_KEY +
                                zipCode)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(list -> list)
                .skip(1)
                .take(1)
                .firstOrError()
                .map(lists -> new CensusModel(
                        lists.get(0),
                        lists.get(1),
                        lists.get(2),
                        lists.get(3)))
                .doOnSuccess(censusModel -> Log.d(Constants.TAG, censusModel.getTotalPopulation() +
                        " " + censusModel.getForeignBorn() +
                        " " + censusModel.getAgeUnder18() +
                        " " + censusModel.getAgeOver65()));
    }
}
