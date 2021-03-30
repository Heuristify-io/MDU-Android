package com.heuristify.mdu.interfaces;

import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.adapter.AddDiagnosisAndMedicineAdapter;
import com.heuristify.mdu.database.entity.StockMedicine;

public interface OnItemClick {

    void onRecyclerViewItemClick(StockMedicine stockMedicine);
}
