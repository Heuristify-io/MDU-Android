package com.heuristify.mdu.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.databinding.AdapterPatientHistoryBinding;
import com.heuristify.mdu.pojo.PatientHistory;

import java.util.List;

public class PatientHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final List<PatientHistory> patientHistoryList;

    public PatientHistoryAdapter(List<PatientHistory> patientHistoryList) {
        this.patientHistoryList = patientHistoryList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterPatientHistoryBinding adapterPatientHistoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_patient_history, parent, false);
        return new PatientHistoryViewHolder(adapterPatientHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PatientHistoryViewHolder viewHolder = (PatientHistoryViewHolder) holder;
        viewHolder.adapterPatientHistoryBinding.setPatientHistory(patientHistoryList.get(position));
    }

    @Override
    public int getItemCount() {
        return patientHistoryList.size();
    }

    private static class PatientHistoryViewHolder extends RecyclerView.ViewHolder {
        AdapterPatientHistoryBinding adapterPatientHistoryBinding;

        public PatientHistoryViewHolder(AdapterPatientHistoryBinding adapterPatientHistoryBinding) {
            super(adapterPatientHistoryBinding.getRoot());
            this.adapterPatientHistoryBinding = adapterPatientHistoryBinding;
        }
    }
}
