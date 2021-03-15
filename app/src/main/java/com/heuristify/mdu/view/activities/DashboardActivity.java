package com.heuristify.mdu.view.activities;


import android.os.Bundle;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityDashboardBinding;
import com.heuristify.mdu.helper.Helper;
import com.heuristify.mdu.sharedPreferences.SharedHelper;

public class DashboardActivity extends BindingBaseActivity<ActivityDashboardBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().textViewName.setText("Dr. "+SharedHelper.getKey(this, Helper.NAME));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dashboard;
    }
}