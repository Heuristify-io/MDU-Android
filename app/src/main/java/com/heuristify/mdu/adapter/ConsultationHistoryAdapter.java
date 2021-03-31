package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.databinding.AdapterConsultationHistoryBinding;
import com.heuristify.mdu.pojo.ConsultationHistory;

import java.util.List;

public class ConsultationHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private Context context;
    private List<ConsultationHistory> consultationHistoryList;

    public ConsultationHistoryAdapter(Context context, List<ConsultationHistory> consultationHistoryList) {
        this.context = context;
        this.consultationHistoryList = consultationHistoryList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        AdapterConsultationHistoryBinding adapterConsultationHistoryBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.adapter_consultation_history, parent, false);
        return new ConsultationHistoryViewHolder(adapterConsultationHistoryBinding);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ConsultationHistoryViewHolder viewHolder = (ConsultationHistoryViewHolder)holder;
        viewHolder.adapterConsultationHistoryBinding.setConsultationHistory(consultationHistoryList.get(position));

        viewHolder.adapterConsultationHistoryBinding.imageView2.setOnClickListener(v -> {
            checkView(viewHolder,position);

        });

        viewHolder.adapterConsultationHistoryBinding.textViewName.setOnClickListener(v -> checkView(viewHolder,position));


    }

    private void checkView(ConsultationHistoryViewHolder viewHolder, int position) {
        if(viewHolder.adapterConsultationHistoryBinding.group2.getVisibility() == View.VISIBLE){
            viewHolder.adapterConsultationHistoryBinding.group2.setVisibility(View.GONE);
            viewHolder.adapterConsultationHistoryBinding.imageView2.setImageResource(R.drawable.chevron_down_24px);
        }else{
            viewHolder.adapterConsultationHistoryBinding.group2.setVisibility(View.VISIBLE);
            viewHolder.adapterConsultationHistoryBinding.imageView2.setImageResource(R.drawable.chevron_up_icon_24px);
            //get data medicine name from db
        }
    }

    @Override
    public int getItemCount() {
        return consultationHistoryList.size();
    }

    private static class ConsultationHistoryViewHolder extends RecyclerView.ViewHolder {
        AdapterConsultationHistoryBinding adapterConsultationHistoryBinding;
        public ConsultationHistoryViewHolder(AdapterConsultationHistoryBinding adapterConsultationHistoryBinding) {
            super(adapterConsultationHistoryBinding.getRoot());
            this.adapterConsultationHistoryBinding = adapterConsultationHistoryBinding;
        }
    }
}
