package ir.hadinemati.steelmarketing.Recivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;

import ir.hadinemati.steelmarketing.Lib.PhoneStateChangeListenerHandler;

public class CallReciver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {

        //// calls a phone state change lister
        TelephonyManager tmgr = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateChangeListenerHandler pclh = new PhoneStateChangeListenerHandler(context);
        tmgr.listen(pclh , PhoneStateListener.LISTEN_CALL_STATE);
    }
}
