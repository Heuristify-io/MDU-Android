package com.heuristify.mdu.view.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityAddNewInventoryDetailsBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.mvvm.viewmodel.MedicineViewModel;

public class AddNewInventoryDetailsActivity extends BindingBaseActivity<ActivityAddNewInventoryDetailsBinding> implements OnClickHandlerInterface {
    String[] From = {"None", "Syrup", "Tablet", "Capsules", "Drops", "Topical", "Inhalers", "Injections", "Implants", "patches"};
    String[] Unit = {"None", "kg", "g", "mg", "mcg", "L", "ml", "cc", "mol", "mmol", "gm", "mm"};
    ArrayAdapter<String> f_adapter, f_unit;
    MedicineViewModel medicineViewModel;
    private final String TAG = "AddNewInventoryDetailsActivity";
    private String text_from_spinner = "", text_unit_spinner = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);
        medicineViewModel = ViewModelProviders.of(this).get(MedicineViewModel.class);

        fromAdapterInitialization();
        unitAdapterInitialization();
        fromSpinnerClickListener();
        unitSpinnerClickListener();


        if (getIntent().getExtras() != null) {
            String medicine_name = getIntent().getExtras().getString("medicine_name");
            getDataBinding().editTextSearch.setText(medicine_name);

            if (getIntent().getExtras().getString("from") != null || !getIntent().getExtras().getString("from").equals("")) {
                getDataBinding().textViewMedicineForm.setText(getIntent().getExtras().getString("from"));

                int pos = 0;
                String text = getIntent().getExtras().getString("from");
                for (int i = 0; i < From.length; i++) {
                    if (text.equals(From[i])) {
                        pos = i;
                        break;
                    }
                }
                getDataBinding().spinnerFrom.setSelection(pos);

            }
            if (getIntent().getExtras().getString("strength") != null || !getIntent().getExtras().getString("strength").equals("")) {

                getDataBinding().editTextStrength.setText(getIntent().getExtras().getString("strength"));
            }
            if (getIntent().getExtras().getString("unit") != null || !getIntent().getExtras().getString("unit").equals("")) {

                int pos = 0;
                String text = getIntent().getExtras().getString("unit");
                for (int i = 0; i < Unit.length; i++) {
                    if (text.equals(Unit[i])) {
                        pos = i;
                        break;
                    }
                }
                getDataBinding().spinnerUnit.setSelection(pos);
            }

        } else {

            getDataBinding().spinnerFrom.setSelection(0);
            getDataBinding().spinnerUnit.setSelection(0);

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

        medicineViewModel.createMedicine().observe(this, responseBodyResponse -> {
            dismissProgressDialog();
            if (responseBodyResponse.isSuccessful()) {
                Toast.makeText(mContext, "Medicine Added Successfully", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddNewInventoryDetailsActivity.this, DashboardActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            } else {
                Toast.makeText(mContext, "Medicine Not Added", Toast.LENGTH_SHORT).show();
            }
            DisplayLog.showLog(TAG, "success " + responseBodyResponse.code());

        });


        medicineViewModel.getError_msg().observe(this, s -> {
            dismissProgressDialog();
            DisplayLog.showLog(TAG, "getError " + s);
            Toast.makeText(mContext, "Medicine Not Added", Toast.LENGTH_SHORT).show();
        });


    }

    private void unitSpinnerClickListener() {

        getDataBinding().spinnerUnit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int pos, long arg3) {
                TextView tv = (TextView) view;
                text_unit_spinner = tv.getText().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void fromSpinnerClickListener() {

        getDataBinding().spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int pos, long arg3) {
                TextView tv = (TextView) view;
                text_from_spinner = tv.getText().toString();
                getDataBinding().textViewMedicineForm.setText(tv.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

    }

    private void unitAdapterInitialization() {

        f_unit = new ArrayAdapter<String>(mContext, R.layout.spinner_item, Unit) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(mContext.getResources().getColor(R.color.dark2));
                return view;
            }
        };
        f_unit.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getDataBinding().spinnerUnit.setAdapter(f_unit);

    }

    private void fromAdapterInitialization() {

        f_adapter = new ArrayAdapter<String>(mContext, R.layout.spinner_item, From) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(mContext.getResources().getColor(R.color.dark2));
                return view;
            }
        };
        f_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        getDataBinding().spinnerFrom.setAdapter(f_adapter);

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
                String from, strength, unit;
                strength = getDataBinding().editTextStrength.getText().toString();
                from = text_from_spinner;
                unit = text_unit_spinner;
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
        medicineViewModel.CreateMedicines(medicine_name, from, strength, unit, quantity);

    }
}