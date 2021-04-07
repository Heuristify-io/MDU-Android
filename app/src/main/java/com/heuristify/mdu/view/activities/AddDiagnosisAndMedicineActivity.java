package com.heuristify.mdu.view.activities;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.AddDiagnosisAndMedicineAdapter;
import com.heuristify.mdu.adapter.PatientHistoryAdapter;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.database.entity.PrescribedMedicine;
import com.heuristify.mdu.databinding.ActivityAddDiagnosisAndMedicineBinding;
import com.heuristify.mdu.helper.Constant;
import com.heuristify.mdu.helper.StoreClickWidget;
import com.heuristify.mdu.helper.Utilities;
import com.heuristify.mdu.helper.WidgetList;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.mvvm.viewmodel.PatientViewModel;
import com.heuristify.mdu.pojo.PatientHistory;
import com.heuristify.mdu.pojo.PatientHistoryList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class AddDiagnosisAndMedicineActivity extends BindingBaseActivity<ActivityAddDiagnosisAndMedicineBinding> implements OnClickHandlerInterface {
    private List<WidgetList> widgetLists;
    private List<StoreClickWidget> storeClickWidgetList;
    private List<PatientHistory> patientHistoryList;
    private AddDiagnosisAndMedicineAdapter addDiagnosisAndMedicineAdapter;
    private PatientHistoryAdapter patientHistoryAdapter;
    private Patient patient;
    private final String TAG = "AddDiagnosisAndMedicineActivity";
    private Dialog mDialog;
    PatientViewModel patientViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Observer observer;
        getDataBinding().setClickHandler(this);
        if (getIntent().getExtras() != null) {
            patient = (Patient) getIntent().getSerializableExtra("patient");
        }



        initializeRecycleView();
        PatientViewModel patientViewModel = ViewModelProviders.of(this).get(PatientViewModel.class);


        observer = (Observer<Response<PatientHistoryList>>) responseBodyResponse -> {
            dismissProgressDialog();
            if (responseBodyResponse.isSuccessful()) {
                PatientHistoryList patientHistoryList1 = responseBodyResponse.body();
                if (patientHistoryList1 != null) {
                    patientHistoryList.addAll(patientHistoryList1.getPatientHistoryList());
                    patientHistoryAdapter.notifyDataSetChanged();
                }
            }
        };

        patientViewModel.getError_msg().observe(this, s -> dismissProgressDialog());

        Log.e("patient_id",""+patient.getId());

        patientViewModel.checkPatient().observe(this,integer -> {
            Log.e("patient_id2",""+integer);
            if(integer > 0){
                showProgressDialog();
                patientViewModel.getPatientResponseMutableLiveData(patient.getId()).observe(this, observer);
            }
        });
        patientViewModel.checkPatientSync(patient.getId(), Constant.patient_sync_one);


    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_diagnosis_and_medicine;
    }

    private void initializeRecycleView() {
        widgetLists = new ArrayList<>();
        storeClickWidgetList = new ArrayList<>();
        getDataBinding().recyclerViewPrescribedMedicine.setHasFixedSize(true);
        getDataBinding().recyclerViewPrescribedMedicine.setItemAnimator(new DefaultItemAnimator());
        getDataBinding().recyclerViewPrescribedMedicine.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        addDiagnosisAndMedicineAdapter = new AddDiagnosisAndMedicineAdapter(widgetLists, this, storeClickWidgetList);
        getDataBinding().recyclerViewPrescribedMedicine.setAdapter(addDiagnosisAndMedicineAdapter);
        getDataBinding().recyclerViewPrescribedMedicine.setItemAnimator(null);

        storeClickWidgetList.add(new StoreClickWidget());
        widgetLists.add(new WidgetList());
        addDiagnosisAndMedicineAdapter.notifyItemInserted(widgetLists.size());
        getDataBinding().recyclerViewPrescribedMedicine.scrollToPosition(widgetLists.size());

        patientHistoryList = new ArrayList<>();
        getDataBinding().recyclerViewPatientHistory.setHasFixedSize(true);
        getDataBinding().recyclerViewPatientHistory.setItemAnimator(new DefaultItemAnimator());
        getDataBinding().recyclerViewPatientHistory.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        patientHistoryAdapter = new PatientHistoryAdapter(patientHistoryList);
        getDataBinding().recyclerViewPatientHistory.setAdapter(patientHistoryAdapter);
        getDataBinding().recyclerViewPatientHistory.setItemAnimator(null);


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
            case R.id.textViewAddAnotherMedicine:
                addAnotherItem();
                break;
            case R.id.imageViewAdd:
                addAnotherItem();
                break;
            case R.id.imageViewUp:
                if (getDataBinding().recyclerViewPatientHistory.getVisibility() == View.VISIBLE) {
                    getDataBinding().recyclerViewPatientHistory.setVisibility(View.GONE);
                } else {
                    getDataBinding().recyclerViewPatientHistory.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.buttonNextConsultation:
                if (dataBinding.editTextPatientDiagnosis.getText().toString().isEmpty() || dataBinding.editTextPatientDiagnosisDes.getText().toString().isEmpty()) {
                    Toast.makeText(mContext, "Patient Diagnosis and Description  Required", Toast.LENGTH_SHORT).show();
                } else {
                    addIntoLocalDb();
                }
                break;
        }
    }

    @SuppressLint("SetTextI18n")
    private void showMedicineDialog(String medicineName, String quantity) {
        mDialog = new Dialog(this);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_medicine_quantity_dialog_view);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.show();
        TextView textViewName = mDialog.findViewById(R.id.textViewTotal);
        TextView textViewQuantity = mDialog.findViewById(R.id.textViewMedicineQuantity);
        textViewName.setText(medicineName);
        textViewQuantity.setText("Medicine Quantity " + quantity);
        Button btnRetry = mDialog.findViewById(R.id.buttonRetry);

        btnRetry.setOnClickListener(v -> mDialog.dismiss());


    }

    private void addIntoLocalDb() {

        new Thread(() -> {

            for (int i = 0; i < storeClickWidgetList.size(); i++) {
                if (storeClickWidgetList.get(i).getStockMedicine() != null && storeClickWidgetList.get(i).getEditTextFrequency() != null && storeClickWidgetList.get(i).getEditTextDays() != null) {
                    int mul = 0;

                    if (storeClickWidgetList.get(i).getEditTextFrequency().equals("T.D.S")) {
                        mul = 2 * Integer.parseInt(storeClickWidgetList.get(i).getEditTextDays());

                    } else {
                        mul = 3 * Integer.parseInt(storeClickWidgetList.get(i).getEditTextDays());
                    }


                    //check medicine quantity available
                    String quantity = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().getStockMedicinesQuantity(storeClickWidgetList.get(i).getStockMedicine().getStock_medicine_medicineId());
                    if (Integer.parseInt(quantity) < mul) {
                        int finalI = i;
                        runOnUiThread(() -> showMedicineDialog(storeClickWidgetList.get(finalI).getStockMedicine().getStock_medicine_name(), quantity));
                        break;

                    }
                    if (i == storeClickWidgetList.size() - 1) {
                        addDiagnosisAndPrescribedMedicine();
                        break;
                    }
                } else {
                    runOnUiThread(() -> Toast.makeText(mContext, "Fill all fields", Toast.LENGTH_SHORT).show());
                    break;
                }
            }

        }).start();
    }

    private void addDiagnosisAndPrescribedMedicine() {
        List<PrescribedMedicine> prescribedMedicineList = new ArrayList<>();
        new Thread(() -> {
            DiagnosisAndMedicine diagnosisAndMedicine = new DiagnosisAndMedicine();
            diagnosisAndMedicine.setPatientDiagnosis(dataBinding.editTextPatientDiagnosis.getText().toString());
            diagnosisAndMedicine.setDescription(dataBinding.editTextPatientDiagnosisDes.getText().toString());
            diagnosisAndMedicine.setLat(0.0);
            diagnosisAndMedicine.setLng(0.0);
            diagnosisAndMedicine.setCreated_date(Utilities.currentDate());
            diagnosisAndMedicine.setPatientId(patient.getId());
//            PatientWithDiagnosisAndMedicine patientWithDiagnosisAndMedicine = new PatientWithDiagnosisAndMedicine(patient, diagnosisAndMedicine);
            int id = (int) MyApplication.getInstance().getLocalDb(mContext).getAppDatabase().diagnosisAndMedicineDao().insertPatientDiagnosis(diagnosisAndMedicine);
            if (id > 0) {
                for (int i = 0; i < storeClickWidgetList.size(); i++) {
                    PrescribedMedicine prescribedMedicine = new PrescribedMedicine();
                    prescribedMedicine.setConsultationId(id);
                    prescribedMedicine.setMedicineId(storeClickWidgetList.get(i).getStockMedicine().getStock_medicine_medicineId());
                    prescribedMedicine.setActualMedicineId(storeClickWidgetList.get(i).getStockMedicine().getMedicineId());
                    prescribedMedicine.setDays(Integer.parseInt(storeClickWidgetList.get(i).getEditTextDays()));
                    prescribedMedicine.setFrequency(storeClickWidgetList.get(i).getEditTextFrequency());
                    prescribedMedicine.setFrequencyNum(1);
                    prescribedMedicineList.add(prescribedMedicine);
                }

//                PrescribedMedicineWithConsultation prescribedMedicineWithConsultation = new PrescribedMedicineWithConsultation(diagnosisAndMedicine, prescribedMedicineList);
                long[] ids = MyApplication.getInstance().getLocalDb(mContext).getAppDatabase().prescribedMedicineDao().insertPrescribedMedicine(prescribedMedicineList);
                if (ids.length > 0) {
                    runOnUiThread(() -> Toast.makeText(mContext, "Consultation Added Successfully", Toast.LENGTH_SHORT).show());
                    Intent intent = new Intent(AddDiagnosisAndMedicineActivity.this, ConsultationSummaryActivity.class);
                    intent.putExtra("patient",patient);
                    intent.putExtra("consultation_id",id);
                    startActivity(intent);
                    finish();
                }


            }

        }).start();
    }

    private void addAnotherItem() {
        storeClickWidgetList.add(new StoreClickWidget());
        widgetLists.add(new WidgetList());
        addDiagnosisAndMedicineAdapter.notifyItemInserted(widgetLists.size());
        getDataBinding().recyclerViewPrescribedMedicine.scrollToPosition(widgetLists.size());

//        addDiagnosisAndMedicineAdapter.notifyDataSetChanged();
    }
}