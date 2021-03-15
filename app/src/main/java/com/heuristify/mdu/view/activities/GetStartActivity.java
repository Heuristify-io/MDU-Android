package com.heuristify.mdu.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityGetStartBinding;

public class GetStartActivity extends BindingBaseActivity<ActivityGetStartBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(GetStartActivity.this, PinViewActivity.class));
                finish();
            }
        });

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_get_start;
    }
}