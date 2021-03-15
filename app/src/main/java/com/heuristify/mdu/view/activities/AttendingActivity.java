package com.heuristify.mdu.view.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivityAttendingBinding;
import com.heuristify.mdu.interfaces.OnClickHandlerInterface;


public class AttendingActivity extends BindingBaseActivity<ActivityAttendingBinding> implements OnClickHandlerInterface {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getDataBinding().setClickHandler(this);
    }

    private void gotoDashboardActivity() {
        startActivity(new Intent(AttendingActivity.this, DashboardActivity.class));
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_attending;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.buttonSkip:
                gotoDashboardActivity();
                break;
            case R.id.buttonAttending:
                gotoDashboardActivity();
                break;
        }

    }
}