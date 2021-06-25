package com.example.debo.siri;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import java.util.Calendar;

public class Alarm extends AppCompatActivity
{
    Button bt1;
    EditText et1,et2;
    int hours,mins;
    Calendar calNow;
    Calendar calSet;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);
        bt1=(Button)(findViewById(R.id.bt1));
        et1=(EditText) (findViewById(R.id.et1));
        et2=(EditText) (findViewById(R.id.et2));
    }
    public void go1(View v)
    {
        hours=Integer.parseInt(et1.getText().toString());
        mins=Integer.parseInt(et2.getText().toString());
        ////////setting alarm////////
         calNow = Calendar.getInstance();
         calSet = (Calendar) calNow.clone();
        calSet.set(Calendar.HOUR_OF_DAY, hours);
        calSet.set(Calendar.MINUTE, mins);
        calSet.set(Calendar.SECOND, 0);
        calSet.set(Calendar.MILLISECOND, 0);

        if(calSet.compareTo(calNow) <= 0)
        {
            //Today Set time passed, count to tomorrow
            calSet.add(Calendar.DATE, 1);
        }

       // setAlarm(calSet);
    }

//    private void setAlarm(Calendar targetCal)
//    {
//
//        textAlarmPrompt.setText(
//                "\n\n***\n"
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
