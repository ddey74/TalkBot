package com.example.debo.siri;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class Features extends AppCompatActivity {
    String s;
    TextView tv;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_features);
        tv=(TextView)(findViewById(R.id.tv));
        s="The app  will perform the following functions:\n" +
                "        1. Call by voice.\n" +
                "        2. Message by voice.\n" +
                "        3. Add important note by voice.\n" +
                "        4. Open any app by voice.\n" +
                "        5. Web search by voice.\n" +
                "        6. Read text message.\n" +
                "        7. Play music by voice.\n" +
                "        8. Change few settings  by voice..\n" +
                "        9. View notes by voice.\n" +
                "        10. Set alarms by voice.\n" +
                "        12. Ask Questions ";
        tv.setText(s);
    }
}
