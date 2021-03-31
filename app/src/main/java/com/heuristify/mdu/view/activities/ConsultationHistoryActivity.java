package com.heuristify.mdu.view.activities;


import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.ConsultationHistoryAdapter;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityConsultationHistoryBinding;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.mvvm.viewmodel.ConsultationViewModel;
import com.heuristify.mdu.pojo.ConsultationHistory;

import java.util.ArrayList;
import java.util.List;

public class ConsultationHistoryActivity extends BindingBaseActivity<ActivityConsultationHistoryBinding> implements OnClickHandlerInterface, LifecycleOwner {

    private List<ConsultationHistory> consultationHistoryList;
    private ConsultationHistoryAdapter consultationHistoryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LifecycleOwner lifecycleOwner = this;
        Observer observer;
        getDataBinding().setClickHandler(this);
        initializeRecycleView();

        ConsultationViewModel consultationViewModel = ViewModelProviders.of(this).get(ConsultationViewModel.class);
        observer = (Observer<List<ConsultationHistory>>) consultationHistoryList2 -> {

            if (consultationHistoryList2 != null) {
                consultationHistoryList.addAll(consultationHistoryList2);
                consultationHistoryAdapter.notifyDataSetChanged();
            }

        };

        consultationViewModel.getConsultationHistoryMutableLiveData().observe(lifecycleOwner, observer);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_consultation_history;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.textViewBack:
                finish();
                break;
            case R.id.buttonBackToDashBoard:
                finish();
                break;
        }
    }

    private void initializeRecycleView() {
        consultationHistoryList = new ArrayList<>();
        getDataBinding().recyclerViewConsultationHistory.setHasFixedSize(true);
        getDataBinding().recyclerViewConsultationHistory.setItemAnimator(new DefaultItemAnimator());
        getDataBinding().recyclerViewConsultationHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        consultationHistoryAdapter = new ConsultationHistoryAdapter(this, consultationHistoryList);
        getDataBinding().recyclerViewConsultationHistory.setAdapter(consultationHistoryAdapter);
        getDataBinding().recyclerViewConsultationHistory.setItemAnimator(null);
    }
}