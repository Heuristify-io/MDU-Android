package com.heuristify.mdu.helper;

import com.heuristify.mdu.database.entity.StockMedicine;

public class StoreClickWidget {

    StockMedicine stockMedicine;
    String editTextFrequency;
    String editTextDays;



    public StockMedicine getStockMedicine() {
        return stockMedicine;
    }

    public void setStockMedicine(StockMedicine stockMedicine) {
        this.stockMedicine = stockMedicine;
    }

    public String getEditTextFrequency() {
        return editTextFrequency;
    }

    public void setEditTextFrequency(String editTextFrequency) {
        this.editTextFrequency = editTextFrequency;
    }

    public String getEditTextDays() {
        return editTextDays;
    }

    public void setEditTextDays(String editTextDays) {
        this.editTextDays = editTextDays;
    }
}
