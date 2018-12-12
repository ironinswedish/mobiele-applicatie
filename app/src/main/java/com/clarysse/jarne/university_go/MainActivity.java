package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clarysse.jarne.university_go.LocationService.LocalBinder;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private Button mapButton;
    private Button serviceButton;
    private Button gotologinButton;
    private LocationService locationService;
    private DatabaseService databaseService;
    private boolean bound = false;
    private TextView startcountview;
    private static final String DATABASE_NAME = "movies_db";
    private UnimonDatabase unimonDatabase;
    private Button pushData;
    private Button getData;
    private TextView id;
    private TextView naam;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        unimonDatabase = Room.databaseBuilder(getApplicationContext(), UnimonDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();


        final Intent intent = new Intent(this, MapsActivity.class);
        final Intent startdbservice;
        final Intent startservice;
        final Intent loginIntent = new Intent(this, LoginActivity.class);
        startservice = new Intent(this, LocationService.class);
        startdbservice = new Intent(this, DatabaseService.class);

        mapButton = (Button) findViewById(R.id.map);
        serviceButton = (Button) findViewById(R.id.serviceButton);
        startcountview = (TextView) findViewById(R.id.startcount);
        mapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                startActivity(intent);
            }
        });
        serviceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startService(startservice);
                startService(startdbservice);
                if (!bound) {
                    bindService(startservice, mConnection, Context.BIND_AUTO_CREATE);

                }
                if (bound) {
                    startcountview.setText(Integer.toString(locationService.getStartcount()));

                }
            }
        });
        gotologinButton = findViewById(R.id.gotologinbutton);
        gotologinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(loginIntent);

            }
        });
        pushData = findViewById(R.id.button);
        pushData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new PushToRoom().execute();
            }
        });
        getData = findViewById(R.id.button2);
        getData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GetFromRoom().execute();
            }
        });

        id = findViewById(R.id.textView2);
        naam = findViewById(R.id.textView3);
        description = findViewById(R.id.textView4);

    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocalBinder binder = (LocalBinder) service;
            locationService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Override
    protected void onDestroy() {
        Intent startservice;
        startservice = new Intent(this, LocationService.class);
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
        stopService(startservice);
        super.onDestroy();
    }

    public class PushToRoom extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {

            Event event = new Event();
            event.setBase_health(15);
            event.setBeschrijving("blablabla");
            event.setEventid(13);
            event.setNaam("sleepy ruben");
            List<Event> eventList = new ArrayList<>();
            eventList.add(event);
            unimonDatabase.daoAcces().insertMultipleEvent(eventList);

            return 0;
        }
    }

    public class GetFromRoom extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {

            List<Event> eventList = unimonDatabase.daoAcces().getEvents();
            if (eventList != null) {
                Log.e("getfromroom", (Integer.toString(eventList.get(eventList.size() - 1).getEventid())));
                Log.e("getfromroom", (eventList.get(eventList.size() - 1).getNaam()));
                Log.e("getfromroom", (eventList.get(eventList.size() - 1).getBeschrijving()));
            }
            /*id.setText(eventList.get(0).getEventid());
            naam.setText(eventList.get(0).getNaam());
            description.setText(eventList.get(0).getBeschrijving());*/

            return 0;
        }
    }

}
