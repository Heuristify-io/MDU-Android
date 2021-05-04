package com.heuristify.mdu.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.base.MyApplication;
import com.heuristify.mdu.helper.StoreClickWidget;
import com.heuristify.mdu.helper.WidgetList;
import com.heuristify.mdu.database.entity.StockMedicine;
import com.heuristify.mdu.interfaces.OnItemClick;
import com.heuristify.mdu.interfaces.OnItemClickPosition;

import java.util.ArrayList;
import java.util.List;


public class AddDiagnosisAndMedicineAdapter extends RecyclerView.Adapter<AddDiagnosisAndMedicineAdapter.ViewHolder> implements OnItemClick {

    private final List<WidgetList> widgetLists;
    private final Context context;
    private final List<StoreClickWidget> storeClickWidgetList;
    private final String[] frequencies = new String[]{"0+0+1", "0+1+0", "0+1+1", "1+0+0", "1+0+1", "1+1+0", "1+1+1",
            "0+0+2", "0+2+0", "0+2+2", "2+0+0", "2+0+2", "2+2+0", "2+2+2", "0+0+3", "0+3+0", "0+3+3", "3+0+0", "3+0+3", "3+3+0", "3+3+3",
            "0+0+4", "0+4+0", "0+4+4", "4+0+0", "4+0+4", "4+4+0", "4+4+4", "0+0+5", "0+5+0", "0+5+5", "5+0+0", "5+0+5", "5+5+0", "5+5+5"};
    private ArrayAdapter frequency_adapter;
    private List<StockMedicine> stockMedicineList;
    private SearchMedicineAdapter searchMedicineAdapter;
    private ViewHolder holders;
    private int positions;
    private OnItemClickPosition onItemClickPosition;
    private String showList = "showList";
    ArrayAdapter<String> gender_adapter;


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

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.setIsRecyclable(false);
        frequency_adapter = new ArrayAdapter<>(context, android.R.layout.simple_spinner_item, frequencies);
        frequency_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.materialSpinner.setAdapter(frequency_adapter);

        gender_adapter = new ArrayAdapter<String>(context, R.layout.spinner_item_for_diagnosis, frequencies) {
            @Override
            public View getDropDownView(int position, View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView tv = (TextView) view;
                tv.setTextColor(context.getResources().getColor(R.color.dark2));
                return view;
            }
        };
        gender_adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.materialSpinner.setAdapter(gender_adapter);

        //initialize recycleView
        initializeRecycleView(holder);

        if (widgetLists.get(position).getStockMedicine() != null) {
            showList = "notShowList";
            holder.editTextCustomSearch.setText(storeClickWidgetList.get(position).getStockMedicine().getStock_medicine_name());
            storeClickWidgetList.get(position).setStockMedicine(widgetLists.get(position).getStockMedicine());
        }

        if (!widgetLists.get(position).getFrequencySpinner().equalsIgnoreCase("")) {
            int pos = 0;
            String text = storeClickWidgetList.get(position).getEditTextFrequency();
            for (int i = 0; i < frequencies.length; i++) {
                if (text.equals(frequencies[i])) {
                    pos = i;
                    break;
                }
            }
            holder.materialSpinner.setSelection(pos);
            storeClickWidgetList.get(position).setEditTextFrequency(widgetLists.get(position).getFrequencySpinner());
        }

        if (!widgetLists.get(position).getEditTextDays().equalsIgnoreCase("")) {
            holder.editTextDays.setText(storeClickWidgetList.get(position).getEditTextDays());
            storeClickWidgetList.get(position).setEditTextDays(storeClickWidgetList.get(position).getEditTextDays());
        }


