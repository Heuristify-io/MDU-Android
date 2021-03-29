package com.heuristify.mdu.adapter;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.helper.DisplayLog;
import com.heuristify.mdu.helper.StoreClickWidget;
import com.heuristify.mdu.helper.WidgetList;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.tiper.MaterialSpinner;

import java.util.ArrayList;
import java.util.List;


public class AddDiagnosisAndMedicineAdapter extends RecyclerView.Adapter<AddDiagnosisAndMedicineAdapter.ViewHolder> {

    private final List<WidgetList> widgetLists;
    private final Context context;
    private final List<StoreClickWidget> storeClickWidgetList;
    private final String[] frequencies = {"T.D.S", "T.D.F", "T.D.E", "T.D.A"};
    ArrayAdapter frequency_adapter;
    AutoCompleteTextViewAdapter aAdapter;
    List<StockMedicine> stockMedicineList = new ArrayList<>();


    public AddDiagnosisAndMedicineAdapter(List<WidgetList> widgetLists, Context context, List<StoreClickWidget> storeClickWidgetList) {
        this.widgetLists = widgetLists;
        this.context = context;
        this.storeClickWidgetList = storeClickWidgetList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.add_diagnosis_medicine_adapter_layout, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {


        frequency_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, frequencies);
        frequency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.materialSpinner.setAdapter(frequency_adapter);


//        holder.materialSpinner.getEditText().setText(frequencies[0]);

        holder.materialSpinner.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {

            @Override
            public void onNothingSelected(@NonNull MaterialSpinner materialSpinner) {

            }

            @Override
            public void onItemSelected(@NonNull MaterialSpinner materialSpinner, View view, int i, long l) {
                storeClickWidgetList.get(position).setEditTextFrequency(String.valueOf(materialSpinner.getEditText().getText()));
            }

        });


        holder.editTextDays.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

                storeClickWidgetList.get(position).setEditTextDays(s.toString());
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        holder.autoCompleteTextView.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable editable) {

            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (s.toString().length() > 0) {
                    // qet list from local db
                    getDataFromDb(s.toString(), holder);
                }
            }

        });

        holder.autoCompleteTextView.setOnItemClickListener((parent, view, position1, l) -> {
            StockMedicine stockMedicine = (StockMedicine) parent.getItemAtPosition(position1);
            holder.autoCompleteTextView.setText(stockMedicine.getStock_medicine_name());
            storeClickWidgetList.get(position).setStockMedicine(stockMedicine);

        });


    }

    private void getDataFromDb(String toString, ViewHolder holder) {

        new Thread(() -> {
            List<StockMedicine> stockMedicineList1 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().taskDao().getStockMedicine(toString);
            if (stockMedicineList1 != null) {
                stockMedicineList.clear();
                stockMedicineList.addAll(stockMedicineList1);
                ((AppCompatActivity) context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        DisplayLog.showLog("stock_medicine_list", "" + stockMedicineList.size());
                        setListToAdapter(holder);
                    }
                });
            }
        }).start();
    }

    private void setListToAdapter(ViewHolder holder) {
        aAdapter = new AutoCompleteTextViewAdapter(context, R.layout.custom_symbol_list_item, stockMedicineList);
        holder.autoCompleteTextView.setAdapter(aAdapter);
        aAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return widgetLists.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        MaterialSpinner materialSpinner;
        EditText editTextDays;
        AutoCompleteTextView autoCompleteTextView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextDays = itemView.findViewById(R.id.editTextDays);
            materialSpinner = itemView.findViewById(R.id.material_spinner_frequencies);
            autoCompleteTextView = itemView.findViewById(R.id.autoCompleteTextView);
        }
    }
}
