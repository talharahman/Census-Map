package com.example.censusmap;

import com.example.censusmap.controller.DataAdapter;
import com.example.censusmap.model.CensusModel;
import com.google.android.gms.location.FusedLocationProviderClient;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TestingClass {

    private FusedLocationProviderClient fusedLocationProviderClient;
    private DataAdapter adapter;

    @Before
    public void set_up () {
        List<CensusModel> testModelsList = new ArrayList<>();
        adapter = new DataAdapter(testModelsList);
    }

    @Test
    public void test_for_zip_code () {

    }

    @Test
    public void test_adapter_pass_model () {
        CensusModel testModel = new CensusModel("0", "0", "0", "0");
        adapter.passModel(testModel);
        int expected = 1;
        int actual = adapter.getItemCount();
        Assert.assertEquals(expected, actual);

    //    System.out.println(adapter.getItemCount());
    }
}
