package com.example.censusmap.repositiory;

import android.annotation.SuppressLint;
import android.util.Log;

import com.example.censusmap.model.CensusModel;
import com.example.censusmap.network.CensusService;
import com.example.censusmap.network.RetrofitSingleton;
import com.example.censusmap.utilities.Constants;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

final class DataRepository {

    private final CensusService service = RetrofitSingleton
            .getInstance()
            .create(CensusService.class);

    @SuppressLint("CheckResult")
    Single<CensusModel> networkCall(final String zipCode) {
        return service.getData(
                Constants.TOTAL_POPULATION + Constants.COMMA +
                        Constants.FOREIGN_BORN + Constants.COMMA +
                        Constants.MINORS + Constants.COMMA +
                        Constants.SENIORS + Constants.COMMA +
                        Constants.SERVICE + Constants.COMMA +
                        Constants.SALES_OFFICE + Constants.COMMA +
                        Constants.CONSTRUCTION + Constants.COMMA +
                        Constants.PUBLIC_ADMIN + Constants.COMMA +
                        Constants.EDU_HC_SOCIAL + Constants.COMMA +
                        Constants.FIN_REAL_ESTATE + Constants.COMMA +
                        Constants.TRANS_UTL +
                        Constants.NAME_KEY, Constants.ZIP_KEY +
                        zipCode)
                .subscribeOn(Schedulers.io())
                .flatMapIterable(new Function<List<List<String>>, Iterable<? extends List<String>>>() {
                    @Override
                    public Iterable<? extends List<String>> apply(List<List<String>> list) throws Exception {
                        return list;
                    }
                })
                .skip(1)
                .take(1)
                .firstOrError()
                .map(new Function<List<String>, CensusModel>() {
                    @Override
                    public CensusModel apply(List<String> lists) throws Exception {
                        return new CensusModel(
                                lists.get(0),
                                lists.get(1),
                                lists.get(2),
                                lists.get(3),
                                lists.get(4),
                                lists.get(5),
                                lists.get(6),
                                lists.get(7),
                                lists.get(8),
                                lists.get(9),
                                lists.get(10));
                    }
                })
                .doOnSuccess(censusModel -> Log.d(Constants.TAG,
                        "onSuccess: " + censusModel.getTotalPopulation() +
                                " " + censusModel.getForeignBorn() +
                                " " + censusModel.getAgeUnder18() +
                                " " + censusModel.getAgeOver65() +
                                " " + censusModel.getEmployedService() +
                                " " + censusModel.getEmployedSalesOffice() +
                                " " + censusModel.getEmployedConstruction() +
                                " " + censusModel.getPublicAdministration() +
                                " " + censusModel.getEducationHealthCareSocial() +
                                " " + censusModel.getFinanceRealEstate() +
                                " " + censusModel.getTransportationUtilities()));
    }

}
