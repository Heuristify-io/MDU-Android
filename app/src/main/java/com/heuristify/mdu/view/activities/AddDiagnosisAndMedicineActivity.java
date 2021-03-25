package com.heuristify.mdu.view.activities;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.AddDiagnosisAndMedicineAdapter;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.DiagnosisAndMedicine;
import com.heuristify.mdu.databinding.ActivityAddDiagnosisAndMedicineBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.helper.StoreClickWidget;
import com.heuristify.mdu.helper.WidgetList;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;

import java.util.ArrayList;
import java.util.List;

public class AddDiagnosisAndMedicineActivity extends BindingBaseActivity<ActivityAddDiagnosisAndMedicineBinding> implements OnClickHandlerInterface {
    private List<WidgetList> widgetLists;
    private List<StoreClickWidget> storeClickWidgetList;
    private AddDiagnosisAndMedicineAdapter addDiagnosisAndMedicineAdapter;
    private int patientId;
    private final String TAG = "AddDiagnosisAndMedicineActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);
        if (getIntent().getExtras() != null) {
            patientId = getIntent().getExtras().getInt("patientID");
            DisplayLog.showLog(TAG, " patientID " + patientId);
        }
        initializeRecycleView();


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
            case R.id.buttonNextConsultation:
                addIntoLocalDb();
                break;
        }
    }

    private void addIntoLocalDb() {

        for (int i = 0; i < storeClickWidgetList.size(); i++) {
            DiagnosisAndMedicine diagnosisAndMedicine = new DiagnosisAndMedicine();
            diagnosisAndMedicine.setPatientDiagnosis("ggg");
            diagnosisAndMedicine.setDescription("panadol");
            diagnosisAndMedicine.setLat(0.0);
            diagnosisAndMedicine.setLng(0.0);
            diagnosisAndMedicine.setPatientId(patientId);
            List<DiagnosisAndMedicine> diagnosisAndMedicineList = new ArrayList<>();
            diagnosisAndMedicineList.add(diagnosisAndMedicine);
            new Thread(() -> {

                MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().insertPatientDiagnosis(diagnosisAndMedicineList);
//                if(insert > 0){
//
//                    runOnUiThread(() -> Toast.makeText(mContext, "Diagnosis Added Successfully", Toast.LENGTH_SHORT).show());
//                }
//                else {
//                    Toast.makeText(mContext, "Diagnosis Not Added Successfully", Toast.LENGTH_SHORT).show();
//                }

            }).start();

            storeClickWidgetList.clear();
            widgetLists.clear();
        }
    }

    private void addAnotherItem() {
        storeClickWidgetList.add(new StoreClickWidget());
        widgetLists.add(new WidgetList());
//        addDiagnosisAndMedicineAdapter.notifyItemInserted(widgetLists.size());
        addDiagnosisAndMedicineAdapter.notifyDataSetChanged();
    }
}