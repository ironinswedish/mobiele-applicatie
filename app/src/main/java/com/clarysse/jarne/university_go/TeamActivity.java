package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Switch;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class TeamActivity extends AppCompatActivity {

    int userTeamSize = 10;
    private ListView teamlist;
    private CustomAdapter customAdapter;
    private UnidexAdapter unidexAdapter;
    private Switch unidexSwitch;
    private static final String DATABASE_NAME = "movies_db";
    private UnimonDatabase unimonDatabase;
    private List<Unimon> unimonList;
    private List<Event> eventList;
    private List<Integer> caughtList;
    private SharedPreferences sp;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);

        teamlist = findViewById(R.id.teamlist);

        unimonDatabase = Room.databaseBuilder(getApplicationContext(), UnimonDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        new GetUnimonsTask().execute();
        customAdapter = new CustomAdapter();
        unidexAdapter = new UnidexAdapter();


        unidexSwitch = findViewById(R.id.unidexswitch);
        unidexSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    teamlist.setAdapter(unidexAdapter);
                } else {
                    teamlist.setAdapter(customAdapter);
                }
            }
        });



    }

    private class CustomAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return unimonList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            convertView = getLayoutInflater().inflate(R.layout.teamentry, null);
            Unimon unimon = unimonList.get(position);
            ImageView imageView = convertView.findViewById(R.id.teamsprite);

            Event event = eventList.get(unimon.getEventid()-1);
            imageView.setImageResource(sp.getInt(""+event.getSprite(),0));
            Log.e("sprite", ""+event.getSprite());
            TextView nickname = convertView.findViewById(R.id.entrynumber);
            Log.e("teamlist", ""+unimon.getNickname());
            nickname.setText(unimon.getNickname());

            TextView level = convertView.findViewById(R.id.realname);
            level.setText("lv: "+unimon.getLevel());
            TextView totalhpvalue = convertView.findViewById(R.id.catchamount);
            totalhpvalue.setText("HP: "+event.getBase_health()*unimon.getLevel()/50);
            TextView type = convertView.findViewById(R.id.type);
            type.setText("Type: "+event.getType());
            TextView experience = convertView.findViewById(R.id.experience);
            experience.setText("Exp: "+unimon.getExp());
            return convertView;
        }

    }

    private class UnidexAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return eventList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Event event = eventList.get(position);
            convertView = getLayoutInflater().inflate(R.layout.unidexentry, null);
            ImageView imageView = convertView.findViewById(R.id.teamsprite);
            Log.e("sprite", ""+event.getSprite());
            imageView.setImageResource(sp.getInt(""+event.getSprite(),0));
            TextView entrynumber = convertView.findViewById(R.id.entrynumber);
            entrynumber.setText("No: "+event.getEventid());
            TextView naam = convertView.findViewById(R.id.realname);
            naam.setText(event.getNaam());
            TextView caught = convertView.findViewById(R.id.catchamount);
            caught.setText("Caught: "+caughtList.get(position));
            TextView type = convertView.findViewById(R.id.type);
            type.setText("Type: "+event.getType());


            return convertView;
        }
    }

    public class GetUnimonsTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
            unimonList = unimonDatabase.daoAcces().getOwnUnimons(sp.getInt("userid",-1));
            eventList = unimonDatabase.daoAcces().getEvents();
            caughtList = new ArrayList<>();
            for(int i=1;i<eventList.size()+1; i++) {
                caughtList.add(unimonDatabase.daoAcces().caughtAmount(i,sp.getInt("userid",-1)));
            }

            return 0;
        }

        @Override
        protected void onPostExecute(Integer result) {
            teamlist.setAdapter(customAdapter);
        }
    }


}
