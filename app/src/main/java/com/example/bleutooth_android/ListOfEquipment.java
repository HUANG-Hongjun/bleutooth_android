package com.example.bleutooth_android;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

public class ListOfEquipment extends AppCompatActivity {

    public static final int CODE_ACCESS_FINE_LOCATION = 5;
    public static final int CODE_ACCESS_COARSE_LOCATION = 6;


    private BlueToothControler myControler = new BlueToothControler();
    private ListView listView;  //list view of equipment
    private ArrayList<BluetoothDevice> listEquipment;   //list of equipment
    private EquipmentAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_equipment);
        setTitle("List of equipment");

        registerBluetoothReceiver();
        listView = findViewById(R.id.ListOfEquipment);
        LayoutInflater inflater =getLayoutInflater();
        listEquipment =new ArrayList<>();
        myAdapter=new EquipmentAdapter(inflater,listEquipment);
        listView.setAdapter(myAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view,
                                    int i, long l) {
                BluetoothDevice device = listEquipment.get(i);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                    device.createBond();
                }
            }
        });


    }
     /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        menu.add(1,101,1,"Set visible");
        menu.add(1,102,2,"Search Equipment");
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        switch (id){
            case 101:
                myControler.enableVisibility(ListOfEquipment.this);
                break;
            case 102:
                myControler.searchEquipment();
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

     */

    public void setVisible(View view){
        myControler.enableVisibility(ListOfEquipment.this);
    }

    public void Search(View view){
        if (Build.VERSION.SDK_INT >= 23) {
            if (ContextCompat.checkSelfPermission(ListOfEquipment.this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ListOfEquipment.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},CODE_ACCESS_COARSE_LOCATION);
            }
            else if (ContextCompat.checkSelfPermission(ListOfEquipment.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(ListOfEquipment.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},CODE_ACCESS_FINE_LOCATION);
            }

            else{
                myControler.myAdapter.startDiscovery();
            }
        }
        else{
            myControler.myAdapter.startDiscovery();
        }
        //Toast.makeText(ListOfEquipment.this, "Searching", Toast.LENGTH_SHORT).show();
    }



    private void registerBluetoothReceiver(){
        IntentFilter filter = new IntentFilter();
        //start searching
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        //stop searching
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        //searching equipments
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        //visibility changed
        filter.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);
        //bond state changed
        filter.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);
        registerReceiver(receiver, filter);
    }





    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if(BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)){
                //init list
                Toast.makeText(ListOfEquipment.this, "Searching", Toast.LENGTH_SHORT).show();
                listEquipment = new ArrayList<>();
                updateList();
            } else if(BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)){
                //setProgressBarIndeterminateVisibility(false);
                Toast.makeText(ListOfEquipment.this, "Searching finished", Toast.LENGTH_SHORT).show();
            }
            else if(BluetoothDevice.ACTION_FOUND.equals(action)) {
                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //add equipment
                listEquipment.add(device);
                updateList();

            } else if(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED.equals(action)) {
                int scanMode = intent.getIntExtra(BluetoothAdapter.EXTRA_SCAN_MODE, 0);
                if(scanMode == BluetoothAdapter.SCAN_MODE_CONNECTABLE_DISCOVERABLE){
                }

            } else if(BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                BluetoothDevice remoteDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                if(remoteDevice == null) {
                    Toast.makeText(ListOfEquipment.this, "No equipment", Toast.LENGTH_SHORT).show();
                    return;
                }
                int status = intent.getIntExtra(BluetoothDevice.EXTRA_BOND_STATE, 0);
                if(status == BluetoothDevice.BOND_BONDED) {
                    Toast.makeText(ListOfEquipment.this, "Equipment bonded", Toast.LENGTH_SHORT).show();
                } else if(status == BluetoothDevice.BOND_BONDING) {
                    Toast.makeText(ListOfEquipment.this, "Equipment bonding", Toast.LENGTH_SHORT).show();
                } else if(status == BluetoothDevice.BOND_NONE) {
                    Toast.makeText(ListOfEquipment.this, "No equipment bonded", Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    public void updateList(){
        listView =findViewById(R.id.ListOfEquipment);
        LayoutInflater inflater =getLayoutInflater();
        myAdapter=new EquipmentAdapter(inflater,listEquipment);
        listView.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }





}