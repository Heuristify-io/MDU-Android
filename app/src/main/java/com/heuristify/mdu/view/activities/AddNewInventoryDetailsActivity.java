package com.heuristify.mdu.view.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityAddNewInventoryDetailsBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.mvvm.viewmodel.MedicineViewModel;
import com.heuristify.mdu.pojo.StockMedicineList;

import java.util.Objects;

import retrofit2.Response;

public class AddNewInventoryDetailsActivity extends BindingBaseActivity<ActivityAddNewInventoryDetailsBinding> implements OnClickHandlerInterface {
    String[] From = {"Tablet", "Syrup"};
    String[] Strength = {"500", "600", "700", "800", "800", "900"};
    String[] Unit = {"mg"};
    ArrayAdapter<String> from_adapter, strength_adapter, unit_adapter;
    MedicineViewModel medicineViewModel;
    private Observer observer;
    private Context context;
    private final String TAG = "AddNewInventoryDetailsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);
        context = this;
        medicineViewModel = ViewModelProviders.of(this).get(MedicineViewModel.class);

        if (getIntent().getExtras() != null) {
            String medicine_name = getIntent().getExtras().getString("medicine_name");
            getDataBinding().editTextSearch.setText(medicine_name);
        }

        if (getDataBinding().editTextSearch.getText().length() > 0) {
            enableButton();
        } else {
            disableButton();
        }

        from_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, From);
        from_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        strength_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Strength);
        strength_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unit_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Unit);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getDataBinding().materialSpinnerForm.setAdapter(from_adapter);
        getDataBinding().materialSpinnerStrength.setAdapter(strength_adapter);
        getDataBinding().materialSpinnerUnit.setAdapter(unit_adapter);

        Objects.requireNonNull(getDataBinding().materialSpinnerForm.getEditText()).setText(From[0]);
        Objects.requireNonNull(getDataBinding().materialSpinnerStrength.getEditText()).setText(Strength[0]);
        Objects.requireNonNull(getDataBinding().materialSpinnerUnit.getEditText()).setText(Unit[0]);

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
                    enableButton();
                } else {
                    disableButton();
                }
            }
        });

        observer = (Observer<Response<StockMedicineList>>) responseBodyResponse -> {
            dismissProgressDialog();
            if (responseBodyResponse.isSuccessful()) {
                Toast.makeText(context, "Medicine Buy Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddNewInventoryDetailsActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
            DisplayLog.showLog(TAG,"success "+responseBodyResponse.code());


        };

        medicineViewModel.getError_msg().observe(this, s -> {
            dismissProgressDialog();
            DisplayLog.showLog(TAG,"getError "+s);
        });

    }

    private void enableButton() {
        getDataBinding().buttonNextInventoryDetais.setBackgroundResource(R.drawable.rounded_shape);
        getDataBinding().buttonNextInventoryDetais.setEnabled(true);
    }

    private void disableButton() {
        getDataBinding().buttonNextInventoryDetais.setBackgroundResource(R.drawable.rounded_shape_light_dark_mix_color);
        getDataBinding().buttonNextInventoryDetais.setEnabled(false);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_new_inventory_details;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.textViewBack:
                finish();
                break;
            case R.id.imageViewBack:
                finish();
                break;
            case R.id.textViewSub:
                if (Integer.parseInt(getDataBinding().textViewNumber.getText().toString()) > 1) {
                    int number = Integer.parseInt(getDataBinding().textViewNumber.getText().toString());
                    number = number - 1;
                    getDataBinding().textViewNumber.setText(String.valueOf(number));

                }
                break;
            case R.id.textViewAdd:
                int number = Integer.parseInt(getDataBinding().textViewNumber.getText().toString());
                number = number + 1;
                getDataBinding().textViewNumber.setText(String.valueOf(number));
                break;
            case R.id.buttonNextInventoryDetais:
                showProgressDialog();
                createMedicine(getDataBinding().editTextSearch.getText().toString(), Objects.requireNonNull(getDataBinding().materialSpinnerForm.getEditText()).getText().toString(), getDataBinding().materialSpinnerStrength.getEditText().getText().toString(), getDataBinding().materialSpinnerUnit.getEditText().getText().toString(), Integer.parseInt(getDataBinding().textViewNumber.getText().toString()));
                break;
        }

    }

    private void createMedicine(String medicine_name, String from, String strength, String unit, int quantity) {
        medicineViewModel.createMedicine(medicine_name, from, strength, unit, quantity).observe((AppCompatActivity) context, observer);

    }
}