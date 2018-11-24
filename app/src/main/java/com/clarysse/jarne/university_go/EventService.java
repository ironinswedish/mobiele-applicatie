package com.clarysse.jarne.university_go;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Binder;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Process;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class EventService extends Service {

    private boolean isRunning;
    private Timer timer;
    private Location location;
    private boolean bound = false;
    private LocationService locationService;
    private int RADIUS = 100;
    private static ArrayList<Event> nearbyEventList;

    private final IBinder mBinder = new EventService.LocalBinder();

    public class LocalBinder extends Binder {
        EventService getService() {
            return EventService.this;
        }
    }


    public EventService() {
    }

    public void addRandomEvents(){
        nearbyEventList = new ArrayList<Event>();

        for(int i = 0; i<10;i++) {
            double x0 = location.getLatitude();
            double y0 = location.getLongitude();

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = RADIUS / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            String name = "funny event "+i;
            nearbyEventList.add(new Event(randomLatLng, name, random.nextInt(50)));
        }
    }

    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = binder.getService();
            bound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bound = false;
        }
    };

    @Nullable
    @Override
    public void onCreate() {
        Log.e("Event", "created");
        isRunning = false;
        timer = new Timer();
        final Intent startservice;

        startservice = new Intent(this, LocationService.class);
        if (!bound) {
            startService(startservice);
            bindService(startservice, mConnection, Context.BIND_AUTO_CREATE);

        }
    }

    public static ArrayList<Event> getEvents(){
        return nearbyEventList;
    }


    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.e("Event", "Started");

        if (!isRunning) {
            isRunning = true;
            TimerTask timertask = new TimerTask() {
                @Override
                public void run() {
                    //Log.e("Event", "Timer executing");
                    location = LocationService.getLocation();
                    if (location != null) {
                        addRandomEvents();

                        Log.e("Event", "Setting events");
                    }
                }
            };
            while(location == null) {
                Log.d("event", "in the loop");
                location = locationService.getLocation();

            }
            addRandomEvents();
            timer.schedule(timertask, new Date(), 5000);
        }


        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("Event", "Destroying");
        Intent startservice;
        startservice = new Intent(this, LocationService.class);
        timer.cancel();
        if (bound) {
            unbindService(mConnection);
            bound = false;
        }
        stopService(startservice);
        super.onDestroy();
    }
}
