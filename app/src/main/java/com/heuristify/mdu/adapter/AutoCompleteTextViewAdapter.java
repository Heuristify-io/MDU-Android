package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ArrayAdapter;
import android.widget.TextView;


import com.heuristify.mdu.R;
import com.heuristify.mdu.database.entity.StockMedicine;

import java.util.List;

public class AutoCompleteTextViewAdapter extends ArrayAdapter<StockMedicine> {

    Context context;
    List<StockMedicine> items;

    public AutoCompleteTextViewAdapter(Context context, int resource, List<StockMedicine> items) {
        super(context, resource, items);
        this.context = context;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.custom_symbol_list_item, parent, false);
        }
        StockMedicine medicine = items.get(position);
        if (medicine != null) {
            TextView name = (TextView) view.findViewById(R.id.textViewName);
            TextView quantity = (TextView) view.findViewById(R.id.textViewQuantity);
            if (name != null)
                name.setText(medicine.getStock_medicine_name());
            quantity.setText(medicine.getStock_medicine_quantity());
        }
        return view;


    }
}
