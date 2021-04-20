package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.database.entity.StockMedicine;

import com.heuristify.mdu.databinding.AdapterRemainingMedQuantityBinding;

import java.util.List;

public class RemainingMedicineQuantityAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    List<StockMedicine> medicineList;
    Context context;

    public RemainingMedicineQuantityAdapter(List<StockMedicine> medicineList, Context context) {
        this.medicineList = medicineList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterRemainingMedQuantityBinding adapterMedicineSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_remaining_med_quantity, parent, false);
        return new MedicineSearchViewHolder(adapterMedicineSearchBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MedicineSearchViewHolder viewHolder = (MedicineSearchViewHolder) holder;
        viewHolder.adapterMedicineSearchBinding.setMedicine(medicineList.get(position));

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    private static class MedicineSearchViewHolder extends RecyclerView.ViewHolder {

        AdapterRemainingMedQuantityBinding adapterMedicineSearchBinding;

        public MedicineSearchViewHolder(AdapterRemainingMedQuantityBinding adapterMedicineSearchBinding) {
            super(adapterMedicineSearchBinding.getRoot());
            this.adapterMedicineSearchBinding = adapterMedicineSearchBinding;

        }
    }
}
