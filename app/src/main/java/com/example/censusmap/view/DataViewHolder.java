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


    public DataViewHolder(@NonNull View itemView) {
        super(itemView);
        findViews();
    }

    private void findViews() {
        populationTotal = itemView.findViewById(R.id.population_total);
        foreignBorn = itemView.findViewById(R.id.foreign_born);
        ageUnder18 = itemView.findViewById(R.id.age_under_18);
        ageOver65 = itemView.findViewById(R.id.age_over_65);
    }

    public void onBind(final CensusModel data) {
        populationTotal.setText(data.getTotalPopulation());
        foreignBorn.setText(data.getForeignBorn());
        ageUnder18.setText(data.getAgeUnder18());
        ageOver65.setText(data.getAgeOver65());
    }
}
