package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.databinding.NestedAdapterMedicineNameBinding;
import com.heuristify.mdu.pojo.MedicineName;

import java.util.List;

public class NestedMedicineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private final Context context;
    private final List<MedicineName> medicineNameList;

    public NestedMedicineAdapter(Context context, List<MedicineName> medicineNameList) {
        this.context = context;
        this.medicineNameList = medicineNameList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NestedAdapterMedicineNameBinding nestedAdapterMedicineNameBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.nested_adapter_medicine_name, parent, false);
        return new NestedMedicineViewHolder(nestedAdapterMedicineNameBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        NestedMedicineViewHolder viewHolder = (NestedMedicineViewHolder)holder;
        viewHolder.nestedAdapterMedicineNameBinding.setMedicineName(medicineNameList.get(position));

    }

    @Override
    public int getItemCount() {
        return medicineNameList.size();
    }

    private class NestedMedicineViewHolder extends RecyclerView.ViewHolder {
        NestedAdapterMedicineNameBinding nestedAdapterMedicineNameBinding;
        public NestedMedicineViewHolder(NestedAdapterMedicineNameBinding nestedAdapterMedicineNameBinding) {
            super(nestedAdapterMedicineNameBinding.getRoot());
            this.nestedAdapterMedicineNameBinding = nestedAdapterMedicineNameBinding;
        }
    }
}
