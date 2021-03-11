package com.heuristify.mdu.view.activities;



import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseActivity;
import com.heuristify.mdu.databinding.ActivitySplashBinding;

public class SplashActivity extends BindingBaseActivity<ActivitySplashBinding> {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(SplashActivity.this, GetStartActivity.class));
//                finish();
            }
        }, 500);

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_splash;
    }
}