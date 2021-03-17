package com.heuristify.mdu.view.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.heuristify.mdu.R;
import com.heuristify.mdu.adapter.MedicineInventoryAdapter;
import com.heuristify.mdu.base.BindingBaseFragment;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.database.entity.MedicineEntity;
import com.heuristify.mdu.databinding.FragmentDashboardBinding;
import com.heuristify.mdu.databinding.FragmentInventoryBinding;
import com.heuristify.mdu.pojo.Medicine;
import com.heuristify.mdu.view.activities.AddNewInventoryActivity;

import java.util.ArrayList;
import java.util.List;


public class InventoryFragment extends BindingBaseFragment<FragmentInventoryBinding> {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private List<Medicine> medicineList;
    private MedicineInventoryAdapter medicineInventoryAdapter;

    public InventoryFragment() {

    }


    public static InventoryFragment newInstance(String param1, String param2) {
        InventoryFragment fragment = new InventoryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
        medicineList = new ArrayList<>();
    }


    @Override
    public void OnCreateView(LayoutInflater inflater, @Nullable Bundle savedInstanceState) {

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialRecycleView();

        new Thread(new Runnable() {
            @Override
            public void run() {

                MedicineEntity medicineEntity = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getMedicine();
                if (medicineEntity != null) {
                    medicineList.addAll(medicineEntity.getMedicineList());
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Log.e("list_size",""+medicineList.size());
                            medicineInventoryAdapter.notifyDataSetChanged();
                        }
                    });
                }

            }
        }).start();

        getDataBinding().buttonAddInventory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AddNewInventoryActivity.class));
            }
        });

    }

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_inventory;
    }

    private void initialRecycleView() {

        getDataBinding().recyclerViewMedicine.setHasFixedSize(true);
        getDataBinding().recyclerViewMedicine.setItemAnimator(new DefaultItemAnimator());
        medicineInventoryAdapter = new MedicineInventoryAdapter(medicineList, getActivity());
        getDataBinding().recyclerViewMedicine.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        getDataBinding().recyclerViewMedicine.setAdapter(medicineInventoryAdapter);
        getDataBinding().recyclerViewMedicine.setItemAnimator(null);
    }
}