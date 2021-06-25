package com.sushank.talkbot;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class apps extends AppCompatActivity
{
    GridView myapps;
    myadapter ad;
    SpeechRecognizer sr;
    ArrayList<data> al;
    Button bt4;
    List<PackageInfo> packList;
    String appName;
    TextToSpeech tts;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_apps);
        bt4=(Button)(findViewById(R.id.bt4));
        al=new ArrayList<>();

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);

            }
        });


        PackageManager pm= getPackageManager();//has info of all apps
        packList = pm.getInstalledPackages(0);//0 means all information of that package
        for (int i=0; i < packList.size(); i++)
        {
            PackageInfo packInfo = packList.get(i);
            if ((packInfo.applicationInfo.flags & ApplicationInfo.FLAG_SYSTEM) == 0)//if it is not system app
            {
                 appName = packInfo.applicationInfo.loadLabel(getPackageManager()).toString();
                Log.d("THE APP IS:",appName);
                al.add(new data(appName, packInfo.packageName));// passing these values to the constructor of class Image
            }
        }


        ad=new myadapter();
        myapps=(GridView)(findViewById(R.id.myapps));
        myapps.setAdapter(ad);


        myapps.setOnItemClickListener(
                new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int pos, long id) {
                Intent in= getPackageManager().getLaunchIntentForPackage(al.get(pos).pkgname);
                if(in!=null)
                {
                    startActivity(in);
                }
                Toast.makeText(getApplicationContext(),"App Opened",Toast.LENGTH_SHORT).show();}});



    }
    public void go4(View v) {


                sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                sr.setRecognitionListener(new Speech());
                Intent in = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                in.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());
                sr.startListening(in);
       }

    ////inner class of RecognitionListener///
    class Speech implements RecognitionListener
    {
        public void onResults(Bundle bundle) {

            ArrayList<String> voiceResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (voiceResults == null) {
                //Toast.makeText(apps.this, "Speak Again", Toast.LENGTH_SHORT).show();
                tts.speak("Kindly Speak Again",TextToSpeech.QUEUE_ADD,null);
            } else {


                String voicemsg=voiceResults.get(0);
                voicemsg=voicemsg.toLowerCase();

                Log.d("MYMSG1",voicemsg);

                for (int j = 0; j < al.size(); j++) {
                    String appname=al.get(j).name;

                    appname=appname.toLowerCase();
                    Log.d("MYMS2",appname);


                    if (appname.contains(voicemsg)) {
                        Intent in = getPackageManager().getLaunchIntentForPackage(al.get(j).pkgname);
                        if (in != null) {
                            startActivity(in);
                            break;
                        }
                    }

                }
                Toast.makeText(apps.this, voicemsg+"opened"
                        , Toast.LENGTH_SHORT).show();
                tts.speak(voicemsg+"Opened", TextToSpeech.QUEUE_ADD,null);
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

//            Log.d("MYMSG","rms changed");

        }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {

           // Log.d("MYMSG","error");
        }




        @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }



    /////inner class for custom adapter/////
    class myadapter extends BaseAdapter
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
        public View getView(int i, View singlerow, ViewGroup viewGroup)
        {

            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            singlerow = inflater.inflate(R.layout.singlerow,viewGroup ,false);

            data d  = al.get(i);



            ImageView appimage;
            TextView tv1;//tv2;

            appimage=(ImageView)(singlerow.findViewById(R.id.appimage));
            tv1=(TextView)(singlerow.findViewById(R.id.tv1));
            //tv2=(TextView)(singlerow.findViewById(R.id.tv2));

//            //code to get the icon of the app
            try {
//                String pkg = "com.sushank.talkbot";
                Drawable icon = getPackageManager().getApplicationIcon(al.get(i).pkgname);


                appimage.setImageDrawable(icon);
                tv1.setText(d.name);
//                //tv2.setText(image.pkgName);
            }
            catch(Exception e)
            {e.printStackTrace();}

            return singlerow;

        }
    }

////inner class for array list datatype
    class data
    {
        String name,pkgname;
        data(String name,String pkgname)
        {
            this.name=name;
            this.pkgname=pkgname;
        }
    }

}
