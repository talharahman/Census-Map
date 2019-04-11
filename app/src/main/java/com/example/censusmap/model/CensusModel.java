package com.example.censusmap.model;

public class CensusModel {

    private String totalPopulation;
    private String foreignBorn;
    private String ageUnder18;
    private String ageOver65;
    private String employedService;
    private String employedSalesOffice;
    private String employedConstruction;
    private String publicAdministration;
    private String educationHealthCareSocial;
    private String financeRealEstate;
    private String transportationUtilities;

    public CensusModel(String totalPopulation,
                       String foreignBorn,
                       String ageUnder18,
                       String ageOver65,
                       String employedService,
                       String employedSalesOffice,
                       String employedConstruction,
                       String publicAdministration,
                       String educationHealthCareSocial,
                       String financeRealEstate,
                       String transportationUtilities) {
        this.totalPopulation = totalPopulation;
        this.foreignBorn = foreignBorn;
        this.ageUnder18 = ageUnder18;
        this.ageOver65 = ageOver65;
        this.employedService = employedService;
        this.employedSalesOffice = employedSalesOffice;
        this.employedConstruction = employedConstruction;
        this.publicAdministration = publicAdministration;
        this.educationHealthCareSocial = educationHealthCareSocial;
        this.financeRealEstate = financeRealEstate;
        this.transportationUtilities = transportationUtilities;
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

    public String getEmployedService() {
        return employedService;
    }

    public String getEmployedSalesOffice() {
        return employedSalesOffice;
    }

    public String getEmployedConstruction() {
        return employedConstruction;
    }

    public String getPublicAdministration() {
        return publicAdministration;
    }

    public String getEducationHealthCareSocial() {
        return educationHealthCareSocial;
    }

    public String getFinanceRealEstate() {
        return financeRealEstate;
    }

    public String getTransportationUtilities() {
        return transportationUtilities;
    }
}
