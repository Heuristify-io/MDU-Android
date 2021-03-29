package com.heuristify.mdu.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientHistoryList {

    @SerializedName("data")
    private List<PatientHistory> patientHistoryList;

    public List<PatientHistory> getPatientHistoryList() {
        return patientHistoryList;
    }
}
