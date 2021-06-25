package com.example.debo.siri;

import android.app.SearchManager;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
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
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.hardware.Camera.Parameters;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.jar.Manifest;

import static com.example.debo.siri.R.layout.singlerow;

public class Conversation extends AppCompatActivity
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
    CameraManager camManager;
    String cameraId;
    int status=0;
    String str;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        lv2 = (ListView) (findViewById(R.id.lv1));
        imv2 = (ImageView) (findViewById(R.id.imv1));
        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);

            }
        });
        ba = BluetoothAdapter.getDefaultAdapter();


          try {
            wifi = (WifiManager) this.getSystemService(Context.WIFI_SERVICE);
                 } catch (Exception e) {
            e.printStackTrace();
                     }
        al = new ArrayList<>();

        ad1 = new myadapter1();
        lv2.setAdapter(ad1);

        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        sr.setRecognitionListener(new Speech());

        imv2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent in = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                in.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
                sr.startListening(in);
            }
        });
    }

    ////////to recognize speach///////
    class Speech implements RecognitionListener
    {
        //        public void onResults(Bundle bundle)
//        {
//              """""THIS IS PREVIOUS CODE """"""""
//
//            voiceResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
//            String res="";
//
//            if (voiceResults == null)
//            {
//                Toast.makeText(Conversation.this, "Speak Again", Toast.LENGTH_SHORT).show();
//                tts.speak("Kindly Speak Again",TextToSpeech.QUEUE_ADD,null);
//            }
//            else
//            {
//                String voicemsg=voiceResults.get(0);
//                voicemsg= voicemsg.toLowerCase();
//
//                al.add(voicemsg);// our msg
//
//                if(voicemsg.equals("hello")||voicemsg.equals("hi")||voicemsg.equals("hello talkbot")||voicemsg.equals("hi talkbot")||voicemsg.equals("hey"))
//                {
//                    res="Helllo There";
//                    tts.speak(res,TextToSpeech.QUEUE_ADD,null);
//                }
//                al.add(res);
//               ad1.notifyDataSetChanged();
//
//            }
//
//        }
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

            if (voiceResults == null)
            {
                //Toast.makeText(Convo.this, "Speak Again", Toast.LENGTH_SHORT).show();
                tts.speak("Kindly Speak Again", TextToSpeech.QUEUE_ADD, null);
            }
            else {
                String voicemsg = voiceResults.get(0);
                voicemsg = voicemsg.toLowerCase();

              //  al.add(new Voice("user", voicemsg));// our msg

                if (voicemsg.equals("hello") || voicemsg.equals("hi") || voicemsg.equals("hello talkbot") || voicemsg.equals("hi talkbot") || voicemsg.equals("hey")) {
                    res = "Helllo There";
                    tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                } else if (voicemsg.equals("how are you") || voicemsg.equals("how are you doing") || voicemsg.equals("whats up talkbot") || voicemsg.equals("whats up")) {
                    res = "I am good";
                    tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                } else if (voicemsg.equals("set wifi on") || voicemsg.equals("switch on the wifi") || voicemsg.equals("wifi on")) {
                    if (wifi.isWifiEnabled()) {
                        res = "Wifi is already on";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                    } else {
                        res = "switching on the wifi";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        wifi.setWifiEnabled(true);
                    }
                } else if (voicemsg.equals("set wifi off") || voicemsg.equals("switch off the wifi") || voicemsg.equals("wifi off")) {
                    if (!(wifi.isWifiEnabled())) {
                        res = "Wifi is already off";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                    } else {
                        res = "switching off the wifi";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        wifi.setWifiEnabled(false);
                    }
                } else if (voicemsg.equals("set bluetooth on") || voicemsg.equals("switch on bluetooth") || voicemsg.equals("bluetooth on")) {
                    if (ba.enable()) {
                        res = "bluetooth is already on";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                    } else if (ba.disable()) {
                        res = "switching on the bluetooth";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        ba.enable();
                    }
                } else if (voicemsg.equals("set bluetooth off") || voicemsg.equals("switch off the bluetooth") || voicemsg.equals("bluetooth off")) {
                    if (ba.disable()) {
                        res = "bluetooth is already off";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                    } else {
                        res = "switching off the bluetooth";
                        tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                    }
                } else if (voicemsg.contains("open")) {
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
                            res = "Opening" + appName;
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                            Intent in = getPackageManager().getLaunchIntentForPackage(p);
                            if (in != null) {
                                startActivity(in);
                                Toast.makeText(Conversation.this, res, Toast.LENGTH_SHORT).show();
                            }
                        }
                    }

                } else if (voicemsg.contains("SIRI are you high ")) {
                    tts.speak("hell yeha", TextToSpeech.QUEUE_ADD, null);
                } else if (voicemsg.contains("call ")) {  //need to have space after the call or it will results as "callDebojyoti"
                    try {
                        StringTokenizer st = new StringTokenizer(voicemsg);
                        st.nextToken();
                        String contactname = st.nextToken();
                        while (st.hasMoreTokens()) {
                            contactname = contactname + " " + st.nextToken();
                        }
                        contactname = contactname.trim();
                        contactname = contactname.toLowerCase();

                        // ArrayList<String> alc=new ArrayList<>();

                        String contactnumber = "Contact not found";
                        ///////////getting list of all contacts///////////////
                        Cursor phonenumber = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                        while (phonenumber.moveToNext()) {
                            //alc.add(phonenumber.getString(phonenumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)));
                            //alc.add(new Call(phonenumber.getString(phonenumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)),phonenumber.getString(phonenumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))));
                            //if(contactname.equalsIgnoreCase(alc.get(position).DISPLAY_NAME));
                            // {}

                            //code to search contacts
                            String name = phonenumber.getString(phonenumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                            String phonenum = phonenumber.getString(phonenumber.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                            if (contactname.equalsIgnoreCase(name)) {
                                res = "calling" + name;
                                tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                            }
                            contactnumber = phonenum;
                            break;
                        }
                        if (!contactnumber.equals("contact not found")) {
                            //code for calling
                            Intent calling = new Intent(Intent.ACTION_CALL);
                            calling.setData(Uri.parse("tel:" + contactnumber));
                            if (ActivityCompat.checkSelfPermission(Conversation.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                                // TODO: Consider calling
                                //    ActivityCompat#requestPermissions
                                // here to request the missing permissions, and then overriding
                                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                //                                          int[] grantResults)
                                // to handle the case where the user grants the permission. See the documentation
                                // for ActivityCompat#requestPermissions for more details.
                                return;
                            }
                            startActivity(calling);

                        } else {
                            res = "contacts not found please try again";
                            tts.speak(res, TextToSpeech.QUEUE_ADD, null);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (voicemsg.contains("open Music") || voicemsg.contains("play music")) {
                    Intent in = new Intent(Conversation.this, PlayMusic.class);
                    startActivity(in);
                }


                //to open message on voice command
                else if (voicemsg.contains("send message ") || voicemsg.contains("send message to ")) {

                    Intent in = new Intent(Conversation.this, SMS.class);
                    startActivity(in);
                } else if (voicemsg.contains("turn on flashlight") || voicemsg.contains("turn on light") || voicemsg.contains("turn on torch")) {
//                    Boolean b=getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH);
//                    if(b)
//                    {
//                        try{
//                            android.graphics.Camera cam = Camera.open();
//                            Parameters p = cam.getParameters();
//                            p.setFlashMode(Parameters.FLASH_MODE_TORCH);
//                            cam.setParameters(p);
//                            cam.startPreview();
//                        }
//                        catch(Exception e)
//                        {
//                            e.printStackTrace();
//                        }
//                    }


                    //////////////////////////ALITER/////////////////////////////////
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) //for android marshmellow AND ABOVE
                    {
                        camManager = (CameraManager) getSystemService(Context.CAMERA_SERVICE);
                        String cameraId = null; // Usually back camera is at 0 position.
                        try {
                            cameraId = camManager.getCameraIdList()[0];//usually back camera is at 0 position
                            camManager.setTorchMode(cameraId, true);   //Turn ON
                        } catch (CameraAccessException e) {
                            e.printStackTrace();
                        }
                    }

                } else if (voicemsg.contains("turn off flashlight") || voicemsg.contains("turn off light")) {
                    try {
                        cameraId = camManager.getCameraIdList()[0];
                        camManager.setTorchMode(cameraId, false);   //Turn ON;//need to pass the camera id8
                    } catch (CameraAccessException e) {
                        e.printStackTrace();
                    }
                } else if (voicemsg.contains("please set alarm") || voicemsg.contains("turn alarm on") || voicemsg.contains("set alarm at")) {
//                    Intent in=new Intent(Conversation.this,Alarm.class);
//                    startActivity(in);
                    StringTokenizer st = new StringTokenizer(voicemsg);
                    st.nextToken();
                    st.nextToken();
                    st.nextToken();
                    // st.nextToken();
                    String h = st.nextToken();//10:10
                    String s = st.nextToken();//pm
                    if (!h.contains(":")) {
                        h = h + ":" + 00;
                    }
                    StringTokenizer stt = new StringTokenizer(h, ":");
                    String h1 = stt.nextToken();//10 first one of 10:10
                    String h2 = stt.nextToken();//10 second one one of 10:10
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
                //////////////////////////////to search on web///////////////////////
                else if (voicemsg.contains("search") || voicemsg.contains("search on web")) {
                    String query = "";
                    StringTokenizer st = new StringTokenizer(voicemsg);
                    st.nextToken();//to bypass "search" the first string
                    while (st.hasMoreTokens()) {
                        query = query + st.nextToken();//concatinate to get the whole string of the query eg the search string
                    }

                    Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                    intent.putExtra(SearchManager.QUERY, query); // query contains search string
                    startActivity(intent);
                } else if (voicemsg.contains("screen brightness") || voicemsg.contains("brightness")) {
                    try {
                        Context context = Conversation.this;
                        int curBrightnesslevel = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
                        android.provider.Settings.System.putInt(
                                context.getContentResolver(),
                                android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightnesslevel + 50);


                        android.provider.Settings.System.putInt(context.getContentResolver(),
                                android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                                android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

                        android.provider.Settings.System.putInt(
                                context.getContentResolver(),
                                android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightnesslevel + 50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (voicemsg.contains("low screen brightness") || voicemsg.contains(" decrease brightness")) {
                    try {
                        Context context = Conversation.this;
                        int curBrightnesslevel = android.provider.Settings.System.getInt(getContentResolver(), android.provider.Settings.System.SCREEN_BRIGHTNESS);
                        android.provider.Settings.System.putInt(
                                context.getContentResolver(),
                                android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightnesslevel - 50);


                        android.provider.Settings.System.putInt(context.getContentResolver(),
                                android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE,
                                android.provider.Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);

                        android.provider.Settings.System.putInt(
                                context.getContentResolver(),
                                android.provider.Settings.System.SCREEN_BRIGHTNESS, curBrightnesslevel - 50);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
//                else if(voicemsg.contains("turn airplane mode on")||voicemsg.contains("turn on airplane mode"))
//                {
//                    Context context=Conversation.this;
//                    Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
//                    Intent  newIntent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//                    newIntent.putExtra("state", true);
//                    sendBroadcast(newIntent);
//                }
//                else if(voicemsg.contains("turn airplane mode off")||voicemsg.contains("turn off airplane mode"))
//                {
//                    Context context=Conversation.this;
//                    Settings.System.putInt(context.getContentResolver(), Settings.System.AIRPLANE_MODE_ON, 1);
//                    Intent  newIntent = new Intent(Intent.ACTION_AIRPLANE_MODE_CHANGED);
//                    newIntent.putExtra("state", true);
//                    sendBroadcast(newIntent);
//                }
//                else if(voicemsg.contains("turn gps on")||voicemsg.contains("turn on gps"))
//                {
//                    String provider = Settings.Secure.getString(getContentResolver(), Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
//
//                    if(!provider.contains("gps")){ //if gps is disabled
//                        Intent intent=new Intent("android.location.GPS_ENABLED_CHANGE");
//                        intent.putExtra("enabled", true);
//                        sendBroadcast(intent);
//                    }
//                    else
//                    {
//                        tts.speak("gps is already on",TextToSpeech.QUEUE_ADD,null);
//                    }
//                }

                //next demo to bold the characters when we are speaking via text to speach
                else if (voicemsg.contains("practice") || voicemsg.contains("practice demo ")) {

                    Intent in = new Intent(Conversation.this, Bold_Demo.class);
                    startActivity(in);
                }
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
                    Intent in=new Intent(Conversation.this,MyNotes.class);
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

            Log.d("MYMSG","rms changed");

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

    //////custom adpter for listview//////
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

    ////////inner class to get phone number//////
//    class Call
//    {
//        String contactname,phonenumber;
//        Call(String contactname,String phonenumber)
//        {
//            this.phonenumber=phonenumber;
//            this.contactname=contactname;
//        }
//    }
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
}
