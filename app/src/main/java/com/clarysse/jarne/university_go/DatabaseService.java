package com.clarysse.jarne.university_go;

import android.app.Service;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.JsonReader;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DatabaseService extends Service {

    private boolean isRunning;
    private Timer timer;
    private Set<String> modified;
    private long lastUpdated;
    private SharedPreferences sp;
    private Iterator<String> setIterator;
    private String modifiedItem;
    private String[] splitItem;
    private int userId;
    private List<Move> moves;
    private List<Event> events;
    private List<Unimon> unimons;
    private static final String DATABASE_NAME = "movies_db";
    private UnimonDatabase unimonDatabase;
    private Retrofit retrofit;
    String ip1 = "192.168.1.10";
    String ip2;
    private ApiCallsInterface apiCallsInterface;




    public DatabaseService() {

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        ip2 =getResources().getString(R.string.IPaddres);
        Log.d("databaseService", "created");
        isRunning = false;
        timer = new Timer();
        sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        modified = sp.getStringSet("modified", null);
        lastUpdated = sp.getInt("lastupdated", 0);
        userId = sp.getInt("userid", -1);

        Log.e("dbservice",""+userId);
        unimonDatabase = Room.databaseBuilder(getApplicationContext(), UnimonDatabase.class, DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build();

        retrofit = new Retrofit.Builder().addConverterFactory(GsonConverterFactory.create()).baseUrl(ip2).build();
        apiCallsInterface = retrofit.create(ApiCallsInterface.class);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (!isRunning) {
            isRunning = true;
            TimerTask timertask = new TimerTask() {
                @Override
                public void run() {
                    if (checkWifiOnAndConnected()) {
                        updateRoom();
                        if(modified!=null) {
                            new pushUnimonModifications().execute();
                            sp.edit().putStringSet("modified", null);
                        }
                    }
                }
            };

            new pushUnimonModifications().execute();
            Log.e("sp", "unimonpushed");
            timer.schedule(timertask, new Date(), 5000);
        }

        return START_STICKY;
    }

    private boolean checkWifiOnAndConnected() {
        WifiManager wifiMgr = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        if (wifiMgr.isWifiEnabled()) { // Wi-Fi adapter is ON

            WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
            if (wifiInfo == null) {
                return false;
            }
            if (wifiInfo.getNetworkId() == -1) {
                return false; // Not connected to an access point
            }
            return true; // Connected to an access point
        } else {
            return false; // Wi-Fi adapter is OFF
        }
    }

    private void updateRoom() {
        if (lastUpdated == 0) {
            new UpdateAllTask().execute();
        } else {
            new LatestUpdateTask().execute();
        }
        lastUpdated = System.currentTimeMillis();
    }



    public class pushUnimonModifications extends AsyncTask<String, Void, Integer> {
        @Override
        protected Integer doInBackground(String... strings) {

            List<Unimon> modifiedUnimon = new ArrayList<>();
            /*setIterator = modified.iterator();
            while (setIterator.hasNext()) {
                modifiedItem = setIterator.next();
                modifiedUnimon.add(unimonDatabase.daoAcces().getUnimonById(Integer.parseInt(modifiedItem)));
            }*/
            Unimon uni = new Unimon();
            uni.setExp(51);
            uni.setLevel(40);
            uni.setNickname("heya");
            uni.setReal_id("2-1");
            uni.setOwnerid(2);
            uni.setEventid(3);
            modifiedUnimon.add(uni);
            GsonBuilder builder = new GsonBuilder();
            builder.serializeNulls();
            Gson gson = builder.create();
            gson.toJson(modifiedUnimon);
            String ip1 = "192.168.1.10";
            String ip2 = "http://192.168.1.10:5000";
            Type collectionType = new TypeToken<Collection<Unimon>>(){}.getType();
            Call<String> unimonCall = apiCallsInterface.updateunimon(gson.toJsonTree(modifiedUnimon,collectionType).getAsJsonArray());
            try {
                Response<String> unimonresponse = unimonCall.execute();
                if (unimonresponse.isSuccessful()) {
                    Log.e("unimon", "succes");
                } else {
                    Log.e("unimon", "failure");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            /*try {
                URL url = new URL("http://10.108.19.9:5000/updateunimon");
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setDoOutput(true);
                conn.setDoInput(true);

                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
                conn.setRequestProperty("Accept", "application/json");

                OutputStream os = conn.getOutputStream();
                os.write(gson.toJson(modifiedUnimon).getBytes("UTF-8"));
                os.close();

                StringBuilder sb = new StringBuilder();
                int HttpResult = conn.getResponseCode();
                if (HttpResult == HttpURLConnection.HTTP_OK) {
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    Log.e("loginTask", "response " + sb.toString());
                } else {
                    Log.e("loginTask", "response " + conn.getResponseMessage());
                }
                conn.disconnect();
            } catch (ProtocolException e) {
                e.printStackTrace();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/

            return 0;
        }
    }


    public class LatestUpdateTask extends AsyncTask<String, Void, Integer> {


        @Override
        protected Integer doInBackground(String... strings) {
            try {
                Gson gson = new Gson();
                Call<JsonElement> moveCall = apiCallsInterface.getLatestMoves(String.valueOf(lastUpdated));
                Call<JsonElement> eventCall = apiCallsInterface.getLatestEvents(String.valueOf(lastUpdated));

                Response<JsonElement> responseMove = moveCall.execute();
                Response<JsonElement> responseEvent = eventCall.execute();


                if (responseMove.isSuccessful()) {
                    Type collectionType = new TypeToken<List<Move>>(){}.getType();

                    moves = gson.fromJson(responseMove.body().getAsJsonArray(),collectionType);
                    System.out.println(moves.size()+"ev");
                } else {
                    Log.e("move", "did not receive all moves");
                }
                if (responseEvent.isSuccessful()) {
                    Type collectionType = new TypeToken<List<Event>>(){}.getType();
                    events = gson.fromJson(responseEvent.body().getAsJsonArray(),collectionType);
                    System.out.println(events.size()+"ev");
                } else {
                    Log.e("move", "did not receive all moves");
                }

                unimonDatabase.daoAcces().updateMoves(moves);
                unimonDatabase.daoAcces().updateEvents(events);

                return 0;
            } catch (Exception e) {
                Log.e("loginTask", "something went wrong", e);
                return 1;
            }
        }

    }

    public class UpdateAllTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected Integer doInBackground(String... strings) {
            try {

                Call<JsonElement> moveCall = apiCallsInterface.getAllMoves();
                Call<JsonElement> eventCall = apiCallsInterface.getAllEvents();

                Response<JsonElement> responseMove = moveCall.execute();
                Response<JsonElement> responseEvent = eventCall.execute();


                if (responseMove.isSuccessful()) {
                    Type collectionType = new TypeToken<List<Move>>(){}.getType();
                    Gson gson = new Gson();
                    moves = gson.fromJson(responseMove.body().getAsJsonArray(),collectionType);
                    Log.e("events", "moves" +moves.size());
                } else {
                    Log.e("move", "did not receive all moves");
                }

                if (responseEvent.isSuccessful()) {
                    Type collectionType = new TypeToken<List<Event>>(){}.getType();
                    Gson gson = new Gson();
                    events = gson.fromJson(responseEvent.body().getAsJsonArray(),collectionType);
                    Log.e("events", "events" + events.size());
                } else {
                    Log.e("move", "did not receive all moves");
                }

                if(userId!=-1) {
                    Call<List<Unimon>> unimonCall = apiCallsInterface.getOwnUnimons(""+userId);
                    Response<List<Unimon>> responseUnimon = unimonCall.execute();
                    if (responseUnimon.isSuccessful()) {
                        unimons = responseUnimon.body();
                    } else {
                        Log.e("move", "did not receive all moves");
                    }
                    unimonDatabase.daoAcces().insertMultipleUnimons(unimons);
                }

                unimonDatabase.daoAcces().insertMultipleMoves(moves);
                unimonDatabase.daoAcces().insertMultipleEvent(events);



                /*OutputStream os = conn.getOutputStream();
                os.write(credObject.toString().getBytes("UTF-8"));
                os.close();*/

                return 0;
            } catch (Exception e) {
                Log.e("loginTask", "something went wrong", e);
                return 1;
            }
        }


    }

    private void setupConnection(URL url, String type) throws IOException, JSONException {


        /*HttpURLConnection conn = (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");
        Log.e("dbservice", conn.getRequestMethod());
        conn.setDoOutput(true);
        conn.setDoInput(true);

        conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
        conn.setRequestProperty("Accept", "application/json");

        StringBuilder sb = new StringBuilder();
        int HttpResult = conn.getResponseCode();
        if (HttpResult == HttpURLConnection.HTTP_OK) {
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), "utf-8"));
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            if (type.equals("move")) {
                moves = new JSONObject(sb.toString());
                updateMoves();
                moves = null;
            } else if (type.equals("unimon")) {
                unimons = new JSONObject(sb.toString());
                updateUnimon();
                unimons = null;
            } else if (type.equals("event")) {
                events = new JSONObject(sb.toString());
                updateEvents();
                events = null;
            }

            Log.e("loginTask", "response " + sb.toString());
        } else {
            Log.e("loginTask", "response " + conn.getResponseMessage());
        }
        conn.disconnect();
        */
    }




    public void updateMoves() {
        //jsonobject move
        ArrayList<Move> movelist = new Gson().fromJson(moves.toString(), ArrayList.class);
        for (Move move : movelist) {
            System.out.println(move.getMoveid());
        }
        unimonDatabase.daoAcces().insertMultipleMoves(movelist);
        moves = null;
    }

    public void updateEvents() {
        //jsonobject event
        ArrayList<Event> eventlist = new Gson().fromJson(events.toString(), ArrayList.class);
        for (Event event : eventlist) {
            System.out.println(event.getEventid());
        }
        unimonDatabase.daoAcces().insertMultipleEvent(eventlist);
        events = null;


    }

    public void updateUnimon() {
        //jsonobject Unimon
        ArrayList<Unimon> unimonlist = new Gson().fromJson(unimons.toString(), ArrayList.class);
        for (Unimon unimon : unimonlist) {
            System.out.println(unimon.getUnimonid());
        }
        unimonDatabase.daoAcces().insertMultipleUnimons(unimonlist);
        unimons = null;
    }

}
