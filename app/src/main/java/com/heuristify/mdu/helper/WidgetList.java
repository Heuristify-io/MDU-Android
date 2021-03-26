package com.heuristify.mdu.helper;

import com.heuristify.mdu.database.entity.StockMedicine;

public class WidgetList {

    StockMedicine stockMedicine;
    String edtSpinner = "";

    public StockMedicine getStockMedicine() {
        return stockMedicine;
    }

    public void setStockMedicine(StockMedicine stockMedicine) {
        this.stockMedicine = stockMedicine;
    }

    public String getEdtSpinner() {
        return edtSpinner;
    }

    public void setEdtSpinner(String edtSpinner) {
        this.edtSpinner = edtSpinner;
    }
}
