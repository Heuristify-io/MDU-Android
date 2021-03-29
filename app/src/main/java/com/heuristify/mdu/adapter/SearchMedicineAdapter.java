package com.heuristify.mdu.adapter;


import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.database.entity.StockMedicine;

import java.util.List;

public class SearchMedicineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<StockMedicine> stockMedicineList;

    public SearchMedicineAdapter(Context context, List<StockMedicine> stockMedicineList) {
        this.context = context;
        this.stockMedicineList = stockMedicineList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
