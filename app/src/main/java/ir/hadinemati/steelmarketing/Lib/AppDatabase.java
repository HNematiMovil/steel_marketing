package ir.hadinemati.steelmarketing.Lib;

import android.content.Context;

import androidx.room.Room;
import androidx.room.RoomDatabase;

import ir.hadinemati.steelmarketing.Models.DAO.PotentialCustomerPhoneCallDao;
import ir.hadinemati.steelmarketing.Models.DAO.SettingDao;
import ir.hadinemati.steelmarketing.Models.Entity.PotentialCustomerPhoneCall;
import ir.hadinemati.steelmarketing.Models.Entity.Setting;

@androidx.room.Database(version = 7,entities = {Setting.class , PotentialCustomerPhoneCall.class} , exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {
    public abstract SettingDao settingDao();
    public abstract PotentialCustomerPhoneCallDao potentialCustomerPhoneCallDao();

    public static AppDatabase mInstance = null;

    public static AppDatabase getInstance(Context context){
        if(AppDatabase.mInstance == null)
            AppDatabase.mInstance = Room.databaseBuilder(context, AppDatabase.class,Constants.DatabaseName)
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();

        return mInstance;
    }
}
