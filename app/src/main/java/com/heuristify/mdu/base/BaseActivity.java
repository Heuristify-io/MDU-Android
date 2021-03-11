package com.heuristify.mdu.base;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


import com.heuristify.mdu.helper.CustomDialog;
import com.heuristify.mdu.sharedPreferences.SharedHelper;

public abstract class BaseActivity extends AppCompatActivity {

    protected Context mContext;
    private CustomDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        progressDialog = new CustomDialog(mContext);
        progressDialog.setCancelable(false);
    }

    @LayoutRes
    protected abstract int getLayoutRes();


    public void showProgressDialog() {
        if (progressDialog != null && !progressDialog.isShowing()) {
            progressDialog.show();
        }
    }

    public void hideProgressDialog() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
    }

    public void logOut() {
        SharedHelper.deleteAllSharedPrefs(mContext);
    }
}
