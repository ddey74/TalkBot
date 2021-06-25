package com.sushank.talkbot;

import android.content.Intent;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;

import java.util.Locale;

public class Splash_Screen extends AppCompatActivity implements TextToSpeech.OnInitListener
{
    TextToSpeech tts;
    /** Duration of wait **/
    private final int SPLASH_DISPLAY_LENGTH = 6500;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle icicle)
    {
        super.onCreate(icicle);
        setContentView(R.layout.activity_splash__screen);

        tts=new TextToSpeech(getApplicationContext(), this);


        /* New Handler to start the Menu-Activity
         * and close this Splash-Screen after some seconds.*/
        new Handler().postDelayed
                (
                        new Runnable(){
            @Override
            public void run() {



                /* Create an Intent that will start the Menu-Activity. */
                Intent mainIntent = new Intent(Splash_Screen.this,MainActivity.class);
                Splash_Screen.this.startActivity(mainIntent);
                Splash_Screen.this.finish();


            }
        }, SPLASH_DISPLAY_LENGTH);
    }

    @Override
    public void onInit(int i) {
        if(i != TextToSpeech.ERROR) {
            tts.setLanguage(Locale.US);

            tts.speak("Welcome To Talkbot",TextToSpeech.QUEUE_ADD,null);

        }

    }
}

