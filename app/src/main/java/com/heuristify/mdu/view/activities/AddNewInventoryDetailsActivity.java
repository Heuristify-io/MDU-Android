package com.heuristify.mdu.view.activities;


import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityAddNewInventoryDetailsBinding;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;

public class AddNewInventoryDetailsActivity extends BindingBaseActivity<ActivityAddNewInventoryDetailsBinding> implements OnClickHandlerInterface {
    private String medicine_name = "";
    String[] From = {"Tablet", "Syrup", "Item 3", "Item 4", "Item 5", "Item 6"};
    String[] Strength = {"500", "600", "700", "800", "800", "900"};
    String[] Unit = {"mg", "mfg", "gfg"};
    ArrayAdapter<String> from_adapter, strength_adapter, unit_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);
        if (getIntent().getExtras() != null) {
            medicine_name = getIntent().getExtras().getString("medicine_name");
            getDataBinding().editTextSearch.setText(medicine_name);
        }

        from_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, From);
        from_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        strength_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Strength);
        strength_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        unit_adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, Unit);
        unit_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        getDataBinding().materialSpinnerForm.setAdapter(from_adapter);
        getDataBinding().materialSpinnerStrength.setAdapter(strength_adapter);
        getDataBinding().materialSpinnerUnit.setAdapter(unit_adapter);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_add_new_inventory_details;
    }

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
                if (Integer.parseInt(getDataBinding().textViewNumber.getText().toString()) > 0) {
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
        }

    }
}