package com.example.debo.siri;

import android.graphics.Typeface;
import android.speech.tts.TextToSpeech;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;
import java.util.StringTokenizer;

public class Text_To_Speech_Demo extends AppCompatActivity {


    TextToSpeech tts;
    Button bt3;
    EditText edt1;
    TextView tv1;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text__to__speech__demo);

        bt3 = (Button)(findViewById(R.id.bt3));
        edt1 = (EditText)(findViewById(R.id.edt1));
        tv1 = (TextView)(findViewById(R.id.tv1));

        tts = new TextToSpeech(this,new TextToSpeech.OnInitListener(){

            @Override
            public void onInit(int i) {

                tts.setLanguage(Locale.US);

            }
        });


        bt3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                String str=edt1.getText().toString();
//                StringTokenizer st=new StringTokenizer(str);
//                while(st.hasMoreTokens())
//                {
//
//                }
                tv1.setTypeface(null, Typeface.BOLD);
                Toast.makeText(Text_To_Speech_Demo.this,str, Toast.LENGTH_SHORT).show();
                tts.speak(str,TextToSpeech.QUEUE_ADD,null);

                tv1.setText(edt1.getText().toString());
            }
        });
    }
}
