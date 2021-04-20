package com.heuristify.mdu.view.activities;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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
    String[] From = {"None", "Syrup", "Tablet", "Capsules", "Drops", "Topical", "Inhalers", "Injections", "Implants or patches"};
    String[] Strength = {"None", "500", "600", "700", "800", "800", "900"};
    String[] Unit = {"None", "kg", "g", "mg", "mcg", "L"};
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

        from_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, From);
        from_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        strength_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Strength);
        strength_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unit_adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, Unit);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getDataBinding().materialSpinnerForm.setAdapter(from_adapter);
        getDataBinding().materialSpinnerStrength.setAdapter(strength_adapter);
        getDataBinding().materialSpinnerUnit.setAdapter(unit_adapter);


        if (getIntent().getExtras() != null) {
            String medicine_name = getIntent().getExtras().getString("medicine_name");
            getDataBinding().editTextSearch.setText(medicine_name);

            if (getIntent().getExtras().getString("from") != null || getIntent().getExtras().getString("from") != "") {
                Objects.requireNonNull(getDataBinding().materialSpinnerForm.getEditText()).setText(getIntent().getExtras().getString("from"));
            }
            if (getIntent().getExtras().getString("strength") != null || getIntent().getExtras().getString("strength") != "") {
                Objects.requireNonNull(getDataBinding().materialSpinnerStrength.getEditText()).setText(getIntent().getExtras().getString("strength"));
            }
            if (getIntent().getExtras().getString("unit") != null || getIntent().getExtras().getString("unit") != "") {
                Objects.requireNonNull(getDataBinding().materialSpinnerUnit.getEditText()).setText(getIntent().getExtras().getString("unit"));
            }

        } else {

            Objects.requireNonNull(getDataBinding().materialSpinnerForm.getEditText()).setText(From[0]);
            Objects.requireNonNull(getDataBinding().materialSpinnerStrength.getEditText()).setText(Strength[0]);
            Objects.requireNonNull(getDataBinding().materialSpinnerUnit.getEditText()).setText(Unit[0]);
        }

        if (getDataBinding().editTextSearch.getText().length() > 0) {
            enableButton();
        } else {
            disableButton();
        }

        int quantity = Integer.parseInt(getDataBinding().textViewNumber.getText().toString());
        int boxes = Integer.parseInt(getDataBinding().textViewBoxNumber.getText().toString());
        getDataBinding().textViewBoxesNumber.setText(String.valueOf(boxes));
        getDataBinding().textViewQuantBoxNumber.setText(String.valueOf(quantity));
        getDataBinding().textViewQuantityAndBoxesTotal.setText(String.valueOf(quantity * boxes));


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
                Toast.makeText(context, "Medicine Added Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddNewInventoryDetailsActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(context, "Medicine Not Added", Toast.LENGTH_SHORT).show();
            }
            DisplayLog.showLog(TAG, "success " + responseBodyResponse.code());


        };

        medicineViewModel.getError_msg().observe(this, s -> {
            dismissProgressDialog();
            DisplayLog.showLog(TAG, "getError " + s);
            Toast.makeText(context, "Medicine Not Added", Toast.LENGTH_SHORT).show();
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

                    multiplyBoxesAndQuantity(number, Integer.parseInt(getDataBinding().textViewBoxNumber.getText().toString()));


                }
                break;
            case R.id.textViewAdd:
                int number = Integer.parseInt(getDataBinding().textViewNumber.getText().toString());
                number = number + 1;
                getDataBinding().textViewNumber.setText(String.valueOf(number));

                multiplyBoxesAndQuantity(number, Integer.parseInt(getDataBinding().textViewBoxNumber.getText().toString()));

                break;

            case R.id.textViewBoxSub:
                if (Integer.parseInt(getDataBinding().textViewBoxNumber.getText().toString()) > 1) {
                    int number2 = Integer.parseInt(getDataBinding().textViewBoxNumber.getText().toString());
                    number2 = number2 - 1;
                    getDataBinding().textViewBoxNumber.setText(String.valueOf(number2));

                    multiplyBoxesAndQuantity(Integer.parseInt(getDataBinding().textViewNumber.getText().toString()), number2);


                }
                break;
            case R.id.textViewBoxAdd:
                int number3 = Integer.parseInt(getDataBinding().textViewBoxNumber.getText().toString());
                number3 = number3 + 1;
                getDataBinding().textViewBoxNumber.setText(String.valueOf(number3));

                multiplyBoxesAndQuantity(Integer.parseInt(getDataBinding().textViewNumber.getText().toString()), number3);

                break;
            case R.id.buttonNextInventoryDetais:
                showProgressDialog();
                String from = "", strength = "", unit = "";


                if (!getDataBinding().materialSpinnerForm.getEditText().getText().toString().equals("None")) {
                    from = getDataBinding().materialSpinnerForm.getEditText().getText().toString();
                }

                if (!getDataBinding().materialSpinnerStrength.getEditText().getText().toString().equals("None")) {
                    strength = getDataBinding().materialSpinnerStrength.getEditText().getText().toString();

                }
                if (!getDataBinding().materialSpinnerUnit.getEditText().getText().toString().equals("None")) {
                    unit = getDataBinding().materialSpinnerUnit.getEditText().getText().toString();

                }
                createMedicine(getDataBinding().editTextSearch.getText().toString(), from, strength, unit, Integer.parseInt(getDataBinding().textViewQuantityAndBoxesTotal.getText().toString()));

                break;
        }

    }

    private void multiplyBoxesAndQuantity(int quantityNumber, int boxesNumber) {

        getDataBinding().textViewBoxesNumber.setText(String.valueOf(boxesNumber));
        getDataBinding().textViewQuantBoxNumber.setText(String.valueOf(quantityNumber));
        getDataBinding().textViewQuantityAndBoxesTotal.setText(String.valueOf(quantityNumber * boxesNumber));

    }


    private void createMedicine(String medicine_name, String from, String strength, String unit, int quantity) {
        medicineViewModel.createMedicine(medicine_name, from, strength, unit, quantity).observe((AppCompatActivity) context, observer);

    }
}