package com.heuristify.mdu.view.activities;


import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityDashboardBinding;
import com.heuristify.mdu.helper.Helper;
import com.heuristify.mdu.sharedPreferences.SharedHelper;
import com.heuristify.mdu.view.fragments.DashboardFragment;
import com.heuristify.mdu.view.fragments.InventoryFragment;

public class DashboardActivity extends BindingBaseActivity<ActivityDashboardBinding> {

    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment = null;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().textViewName.setText("Dr. " + SharedHelper.getKey(this, Helper.NAME));

        dashboardFragment();
        getDataBinding().textViewDashboard.setOnClickListener(v -> dashboardFragment());

        getDataBinding().textViewInventory.setOnClickListener(v -> inventoryFragment());

        getDataBinding().imageViewDashboard.setOnClickListener(v -> dashboardFragment());

        getDataBinding().imageViewInventory.setOnClickListener(v -> inventoryFragment());


    }

    private void inventoryFragment() {
        darkInventory();
        fragment = new InventoryFragment();

        InventoryFragment myFragment = (InventoryFragment)getSupportFragmentManager().findFragmentByTag("Inventory");
        if (!(myFragment != null && myFragment.isVisible())) {
            fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment,"Inventory").addToBackStack("Inventory").commit();
        }
    }

    private void dashboardFragment() {
        darkDashboard();
        fragment = new DashboardFragment();
        fragmentManager.beginTransaction().replace(R.id.frameLayout, fragment,"Dashboard").addToBackStack("Dashboard").commit();

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
        DashboardFragment myFragment = (DashboardFragment)getSupportFragmentManager().findFragmentByTag("Dashboard");
        if (myFragment != null && myFragment.isVisible()) {
             finish();
        } else {
            darkDashboard();
            fragmentManager.popBackStack();
        }
    }


}