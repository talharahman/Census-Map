package com.example.censusmap.view;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.example.censusmap.R;
import com.example.censusmap.model.CensusModel;

public class DataViewHolder extends RecyclerView.ViewHolder {

    private TextView populationTotal;
    private TextView foreignBorn;
    private TextView ageUnder18;
    private TextView ageOver65;
    private TextView employedService;
    private TextView employedSalesOffice;
    private TextView employedConstruction;
    private TextView publicAdministration;
    private TextView educationHealthCareSocial;
    private TextView financeRealEstate;
    private TextView transportationUtilities;


    public DataViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }

    private void findViews() {
        populationTotal = itemView.findViewById(R.id.population_total);
        foreignBorn = itemView.findViewById(R.id.foreign_born);
        ageUnder18 = itemView.findViewById(R.id.age_under_18);
        ageOver65 = itemView.findViewById(R.id.age_over_65);
        employedService = itemView.findViewById(R.id.employed_service);
        employedSalesOffice = itemView.findViewById(R.id.employed_sales_office);
        employedConstruction = itemView.findViewById(R.id.employed_construction);
        publicAdministration = itemView.findViewById(R.id.public_administration);
        educationHealthCareSocial = itemView.findViewById(R.id.education_healthcare_social);
        financeRealEstate = itemView.findViewById(R.id.finance_realEstate);
        transportationUtilities = itemView.findViewById(R.id.transportation_utilities);
    }

    public void onBind(final CensusModel data) {
        populationTotal.setText(data.getTotalPopulation());
        foreignBorn.setText(data.getForeignBorn());
        ageUnder18.setText(data.getAgeUnder18());
        ageOver65.setText(data.getAgeOver65());
        employedService.setText(data.getEmployedService());
        employedSalesOffice.setText(data.getEmployedSalesOffice());
        employedConstruction.setText(data.getEmployedConstruction());
        publicAdministration.setText(data.getPublicAdministration());
        educationHealthCareSocial.setText(data.getEducationHealthCareSocial());
        financeRealEstate.setText(data.getFinanceRealEstate());
        transportationUtilities.setText(data.getTransportationUtilities());
    }
}
