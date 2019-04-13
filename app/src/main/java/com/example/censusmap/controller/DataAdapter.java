package com.example.censusmap.controller;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.example.censusmap.R;
import com.example.censusmap.model.CensusModel;
import com.example.censusmap.view.DataViewHolder;

import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<DataViewHolder> {

    private List<CensusModel> dataList;

    public DataAdapter(List<CensusModel> dataList) {
        this.dataList = dataList;
    }

    public List<CensusModel> getDataList() {
        return dataList;
    }

    @NonNull
    @Override
    public DataViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new DataViewHolder(
                LayoutInflater.from(
                        viewGroup.getContext())
                        .inflate(R.layout.data_itemview, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DataViewHolder dataViewHolder, int i) {
        dataViewHolder.onBind(dataList.get(i));
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void passModel(CensusModel model) {
        dataList.add(model);
        notifyDataSetChanged();
    }
}
