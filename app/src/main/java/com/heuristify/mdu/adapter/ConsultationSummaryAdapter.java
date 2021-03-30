package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.databinding.AdapterConsultationSummaryBinding;
import com.heuristify.mdu.pojo.PatientPrescribedMedicine;

import java.util.List;

public class ConsultationSummaryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private Context context;
    private List<PatientPrescribedMedicine> patientPrescribedMedicineList;

    public ConsultationSummaryAdapter(Context context, List<PatientPrescribedMedicine> patientPrescribedMedicineList) {
        this.context = context;
        this.patientPrescribedMedicineList = patientPrescribedMedicineList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterConsultationSummaryBinding adapterConsultationSummaryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_consultation_summary, parent, false);
        return new ConsultationViewHolder(adapterConsultationSummaryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConsultationViewHolder consultationViewHolder = (ConsultationViewHolder)holder;
        consultationViewHolder.adapterConsultationSummaryBinding.setPatientPrescribedMedicine(patientPrescribedMedicineList.get(position));
    }

    @Override
    public int getItemCount() {
        return patientPrescribedMedicineList.size();
    }

    private static class ConsultationViewHolder extends RecyclerView.ViewHolder {
        AdapterConsultationSummaryBinding adapterConsultationSummaryBinding;
        public ConsultationViewHolder(AdapterConsultationSummaryBinding adapterConsultationSummaryBinding) {
            super(adapterConsultationSummaryBinding.getRoot());
            this.adapterConsultationSummaryBinding = adapterConsultationSummaryBinding;
        }
    }
}
