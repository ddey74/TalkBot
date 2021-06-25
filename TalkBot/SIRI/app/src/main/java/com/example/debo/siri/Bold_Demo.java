package com.example.debo.siri;

import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import java.util.Locale;
import java.util.StringTokenizer;

public class Bold_Demo extends AppCompatActivity {

    EditText thtv;
    TextToSpeech tts;
    String str="";
    String txt;

    Button speakbtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bold__demo);


        tts=new TextToSpeech(getApplicationContext(), new TextToSpeech.OnInitListener() {
            @Override
            public void onInit(int status) {
                if(status != TextToSpeech.ERROR) {
                    tts.setLanguage(Locale.US);
                }
            }

        });
        thtv=(EditText)(findViewById(R.id.thetv));
        speakbtn=(Button)(findViewById(R.id.speakbtn));





        speakbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                txt=thtv.getText().toString();
                Log.d("MYMSG",txt);
//                tts.speak(txt,TextToSpeech.QUEUE_FLUSH,null);

                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        final StringTokenizer st=new StringTokenizer(txt);
                        int i=0;
                        while (st.hasMoreTokens())
                        {
                            String sttoken=st.nextToken();
                            Log.d("MYMSG",sttoken);

                            str="";
                            tts.speak(sttoken,TextToSpeech.QUEUE_FLUSH,null);

                            StringTokenizer st1=new StringTokenizer(txt);
                            int j=0;
                            while (st1.hasMoreTokens())
                            {
                                if(i==j)
                                {
                                    str=str+" <b>"+st1.nextToken()+"</b>";
                                }
                                else
                                {
                                    str=str+" "+st1.nextToken();
                                }
                                j++;

                            }
                            Log.d("MYMSG",str);

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    thtv.setText(Html.fromHtml(str));

                                }
                            });
                            try {
                                Thread.sleep(400);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            i++;
                        }

                    }
                }).start();
            }
        });
    }




}

