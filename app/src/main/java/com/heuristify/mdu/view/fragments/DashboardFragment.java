package com.heuristify.mdu.view.fragments;

import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProviders;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.Toast;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseFragment;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.Patient;
import com.heuristify.mdu.databinding.FragmentDashboardBinding;
import com.heuristify.mdu.helper.Constant;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.helper.Helper;
import com.heuristify.mdu.helper.Utilities;
import com.heuristify.mdu.mvvm.viewmodel.ConsultationViewModel;
import com.heuristify.mdu.mvvm.viewmodel.DataSyncViewModel;
import com.heuristify.mdu.mvvm.viewmodel.HelperViewModel;
import com.heuristify.mdu.sharedPreferences.SharedHelper;
import com.heuristify.mdu.view.activities.ConsultationHistoryActivity;
import com.heuristify.mdu.view.activities.PinViewActivity;

import java.util.ArrayList;
import java.util.List;


public class DashboardFragment extends BindingBaseFragment<FragmentDashboardBinding> implements LifecycleOwner {


    public static final String ARG_PARAM2;

    static {
        ARG_PARAM2 = "param2";
    }

    public static final String TAG = "DashboardFragment";
    HelperViewModel helperViewModel;
    DataSyncViewModel dataSyncViewModel;
    ConsultationViewModel consultationViewModel;
    List<Patient> patientListContainingImage;
    private Dialog mDialog;

    public DashboardFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        patientListContainingImage = new ArrayList<>();
        dataSyncViewModel = ViewModelProviders.of(this).get(DataSyncViewModel.class);
        consultationViewModel = ViewModelProviders.of(this).get(ConsultationViewModel.class);
        helperViewModel = ViewModelProviders.of(this).get(HelperViewModel.class);

    }

    @Override
    public void OnCreateView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

        getDataBinding().buttonAttending22.setOnClickListener(v -> {
            showProgressDialog();
            helperViewModel.verifyToken();
        });
        getDataBinding().buttonAttending2.setOnClickListener(v -> startActivity(new Intent(getActivity(), ConsultationHistoryActivity.class)));
        getDataBinding().textViewDate.setText(Utilities.currentDate());

    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        observeToken();
        observeTokenError();
        observeGetPatientImages();
        observerErrorMsgPatientImages();
        observerSyncDataResponse();
        observerSyncDataErrorResponse();
        observeTotalConsultation();
        observerUpdateConsultation();
        observerPendingConsultation();
    }


    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_dashboard;
    }

    private void observeTokenError() {
        helperViewModel.getErrorMutableLiveData().observe(getViewLifecycleOwner(), s -> {
            dismissProgressDialog();
            showMedicineDialog();
        });
    }

    private void observeToken() {
        helperViewModel.getResponseMutableLiveData().observe(getViewLifecycleOwner(), responseBodyResponse -> {
            dismissProgressDialog();
            if (responseBodyResponse.code() == 200) {
                showProgressDialogWithText("Uploading Images");
                getAllPatientImages();
            }

            else if(responseBodyResponse.code() == 401) {
                showAlertDialog();
            }
            else {
                showMedicineDialog();
            }
        });
    }

    private void observerPendingConsultation() {
        consultationViewModel.getAllPendingConsultation().observe(getViewLifecycleOwner(), integer ->
                getDataBinding().textViewPending.setText(String.valueOf(integer)));
    }

    private void observerUpdateConsultation() {
        consultationViewModel.getAllUpdatedConsultation().observe(getViewLifecycleOwner(), integer ->
                getDataBinding().textViewUpdate.setText(String.valueOf(integer)));
    }

    private void observeTotalConsultation() {
        consultationViewModel.getAllTotalConsultation().observe(getViewLifecycleOwner(), integer ->
                getDataBinding().textViewTotal.setText(String.valueOf(integer)));

    }

    private void getAllPatientImages() {
        //sync field value
        dataSyncViewModel.callGetAllPatientImagesMethod(Constant.patient_sync_zero);
    }

    private void observeGetPatientImages() {
        dataSyncViewModel.getPatientImagesList().observe(getViewLifecycleOwner(), patientList -> {
            DisplayLog.showLog(TAG, "imageResponse " + patientList);
            dismissProgressDialog();
            showProgressDialogWithText("Upload Records");
            dataSyncViewModel.uploadRecords(Constant.patient_sync_zero, Constant.diagnosis_sync_zero, Constant.prescribed_medicine_sync_zero);

        });

    }

    private void observerErrorMsgPatientImages() {
        dataSyncViewModel.errorMsgPatientImages().observe(getViewLifecycleOwner(), String -> {
            dismissProgressDialog();
            DisplayLog.showLog(TAG, "imageError " + String);
            Toast.makeText(mContext, "Image Uploading Fail Try Again", Toast.LENGTH_SHORT).show();
        });
    }

    private void observerSyncDataErrorResponse() {
        dataSyncViewModel.getSyncMutableLiveDataErrorResponse().observe(getViewLifecycleOwner(), String -> {
            dismissProgressDialog();
            DisplayLog.showLog(TAG, "syncErrorResponse " + String);
            Toast.makeText(mContext, "Unable to upload records", Toast.LENGTH_SHORT).show();
        });

    }

    private void observerSyncDataResponse() {
        dataSyncViewModel.observeUploadRecordMutableResponsive().observe(getViewLifecycleOwner(), syncApiResponse -> {
            dismissProgressDialog();
            if (syncApiResponse.code() == 200) {
                Toast.makeText(mContext, "Records uploaded", Toast.LENGTH_SHORT).show();
            }
            else if(syncApiResponse.code() == 401) {
                showAlertDialog();
            }
            else {
                Toast.makeText(mContext, "Unable to upload records", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
        builder.setMessage("Your MDU has been changed, please sign in again to verify your account. Don't worry your data has been synced to the server.");
        builder.setPositiveButton("Go To Login", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                goToLogin();
            }
        });
        builder.show();
    }


    private void goToLogin() {
        SharedHelper.putKey(MyApplication.getInstance(), Helper.NAME, "");
        SharedHelper.putKey(MyApplication.getInstance(), Helper.PMDC, "");
        SharedHelper.putKey(MyApplication.getInstance(), Helper.PHONE, "");
        SharedHelper.putKey(MyApplication.getInstance(), Helper.EMAIL, "");
        SharedHelper.putKey(MyApplication.getInstance(), Helper.JWT, "");
        Intent intent = new Intent(getActivity() , PinViewActivity.class);
        startActivity(intent);
        getActivity().finish();

    }

    private void showMedicineDialog() {
        mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.setContentView(R.layout.custom_internet_connectivity_dialog_view);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(false);
        mDialog.show();
        Button btnRetry = mDialog.findViewById(R.id.buttonRetry);
        Button btnCancel = mDialog.findViewById(R.id.buttonCancel);

        btnRetry.setOnClickListener(v -> {
            mDialog.dismiss();
            showProgressDialog();
            helperViewModel.verifyToken();
        });

        btnCancel.setOnClickListener(v -> mDialog.dismiss());
    }


}