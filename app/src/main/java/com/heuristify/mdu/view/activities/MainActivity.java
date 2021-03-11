package com.heuristify.mdu.view.activities;

import android.os.Bundle;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityMainBinding;

public class MainActivity extends BindingBaseActivity<ActivityMainBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_main;
    }
}