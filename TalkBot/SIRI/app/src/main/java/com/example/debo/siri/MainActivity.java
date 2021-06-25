package com.example.debo.siri;

import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

        Button bt1,bt2,bt4,bt5,bt6;
    ListView dmenu;
    DrawerLayout dl;
    String[] op={"About TalkBot","Features","View Notes"};
    int status=1;
    ActionBarDrawerToggle abdt;
    ArrayAdapter<String> ad;
        @Override
        protected void onCreate(Bundle savedInstanceState)
        {
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_main);
            bt5 = (Button) (findViewById(R.id.bt5));
            try {

                dmenu=(ListView)(findViewById(R.id.dmenu));
                dmenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Log.d("MYMSG",i+"");

                        if(i==0)
                        {
                            Intent in=new Intent(MainActivity.this,About.class);
                            startActivity(in);
                        }
                        else if(i==1)
                        {

                            Intent in=new Intent(MainActivity.this,Features.class);
                            startActivity(in);
                        }
                        else
                        {
                            Intent in=new Intent(MainActivity.this,MyNotes.class);
                            startActivity(in);
                        }
                    }
                });

                ad = new ArrayAdapter<>(getApplicationContext(),android.R.layout.simple_list_item_1, op);
                dl = (DrawerLayout) (findViewById(R.id.dl));
                dmenu.setAdapter(ad);
//        bt1=(Button)(findViewById(R.id.bt1));
//        bt2=(Button)(findViewById(R.id.button1));
//        bt3=(Button)(findViewById(R.id.bt3));
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }

            getSupportActionBar().setHomeButtonEnabled(true);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            abdt = new ActionBarDrawerToggle(this, dl,
                    R.string.drawer_open, R.string.drawer_close)
            {

            };

            // Set the drawer toggle as the DrawerListener
            dl.setDrawerListener(abdt);
            abdt.syncState();

            ///adding listener to listview//

        }
    public boolean onOptionsItemSelected(MenuItem item) {

        if(status==1)
        {
            dl.openDrawer(GravityCompat.START);
            status*=-1;
        }
        else
        {
            dl.closeDrawer(GravityCompat.START);
            status*=-1;

        }


        return super.onOptionsItemSelected(item);
    }

    public void go5(View v)
    {

        Intent in=new Intent(this,Conversation.class);
        startActivity(in);

    }
    //    public void go4(View v)
//        {
//            Intent in=new Intent(this,BlueWifi.class);
//            startActivity(in);
//
//        }
//    public void go6(View v)
//    {
//        Intent in=new Intent(this,PlayMusic.class);
//        startActivity(in);
//
//    }
    }

