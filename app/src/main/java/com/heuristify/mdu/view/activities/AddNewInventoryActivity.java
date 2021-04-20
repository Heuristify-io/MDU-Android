package com.heuristify.mdu.view.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.MedicineSearchAdapter;
import com.heuristify.mdu.adapter.RemainingMedicineQuantityAdapter;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.databinding.ActivityAddNewInventoryBinding;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.mvvm.viewmodel.MedicineViewModel;
import com.heuristify.mdu.pojo.Medicine;

import java.util.ArrayList;
import java.util.List;


public class AddNewInventoryActivity extends BindingBaseActivity<ActivityAddNewInventoryBinding> implements OnClickHandlerInterface {
    MedicineViewModel medicineViewModel;
    MedicineSearchAdapter medicineSearchAdapter;
    RemainingMedicineQuantityAdapter remainingMedicineQuantityAdapter;
    private List<Medicine> medicineList;
    private List<StockMedicine> stockMedicineList;
    boolean isShowSuggestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);
        medicineList = new ArrayList<>();
        stockMedicineList = new ArrayList<>();
        initializeRecycleView();
        goneRecycleViewAndOtherViews();

        medicineViewModel = ViewModelProviders.of(this).get(MedicineViewModel.class);

        getDataBinding().editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() > 0) {
                    if(s.toString().equals("")){
                        getDataBinding().editTextSearch.setText("Search medicine");
                        goneRecycleViewAndOtherViews();

                    }
                    else{
                        if(countNumChars(s.toString()) == 0){
                            getSuggestion(s.toString().toUpperCase());
                        }
                    }
                } else {
                    medicineList.clear();
                    medicineSearchAdapter.notifyDataSetChanged();
                    goneRecycleViewAndOtherViews();
                    visibleRemainingMEdQuantityRecycleViewAndOtherViews();
                }
            }

        });

        medicineViewModel.getError_msg().observe(this, s -> {

        });

        medicineViewModel.getBooleanMutableLiveData().observe(this, aBoolean -> isShowSuggestion = aBoolean);

        medicineViewModel.getRemainingStockMedicine();
        medicineViewModel.getRemainingStockMedicineLists().observe(this, stockMedicines -> {
            if(!stockMedicines.isEmpty()){
                visibleRemainingMEdQuantityRecycleViewAndOtherViews();
                stockMedicineList.addAll(stockMedicines);
                remainingMedicineQuantityAdapter.notifyDataSetChanged();
            }
        });

    }


    private void goneRemainingMEdQuantityRecycleViewAndOtherViews() {
        getDataBinding().viewForRemainingMedQuantity.setVisibility(View.GONE);
        getDataBinding().recyclerViewForRemainingMedQuantityt.setVisibility(View.GONE);
        getDataBinding().viewForRemainingMedQuantity.setVisibility(View.GONE);


    }

    private void visibleRemainingMEdQuantityRecycleViewAndOtherViews() {
        getDataBinding().textViewLowStockMed.setVisibility(View.VISIBLE);
        getDataBinding().recyclerViewForRemainingMedQuantityt.setVisibility(View.VISIBLE);
        getDataBinding().viewForRemainingMedQuantity.setVisibility(View.VISIBLE);

    }

    private void goneRecycleViewAndOtherViews() {
        getDataBinding().recyclerViewMedicine.setVisibility(View.GONE);
        getDataBinding().view4.setVisibility(View.GONE);

    }

    private void visibleRecycleViewAndOtherViews() {
        getDataBinding().recyclerViewMedicine.setVisibility(View.VISIBLE);
        getDataBinding().view4.setVisibility(View.VISIBLE);

    }


    private void getSuggestion(String toUpperCase) {

        medicineViewModel.getSearchStockMutableLiveData(toUpperCase).observe(this, medicineListResponse -> {

            if(medicineListResponse.code() == 200){
                Log.e("search List",""+medicineListResponse.body().getMedicineList().size());
                if(isShowSuggestion){
                    if(!medicineListResponse.body().getMedicineList().isEmpty() && medicineListResponse.body().getMedicineList().size() > 0)
                    {
                        if(!medicineList.isEmpty()){
                            medicineList.clear();
                            medicineSearchAdapter.notifyDataSetChanged();
                        }
                        if(getDataBinding().editTextSearch.length()> 0){
                            goneRemainingMEdQuantityRecycleViewAndOtherViews();
                            visibleRecycleViewAndOtherViews();
                            medicineList.addAll(medicineListResponse.body().getMedicineList());
                            medicineSearchAdapter.notifyDataSetChanged();
                        }
                    }
                    else{
                        medicineList.clear();
                        medicineSearchAdapter.notifyDataSetChanged();
                        goneRecycleViewAndOtherViews();
//                        visibleRemainingMEdQuantityRecycleViewAndOtherViews();
//                        remainingMedicineQuantityAdapter.notifyDataSetChanged();
                    }
                }
            }
        });


    }

    private  int countNumChars(String s) {
        int count = 0;
        for(char c : s.toCharArray()){
            if (c == ' '){
                count++;
            }
            else{
                count = 0;
            }
        }
        return count;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_new_inventory;
    }

    private void initializeRecycleView() {
        getDataBinding().recyclerViewMedicine.setHasFixedSize(true);
        getDataBinding().recyclerViewMedicine.setItemAnimator(new DefaultItemAnimator());
        medicineSearchAdapter = new MedicineSearchAdapter(medicineList, this);
        getDataBinding().recyclerViewMedicine.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getDataBinding().recyclerViewMedicine.setAdapter(medicineSearchAdapter);
        getDataBinding().recyclerViewMedicine.setItemAnimator(null);

        getDataBinding().recyclerViewForRemainingMedQuantityt.setHasFixedSize(true);
        getDataBinding().recyclerViewForRemainingMedQuantityt.setItemAnimator(new DefaultItemAnimator());
        remainingMedicineQuantityAdapter = new RemainingMedicineQuantityAdapter(stockMedicineList, this);
        getDataBinding().recyclerViewForRemainingMedQuantityt.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        getDataBinding().recyclerViewForRemainingMedQuantityt.setAdapter(remainingMedicineQuantityAdapter);
        getDataBinding().recyclerViewMedicine.setItemAnimator(null);
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
            case R.id.imageViewAdd:
                gotoNextActivity();
                break;
            case R.id.textViewAddAnotherMedicine:
                gotoNextActivity();
                break;
        }
    }



    private void gotoNextActivity(){
        startActivity(new Intent(AddNewInventoryActivity.this,AddNewInventoryDetailsActivity.class));
    }
}