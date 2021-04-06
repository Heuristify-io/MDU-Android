package com.heuristify.mdu.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseFragment;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.databinding.FragmentDashboardBinding;
import com.heuristify.mdu.helper.Constant;
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
        observerSyncDataResponse();
        observerSyncDataErrorResponse();
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_dashboard;
    }

    private void getAllPatients() {
        //sync field value
        dataSyncViewModel.callGetAllPatientMethod(Constant.patient_sync_zero);
    }

    private void observeGetPatientList() {
        dataSyncViewModel.getPatientList().observe(getViewLifecycleOwner(), patientList -> {
            DisplayLog.showLog(TAG,"imageResponse "+patientList);
            dataSyncViewModel.uploadRecords(Constant.patient_sync_zero,Constant.diagnosis_sync_zero,Constant.prescribed_medicine_sync_zero);

        });

    }

    private void observerErrorMsg() {
        dataSyncViewModel.errorMsg().observe(getViewLifecycleOwner(),String ->{
            DisplayLog.showLog(TAG,"imageError "+String);
            dataSyncViewModel.uploadRecords(Constant.patient_sync_zero,Constant.diagnosis_sync_zero,Constant.prescribed_medicine_sync_zero);
        });
    }

    private void observerSyncDataErrorResponse() {
        dataSyncViewModel.getSyncMutableLiveDataErrorResponse().observe(getViewLifecycleOwner(),String ->{
            dismissProgressDialog();
            DisplayLog.showLog(TAG,"syncErrorResponse "+String);
        });

    }

    private void observerSyncDataResponse() {
        dataSyncViewModel.observeUploadRecordMutableResponsive().observe(getViewLifecycleOwner(), syncApiResponse -> {
            DisplayLog.showLog(TAG,"observerSyncDataResponse "+syncApiResponse.code());
            dismissProgressDialog();
            if(syncApiResponse.code() == 200){
                Toast.makeText(mContext, "Data Sync Successfully", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(mContext, "Data Not Sync", Toast.LENGTH_SHORT).show();
            }
        });
    }
}