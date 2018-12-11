package com.clarysse.jarne.university_go;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Bundle;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Process;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import android.os.Handler;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class LocationService extends Service {

    private boolean isRunning;
    private boolean noPermission;
    private Timer timer;
    private int startcount;
    private Looper mServiceLooper;
    private ServiceHandler mServiceHandler;
    private static boolean locate;

    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;
    private boolean mLocationPermission;
    private LocationManager mLocationManager;
    private static Location location;

    public static final int LOCATION_UPDATE_MIN_DISTANCE = 10;
    public static final int LOCATION_UPDATE_MIN_TIME = 5000;

    private final IBinder mBinder = new LocalBinder();

    public class LocalBinder extends Binder {
        LocationService getService() {
            return LocationService.this;
        }
    }

    // Thread aanmaken om locatie te scannen*****************************************************************************************
    private final class ServiceHandler extends Handler {
        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            // Normally we would do some work here, like download a file.
            // For our sample, we just sleep for 5 seconds.
            try {
                while (locate) {
                    Thread.sleep(5000);
                    getCurrentLocation();
                    if (location != null) {
                        Log.e("location", location.toString());
                    }

                }

            } catch (InterruptedException e) {
                // Restore interrupt status.
                Thread.currentThread().interrupt();
            }
            // Stop the service using the startId, so that we don't stop
            // the service in the middle of handling another job
            stopSelf(msg.arg1);
        }
    }

    private void getCurrentLocation() {
        boolean isGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermission = true;
            noPermission = false;

        } else {
            noPermission = true;
        }

        location = null;
        if (!(isGPSEnabled || isNetworkEnabled)) {

        } else {
            if (isNetworkEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            }

            if (isGPSEnabled) {
                mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        LOCATION_UPDATE_MIN_TIME, LOCATION_UPDATE_MIN_DISTANCE, mLocationListener);
                location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            }
        }
    }

    private LocationListener mLocationListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            if (location != null) {


                mLocationManager.removeUpdates(mLocationListener);

            } else {

            }
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };


    public static Location getLocation(){
        return location;
    }

    public static void stopLocation(){
        locate = false;

    }

    //Basic Service methodes**********************************************
    @Override
    public void onCreate() {
        Log.e("location", "created");
        isRunning = false;
        startcount = 0;
        locate = true;
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        timer = new Timer();
        HandlerThread thread = new HandlerThread("ServiceStartArguments", Process.THREAD_PRIORITY_BACKGROUND);
        thread.start();
        mServiceLooper = thread.getLooper();
        mServiceHandler = new ServiceHandler(mServiceLooper);

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public int getStartcount() {
        return startcount;
    }

    public void doSomething() {
        isRunning = true;
        Log.e("doSomething", "entering something");
        // Stop the service using the startId, so that we don't stop
        // the service in the middle of handling another job
        //stopSelf();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        startcount += 1;
        Log.e("count", String.valueOf(startcount));
        Toast.makeText(this, "service starting", Toast.LENGTH_SHORT).show();


        if (!isRunning) {
            isRunning = true;
            Log.d("location", "this is something");
            Message msg = mServiceHandler.obtainMessage();
            msg.arg1 = startId;
            mServiceHandler.sendMessage(msg);
            /*TimerTask timerTask = new TimerTask() {
                @Override
                public void run() {
                    Log.e("location", "do location shizzle");
                    getCurrentLocation();
                }
            };*/

            doSomething();
            //timer.schedule(timerTask, new Date(), 5000);
        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        Log.e("end", "done");
        Toast.makeText(this, "service done", Toast.LENGTH_SHORT).show();
    }
}

