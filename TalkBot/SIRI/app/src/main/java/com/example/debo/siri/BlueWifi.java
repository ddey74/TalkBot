package com.example.debo.siri;

import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import android.widget.ToggleButton;
//alt+enter to add permissions
public class BlueWifi extends AppCompatActivity {

    ToggleButton tg1,tg2;
    BluetoothAdapter ba;
    WifiManager wifi;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blue_wifi);

        ba=BluetoothAdapter.getDefaultAdapter();

        tg1=(ToggleButton)(findViewById(R.id.tg1));
        tg2=(ToggleButton)(findViewById(R.id.tg2));
        try{
            wifi=(WifiManager)this.getSystemService(Context.WIFI_SERVICE);
            if (wifi.isWifiEnabled())
            {
                tg1.setChecked(true);
            }
            if (ba.isEnabled())
            {
                tg2.setChecked(true);
            }
            tg1.setOnClickListener(new View.OnClickListener(){//listner
                                       @Override
                                       public void onClick(View view)
                                       {
                                           if(tg1.isChecked())
                                           {
                                               wifi.setWifiEnabled(true);// alt+enter to add permissions
                                               Toast.makeText(BlueWifi.this, "Wifi Enabled", Toast.LENGTH_SHORT).show();
                                           }
                                           else
                                           {
                                               wifi.setWifiEnabled(false);
                                               Toast.makeText(BlueWifi.this, "Wifi Disabled", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   }
            );
            tg2.setOnClickListener(new View.OnClickListener(){//listner
                                       @Override
                                       public void onClick(View view)
                                       {
                                           if(tg2.isChecked())
                                           {
                                               ba.enable();// alt+enter to add permissions
                                               Toast.makeText(BlueWifi.this, "BlueTooth Enabled", Toast.LENGTH_SHORT).show();
                                           }
                                           else
                                           {
                                               ba.disable();
                                               Toast.makeText(BlueWifi.this, "BlueTooth Disabled", Toast.LENGTH_SHORT).show();
                                           }
                                       }
                                   }
            );
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
