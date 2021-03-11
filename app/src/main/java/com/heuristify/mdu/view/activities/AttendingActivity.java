package com.heuristify.mdu.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityAttendingBinding;


public class AttendingActivity extends BindingBaseActivity<ActivityAttendingBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dataBinding.buttonAttending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AttendingActivity.this,DashboardActivity.class));
            }
        });
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_attending;
    }
}