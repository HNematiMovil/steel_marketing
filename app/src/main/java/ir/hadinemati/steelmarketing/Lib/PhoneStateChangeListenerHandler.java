package ir.hadinemati.steelmarketing.Lib;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Models.Entity.ContactDO;
import ir.hadinemati.steelmarketing.Views.CallerInformationView;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

public class PhoneStateChangeListenerHandler extends PhoneStateListener {

    Context context;


    public PhoneStateChangeListenerHandler(Context context) {
        this.context = context;
    }

    @Override
    public void onCallStateChanged(int state, String phoneNumber) {

        SharedPreferences _shared = context.getSharedPreferences(Constants.sharedPreferenceName, Context.MODE_PRIVATE);
        AppDatabase db;
        if (state == TelephonyManager.CALL_STATE_RINGING) {
            Date c = Calendar.getInstance().getTime();
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());
            PersianDate pdate = new PersianDate();
            PersianDateFormat pdateFormat = new PersianDateFormat("Y-m-d");
            // save caller information

            _shared.edit().putString(Constants.phoneNumber, phoneNumber.replace("+98", "0").replace(" ", "")).apply();
            _shared.edit().putString(Constants.userName, ContactsHelper.getContactNameByPhoneNumber(this.context, phoneNumber)).apply();
            _shared.edit().putString(Constants.DateTime, pdateFormat.format(pdate) + " " + sdf.format(c)).apply();

            _shared.edit().putBoolean(Constants.isCallIncoming, true).apply();

        }

        // call ended and it's time to open caller info form
        if (state == TelephonyManager.CALL_STATE_IDLE) {

            if (!_shared.getBoolean(Constants.isCallIncoming, false))
                return;


            _shared.edit().putBoolean(Constants.isCallIncoming, false).apply();


            // check if phone number is in ignore list
            db = AppDatabase.getInstance(context.getApplicationContext());
            String _phoneNumber = _shared.getString(Constants.phoneNumber, "");
            List<ContactDO> ignoredContacts = getAllIgnoredContactsToList(db);
            List<ContactDO> ignoredContactsFilter = ignoredContacts.stream().filter(contactDO -> contactDO.getPhoneNumber().replace("+98", "").replace(" ", "").equalsIgnoreCase(_phoneNumber)).collect(Collectors.toList());

            if (ignoredContactsFilter.size() == 0) // no contacts
                context.startActivity(new Intent(context, CallerInformationView.class));
        }


        super.onCallStateChanged(state, phoneNumber);
    }

    private List<ContactDO> getAllIgnoredContactsToList(AppDatabase db) {
        List<ContactDO> ignoredContact = new ArrayList<>();
        String ignoredContactsJson = db.settingDao().getSettingByName(Constants.IgnoredContacts);
        Log.d("PhoneStateChangeListener", "getAllIgnoredContactsToList: " + ignoredContactsJson);
        try {
            JSONArray ja = new JSONArray(ignoredContactsJson);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                ignoredContact.add(new ContactDO(jo.getString("Name"), jo.getString("PhoneNumber")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d("PhoneStateChangeListener", "getAllIgnoredContactsToList: " + e.getMessage());

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d("PhoneStateChangeListener", "getAllIgnoredContactsToList: " + e.getMessage());

        }
        return ignoredContact;
    }
}

