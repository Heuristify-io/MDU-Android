package com.heuristify.mdu.view.activities;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.ConsultationSummaryAdapter;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.databinding.ActivityConsultationSummaryBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.mvvm.viewmodel.ConsultationViewModel;
import com.heuristify.mdu.pojo.PatientPrescribedMedicine;
import com.heuristify.mdu.pojo.PatientPrescribedMedicineAndDiagnosis;

import java.util.ArrayList;
import java.util.List;

public class ConsultationSummaryActivity extends BindingBaseActivity<ActivityConsultationSummaryBinding> implements OnClickHandlerInterface, LifecycleOwner {
    private final String TAG = "ConsultationSummaryActivity";
    private int consultation_id;
    private List<PatientPrescribedMedicine> patientPrescribedMedicinesList;
    private ConsultationSummaryAdapter consultationSummaryAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LifecycleOwner lifecycleOwner = this;
        Observer observer;
        getDataBinding().setClickHandler(this);
        initializeRecycleView();


        if (getIntent().getExtras() != null) {
            Patient patient = (Patient) getIntent().getSerializableExtra("patient");
            DisplayLog.showLog(TAG, " patientID " + patient.getId());
            getDataBinding().editTextTextFullName.setText(patient.getFullName());
            getDataBinding().editTextTextAge.setText(String.valueOf(patient.getAge()));
            getDataBinding().editTextGender.setText(patient.getGender());
            consultation_id = getIntent().getExtras().getInt("consultation_id");
        }

        ConsultationViewModel consultationViewModel = ViewModelProviders.of(this).get(ConsultationViewModel.class);

        observer = (Observer<PatientPrescribedMedicineAndDiagnosis>) patientPrescribedMedicineAndDiagnosis -> {

            if (patientPrescribedMedicineAndDiagnosis.getDiagnosisAndMedicine() != null) {
                getDataBinding().editTextTextPatientDiagnosis.setText(patientPrescribedMedicineAndDiagnosis.getDiagnosisAndMedicine().getPatientDiagnosis());
                getDataBinding().editTextTextPatientDescription.setText(patientPrescribedMedicineAndDiagnosis.getDiagnosisAndMedicine().getDescription());
            }

            if (patientPrescribedMedicineAndDiagnosis.getList() != null) {
                DisplayLog.showLog(TAG, "prescribed_med_list " + patientPrescribedMedicineAndDiagnosis.getList().size());
                patientPrescribedMedicinesList.addAll(patientPrescribedMedicineAndDiagnosis.getList());
                consultationSummaryAdapter.notifyDataSetChanged();
            }

        };

        consultationViewModel.getPatientPrescribedMedicineList(consultation_id).observe(lifecycleOwner, observer);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_consultation_summary;
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
            case R.id.buttonPrint:
                break;
            case R.id.buttonDone:
                Intent intent = new Intent(ConsultationSummaryActivity.this, DashboardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();
                break;
        }

    }

    private void initializeRecycleView() {
        patientPrescribedMedicinesList = new ArrayList<>();
        getDataBinding().recyclerViewConsultation.setHasFixedSize(true);
        getDataBinding().recyclerViewConsultation.setItemAnimator(new DefaultItemAnimator());
        getDataBinding().recyclerViewConsultation.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        consultationSummaryAdapter = new ConsultationSummaryAdapter(this, patientPrescribedMedicinesList);
        getDataBinding().recyclerViewConsultation.setAdapter(consultationSummaryAdapter);
        getDataBinding().recyclerViewConsultation.setItemAnimator(null);
    }
}