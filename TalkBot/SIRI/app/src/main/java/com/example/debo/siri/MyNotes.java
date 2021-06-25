package com.example.debo.siri;


import android.content.DialogInterface;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;
public class MyNotes extends AppCompatActivity
{

    ListView lvnotes;
    ArrayList<notesdemo>alnotes;
    String notes;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_notes);

        lvnotes=(ListView)findViewById(R.id.lvnotes);
        alnotes=new ArrayList<>();

        try
        {
            FileInputStream fis=new FileInputStream("/mnt/sdcard/TalkBot.txt");
            BufferedReader br=new BufferedReader(new InputStreamReader(fis));

            while(true)
            {
                notes=br.readLine();
                if(notes.equals(""))
                {
                    break;
                }
                Log.d("MYMSGn",notes);
                alnotes.add(new notesdemo(notes,R.drawable.cross));
            }
            Log.d("MYMSGa",notes);
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

        final NotesAdapter na=new NotesAdapter();
        lvnotes.setAdapter(na);
        final AlertDialog.Builder ad;
        ad=new AlertDialog.Builder(this);


        lvnotes.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                final int pos = position;
                ad.setTitle("Delete Note");
                ad.setMessage("Do you really want to delete this note?");
                ad.setIcon(R.drawable.del);
                ad.setPositiveButton("Yes", new DialogInterface.OnClickListener()
                {

                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {
                        alnotes.remove(pos);
                        na.notifyDataSetChanged();
                        try
                        {
                            FileOutputStream fos = new FileOutputStream(Environment.getExternalStorageDirectory()+ File.separator+"TalkBot.txt");
                            PrintWriter pw = new PrintWriter(fos);

                            pw.print("");
                            for (int i = 0; i < alnotes.size(); i++) {
                                pw.append(alnotes.get(i).n + "\n");
                                Log.d("MSG", alnotes.get(i).n + "");
                                pw.flush();
                            }


                            pw.close();
                        }
                        catch (Exception ex) {
                            ex.printStackTrace();
                        }
                    }
                });
                ad.setNegativeButton("cancel",new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int which)
                    {

                    }
                }).show();
            }
        });


    }
    //////// custom  adaapter//////

    public class NotesAdapter extends BaseAdapter
    {

        public int getCount()
        {
            return alnotes.size();
        }

        public Object getItem(int position)
        {
            return position;
        }

        public long getItemId(int position)
        {
            return position*10;
        }
        public View getView(int position, View notes, ViewGroup parent)
        {
            LayoutInflater inflater = LayoutInflater.from(parent.getContext());
            notes = inflater.inflate(R.layout.notes, parent, false);
            notesdemo nd = alnotes.get(position);
            TextView tv;
            ImageView imv;
            tv = (TextView) notes.findViewById(R.id.tv);
            imv = (ImageView) notes.findViewById(R.id.imv);
            tv.setText(nd.n);
            imv.setImageResource(nd.i);
            return notes;
        }
    }
    /////notesdemo inner class////\
    class notesdemo
    {
        String n;
        int i;
        notesdemo(String n,int i)
        {
            this.n=n;
            this.i=i;

        }
    }


}
