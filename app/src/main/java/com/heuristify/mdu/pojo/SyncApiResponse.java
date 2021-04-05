package com.heuristify.mdu.pojo;

import com.google.gson.annotations.SerializedName;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.DoctorAttendance;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.database.entity.PrescribedMedicine;
import com.heuristify.mdu.database.entity.StockMedicine;

import java.util.List;

public class SyncApiResponse {
    @SerializedName("success")
    private boolean success;

    @SerializedName("msg")
    private String msg;

    @SerializedName("stocks")
    private List<StockMedicine> stockMedicineList;

    @SerializedName("patients")
    private List<Patient> patientList;

    @SerializedName("consultations")
    private List<DiagnosisAndMedicine> diagnosisAndMedicineList;

    @SerializedName("prescribed_medicines")
    private List<PrescribedMedicine> prescribedMedicineList;

    @SerializedName("attendance")
    private List<DoctorAttendance> doctorAttendanceList;


    public boolean isSuccess() {
        Boolean b1=Boolean.parseBoolean("true");
        success = b1;
        return success;
    }

    public String getMsg() {
        return msg;
    }

    public List<StockMedicine> getStockMedicineList() {
        return stockMedicineList;
    }

    public List<Patient> getPatientList() {
        return patientList;
    }

    public List<DiagnosisAndMedicine> getDiagnosisAndMedicineList() {
        return diagnosisAndMedicineList;
    }

    public List<PrescribedMedicine> getPrescribedMedicineList() {
        return prescribedMedicineList;
    }

    public List<DoctorAttendance> getDoctorAttendanceList() {
        return doctorAttendanceList;
    }
}
