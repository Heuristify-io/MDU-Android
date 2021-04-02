package com.heuristify.mdu.view.activities;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityDashboardBinding;
import com.heuristify.mdu.helper.Constant;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.helper.Helper;
import com.heuristify.mdu.mvvm.viewmodel.DataSyncViewModel;
import com.heuristify.mdu.mvvm.viewmodel.DoctorViewModel;
import com.heuristify.mdu.sharedPreferences.SharedHelper;
import com.heuristify.mdu.view.fragments.DashboardFragment;
import com.heuristify.mdu.view.fragments.InventoryFragment;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashboardActivity extends BindingBaseActivity<ActivityDashboardBinding> {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment = null;

    Date date;
    SimpleDateFormat df;
    DoctorViewModel doctorViewModel;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().textViewName.setText("Dr. " + SharedHelper.getKey(this, Helper.NAME));

        date = Calendar.getInstance().getTime();
        df = new SimpleDateFormat(Constant.DOB_FORMAT, Locale.getDefault());
        doctorViewModel = ViewModelProviders.of(this).get(DoctorViewModel.class);

        observeAttendance();

        dashboardFragment();
        getDataBinding().textViewDashboard.setOnClickListener(v -> dashboardFragment());

        getDataBinding().textViewInventory.setOnClickListener(v -> inventoryFragment());

        getDataBinding().imageViewDashboard.setOnClickListener(v -> dashboardFragment());

        getDataBinding().imageViewInventory.setOnClickListener(v -> inventoryFragment());

        getDataBinding().floatingActionButton.setOnClickListener(v -> {
            doctorViewModel.check(df.format(date));

        });

    }


    private void observeAttendance() {
        doctorViewModel.getDoctorAttendanceMutableLiveData1().observe(this, (String String) -> {
            if (String == null) {
                gotoAttendingActivity();
            } else {
                gotoAddNewConsultationScreen();
            }
        });

    }

    private void gotoAttendingActivity() {
        startActivity(new Intent(DashboardActivity.this, AttendingActivity.class));
        finish();
    }

    private void gotoAddNewConsultationScreen() {
        startActivity(new Intent(DashboardActivity.this, AddNewConsultationActivity.class));
    }

    private void inventoryFragment() {
        darkInventory();
        fragment = new InventoryFragment();

        InventoryFragment myFragment = (InventoryFragment) getSupportFragmentManager().findFragmentByTag("Inventory");
        if (!(myFragment != null && myFragment.isVisible())) {
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment, "Inventory").addToBackStack("Inventory").commit();
        }
    }

    private void dashboardFragment() {
        darkDashboard();
        fragment = new DashboardFragment();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment, "Dashboard").addToBackStack("Dashboard").commit();

    }

    private void darkDashboard() {
        getDataBinding().imageViewDashboard.setImageResource(R.drawable.dashboard_dark_24px);
        getDataBinding().textViewDashboard.setTextColor(this.getResources().getColor(R.color.dark2));
        getDataBinding().imageViewInventory.setImageResource(R.drawable.assignment_hover_24px);
        getDataBinding().textViewInventory.setTextColor(this.getResources().getColor(R.color.light1));
    }

    private void darkInventory() {
        getDataBinding().imageViewInventory.setImageResource(R.drawable.assignment_dark_24px);
        getDataBinding().textViewInventory.setTextColor(this.getResources().getColor(R.color.dark2));
        getDataBinding().imageViewDashboard.setImageResource(R.drawable.dashboard_hover_24px);
        getDataBinding().textViewDashboard.setTextColor(this.getResources().getColor(R.color.light1));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dashboard;
    }

    @Override
    public void onBackPressed() {
        DashboardFragment myFragment = (DashboardFragment) getSupportFragmentManager().findFragmentByTag("Dashboard");
        if (myFragment != null && myFragment.isVisible()) {
            finish();
        } else {
            darkDashboard();
            fragmentManager.popBackStack();
        }
    }


}