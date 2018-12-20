package com.clarysse.jarne.university_go;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

public class WifiBroadCastReceiver extends BroadcastReceiver{
    @Override
    public void onReceive(Context context, Intent intent) {

        NetworkInfo currentNetworkInfo = (NetworkInfo) intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

        if(currentNetworkInfo.isConnected()){
            Toast.makeText(context, "Mogelijk om in te loggen", Toast.LENGTH_SHORT).show();
            Log.e("con", "connected");
        }else{
            Log.e("con", "not connected");
            Toast.makeText(context, "Niet mogelijk om in te loggen", Toast.LENGTH_SHORT).show();

        }
    }
}
