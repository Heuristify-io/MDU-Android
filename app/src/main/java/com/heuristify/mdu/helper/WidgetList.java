package com.heuristify.mdu.helper;

import com.heuristify.mdu.database.entity.StockMedicine;

public class WidgetList {

    StockMedicine stockMedicine;
    String frequencySpinner = "";
    String editTextDays = "";

    public StockMedicine getStockMedicine() {
        return stockMedicine;
    }

    public void setStockMedicine(StockMedicine stockMedicine) {
        this.stockMedicine = stockMedicine;
    }

    public String getFrequencySpinner() {
        return frequencySpinner;
    }

    public void setFrequencySpinner(String frequencySpinner) {
        this.frequencySpinner = frequencySpinner;
    }

    public String getEditTextDays() {
        return editTextDays;
    }

    public void setEditTextDays(String editTextDays) {
        this.editTextDays = editTextDays;
    }
}
