package com.clarysse.jarne.university_go;

import android.Manifest;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 11;
    private boolean mLocationPermission;
    private LocationManager mLocationManager;
    private LocationService locationService;
    private EventService eventService;
    private boolean eventbound = false;
    private Thread locationUpdater;
    private boolean locationbound = false;

    private Marker locationMarker;
    private ArrayList<Marker> eventMarkerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Intent startservice;
        startservice = new Intent(this, LocationService.class);
        if (!locationbound) {
            startService(startservice);
            bindService(startservice, locationConnection, Context.BIND_AUTO_CREATE);

        }
        startservice = new Intent(this, EventService.class);
        if (!eventbound) {
            startService(startservice);
            bindService(startservice, eventConnection, Context.BIND_AUTO_CREATE);

        }

    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermission = true;

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
            // Show rationale and request permission.
        }

        if (mLocationPermission) {


        } else {
            // Add a marker in Sydney and move the camera
            LatLng sydney = new LatLng(-34, 151);
            mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        }
        locationLoop();
        eventLoop();
    }

    public void locationLoop() {
        locationUpdater = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (locationbound) {
                                    Location location = locationService.getLocation();
                                    Log.e("thread", "still executing");
                                    if (location != null) {
                                        drawMarker(location);
                                    }

                                }


                            }
                        });
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {

                }
                Log.e("thread", "bye bye");
            }
        };
        locationUpdater.start();
    }


    private void drawMarker(Location location) {
        if (mMap != null) {
            if (locationMarker != null) {
                locationMarker.remove();
            }
            LatLng gps = new LatLng(location.getLatitude(), location.getLongitude());


            locationMarker = mMap.addMarker(new MarkerOptions().position(gps).title("Current Position"));
            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(gps, 17));
        }

    }

    public void eventLoop() {
        locationUpdater = new Thread() {
            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (eventbound) {
                                    ArrayList<Event> events = eventService.getEvents();
                                    if (events != null) {
                                        setEvents(events);
                                    }

                                }


                            }
                        });
                        Thread.sleep(5000);
                    }
                } catch (InterruptedException e) {

                }
                Log.e("eventthread", "bye bye");
            }
        };
        locationUpdater.start();
    }

    public void setEvents(ArrayList<Event> events) {
        if (mMap != null) {
            if (eventMarkerList != null) {
                for (Marker mark : eventMarkerList) {
                    mark.remove();
                }

            }
            eventMarkerList = new ArrayList<Marker>();
            for (Event event : events) {
                eventMarkerList.add(mMap.addMarker(new MarkerOptions().position(event.getLocation()).title(event.getEventName() + "\n" + "level: " + event.getLevel()).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));
            }
        }
    }

    @Override
    protected void onDestroy() {
        Intent startservice;
        startservice = new Intent(this, LocationService.class);
        Log.e("maps", "unbinding locationConnection");
        if (locationbound) {
            Log.e("maps", "unbinding locationConnection");
            unbindService(locationConnection);
            locationbound = false;
        }
        if (eventbound) {
            Log.e("maps", "unbinding eventConnection");
            unbindService(eventConnection);
            eventbound = false;
        }
        locationUpdater.interrupt();
        stopService(startservice);
        startservice = new Intent(this, EventService.class);
        stopService(startservice);
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationLoop();
    }

    @Override
    protected void onPause() {
        super.onPause();
        locationUpdater.interrupt();

    }


    private ServiceConnection locationConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationService.LocalBinder binder = (LocationService.LocalBinder) service;
            locationService = binder.getService();
            locationbound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            locationbound = false;
        }
    };

    private ServiceConnection eventConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            EventService.LocalBinder binder = (EventService.LocalBinder) service;
            eventService = binder.getService();
            eventbound = true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            eventbound = false;
        }
    };
}
