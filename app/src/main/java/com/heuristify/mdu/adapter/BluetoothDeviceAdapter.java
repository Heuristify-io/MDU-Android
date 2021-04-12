package com.heuristify.mdu.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.heuristify.mdu.R;
import com.heuristify.mdu.interfaces.OnItemClickPosition;
import com.heuristify.mdu.pojo.Bluetooth;

import java.util.List;

public class BluetoothDeviceAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    private List<Bluetooth> blueetooths;
    private Context context;
    private OnItemClickPosition onItemClickPosition;

    public BluetoothDeviceAdapter(List<Bluetooth> blueetooths, Context context) {
        this.blueetooths = blueetooths;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.bluetooth_device_list, parent, false);
        return new DeviceViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DeviceViewHolder viewHolder = (DeviceViewHolder)holder;
        viewHolder.nameTextView.setText(blueetooths.get(position).getDevice_name());
        viewHolder.addressTextView.setText(blueetooths.get(position).getDevice_address());

        viewHolder.nameTextView.setOnClickListener(v -> onItemClickPosition.onRecyclerViewItemClick(position));

        viewHolder.addressTextView.setOnClickListener(v -> onItemClickPosition.onRecyclerViewItemClick(position));
    }

    public void setOnItemClickPosition(OnItemClickPosition onItemClickPosition) {
        this.onItemClickPosition = onItemClickPosition;
    }

    @Override
    public int getItemCount() {
        return blueetooths.size();
    }

    public class DeviceViewHolder extends RecyclerView.ViewHolder{
        public TextView nameTextView,addressTextView;
        public DeviceViewHolder(View itemView) {
            super(itemView);
            nameTextView = (TextView) itemView.findViewById(R.id.name);
            addressTextView = (TextView) itemView.findViewById(R.id.address);
        }
    }

}
