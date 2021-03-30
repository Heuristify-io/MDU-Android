package com.heuristify.mdu.adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.databinding.AdapterSearchMedicineBinding;
import com.heuristify.mdu.mvvm.OnItemClick;

import java.util.List;

public class SearchMedicineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private final Context context;
    private final List<StockMedicine> stockMedicineList;
    private OnItemClick onItemClick;

    public SearchMedicineAdapter(Context context, List<StockMedicine> stockMedicineList) {
        this.context = context;
        this.stockMedicineList = stockMedicineList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterSearchMedicineBinding adapterSearchMedicineBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_search_medicine, parent, false);
        return new SearchMedicneViewHolder(adapterSearchMedicineBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        SearchMedicneViewHolder viewHolder = (SearchMedicneViewHolder)holder;
        viewHolder.adapterSearchMedicineBinding.setStockMedicine(stockMedicineList.get(position));
        viewHolder.adapterSearchMedicineBinding.textViewSearchMedicineName.setOnClickListener(v -> {
            StockMedicine stockMedicine = stockMedicineList.get(position);
            onItemClick.onRecyclerViewItemClick(stockMedicine);
            stockMedicineList.clear();
            notifyDataSetChanged();
        });

        viewHolder.adapterSearchMedicineBinding.textViewSearchMedicineQuantity.setOnClickListener(v -> {
            StockMedicine stockMedicine = stockMedicineList.get(position);
            onItemClick.onRecyclerViewItemClick(stockMedicine);
            stockMedicineList.clear();
            notifyDataSetChanged();
        });

    }

    public void setItemClick(OnItemClick itemClick){
        this.onItemClick = itemClick;
    }

    @Override
    public int getItemCount() {
        return stockMedicineList.size();
    }

    private static class SearchMedicneViewHolder extends RecyclerView.ViewHolder {
        AdapterSearchMedicineBinding adapterSearchMedicineBinding;
        public SearchMedicneViewHolder(AdapterSearchMedicineBinding adapterSearchMedicineBinding) {
            super(adapterSearchMedicineBinding.getRoot());
            this.adapterSearchMedicineBinding = adapterSearchMedicineBinding;
        }
    }
}
