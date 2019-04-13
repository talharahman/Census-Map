package com.example.censusmap.network;

import com.example.censusmap.utilities.Constants;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CensusService {

    String PROFILE_KEY = "profile";

    @GET(PROFILE_KEY)
    Observable<List<List<String>>> getData(@Query(value = "get", encoded = true) String code, @Query(value = "for", encoded = true) String zip);
}
