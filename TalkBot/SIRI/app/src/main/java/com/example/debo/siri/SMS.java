package com.example.debo.siri;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.speech.RecognitionListener;
import android.speech.RecognizerIntent;
import android.speech.SpeechRecognizer;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class SMS extends AppCompatActivity
{
    SpeechRecognizer sr;
    TextToSpeech tts;
    TextView tv1;
    String name;
    ArrayList<String> voiceResults;
    EditText edt1;
    String msg;
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms);
        tv1=(TextView)(findViewById(R.id.tv1));
        edt1=(EditText)(findViewById(R.id.edt1));


        tts = new TextToSpeech(this, new TextToSpeech.OnInitListener() {

            @Override
            public void onInit(int i) {

                tts.setLanguage(Locale.US);//giving memory to the speaking component

            }
        });


        String ques="to whom you want to send message";
        tts.speak(ques,TextToSpeech.QUEUE_ADD,null);
        tv1.setText(ques);


    }
    public void go7(View v)
    {
        msg=edt1.getText().toString();

        String ques="To Whom Do You Want To Message";
        tts.speak(ques,TextToSpeech.QUEUE_ADD,null);
        tv1.setText("To Whom Do You Want To Message?");


        ////thread sleep used because mixing of voice taking place////
        try {
            Thread.sleep(2000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }

        sr = SpeechRecognizer.createSpeechRecognizer(getApplicationContext());
        sr.setRecognitionListener(new SMS.Speech());
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
            voiceResults = bundle.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION);

            name =voiceResults.get(0);//to get the name of the receiver
            String res = "";
            Log.d("mymessage:",name);


            if(name.equals(" "))
            {
                tts.speak("Kindly Speak Again", TextToSpeech.QUEUE_ADD, null);
            }

            else
            {
                Log.d("mymsg:","getting contacts");
                //////getting contacts////
                String contactnum = "contact not found";
                Cursor phone = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);
                while (phone.moveToNext())
                {
                    String names = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                    String phonenums = phone.getString(phone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));


                    Log.d("mymsg",name);
                    Log.d("mymsg",names);
                    if(name.equalsIgnoreCase(names))
                    {
                        res="sending message to"+ name;
                        tts.speak(res,TextToSpeech.QUEUE_ADD,null);
                        contactnum=phonenums;
                        break;
                    }

                }

                if (!contactnum.equals("contact not found"))
                {
                    Log.d("mymsg","sending message");
                    Intent smsintent = new Intent(Intent.ACTION_VIEW, Uri.parse("sms:" + contactnum));//it will open default messaging app with the message that you have speak
                    smsintent.putExtra("sms_body",msg);//default parameter  "sms_body"////
                    startActivity(smsintent);

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
         }

        @Override
        public void onBufferReceived(byte[] bytes) {

        }

        @Override
        public void onEndOfSpeech() {

        }

        @Override
        public void onError(int i) {
         }

         @Override
        public void onPartialResults(Bundle bundle) {

        }

        @Override
        public void onEvent(int i, Bundle bundle) {

        }
    }
}
