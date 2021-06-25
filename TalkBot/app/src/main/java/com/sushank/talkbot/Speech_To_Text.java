package com.sushank.talkbot;

import android.content.Intent;
import android.graphics.Typeface;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

import static android.R.attr.data;

public class Speech_To_Text extends AppCompatActivity
{
    SpeechRecognizer sr;
    //RECORD_AUDIO" add this permission in manifest for android 6 or higher//
    Button bt1;
    TextView tv1;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_speech__to__text);
        bt1=(Button)(findViewById(R.id.bt1));
        tv1=(TextView) (findViewById(R.id.tv1));


        bt1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
                sr.setRecognitionListener(new Speech());
               // Log.d("MYMSG", "in method");
                Intent in = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                //in.putExtra(RecognizerIntent.EXTRA_PROMPT, "Hello, How can I help you?");
                in.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                in.putExtra(RecognizerIntent.EXTRA_CALLING_PACKAGE, getApplicationContext().getPackageName());

                //Log.d("MYMSG", "end of the method");

                sr.startListening(in);

            }
        });

    }




    class Speech implements RecognitionListener
    {
        public void onResults(Bundle bundle) {

            ArrayList<String> voiceResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);
            if (voiceResults == null)
            {
                Toast.makeText(Speech_To_Text.this, "Speak Again", Toast.LENGTH_SHORT).show();
            } else {
                for (int i = 0; i < voiceResults.size(); i++)
                {
                    tv1.setText(voiceResults.get(i));
                    tv1.setTypeface(null, Typeface.BOLD);
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




}
