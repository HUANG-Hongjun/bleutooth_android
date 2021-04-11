package com.example.bleutooth_android;

import android.bluetooth.BluetoothDevice;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class EquipmentAdapter extends BaseAdapter {
    private ArrayList<BluetoothDevice> myData;
    private LayoutInflater Inflater;

    public EquipmentAdapter(LayoutInflater inflater,ArrayList<BluetoothDevice> data){
        Inflater = inflater;
        myData = data;
    }

    @Override
    public int getCount() {
        return myData.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View viewlist = Inflater.inflate(R.layout.single_equipment,null);
        BluetoothDevice device =myData.get(position);
        TextView name =(TextView)viewlist.findViewById(R.id.EquipmentName);
        TextView address = (TextView)viewlist.findViewById(R.id.EquipmentAddress);
        name.setText(device.getName());
        address.setText(device.getAddress());
        return viewlist;
    }
}
