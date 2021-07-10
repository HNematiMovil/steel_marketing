package ir.hadinemati.steelmarketing.Presenters;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Lib.AppDatabase;
import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Models.Entity.ContactDO;
import ir.hadinemati.steelmarketing.Models.Entity.Setting;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.ISettingsPresenter;
import ir.hadinemati.steelmarketing.Views.Interfaces.ISettingView;

public class SettingsPresenter implements ISettingsPresenter {
    String TAG = "SETTINGPRESENTER";

    ISettingView settingView;
    Context context;
    AppDatabase db;

    public SettingsPresenter(ISettingView settingView, Context context) {
        this.settingView = settingView;
        this.context = context;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public void getAllIgnoredContacts() {
        List<ContactDO> list = getAllIgnoredContactsToList();
        settingView.OnIgnoreContactsListGenerated(list);
        Log.d(TAG, "getAllIgnoredContacts: " + list.size());

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void AddNewIgnoredContact(String phoneNumber, @Nullable String Name) {
        List<ContactDO> allContacts = getAllIgnoredContactsToList();


        if(db.settingDao().getSettingByName(Constants.IgnoredContacts) !=null) {
            // check if phone number exists
            if (db.settingDao().getSettingByName(Constants.IgnoredContacts).contains(phoneNumber)) {
                settingView.OnNewContactExists();
                return;
            }
        }
        // if exists return

        if (Name == null)
            Name = "";
        allContacts.add(new ContactDO(Name, phoneNumber));
        String ToSaveJson = new Gson().toJson(allContacts);

        // save to DB

        // remove all contacts
        db.settingDao().RemoveSettingByName(Constants.IgnoredContacts);
        // and add all again
        Setting set = new Setting(Constants.IgnoredContacts, ToSaveJson);

        db.settingDao().AddSetting(set);

        settingView.OnIgnoreContactsListGenerated(allContacts);


    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void RemoveIgnoredContact(String phoneNumber) {
        List<ContactDO> newList = getAllIgnoredContactsToList().stream().filter(contactDO -> !contactDO.getPhoneNumber().equals(phoneNumber)).collect(Collectors.toList());

        db.settingDao().UpdateSetting(Constants.IgnoredContacts, (new Gson().toJson(newList)));

        settingView.OnIgnoreContactsListItemRemoved(newList);
    }

    @Override
    public void getAllProducts() {
        Log.d(TAG, "getAllProducts: called");
        settingView.OnProductListGenerated(getProductsList());
    }

    @Override
    public void AddNewProduct(String Name) {

        if (db.settingDao().getSettingByName(Constants.ProductsList).contains(Name)) {
            settingView.OnNewProductExists();
            return;
        }

        List<String> products = getProductsList();
        products.add(Name);
        // sort
        products = products.stream().sorted().collect(Collectors.toList());
        // add to db
        db.settingDao().RemoveSettingByName(Constants.ProductsList);
        db.settingDao().AddSetting(new Setting(Constants.ProductsList, products.stream().collect(Collectors.joining("|"))));
        settingView.OnProductListGenerated(products);
    }

    @Override
    public void RemoveProductByName(String Name) {
        List<String> products = getProductsList();

        if (!products.contains(Name))
            return;

        products.remove(Name);
        db.settingDao().RemoveSettingByName(Constants.ProductsList);
        db.settingDao().AddSetting(new Setting(Constants.ProductsList, products.stream().collect(Collectors.joining("|"))));
        settingView.OnProductListGenerated(products);
    }

    private List<ContactDO> getAllIgnoredContactsToList() {
        List<ContactDO> ignoredContact = new ArrayList<>();
        String ignoredContactsJson = db.settingDao().getSettingByName(Constants.IgnoredContacts);
        Log.d(TAG, "getAllIgnoredContactsToList: " + ignoredContactsJson);
        try {
            JSONArray ja = new JSONArray(ignoredContactsJson);
            for (int i = 0; i < ja.length(); i++) {
                JSONObject jo = (JSONObject) ja.get(i);
                ignoredContact.add(new ContactDO(jo.getString("Name"), jo.getString("PhoneNumber")));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG, "getAllIgnoredContactsToList: " + e.getMessage());

        } catch (NullPointerException e) {
            e.printStackTrace();
            Log.d(TAG, "getAllIgnoredContactsToList: " + e.getMessage());

        }
        return ignoredContact;
    }

    private List<String> getProductsList() {
        List<String> products = new ArrayList<>();
        String productsJson = db.settingDao().getSettingByName(Constants.ProductsList);

        if (productsJson.length() <= 0)
            return null;

        String[] arrayOfProducts = productsJson.split("\\|");

        return Arrays.asList(arrayOfProducts).stream().sorted().collect(Collectors.toList());
    }
}
