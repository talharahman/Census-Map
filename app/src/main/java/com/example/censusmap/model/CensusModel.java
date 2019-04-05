package com.example.censusmap.model;

public class CensusModel {

    private String totalPopulation;
    private String foreignBorn;
    private String ageUnder18;
    private String ageOver65;

    public CensusModel(String totalPopulation, String foreignBorn, String ageUnder18, String ageOver65) {
        this.totalPopulation = totalPopulation;
        this.foreignBorn = foreignBorn;
        this.ageUnder18 = ageUnder18;
        this.ageOver65 = ageOver65;
    }

    public String getTotalPopulation() {
        return totalPopulation;
    }

    public String getForeignBorn() {
        return foreignBorn;
    }

    public String getAgeUnder18() {
        return ageUnder18;
    }

    public String getAgeOver65() {
        return ageOver65;
    }
}
