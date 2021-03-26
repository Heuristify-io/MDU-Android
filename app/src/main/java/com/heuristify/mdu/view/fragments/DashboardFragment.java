package com.heuristify.mdu.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.BindingBaseFragment;
import com.heuristify.mdu.databinding.FragmentDashboardBinding;
import com.heuristify.mdu.view.activities.AddNewConsultationActivity;


public class DashboardFragment extends BindingBaseFragment<FragmentDashboardBinding> {


    public static final String ARG_PARAM2;

    static {
        ARG_PARAM2 = "param2";
    }

    public DashboardFragment() {

    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void OnCreateView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {
//        getDataBinding().buttonAttending2.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddNewConsultationActivity.class)));
//        getDataBinding().buttonAttending22.setOnClickListener(v -> startActivity(new Intent(getActivity(), AddNewConsultationActivity.class)));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_dashboard;
    }
}