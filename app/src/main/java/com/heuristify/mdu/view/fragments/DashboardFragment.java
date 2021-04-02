package com.heuristify.mdu.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseFragment;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.databinding.FragmentDashboardBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.mvvm.viewmodel.DataSyncViewModel;
import com.heuristify.mdu.view.activities.ConsultationHistoryActivity;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends BindingBaseFragment<FragmentDashboardBinding>implements LifecycleOwner {


    public static final String ARG_PARAM2;
    static {
        ARG_PARAM2 = "param2";
    }

    public static final String TAG = "DashboardFragment";
    DataSyncViewModel dataSyncViewModel;
    List<Patient> patientListContainingImage;

    public DashboardFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientListContainingImage = new ArrayList<>();
        dataSyncViewModel = ViewModelProviders.of(this).get(DataSyncViewModel.class);
    }

    @Override
    public void OnCreateView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

        getDataBinding().buttonAttending22.setOnClickListener(v -> {
            showProgressDialog();
            getAllPatients();
        });
        getDataBinding().buttonAttending2.setOnClickListener(v -> startActivity(new Intent(getActivity(), ConsultationHistoryActivity.class)));

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeGetPatientList();
        observerErrorMsg();
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_dashboard;
    }

    private void getAllPatients() {
        //sync field value
        dataSyncViewModel.callGetAllPatientMethod(0);
    }

    private void observeGetPatientList() {
        dataSyncViewModel.getPatientList().observe(getViewLifecycleOwner(), patientList -> {
            dismissProgressDialog();
            DisplayLog.showLog(TAG,"imageResponse "+patientList);
        });

    }

    private void observerErrorMsg() {
        dataSyncViewModel.errorMsg().observe(getViewLifecycleOwner(),String ->{
            dismissProgressDialog();
            DisplayLog.showLog(TAG,"imageError "+String);
        });
    }
}