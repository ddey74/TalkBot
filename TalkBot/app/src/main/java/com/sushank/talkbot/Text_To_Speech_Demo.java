package com.sushank.talkbot;

import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Locale;
import java.util.StringTokenizer;

public class Text_To_Speech_Demo extends AppCompatActivity implements  TextToSpeech.OnInitListener
{

    TextToSpeech tts;
    Button bt3;
    EditText edt1;
    TextView tv7;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text__to__speech__demo);

        bt3 = (Button) (findViewById(R.id.bt3));
        edt1 = (EditText) (findViewById(R.id.edt1));
        tv7=(TextView)(findViewById(R.id.tv7));
        tv7.setTypeface(null, Typeface.BOLD);

        tts=new TextToSpeech(this,this);



        bt3.setOnClickListener(

                new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {


                String str=edt1.getText().toString();
                StringTokenizer st=new StringTokenizer(str);

                while(st.hasMoreTokens())
                {
                     String s=st.nextToken();
                    try {
                        tv7.append(s+" ");
                        Thread.sleep(500);
                        tts.speak(s+" ",TextToSpeech.QUEUE_ADD,null);
                        Thread.sleep(500);


                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }


                }
                //tv7.setText(" ");

               // Toast.makeText(Text_To_Speech_Demo.this,str, Toast.LENGTH_SHORT).show();


            }
        });
    }

    @Override
    public void onInit(int i) {
        tts.setLanguage(Locale.US);

    }
}