        holder.materialSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View view,
                                       int pos, long arg3) {
                TextView tv = (TextView) view;
                storeClickWidgetList.get(position).setEditTextFrequency(tv.getText().toString());
                widgetLists.get(position).setFrequencySpinner(tv.getText().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });


        holder.editTextDays.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (position < storeClickWidgetList.size()) {
                    storeClickWidgetList.get(position).setEditTextDays(s.toString());
                    widgetLists.get(position).setEditTextDays(s.toString());
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        holder.editTextCustomSearch.setOnTouchListener((view, motionEvent) -> {
            showList = "showList";
            return false;
        });

        holder.editTextCustomSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable s) {
                if (!showList.equals("notShowList")) {
                    if (s.length() > 0) {
                        if (s.toString().equals("")) {
                            goneRecycleViewAndOtherViews(holder);

                        } else {
                            if (countNumChars(s.toString()) == 0) {
                                // qet list from local db
                                getDataFromDb(s.toString(), holder, position);
                            }
                        }
                    } else {
                        stockMedicineList.clear();
                        searchMedicineAdapter.notifyDataSetChanged();
                        goneRecycleViewAndOtherViews(holder);
                        if (storeClickWidgetList.get(position).getStockMedicine() != null) {
                            StockMedicine stockMedicine = storeClickWidgetList.get(position).getStockMedicine();
                            storeClickWidgetList.remove(stockMedicine);
                        }
                    }
                }
            }

        });

        holder.btnDelete.setOnClickListener(v -> onItemClickPosition.onRecyclerViewItemClick(position));

    }

    public void setOnItemClickPosition(OnItemClickPosition onItemClickPosition) {
        this.onItemClickPosition = onItemClickPosition;
    }

    private int countNumChars(String s) {
        int count = 0;
        for (char c : s.toCharArray()) {
            if (c == ' ') {
                count++;
            } else {
                count = 0;
            }
        }
        return count;
    }

    private void goneRecycleViewAndOtherViews(ViewHolder holder) {

        holder.recyclerView.setVisibility(View.GONE);
    }

    private void initializeRecycleView(ViewHolder holder) {
        stockMedicineList = new ArrayList<>();
        holder.recyclerView.setHasFixedSize(true);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        searchMedicineAdapter = new SearchMedicineAdapter(context, stockMedicineList);
        holder.recyclerView.setAdapter(searchMedicineAdapter);
        holder.recyclerView.setItemAnimator(null);
        searchMedicineAdapter.setItemClick(this);
    }

    private void getDataFromDb(String toString, ViewHolder holder, int position) {
        new Thread(() -> {
            List<StockMedicine> stockMedicineList1 = MyApplication.getInstance().getLocalDb(MyApplication.getInstance()).getAppDatabase().stockMedicineDoa().getStockMedicine(toString);
            if (stockMedicineList1 != null) {

                new Handler(MyApplication.getInstance().getMainLooper()).post(() -> {

                    if (!stockMedicineList.isEmpty()) {
                        stockMedicineList.clear();
                        searchMedicineAdapter.notifyDataSetChanged();
                    }

                    if (holder.editTextCustomSearch.getText().toString().length() > 0) {
                        holder.recyclerView.setVisibility(View.VISIBLE);
                        stockMedicineList.addAll(stockMedicineList1);
                        searchMedicineAdapter.notifyDataSetChanged();
                        holders = holder;
                        positions = position;
                    }

                });

            } else {

                new Handler(MyApplication.getInstance().getMainLooper()).post(() -> {
                    stockMedicineList.clear();
                    searchMedicineAdapter.notifyDataSetChanged();
                    goneRecycleViewAndOtherViews(holder);
                });
            }

        }).start();
    }


    @Override
    public int getItemCount() {
        return widgetLists.size();
    }

    @Override
    public void onRecyclerViewItemClick(StockMedicine stockMedicine) {
        boolean isMedExist = false;
        for (int i = 0; i < storeClickWidgetList.size(); i++) {
            if (storeClickWidgetList.get(i).getStockMedicine() != null) {
                if (storeClickWidgetList.get(i).getStockMedicine().getMedicineId() == stockMedicine.getMedicineId()) {
                    isMedExist = true;
                    break;
                }
            }
        }

        if (isMedExist) {
            Toast.makeText(context, "" + stockMedicine.getStock_medicine_name() + " already selected", Toast.LENGTH_SHORT).show();
        } else {
            prescribedMedicine(stockMedicine);
        }

    }

    private void prescribedMedicine(StockMedicine stockMedicine) {

        holders.editTextCustomSearch.setText(stockMedicine.getStock_medicine_name());
        holders.editTextCustomSearch.setSelection(holders.editTextCustomSearch.length());
        storeClickWidgetList.get(positions).setStockMedicine(stockMedicine);
        widgetLists.get(positions).setStockMedicine(stockMedicine);

        new Handler(MyApplication.getInstance().getMainLooper()).postDelayed(() -> {
            stockMedicineList.clear();
            searchMedicineAdapter.notifyDataSetChanged();

        }, 300);
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        Spinner materialSpinner;
        EditText editTextDays;
        EditText editTextCustomSearch;
        RecyclerView recyclerView;
        ImageView btnDelete;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            editTextDays = itemView.findViewById(R.id.editTextDays);
            editTextCustomSearch = itemView.findViewById(R.id.editTextSearchMedicine);
            materialSpinner = itemView.findViewById(R.id.material_spinner_frequencies);
            recyclerView = itemView.findViewById(R.id.recyclerViewMedicineSearch);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}
