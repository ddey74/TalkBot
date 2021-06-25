
package com.sushank.talkbot;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;

public class BlueWifi extends AppCompatActivity {
    ToggleButton tb1, tb2;
    BluetoothAdapter ba;
    WifiManager wifi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_wifi);

        tb1 = (ToggleButton) (findViewById(R.id.tb1));
        tb2 = (ToggleButton) (findViewById(R.id.tb2));

        ba = BluetoothAdapter.getDefaultAdapter();

        try {
            wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
            if (wifi.isWifiEnabled()) {
                tb1.setChecked(true);}
                if (ba.isEnabled())
                {
                    tb2.setChecked(true);

            }
                tb1.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d("Mymsg"," for wifi");
                        if (tb1.isChecked()) {
                            wifi.setWifiEnabled(true);
                            Toast.makeText(BlueWifi.this, "WiFI Enabled", Toast.LENGTH_SHORT);
                        } else {
                            wifi.setWifiEnabled(false);
                            Toast.makeText(BlueWifi.this, "WiFI Disabled", Toast.LENGTH_SHORT);
                        }

                    }
                });

                tb2.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("Mymsg2"," for Bluetooth");

                        if (tb2.isChecked()) {
                            ba.enable();
                            Toast.makeText(BlueWifi.this, "Bluetooth Enabled", Toast.LENGTH_SHORT);
                        } else {
                            ba.disable();
                            Toast.makeText(BlueWifi.this, "Bluetooth Disabled", Toast.LENGTH_SHORT);
                        }
                    }
                });





        }
        catch(Exception e)
        {
            e.printStackTrace();
        }

    }
}