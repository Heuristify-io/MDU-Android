package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.databinding.AdapterInventoryMedicineBinding;
import com.heuristify.mdu.database.entity.StockMedicine;

import java.util.List;

public class MedicineInventoryAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<StockMedicine> medicineList;
    Context context;

    public MedicineInventoryAdapter(List<StockMedicine> medicineList, Context context) {
        this.medicineList = medicineList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterInventoryMedicineBinding adapterInventoryMedicineBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_inventory_medicine, parent, false);
        return new InventoryViewHolder(adapterInventoryMedicineBinding);

    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InventoryViewHolder inventoryViewHolder = (InventoryViewHolder) holder;
        inventoryViewHolder.adapterInventoryMedicineBinding.setMedicine(medicineList.get(position));
        if(Integer.parseInt(medicineList.get(position).getStock_medicine_quantity()) < 20){
            inventoryViewHolder.adapterInventoryMedicineBinding.textViewMedQuantAdapter.setTextColor(context.getResources().getColor(R.color.red));
            inventoryViewHolder.adapterInventoryMedicineBinding.textViewTotalAdapter.setTextColor(context.getResources().getColor(R.color.red));
        }else{
            inventoryViewHolder.adapterInventoryMedicineBinding.textViewMedQuantAdapter.setTextColor(context.getResources().getColor(R.color.dark2));
            inventoryViewHolder.adapterInventoryMedicineBinding.textViewTotalAdapter.setTextColor(context.getResources().getColor(R.color.dark2));
        }
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    private static class InventoryViewHolder extends RecyclerView.ViewHolder {
        AdapterInventoryMedicineBinding adapterInventoryMedicineBinding;
        public InventoryViewHolder(AdapterInventoryMedicineBinding adapterInventoryMedicineBinding) {
            super(adapterInventoryMedicineBinding.getRoot());
            this.adapterInventoryMedicineBinding = adapterInventoryMedicineBinding;
        }
    }
}
