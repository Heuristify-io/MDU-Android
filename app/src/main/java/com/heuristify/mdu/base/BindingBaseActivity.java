package com.heuristify.mdu.base;


import android.content.Context;
import android.os.Bundle;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;

import com.heuristify.mdu.helper.CustomDialog;
import com.heuristify.mdu.helper.CustomProgressDialog;
import com.heuristify.mdu.sharedPreferences.SharedHelper;

public abstract class BindingBaseActivity<DB extends ViewDataBinding> extends AppCompatActivity {

    public DB dataBinding;
    protected Context mContext;
    private CustomDialog progressDialog;
    private CustomProgressDialog customProgressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        dataBinding = DataBindingUtil.setContentView(this,getLayoutRes());
        progressDialog = new CustomDialog(mContext);
        customProgressDialog = new CustomProgressDialog(mContext);
        progressDialog.setCancelable(false);
    }

    @LayoutRes
    protected abstract int getLayoutRes();

    public DB getDataBinding() {
        return dataBinding;
    }

    public void showProgressDialog() {
            customProgressDialog.showProgress();


    }

    public void hideProgressDialog() {
        customProgressDialog.dismiss();
    }

    public void logOut(){
        SharedHelper.deleteAllSharedPrefs(mContext);
    }

}
