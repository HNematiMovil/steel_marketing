package ir.hadinemati.steelmarketing.Presenters;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Lib.AppDatabase;
import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Lib.Http;
import ir.hadinemati.steelmarketing.Models.Entity.ProductPriceDO;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.INewSellPresenter;
import ir.hadinemati.steelmarketing.Views.Interfaces.ISellView;

public class NewSellPresenter implements INewSellPresenter {


    ISellView sellView;
    Context context;

    AppDatabase db;

    public NewSellPresenter(ISellView sellView, Context context) {
        this.sellView = sellView;
        this.context = context;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public void getProductsList() {
        sellView.OnProductsListGenerated(getAllProductsList());
    }

    @Override
    public void addNewSell(String phoneNumber, String Gender, String Name, List<ProductPriceDO> productPriceDOS, String persianDateTime) {

        if (phoneNumber.isEmpty() || Name.isEmpty() || productPriceDOS.size() == 0 || persianDateTime.isEmpty()) {
            sellView.OnFormCompletionError();
            return;
        }

        HashMap<String, String> params = new HashMap<>();
        params.put("name",Name);
        params.put("order",new Gson().toJson(productPriceDOS));
        params.put("gender",Gender);
        params.put("persian_date", persianDateTime);
        params.put("phone_number",phoneNumber);


        Http http = new Http(Constants.getPostUrl("add_new_sell_order"), new Http.IHTTPResult() {
            @Override
            public void OnStarted() {

                sellView.WaitToSync();
            }

            @Override
            public void OnSuccess(String Result) {
                Log.d("new sell", "OnSuccess: ");
                sellView.SyncDone();
            }

            @Override
            public void OnProgress(int percent) {

            }

            @Override
            public void OnTimeOut() {

            }

            @Override
            public void OnFailed(String message) {

            }
        });

        http.BufferedPost(params);


    }

    private List<String> getAllProductsList() {
        List<String> products = new ArrayList<>();
        String productsJson = db.settingDao().getSettingByName(Constants.ProductsList);

        if (productsJson.length() <= 0)
            return null;

        String[] arrayOfProducts = productsJson.split("\\|");

        return Arrays.asList(arrayOfProducts).stream().sorted().collect(Collectors.toList());
    }
}
