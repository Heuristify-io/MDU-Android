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
import com.heuristify.mdu.helper.Utilities;
import com.heuristify.mdu.mvvm.viewmodel.ConsultationViewModel;
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
    ConsultationViewModel consultationViewModel;
    List<Patient> patientListContainingImage;

    public DashboardFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientListContainingImage = new ArrayList<>();
        dataSyncViewModel = ViewModelProviders.of(this).get(DataSyncViewModel.class);
        consultationViewModel = ViewModelProviders.of(this).get(ConsultationViewModel.class);

    }

    @Override
    public void OnCreateView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

        getDataBinding().buttonAttending22.setOnClickListener(v -> {
            showProgressDialog();
            getAllPatients();
        });
        getDataBinding().buttonAttending2.setOnClickListener(v -> startActivity(new Intent(getActivity(), ConsultationHistoryActivity.class)));
        getDataBinding().textViewDate.setText(Utilities.currentDate());

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeGetPatientList();
        observerErrorMsg();
        observerSyncDataResponse();
        observerSyncDataErrorResponse();
        observeTotalConsultation();
        observerUpdateConsultation();
        observerPendingConsultation();

        consultationViewModel.getAllTotalConsultation();
        consultationViewModel.getAllUpdatedConsultation();
        consultationViewModel.getAllPendingConsultation();

    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_dashboard;
    }

    private void observerPendingConsultation() {
        consultationViewModel.pendingConsultationObserver().observe(getViewLifecycleOwner(),integer ->
                getDataBinding().textViewPending.setText(String.valueOf(integer)));
    }

    private void observerUpdateConsultation() {
        consultationViewModel.updatedConsultationObserver().observe(getViewLifecycleOwner(),integer ->
                getDataBinding().textViewUpdate.setText(String.valueOf(integer)));
    }

    private void observeTotalConsultation() {
        consultationViewModel.totalConsultationObserver().observe(getViewLifecycleOwner(),integer ->
                getDataBinding().textViewTotal.setText(String.valueOf(integer)));
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
            Toast.makeText(mContext, "Unable to upload records", Toast.LENGTH_SHORT).show();
        });

    }

    private void observerSyncDataResponse() {
        dataSyncViewModel.observeUploadRecordMutableResponsive().observe(getViewLifecycleOwner(), syncApiResponse -> {
            dismissProgressDialog();
            if(syncApiResponse.code() == 200){
                Toast.makeText(mContext, "Records uploaded", Toast.LENGTH_SHORT).show();
                consultationViewModel.getAllTotalConsultation();
                consultationViewModel.getAllUpdatedConsultation();
                consultationViewModel.getAllPendingConsultation();

            }else{
                Toast.makeText(mContext, "Unable to upload records", Toast.LENGTH_SHORT).show();
            }
        });
    }


}