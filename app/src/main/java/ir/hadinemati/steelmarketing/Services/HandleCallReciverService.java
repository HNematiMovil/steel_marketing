package ir.hadinemati.steelmarketing.Services;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.widget.Toast;

import androidx.annotation.Nullable;

import ir.hadinemati.steelmarketing.Recivers.CallReciver;

public class HandleCallReciverService extends Service {

    CallReciver _callReciver = null;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }


    @Override
    public void onCreate() {
        super.onCreate();

        IntentFilter intentFilter = new IntentFilter("android.intent.action.PHONE_STATE");
        intentFilter.setPriority(100);
        _callReciver = new CallReciver();
        registerReceiver(_callReciver , intentFilter);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if(_callReciver !=null)
            return;

        unregisterReceiver(_callReciver);

        startService(new Intent(this,this.getClass()));


    }
}
