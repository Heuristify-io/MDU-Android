package com.heuristify.mdu.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.databinding.AdapterMedicineSearchBinding;
import com.heuristify.mdu.pojo.Medicine;
import com.heuristify.mdu.view.activities.AddNewInventoryDetailsActivity;

import java.util.List;

public class MedicineSearchAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<Medicine> medicineList;
    Context context;

    public MedicineSearchAdapter(List<Medicine> medicineList, Context context) {
        this.medicineList = medicineList;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterMedicineSearchBinding adapterMedicineSearchBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_medicine_search, parent, false);
        return new MedicineSearchViewHolder(adapterMedicineSearchBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MedicineSearchViewHolder viewHolder = (MedicineSearchViewHolder) holder;
        viewHolder.adapterMedicineSearchBinding.setMedicine(medicineList.get(position));
        viewHolder.adapterMedicineSearchBinding.relative.setOnClickListener(v -> context.startActivity(new Intent(context, AddNewInventoryDetailsActivity.class)
                .putExtra("medicine_name", medicineList.get(position).getMedicine_name())
                .putExtra("from", medicineList.get(position).getMedicine_form())
                .putExtra("strength", medicineList.get(position).getMedicine_strength())
                .putExtra("unit", medicineList.get(position).getMedicine_units())
        ));

    }

    @Override
    public int getItemCount() {
        return medicineList.size();
    }

    private static class MedicineSearchViewHolder extends RecyclerView.ViewHolder {

        AdapterMedicineSearchBinding adapterMedicineSearchBinding;

        public MedicineSearchViewHolder(AdapterMedicineSearchBinding adapterMedicineSearchBinding) {
            super(adapterMedicineSearchBinding.getRoot());
            this.adapterMedicineSearchBinding = adapterMedicineSearchBinding;

        }
    }
}
