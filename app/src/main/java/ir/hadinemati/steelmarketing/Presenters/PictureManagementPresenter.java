package ir.hadinemati.steelmarketing.Presenters;


import android.content.Context;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Lib.AppDatabase;
import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.IPictureManagementPresenter;
import ir.hadinemati.steelmarketing.Views.Interfaces.IPictureManagementView;

public class PictureManagementPresenter implements IPictureManagementPresenter {

    IPictureManagementView pictureManagementView;
    Context context;
    AppDatabase db;

    public PictureManagementPresenter(IPictureManagementView pictureManagementView, Context context) {
        this.pictureManagementView = pictureManagementView;
        this.context = context;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public void getProductList() {
        pictureManagementView.OnGenerateProductList(getProductsList());
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
