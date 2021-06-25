package com.sushank.talkbot;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Alarm extends AppCompatActivity
{
   EditText edt3,edt4;
    Button bt7;
    int hours,mins;
    Calendar calNow;
    Calendar calSet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        bt7=(Button)(findViewById(R.id.bt7));
        edt3=(EditText) (findViewById(R.id.edt3));
        edt4=(EditText) (findViewById(R.id.edt4));



    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public  void go7(View v)
    {
        hours=Integer.parseInt( edt3.getText().toString());
        mins=Integer.parseInt( edt4.getText().toString());

        ////setting alarm////
        calNow = Calendar.getInstance();
        calSet = (Calendar) calNow.clone();


        calSet.set(Calendar.HOUR_OF_DAY, hours);
        calSet.set(Calendar.MINUTE, mins);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if(calSet.compareTo(calNow) <= 0){
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

        Intent intent = new Intent(this, Alarm.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this.getApplicationContext(), 1253, intent, PendingIntent.FLAG_UPDATE_CURRENT|  Intent.FILL_IN_DATA);

        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calSet.getTimeInMillis(),pendingIntent );
        Toast.makeText(this, "Alarm worked.", Toast.LENGTH_LONG).show();

       // setAlarm(calSet);

    }

//    /////method to set alarm///
//    private void setAlarm(Calendar targetCal){
//
//        textAlarmPrompt.setText(
//                "\n\ n***\n"
//                        + "Alarm is set@ " + targetCal.getTime() + "\n"
//                        + "***\n");
//
//        Intent intent = new Intent(getBaseContext(), AlarmReceiver.class);
//        PendingIntent pendingIntent = PendingIntent.getBroadcast(getBaseContext(), RQS_1, intent, 0);
//        AlarmManager alarmManager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
//        //      alarmManager.set(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(), pendingIntent);
//        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, targetCal.getTimeInMillis(),AlarmManager.INTERVAL_DAY, pendingIntent);
//    }
}
