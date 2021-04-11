package com.example.bleutooth_android;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private BlueToothControler myBlueToothControler = new BlueToothControler();

    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            int state = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE,-1);
            switch(state){
                case BluetoothAdapter.STATE_OFF:
                    Toast.makeText(MainActivity.this, "Bleutooth off", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.STATE_ON:
                    Toast.makeText(MainActivity.this, "Bleutooth on", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.STATE_TURNING_ON:
                    Toast.makeText(MainActivity.this, "Bleutooth turnning on", Toast.LENGTH_SHORT).show();
                    break;
                case BluetoothAdapter.STATE_TURNING_OFF:
                    Toast.makeText(MainActivity.this, "Bleutooth turnning off", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    Toast.makeText(MainActivity.this, "Unkown state", Toast.LENGTH_SHORT).show();
                    break;
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        IntentFilter filter = new IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED);
        registerReceiver(receiver,filter);
    }

    public void isSupportBluetooth(View view){
        boolean result = myBlueToothControler.isSupportBluetooth();
        if(result){
            Toast.makeText(MainActivity.this, "This equipment supports bluetooth", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, "This equipment does not support bluetooth", Toast.LENGTH_SHORT).show();
        }
    }
    public void isOpenBluetooth(View view){
        boolean result = myBlueToothControler.isBluetoothOn();
        if(result){
            Toast.makeText(MainActivity.this, "Bluetooth is on", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(MainActivity.this, "Bluetooth is off", Toast.LENGTH_SHORT).show();
        }
    }
    public void OpenBluetooth(View view){
        myBlueToothControler.openBluetooth();
    }
    public void CloseBluetooth(View view){
        myBlueToothControler.closeBluetooth();
    }

    public void SearchEquipment(View view){
        Intent intent = new Intent(MainActivity.this,ListOfEquipment.class);
        startActivity(intent);
    }

    public void LightControl(View view){
        Intent intent = new Intent(MainActivity.this,Brightness.class);
        startActivity(intent);
    }

}