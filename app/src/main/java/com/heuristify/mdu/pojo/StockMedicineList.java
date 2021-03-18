package com.heuristify.mdu.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class StockMedicineList {


    @SerializedName("success")
    private Boolean success;

    @SerializedName("medicine")
    private Medicine medicine;

    @SerializedName("msg")
    private String msg;

    @SerializedName("stocks")
    private List<StockMedicine> stockMedicineListList;

    public Boolean getSuccess() {
        return success;
    }

    public Medicine getMedicine() {
        return medicine;
    }

    public String getMsg() {
        return msg;
    }

    public List<StockMedicine> getStockMedicineListList() {
        return stockMedicineListList;
    }

}
