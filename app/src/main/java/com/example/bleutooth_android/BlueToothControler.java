package com.example.bleutooth_android;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;

import java.util.ArrayList;

/**
 * Bluetooth Adapter
 * Created by Huang & Yuan 29/03/2021
 */
public class BlueToothControler {

    public BluetoothAdapter myAdapter;

    public BlueToothControler(){
        myAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    /**
     * Verify whether bluetooth is supported
     * @return true = support, false = not support
     */
    public boolean isSupportBluetooth(){
        if(myAdapter != null){
            return true;
        }
        else{
            return false;
        }
    }

    /**
     * Determining Bluetooth status
     * @return true = open, false = close
     */
    public boolean isBluetoothOn()
    {
        assert (myAdapter != null);
        return myAdapter.isEnabled();
    }

    /**
     * Turn on Bluetooth
     */
    public void openBluetooth(){
        myAdapter.enable();
    }

    /**
     * turn off bluetooth
     */
    public void closeBluetooth(){
        myAdapter.disable();
    }

    /**
     * Turn on Bluetooth visibility
     */
    public void enableVisibility(Context context){
        Intent intent = new
                Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
        intent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION,300);
        context.startActivity(intent);
    }

    /**
     * Search for equipment
     */
    public void searchEquipment(){
        //assert (myAdapter != null);
        myAdapter.startDiscovery();
    }

    /**
     * Get list of equipment
     */
    public ArrayList<BluetoothDevice> getListOfEquipment(){
        return new ArrayList<>(myAdapter.getBondedDevices());
    }
}
