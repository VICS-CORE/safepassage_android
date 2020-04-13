package org.covid19india.android.safepassageindia;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class BroadcastReceiverService extends Service {
    private static BroadcastReceiver simChangedReceiver;
    public BroadcastReceiverService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("Broadcast Receiver", "Service created");
        registerScreenOffReceiver();
    }
    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
        super.onTaskRemoved(rootIntent);
    }
    @Override
    public void onDestroy()
    {
        Log.d("Broadcast Receiver", "Service destroyed");
        Intent restartServiceIntent = new Intent(getApplicationContext(),this.getClass());
        restartServiceIntent.setPackage(getPackageName());
        startService(restartServiceIntent);
//        unregisterReceiver(simChangedReceiver);
//        m_ScreenOffReceiver = null;
    }
    private void registerScreenOffReceiver()
    {
        simChangedReceiver = new BroadcastReceiver()
        {
            @Override
            public void onReceive(Context context, Intent intent)
            {
                Log.d("Broadcast Receiver", "Receiver executed");
                String state = intent.getExtras().getString("ss");
                if (state == null) {
                    Log.d("Broadcast Receiver", "SIM null");
                }
                try {
                    if ("NOT_READY".equals(state)) {
                        if(FirebaseAuth.getInstance().getCurrentUser()!=null){
                            FirebaseAuth.getInstance().signOut();
                        }
                        Log.d("Broadcast Receiver", "SIM not ready");
                    }
                } catch (Exception e){
                    Log.e("Broadcast Receiver","State is null");
                }
            }
        };
        IntentFilter filter = new IntentFilter("android.intent.action.SIM_STATE_CHANGED");
        registerReceiver(simChangedReceiver, filter);
    }
}
