package com.heuristify.mdu.view.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.MedicineInventoryAdapter;
import com.heuristify.mdu.base.BindingBaseFragment;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.databinding.FragmentInventoryBinding;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.helper.Utilities;
import com.heuristify.mdu.mvvm.viewmodel.HelperViewModel;
import com.heuristify.mdu.mvvm.viewmodel.MedicineViewModel;
import com.heuristify.mdu.view.activities.AddNewInventoryActivity;

import java.util.ArrayList;
import java.util.List;


public class InventoryFragment extends BindingBaseFragment<FragmentInventoryBinding> {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public String mParam1;
    public String mParam2;
    private List<StockMedicine> medicineList;
    private MedicineInventoryAdapter medicineInventoryAdapter;
    private HelperViewModel helperViewModel;
    private Dialog mDialog;

    public InventoryFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        medicineList = new ArrayList<>();
        helperViewModel = ViewModelProviders.of(this).get(HelperViewModel.class);
    }


    @Override
    public void OnCreateView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialRecycleView();
        MyApplication.getInstance().setCurrentActivity(getActivity());
        getDataBinding().textViewDate.setText(Utilities.currentDate());
        MedicineViewModel medicineViewModel = ViewModelProviders.of(this).get(MedicineViewModel.class);

        medicineViewModel.getMedicineList().observe(getViewLifecycleOwner(), stockMedicineList -> {
            if (stockMedicineList != null) {
                medicineList.addAll(stockMedicineList);
                medicineInventoryAdapter.notifyDataSetChanged();
            }
        });

        getDataBinding().buttonAddInventory.setOnClickListener(v -> {

            showProgressDialog();
            helperViewModel.verifyToken();
        });

        observeToken();
        observeTokenError();

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_inventory;
    }

    private void initialRecycleView() {
        getDataBinding().recyclerViewMedicine.setHasFixedSize(true);
        getDataBinding().recyclerViewMedicine.setItemAnimator(new DefaultItemAnimator());
        medicineInventoryAdapter = new MedicineInventoryAdapter(medicineList, getActivity());
        getDataBinding().recyclerViewMedicine.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getDataBinding().recyclerViewMedicine.setAdapter(medicineInventoryAdapter);
        getDataBinding().recyclerViewMedicine.setItemAnimator(null);
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
                startActivity(new Intent(getActivity(), AddNewInventoryActivity.class));
            } else {
                showMedicineDialog();
            }
        });
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
            helperViewModel.verifyToken();
        });

        btnCancel.setOnClickListener(v -> mDialog.dismiss());
    }
}