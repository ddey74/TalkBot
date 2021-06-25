package com.sushank.talkbot;

import android.Manifest;
import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Camera;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraManager;
import android.net.Uri;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.AlarmClock;
import android.provider.ContactsContract;
import android.provider.Settings;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.os.Bundle;
import android.speech.tts.Voice;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.security.Policy;
import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import android.hardware.Camera.Parameters;
import static com.sushank.talkbot.R.layout.singlerow;

public class Convo extends AppCompatActivity
{
    ListView lv2;
    ImageView imv2;
    SpeechRecognizer sr;
    myadapter1 ad1;
    ArrayList<String> voiceResults;
    TextToSpeech tts;
    ArrayList<Voice> al;
    BluetoothAdapter ba;
    WifiManager wifi;
    CameraManager cm;
    String cameraId;
    String provider;
    int status=0;
    String str;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convo);

        lv2 = (ListView) (findViewById(R.id.lv2));
        imv2 = (ImageView) (findViewById(R.id.imv2));

        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);

            }
        });

        ba = BluetoothAdapter.getDefaultAdapter();


        provider= Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);//for gps


        try {
            wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
        } catch (Exception e) {
            e.printStackTrace();
        }

        al = new ArrayList<>();

        ad1 = new myadapter1();
        lv2.setAdapter(ad1);


        imv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                sr.setRecognitionListener(new Speech());

                Intent in = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                in.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
                sr.startListening(in);
            }
        });
    }

    class Speech implements RecognitionListener
    {
        @RequiresApi(api = Build.VERSION_CODES.M)
        public void onResults(Bundle bundle)
        {

            voiceResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String res = "";
            str = voiceResults.get(0) + "";
            if (status == 1)
            {
                Log.d("MYMSG", str);
                try {
                    FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+ File.separator+"TalkBot.txt", true);
                    PrintWriter pw = new PrintWriter(fos);
                    pw.append(new SimpleDateFormat("dd/MM/yyyy_HH:mm:ss")
                            .format(Calendar.getInstance().getTime()));
                    pw.append(" ->" + str + "\n");
                    pw.flush();
                    pw.close();
                } catch (Exception ex)
                {
                    ex.printStackTrace();
                }
                res = str;
                al.add(new Voice("res", res));
                ad1.notifyDataSetChanged();
                tts.speak("note saved", TextToSpeech.QUEUE_ADD, null);
                status = 0;
            }
            else
            {

                Log.d("MYMSGnav", str);
                al.add(new Voice("user", str));
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        ad1.notifyDataSetChanged();

                    }
                });


                if (voiceResults == null) {
                    //Toast.makeText(Convo.this, "Speak Again", Toast.LENGTH_SHORT).show();
                    tts.speak("Kindly Speak Again", TextToSpeech.QUEUE_ADD, null);
                } else
                {

                    String voicemsg = voiceResults.get(0);
                    voicemsg = voicemsg.toLowerCase();


                    Log.d("MYMSG", voicemsg);
//                    al.add(new Voice("user", voicemsg));// our msg
                    ///////conversation////////
                    if (voicemsg.equals("hello") || voicemsg.equals("hi") || voicemsg.equals("hello talkbot") || voicemsg.equals("hi talkbot") || voicemsg.equals("hey")) {
                        res = "Hello There";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                    }
                    else if (voicemsg.equals("how are you") || voicemsg.equals("how are you doing") || voicemsg.equals("whats up talkbot") || voicemsg.equals("whats up")) {
                        res = "I am good";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                    }
                    /////////////////////////////////////////////////

                    /////////Enable wifi////////////////////////////
                    else if (voicemsg.equals("set wifi on") || voicemsg.equals("switch on the wifi") || voicemsg.equals("wifi on")) {
                        if (wifi.isWifiEnabled()) {
                            res = "Wifi is  on";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        } else {
                            res = "switching on the wifi";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                            wifi.setWifiEnabled(true);
                        }
                    }

                    ////////////////////////////////////////////
                    /////////disable wifi///////////////////////
                    else if (voicemsg.equals("set wifi off") || voicemsg.equals("switch off the wifi") || voicemsg.equals("wifi off")) {
                        if (!(wifi.isWifiEnabled())) {
                            res = "Wifi is  off";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        } else {
                            res = "switching off the wifi";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                            wifi.setWifiEnabled(false);
                        }
                    }
                    /////////////////////////////////////////////////

                    /////////////////enable bluetooth///////////////
                    else if (voicemsg.equals("set bluetooth on") || voicemsg.equals("switch on bluetooth") || voicemsg.equals("bluetooth on")) {
                        if (ba.enable()) {
                            res = "bluetooth is already on";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        } else if (ba.disable()) {
                            res = "switching on the bluetooth";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                            ba.enable();
                        }
                    }
                    /////////////////////////////////////////////////////
                    //////////////////disable bluetooth////////////////////
                    else if (voicemsg.equals("set bluetooth off") || voicemsg.equals("switch off the bluetooth") || voicemsg.equals("bluetooth off")) {
                        if (ba.disable()) {
                            res = "bluetooth is already off";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        } else {
                            res = "switching off the bluetooth";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        }
                    }
                    //////////////////////////////////////////////
                    ///////////////////asking the date////////////

                    else if (voicemsg.equals("what is the date today") || voicemsg.equals("what is the date") || voicemsg.equals("show me the date")) {
                        Date d = new Date();
                        res = d + "";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                    }
                    ////////////////////////////////////////////////////////////

                    //////////////opening an app//////////////////////////////////
                    else if (voicemsg.contains("open")) {
                        StringTokenizer st = new StringTokenizer(voicemsg);
                        st.nextToken();
                        String appname = st.nextToken();
                        while (st.hasMoreTokens()) {
                            appname = appname + " " + st.nextToken();
                        }
                        appname = appname.trim();
                        appname = appname.toLowerCase();
                        List<PackageInfo> packList = getPackageManager().getInstalledPackages(0);
                        for (int i = 0; i < packList.size(); i++) {
                            PackageInfo packInfo = packList.get(i);


                            String appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString().toLowerCase();
                            //Log.d("THE APP IS:",appName);
                            String p = packInfo.packageName;
                            if (appName.equals(appname)) {
                                res = "Opening" + " " + appName;
                                tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                                Intent in = getPackageManager().getLaunchIntentForPackage(p);
                                if (in != null) {
                                    startActivity(in);
                                    Toast.makeText(Convo.this, res, Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                    }
                    //////////////////////////////////////////////
                    /////////////////callling///////////////////////

                    else if (voicemsg.contains("call ")) {
                        try {
                            StringTokenizer st = new StringTokenizer(voicemsg);
                            st.nextToken();
                            String person_name = st.nextToken();
                            while (st.hasMoreTokens()) {
                                person_name = person_name + " " + st.nextToken();
                            }
                            person_name = person_name.trim();
                            person_name = person_name.toLowerCase();

                            Log.d("MYMSG", person_name);


                            ////getting list of all contacts in the phone///
                            String contactnum = "contact not found";
                            Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                            Log.d("MYMSG no", "before while");

                            while (phone.moveToNext()) {
                                String name = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                String phonenum = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                Log.d("MYMSG no", "in while");

                                Log.d("MYMSG no", contactnum + " " + name);

                                if (person_name.equalsIgnoreCase(name)) {
                                    ///add permission write contacts////
                                    res = "callling " + name;

                                    contactnum = phonenum;
                                    tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                                    break;

                                }

                            }
                            if (!contactnum.equals("contact not found")) {
                                Intent callin = new Intent(Intent.ACTION_CALL);
                                Log.d("MYMSG", contactnum);

                                callin.setData(Uri.parse("tel:" + contactnum));

                                if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                    // TODO: Consider calling
                                    //    ActivityCompat#requestPermissions
                                    // here to request the missing permissions, and then overriding
                                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                    //                                          int[] grantResults)
                                    // to handle the case where the user grants the permission. See the documentation
                                    // for ActivityCompat#requestPermissions for more details.
                                    return;
                                }
                                startActivity(callin);

                            } else {
                                res = "contact not found";
                                tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                            }

                        } catch (Exception ae) {

                            ae.printStackTrace();
                        }
                    }
                    /////////////////////////////////////////////////////////
                    //////////////sending message/////////////////////////////
                    else if (voicemsg.contains("send message")) {
                        Intent sms_in = new Intent(Convo.this, SMS.class);
                        startActivity(sms_in);
                    }
                    /////////////////////////////////////////////////////
                    ///////////////plating songs////////////////////////
                    else if (voicemsg.contains("play music")) {
                        Intent inplay = new Intent(Convo.this, PlayMusic.class);
                        startActivity(inplay);
                    }
                    //////////////////////////////////////////////////////////
                    ///////////////turning on torch///////////////////////////
                    else if (voicemsg.contains("turn on torch") || voicemsg.contains("turn on the torch")) {
                        Log.d("msg:", "torch on code");
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            cm = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                            cameraId = null; // Usually back camera is at 0 position.
                            try {
                                cameraId = cm.getCameraIdList()[0];
                                cm.setTorchMode(cameraId, true);   //Turn ON
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    }
                    ///////////////////////////////////////////////////////
                    /////////////turning off torch/////////////////////////
                    else if (voicemsg.contains("turn off torch") || voicemsg.contains("turn off the torch")) {
                        Log.d("msg2:", "in torch off code");
                        cm = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        cameraId = null; // Usually back camera is at 0 position.
                        try {
                            cameraId = cm.getCameraIdList()[0];
                            cm.setTorchMode(cameraId, false);   //Turn Off

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    ////////////////////////////////////////////////
                    ///////////////setting alarm////////////////////
                    else if (voicemsg.contains("set alarm for")) {
                        StringTokenizer st = new StringTokenizer(voicemsg);
                        st.nextToken();
                        st.nextToken();
                        st.nextToken();
                        String h = st.nextToken();//10:10
                        String s = st.nextToken();//pm

                        if (!h.contains(":")) {
                            h = h + ":" + 00;
                        }

                        StringTokenizer stt = new StringTokenizer(h, ":");
                        String h1 = stt.nextToken();//10 first one of 10:10
                        String h2 = stt.nextToken();//10 second one of 10:10
                        int sh = Integer.parseInt(h1);
                        int ss = Integer.parseInt(h2);

                        s = s.toUpperCase();

                        Log.d("MYTIME", s);

                        if (s.equalsIgnoreCase("P.M"))
                            sh = sh + 12;
                        else
                            sh = sh;

                        Log.d("MYTIME", sh + "");

                        Intent in = new Intent(AlarmClock.ACTION_SET_ALARM);
                        in.putExtra(AlarmClock.EXTRA_ALARM_SEARCH_MODE, AlarmClock.ALARM_SEARCH_MODE_TIME);
                        in.putExtra(AlarmClock.EXTRA_IS_PM, true);
                        in.putExtra(AlarmClock.EXTRA_MESSAGE, "New Alarm");
                        in.putExtra(AlarmClock.EXTRA_HOUR, sh);
                        in.putExtra(AlarmClock.EXTRA_MINUTES, ss);
                        startActivity(in);

                    }
                    /////////////////////////////////////////////////////
                    /////////////////searching on web browser/////////////
                    else if (voicemsg.contains("search on web")) {
                        String query = "";
                        StringTokenizer st = new StringTokenizer(voicemsg);
                        st.nextToken();//to bypass "search" the first string
                        st.nextToken();
                        st.nextToken();
                        while (st.hasMoreTokens()) {
                            query = query + st.nextToken();//concatinate to get the whole string of the query eg the search string
                        }
                        Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                        intent.putExtra(SearchManager.QUERY, query); // query contains search string
                        startActivity(intent);


                    }
                    ///////////////////////////////////////////////////////////////////
                    /////////////brightness increased/////////////////////////////////
                    else if (voicemsg.contains("increase brightness")) {
                        try {
                            Context context = Convo.this;
                            int curBrightnesslevel = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
                            android.provider.Settings.System.putInt(
                                    context.getContentResolver(),
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightnesslevel + 100);


                            android.provider.Settings.System.putInt(context.getContentResolver(),
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

                            android.provider.Settings.System.putInt(
                                    context.getContentResolver(),
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightnesslevel + 100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    /////////////////////////////////////////////////////////////
                    //////////////////brightness decrease/////////////////////////
                    else if (voicemsg.contains("decrease brightness")) {
                        try {
                            Context context = Convo.this;
                            int curBrightnesslevel = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
                            android.provider.Settings.System.putInt(
                                    context.getContentResolver(),
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightnesslevel - 100);


                            android.provider.Settings.System.putInt(context.getContentResolver(),
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

                            android.provider.Settings.System.putInt(
                                    context.getContentResolver(),
                                    android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightnesslevel - 100);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }
                    //////////////////////////////////////////////////
                    ///////////////adding notes//////////////////////
                    else if (voicemsg.contains("add note")) {
                        status = 1;
                        res = "speak note";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        try {
                            Thread.sleep(1000);
                        } catch (Exception ex) {
                            ex.printStackTrace();
                        }


                        note();

                    }

                    /////////////////////////////////////////////////////////////////////
                    ///////////////////reading notes/////////////////////////////////////
                    else if(str.equalsIgnoreCase("read notes")||str.equalsIgnoreCase("read the notes")
                            ||str.equalsIgnoreCase("read a note")||str.equalsIgnoreCase("view notes"))
                    {
                        res="moving to notes list";
                        tts.speak(res,TextToSpeech.QUEUE_ADD,null);
                        try{Thread.sleep(1000);}
                        catch (Exception ex){
                            ex.printStackTrace();
                        }
                        Intent in=new Intent(Convo.this,MyNotes.class);
                        startActivity(in);
                    }


                    al.add(new Voice("response", res));
                    ad1.notifyDataSetChanged();


                }
            }
        }





               @Override
        public void onReadyForSpeech(Bundle bundle) {

        }

        @Override
        public void onBeginningOfSpeech() {

        }

        @Override
        public void onRmsChanged(float v) {

           // Log.d("MYMSG","rms changed");

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {

            Log.d("MYMSG","error");
        }




        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }


    ////function go for add notes ////
    public void note()
    {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);

        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);

        intent.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE,
                "voice.recognition.test");

        intent.putExtra(RecognizerIntent.EXTRA_MAX_RESULTS, 5);
        sr.startListening(intent);


    }
    //////custom adpter for listview////
    class myadapter1 extends BaseAdapter
    {

        @Override
        public int getCount() {
            return al.size();
        }

        @Override
        public Object getItem(int i) {
            return al.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

            View singlerow1;
            singlerow1 = inflater.inflate(R.layout.singlerow1,viewGroup ,false);
            TextView tv4 = (TextView) (singlerow1.findViewById(R.id.tv4));
            TextView tv5=(TextView)(singlerow1.findViewById(R.id.tv5));

            if(al.get(i).res.equals("user")) {
                tv4.setText(al.get(i).spoke);
                tv5.setVisibility(View.GONE);

            }
            else {
                tv5.setText(al.get(i).spoke);
                tv4.setVisibility(View.GONE);

            }
            return singlerow1;

        }
    }
    ///inner class for class datatype///
    class Voice
    {
        String res,spoke;
        Voice(String res,String spoke)
        {
            this.res=res;
            this.spoke=spoke;
        }
    }
}






















































