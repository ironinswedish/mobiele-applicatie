package com.clarysse.jarne.university_go;

import android.arch.persistence.room.Room;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
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
    private int SPLASHTIME=4000;


    private Button mainMenu;





    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent starterIntent = new Intent(this,LoginActivity.class);
        startActivity(starterIntent);



/*
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
        final Intent starterIntent = new Intent(this,LoginActivity.class);
        gotologinButton = findViewById(R.id.gotologinbutton);
        gotologinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(starterIntent);

            }
        });

        final Intent intent5 = new Intent(this, MainMenuActivity.class);
        mainMenu = findViewById(R.id.button3);
        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(intent5);
            }
        });
        WifiBroadCastReceiver broadCastReceiver = new WifiBroadCastReceiver();
        this.registerReceiver(broadCastReceiver,
                new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    */
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

    public void sendMessage(View view) {
        System.out.println("Gewoon een test");
    }

    @Override
    protected void onDestroy() {
        Intent startservice;
        Intent startdbservice;
        startservice = new Intent(this, LocationService.class);
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
        startdbservice = new Intent(this, LocationService.class);
        stopService(startservice);
        stopService(startdbservice);
        super.onDestroy();
    }


}
