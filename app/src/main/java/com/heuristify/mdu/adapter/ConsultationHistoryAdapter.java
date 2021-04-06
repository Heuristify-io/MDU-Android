package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.databinding.AdapterConsultationHistoryBinding;
import com.heuristify.mdu.interfaces.OnItemClickId;
import com.heuristify.mdu.pojo.ConsultationHistory;
import com.heuristify.mdu.pojo.MedicineName;

import java.util.ArrayList;
import java.util.List;

public class ConsultationHistoryAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final Context context;
    private final List<ConsultationHistory> consultationHistoryList;
    private NestedMedicineAdapter nestedMedicineAdapter;
    private List<MedicineName> childMedicineNamesList;
    private OnItemClickId onItemClickId;
    private int focus_position = -1;

    public ConsultationHistoryAdapter(Context context, List<ConsultationHistory> consultationHistoryList) {
        this.context = context;
        childMedicineNamesList = new ArrayList<>();
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
        ConsultationHistoryViewHolder viewHolder = (ConsultationHistoryViewHolder) holder;
        viewHolder.adapterConsultationHistoryBinding.setConsultationHistory(consultationHistoryList.get(position));
        if(focus_position == position){
            initializeRecycleView(viewHolder);
            viewHolder.adapterConsultationHistoryBinding.group2.setVisibility(View.VISIBLE);
            viewHolder.adapterConsultationHistoryBinding.imageView2.setImageResource(R.drawable.chevron_up_icon_24px);

        }else{
            viewHolder.adapterConsultationHistoryBinding.group2.setVisibility(View.GONE);
            viewHolder.adapterConsultationHistoryBinding.imageView2.setImageResource(R.drawable.chevron_down_24px);
        }



        viewHolder.adapterConsultationHistoryBinding.imageView2.setOnClickListener(v -> checkView(viewHolder, position));
        viewHolder.adapterConsultationHistoryBinding.textViewName.setOnClickListener(v -> checkView(viewHolder, position));


    }

    public void setOnItemClickId(OnItemClickId onItemClickId) {
        this.onItemClickId = onItemClickId;
    }

    private void checkView(ConsultationHistoryViewHolder viewHolder, int position) {
        if(focus_position == position){
            focus_position = -1;
            notifyDataSetChanged();
        }else{
            onItemClickId.onRecyclerViewItemClick(position,consultationHistoryList.get(position).getId());
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

    public void updateList(int position,List<MedicineName> medicineNamesList){
        focus_position=position;
        if(!childMedicineNamesList.isEmpty()){
            childMedicineNamesList.clear();
        }
        childMedicineNamesList.addAll(medicineNamesList);
        notifyDataSetChanged();
       // nestedMedicineAdapter.notifyDataSetChanged();
    }

    private void initializeRecycleView(ConsultationHistoryViewHolder viewHolder) {

        viewHolder.adapterConsultationHistoryBinding.recyclerView.setHasFixedSize(true);
        viewHolder.adapterConsultationHistoryBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        viewHolder.adapterConsultationHistoryBinding.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        nestedMedicineAdapter = new NestedMedicineAdapter(context, childMedicineNamesList);
        viewHolder.adapterConsultationHistoryBinding.recyclerView.setAdapter(nestedMedicineAdapter);
        viewHolder.adapterConsultationHistoryBinding.recyclerView.setItemAnimator(null);
    }
}
