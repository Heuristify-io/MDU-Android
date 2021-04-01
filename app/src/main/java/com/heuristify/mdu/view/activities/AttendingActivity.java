package com.heuristify.mdu.view.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.lifecycle.ViewModelProviders;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.database.entity.DoctorAttendance;
import com.heuristify.mdu.databinding.ActivityAttendingBinding;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;
import com.heuristify.mdu.mvvm.viewmodel.DoctorViewModel;

import java.util.Date;


public class AttendingActivity extends BindingBaseActivity<ActivityAttendingBinding> implements OnClickHandlerInterface {

    DoctorViewModel doctorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);

        doctorViewModel = ViewModelProviders.of(this).get(DoctorViewModel.class);
        doctorViewModel.insertDoctorAttendanceMutableLiveDataObserver().observe(this, longValue -> {
            if (longValue > 0) {
                startActivity(new Intent(AttendingActivity.this, DashboardActivity.class));
                finish();
            }
        });


    }


    @Override
    protected int getLayoutRes() {
        return R.layout.activity_attending;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSkip:
                gotoDashboardActivity();
                break;
            case R.id.buttonAttending:
                addDoctorAttendance();
                break;
        }

    }

    private void addDoctorAttendance() {
        DoctorAttendance doctorAttendance = new DoctorAttendance();
        doctorAttendance.setAttendanceDate(new Date());
        doctorViewModel.insertDoctorAttendance(doctorAttendance);

    }

    private void gotoDashboardActivity() {
        startActivity(new Intent(AttendingActivity.this, DashboardActivity.class));
        finish();
    }
}