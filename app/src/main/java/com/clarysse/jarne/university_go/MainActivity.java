package com.clarysse.jarne.university_go;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.LocationManager;
import android.os.Binder;
import android.os.IBinder;
import android.os.VibrationEffect;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.clarysse.jarne.university_go.LocationService.LocalBinder;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    private Button mapButton;
    private Button serviceButton;
    private Button gotologinButton;
    private LocationService locationService;
    private boolean bound=false;
    private TextView startcountview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final Intent intent = new Intent(this, MapsActivity.class);
        final Intent startservice;
        final Intent loginIntent = new Intent(this, LoginActivity.class);
        startservice = new Intent(this, LocationService.class);

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
    }

    private ServiceConnection mConnection = new ServiceConnection(){

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

}
