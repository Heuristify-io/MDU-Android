package com.heuristify.mdu.view.activities;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProviders;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityDashboardBinding;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.helper.Helper;
import com.heuristify.mdu.helper.LocationLatLng;
import com.heuristify.mdu.helper.Utilities;
import com.heuristify.mdu.mvvm.viewmodel.DoctorViewModel;
import com.heuristify.mdu.sharedPreferences.SharedHelper;
import com.heuristify.mdu.view.fragments.DashboardFragment;
import com.heuristify.mdu.view.fragments.InventoryFragment;

public class DashboardActivity extends BindingBaseActivity<ActivityDashboardBinding> {

    private static final int REQUEST_CHECK_SETTINGS = 000;
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 102;
    // location updates interval - 10sec
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 20000;

    // fastest updates interval - 5 sec
    // location updates will be received if another app is requesting the locations
    // than your app can handle
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    final FragmentManager fragmentManager = getSupportFragmentManager();
    Fragment fragment = null;
    DoctorViewModel doctorViewModel;

    // bunch of location related apis
    private FusedLocationProviderClient mFusedLocationClient;
    private SettingsClient mSettingsClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private Boolean requestingLocationUpdates = false;

    private final String TAG = "DashboardActivity";


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getDataBinding().textViewName.setText("Dr. " + SharedHelper.getKey(this, Helper.NAME));
        doctorViewModel = ViewModelProviders.of(this).get(DoctorViewModel.class);
        init();
        checkCameraPermission();



        observeAttendance();

        dashboardFragment();
        getDataBinding().textViewDashboard.setOnClickListener(v -> dashboardFragment());

        getDataBinding().textViewInventory.setOnClickListener(v -> inventoryFragment());

        getDataBinding().imageViewDashboard.setOnClickListener(v -> dashboardFragment());

        getDataBinding().imageViewInventory.setOnClickListener(v -> inventoryFragment());

        getDataBinding().floatingActionButton.setOnClickListener(v -> doctorViewModel.checkPatientAttendance(Utilities.currentDate()));

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_dashboard;
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

    private void updateLocation() {
        if (mCurrentLocation != null) {
            DisplayLog.showLog(TAG, "locationResult" + "Lat" + mCurrentLocation.getLatitude() + " Lng " + mCurrentLocation.getLongitude());
            LocationLatLng.latitude = mCurrentLocation.getLatitude();
            LocationLatLng.longitude = mCurrentLocation.getLongitude();
        }

    }

    private void init() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mSettingsClient = LocationServices.getSettingsClient(this);

        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                // location is received
                mCurrentLocation = locationResult.getLastLocation();
                updateLocation();

            }
        };

        requestingLocationUpdates = false;
        createLocationRequest();
    }

    public void stopLocationUpdates() {
        // Removing location updates
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        DisplayLog.showLog(TAG, "locationUpdate" + " Stop ");
                    }
                });
    }


    private void startLocationUpdates() {
        mSettingsClient
                .checkLocationSettings(mLocationSettingsRequest)
                .addOnSuccessListener(this, locationSettingsResponse -> {
                    //noinspection MissingPermission
                    mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
                    updateLocation();
                })
                .addOnFailureListener(this, e -> {
                    int statusCode = ((ApiException) e).getStatusCode();
                    switch (statusCode) {
                        case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                            DisplayLog.showLog(TAG, "location" + " not satisfy ");
                            try {
                                // Show the dialog by calling startResolutionForResult(), and check the
                                // result in onActivityResult().
                                ResolvableApiException rae = (ResolvableApiException) e;
                                rae.startResolutionForResult(DashboardActivity.this, REQUEST_CHECK_SETTINGS);
                            } catch (IntentSender.SendIntentException sie) {
                                Log.e(TAG, "PendingIntent unable to execute request.");
                            }
                            break;
                        case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                            String errorMessage = "Location settings are inadequate, and cannot be " +
                                    "fixed here. Fix in Settings.";
                            Log.e(TAG, errorMessage);

                            Toast.makeText(mContext, errorMessage, Toast.LENGTH_LONG).show();
                    }
                    updateLocation();

                });
    }

    protected void createLocationRequest() {

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();

    }

    private void checkCameraPermission() {

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
        } else {
//request location
            startLocationUpdates();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (requestingLocationUpdates) {
            startLocationUpdates();
        }
        updateLocation();
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (requestingLocationUpdates) {
            // pausing location updates
            stopLocationUpdates();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_LOCATION: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //request location
                    requestingLocationUpdates = true;
                    startLocationUpdates();
                } else {
                    checkCameraPermission();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // Check for the integer request code originally supplied to startResolutionForResult().
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            switch (resultCode) {
                case Activity.RESULT_OK:
                    Log.e(TAG, "User agreed to make required location settings changes.");
                    // Nothing to do. startLocationupdates() gets called in onResume again.
                    startLocationUpdates();
                    break;
                case Activity.RESULT_CANCELED:
                    Log.e(TAG, "User chose not to make required location settings changes.");
                    break;
            }
        }
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