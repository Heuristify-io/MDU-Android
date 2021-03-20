package com.heuristify.mdu.base;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
import androidx.fragment.app.Fragment;

import com.heuristify.mdu.helper.CustomProgressDialog;
import com.heuristify.mdu.sharedPreferences.SharedHelper;

public abstract class BindingBaseFragment<DB extends ViewDataBinding> extends Fragment {

    public DB dataBinding;
    protected Context mContext;
    private CustomProgressDialog customProgressDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        dataBinding  = DataBindingUtil.inflate(inflater,getLayoutRes(),container,false);

        customProgressDialog = new CustomProgressDialog(getActivity());
        mContext  = getActivity();
        OnCreateView(inflater,savedInstanceState);
        return dataBinding.getRoot();
    }

    public abstract void OnCreateView(LayoutInflater inflater,@Nullable Bundle savedInstanceState);
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
