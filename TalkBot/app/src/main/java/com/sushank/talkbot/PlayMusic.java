package com.sushank.talkbot;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.MediaStore;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class PlayMusic extends AppCompatActivity
{
    Button bt6;
    ListView lv3;
    MyAdapter ad;
    ArrayList<Songs> als;
    MediaPlayer mp;
    SpeechRecognizer sr;
    TextToSpeech tts;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_music);

        bt6=(Button) ( findViewById(R.id.bt6));
        lv3=(ListView) (findViewById(R.id.lv3));

        ad=new MyAdapter();
        als=new ArrayList<>();
        lv3.setAdapter(ad);

        mp=new MediaPlayer();

        tts=new TextToSpeech(this, new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int i) {
                tts.setLanguage(Locale.US);

            }
        });



        /////adding listener to listview to play music on click of song name /////
        lv3.setOnItemClickListener(new AdapterView.OnItemClickListener(){

                                       @Override
                                       public void onItemClick( AdapterView<?> adapterView , View view, int pos, long l)
                                       {

                                           try {
                                               if (mp.isPlaying())
                                               {
                                                   mp.reset();
                                               }
                                               mp.setDataSource(als.get(pos).data);
                                               mp.prepare();
                                               mp.start();

                                           }
                                           catch (Exception e)
                                           {
                                               e.printStackTrace();
                                           }

                                       }});



        /////code to get list of songs on the device/////
        ContentResolver cr = getContentResolver();

        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + "!= 0";
        String sortOrder = MediaStore.Audio.Media.TITLE + " ASC";
        Cursor cur = cr.query(uri, null, selection, null, sortOrder);
        int count = 0;

        if(cur != null)
        {
            count = cur.getCount();

            if(count > 0)
            {
                while(cur.moveToNext())
                {
                    String data = cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DATA));
                    String song_name=cur.getString(cur.getColumnIndex(MediaStore.Audio.Media.DISPLAY_NAME));
                    als.add(new Songs(song_name,data));
                    Log.d("MYMESSAGE",song_name);
                }

            }
        }

        cur.close();

        ad.notifyDataSetChanged();

    }


    public void go6(View v)
    {
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
        public void onResults(Bundle bundle)
        {

            ArrayList<String> voiceResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            String voicemsg=voiceResults.get(0);
            voicemsg=voicemsg.toLowerCase();
            Log.d("MYMSG1",voicemsg);
            if (voicemsg == null)
            {
                //Toast.makeText(apps.this, "Speak Again", Toast.LENGTH_SHORT).show();
                tts.speak("Kindly Speak Again", TextToSpeech.QUEUE_ADD,null);
            }
            else if (voicemsg.contains("play"))
            {

                StringTokenizer st = new StringTokenizer(voicemsg);
                st.nextToken();
                String song_from_voice = st.nextToken();
                while (st.hasMoreTokens()) {
                    song_from_voice = song_from_voice + " " + st.nextToken();
                }
                song_from_voice = song_from_voice.trim();
                song_from_voice  = song_from_voice.toLowerCase();

                for (int j = 0; j < als.size(); j++)
                {
                   String song_in_als=als.get(j).song_name;

                    song_in_als=song_in_als.toLowerCase();
                    Log.d("MYMS2",song_in_als);
                    Log.d("MYMS2",song_from_voice);


                    if (song_in_als.contains(song_from_voice))
                    {
                        ////to play song code////
                        try {
                            if (mp.isPlaying())
                            {
                                mp.reset();
                            }
                            mp.setDataSource(als.get(j).data);
                            mp.prepare();
                            mp.start();

                        }
                        catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                        break;

                    }
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


    //////custom adapter inner class///
    class MyAdapter extends BaseAdapter
    {

        @Override
        public int getCount() {
            return als.size();
        }

        @Override
        public Object getItem(int i) {
            return als.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View singlerow2, ViewGroup viewGroup)
        {
            LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());
            singlerow2 = inflater.inflate(R.layout.singlerow2,viewGroup ,false);

            Songs s=als.get(i);

            ImageView imv4;
            TextView tv4;

            //imv4=(ImageView)(singlerow2.findViewById(R.id.imv4));
            tv4=(TextView)(singlerow2.findViewById(R.id.tv4));
            tv4.setText(s.song_name);

            return singlerow2;
        }
    }
    ////inner class for array list datatype to get name and path of songs//
    class Songs
    {
        String song_name,data;
        Songs(String song_name,String data)
        {
            this.song_name=song_name;
            this.data=data;
        }
    }

//////////////to stop playing music on click of back button/////////
    public void onBackPressed()
    {
        super.onBackPressed();
        mp.stop();
    }
}
