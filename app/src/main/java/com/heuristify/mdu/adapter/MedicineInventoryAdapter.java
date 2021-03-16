package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.databinding.AdapterInventoryMedicineBinding;
import com.heuristify.mdu.pojo.Medicine;

import java.util.List;

public class MedicineInventoryAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    List<Medicine> medicineList;
    Context context;

    public MedicineInventoryAdapter(List<Medicine> medicineList, Context context) {
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
    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    private class InventoryViewHolder extends RecyclerView.ViewHolder {
        AdapterInventoryMedicineBinding adapterInventoryMedicineBinding;
        public InventoryViewHolder(AdapterInventoryMedicineBinding adapterInventoryMedicineBinding) {
            super(adapterInventoryMedicineBinding.getRoot());
            this.adapterInventoryMedicineBinding = adapterInventoryMedicineBinding;
        }
    }
}
