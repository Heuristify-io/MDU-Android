package com.heuristify.mdu.view.activities;


import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.MedicineSearchAdapter;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityAddNewInventoryBinding;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.mvvm.viewmodel.MedicineViewModel;
import com.heuristify.mdu.pojo.Medicine;
import com.heuristify.mdu.pojo.MedicineList;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Response;

public class AddNewInventoryActivity extends BindingBaseActivity<ActivityAddNewInventoryBinding> implements OnClickHandlerInterface {
    MedicineViewModel medicineViewModel;
    MedicineSearchAdapter medicineSearchAdapter;
    private List<Medicine> medicineList;
    boolean isShowSuggestion = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);
        medicineList = new ArrayList<>();
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
                }
            }

        });

        medicineViewModel.getError_msg().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {

            }
        });

        medicineViewModel.getBooleanMutableLiveData().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                isShowSuggestion = aBoolean;
            }
        });

    }

    private void goneRecycleViewAndOtherViews() {
        getDataBinding().recyclerViewMedicine.setVisibility(View.GONE);
        getDataBinding().view4.setVisibility(View.GONE);
        getDataBinding().imageViewAdd.setVisibility(View.GONE);
        getDataBinding().textViewAddAnotherMedicine.setVisibility(View.GONE);

    }

    private void visibleRecycleViewAndOtherViews() {
        getDataBinding().recyclerViewMedicine.setVisibility(View.VISIBLE);
        getDataBinding().view4.setVisibility(View.VISIBLE);
        getDataBinding().imageViewAdd.setVisibility(View.VISIBLE);
        getDataBinding().textViewAddAnotherMedicine.setVisibility(View.VISIBLE);

    }

    private void getSuggestion(String toUpperCase) {

        medicineViewModel.getSearchStockMutableLiveData(toUpperCase).observe(this, new Observer<Response<MedicineList>>() {
            @Override
            public void onChanged(Response<MedicineList> medicineListResponse) {

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
                                visibleRecycleViewAndOtherViews();
                                medicineList.addAll(medicineListResponse.body().getMedicineList());
                                medicineSearchAdapter.notifyDataSetChanged();
                            }
                        }
                        else{
                            medicineList.clear();
                            medicineSearchAdapter.notifyDataSetChanged();
                            goneRecycleViewAndOtherViews();
                        }
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
    }

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

                break;
            case R.id.textViewAddAnotherMedicine:

                break;
            case R.id.buttonNext:
//                startActivity(new Intent(AddNewInventoryActivity.this,));
//                finish();
                break;
        }
    }
}