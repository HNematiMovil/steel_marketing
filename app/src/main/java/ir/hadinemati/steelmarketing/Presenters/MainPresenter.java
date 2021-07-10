package ir.hadinemati.steelmarketing.Presenters;

import android.content.Context;
import android.util.Log;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Lib.AppDatabase;
import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Models.Entity.Setting;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.IMainPresenter;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Views.Interfaces.IMainView;


public class MainPresenter implements IMainPresenter {
    String TAG = "MainPresenter";

    IMainView mainView;
    Context context;
    AppDatabase db;

    public MainPresenter(IMainView mainView, Context context) {
        this.mainView = mainView;
        this.context = context;
        db = AppDatabase.getInstance(context);

       // context.startActivity(new Intent(context , SettingsActivity.class));

    }

    @Override
    public void initDatabase() {
        // add Products to db
        String productsJson = db.settingDao().getSettingByName(Constants.ProductsList);
        Log.d(TAG, "initDatabase: " + productsJson);

        if(productsJson != null)
            return;

        List<String> productsList = Arrays.asList(context.getResources().getStringArray(R.array.products));
        db.settingDao().RemoveSettingByName(Constants.ProductsList);
        db.settingDao().AddSetting(new Setting(Constants.ProductsList ,productsList.stream().collect(Collectors.joining("|"))));


    }
}
