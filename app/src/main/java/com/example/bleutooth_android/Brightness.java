package com.example.bleutooth_android;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class Brightness extends AppCompatActivity {

    //seekbar for brightness 0-255
    private SeekBar seekBar;
    //value of brightness 0-255
    private TextView textView;
    //auto-no auto
    private CheckBox checkbox;
    //lightness sensor
    private SensorManager mSensorManager;
    private Sensor lightSensor;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brightness);

        seekBar = (SeekBar) findViewById(R.id.seekBar2);
        checkbox =(CheckBox)findViewById(R.id.checkBox);
        textView = (TextView) findViewById(R.id.brightnessValue);
        mSensorManager= (SensorManager) Brightness.this.getSystemService(Context.SENSOR_SERVICE);
        lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        //check if there is lightness sensor,if not checkbox of auto will be unclickable
        if (lightSensor == null)
            checkbox.setEnabled(false);
        //check if is auto mode
        checkbox.setChecked(isAutoBrightness(Brightness.this));
        //setScreenBrightness(MainActivity.this,100);
        //get instance brightness
        textView.setText("Brightness:" + Integer.toString(getScreenBrightness(Brightness.this)));
        seekBar.setProgress(getScreenBrightness(Brightness.this));
        //change seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //active when seekbar changed
                textView.setText("Brightness:" + Integer.toString(progress));
                setScreenBrightness(Brightness.this,progress);
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        });
        //change mode
        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    if(autoBrightness(Brightness.this,true)){
                        Toast.makeText(Brightness.this,"Auto",Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    if(autoBrightness(Brightness.this,false)){
                        Toast.makeText(Brightness.this,"No Auto",Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }


    /**
     *set brightness
     * @param paramInt 0-255
     */
    public static void setScreenBrightness(Context context,int paramInt) {
        /*
        Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS, paramInt);
        Uri uri = Settings.System
                .getUriFor("screen_brightness");
        context.getContentResolver().notifyChange(uri, null);

        ---------------------------not enough for android 6 and further version-----------------------------------------
         */
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                //demand for right
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            } else {
                Settings.System.putInt(context.getContentResolver(),
                        Settings.System.SCREEN_BRIGHTNESS, paramInt);

            }
        }

    }

    /**
     * check mode
     */
    public static boolean isAutoBrightness(Context context) {
        ContentResolver resolver = context.getContentResolver();
        boolean automicBrightness = false;

        try {
            automicBrightness = Settings.System.getInt(resolver, Settings.System.SCREEN_BRIGHTNESS_MODE) == Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;
        } catch (Settings.SettingNotFoundException e) {
            e.printStackTrace();
        }

        return automicBrightness;
    }


    /**
     * open/close auto
     */
    public static boolean autoBrightness(Context context, boolean flag) {
        int value = 0;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(context)) {
                //demand for right
                Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_WRITE_SETTINGS);
                intent.setData(Uri.parse("package:" + context.getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
            }
        }
        if (flag) {
            value = Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC;  //open
        } else {
            value = Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL;//close
        }
        return Settings.System.putInt(context.getContentResolver(),
                Settings.System.SCREEN_BRIGHTNESS_MODE,
                value);
    }

    /**
     * get brightness
     */
    public static <ContentResolver> int getScreenBrightness(Context context) {
        int nowBrightnessValue = 0;
        ContentResolver resolver = (ContentResolver) context.getContentResolver();
        try {
            nowBrightnessValue = android.provider.Settings.System.getInt((android.content.ContentResolver) resolver, Settings.System.SCREEN_BRIGHTNESS);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return nowBrightnessValue;
    }

    /**
     *set window brightness(no change for system)
     */
    private void setWindowBrightness(int brightness) {
        Window window = getWindow();
        WindowManager.LayoutParams lp = window.getAttributes();
        lp.screenBrightness = brightness / 255.0f;
        window.setAttributes(lp);
    }


}