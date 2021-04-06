package com.heuristify.mdu.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;


import androidx.lifecycle.ViewModelProviders;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.databinding.ActivitySplashBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.helper.Helper;
import com.heuristify.mdu.helper.Utilities;
import com.heuristify.mdu.mvvm.viewmodel.DoctorViewModel;
import com.heuristify.mdu.sharedPreferences.SharedHelper;

public class SplashActivity extends BindingBaseActivity<ActivitySplashBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            if (SharedHelper.getKey(MyApplication.getInstance(), Helper.JWT).length() > 0) {

                DoctorViewModel doctorViewModel = ViewModelProviders.of(this).get(DoctorViewModel.class);
                doctorViewModel.checkPatientAttendance(Utilities.currentDate());
                doctorViewModel.getDoctorAttendanceMutableLiveData1().observe(this, (String String) -> {
                    DisplayLog.showLog("splash", "" + String);
                    if (String == null) {
                        gotoAttendingActivity();
                    } else {
                        gotoDashBoard();
                    }
                    finish();
                });

            } else {
                startActivity(new Intent(SplashActivity.this, GetStartActivity.class));
                finish();
            }
        }, 500);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }

    private void gotoDashBoard() {
        startActivity(new Intent(SplashActivity.this, DashboardActivity.class));
    }

    private void gotoAttendingActivity() {
        startActivity(new Intent(SplashActivity.this, AttendingActivity.class));
    }
}