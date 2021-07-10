package ir.hadinemati.steelmarketing.Presenters;

import android.content.Context;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Lib.AppDatabase;
import ir.hadinemati.steelmarketing.Lib.ConnectionHelper;
import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Lib.Http;
import ir.hadinemati.steelmarketing.Models.Entity.PotentialCustomerPhoneCall;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.ICallerInformationPresenter;
import ir.hadinemati.steelmarketing.Views.Interfaces.ICallerInformationView;
import saman.zamani.persiandate.PersianDate;

public class CallerInformationPresenter implements ICallerInformationPresenter {

    final String TAG = "CallerInformationPresenter";

    ICallerInformationView callerInformationView;
    Context context;
    AppDatabase db;

    public CallerInformationPresenter(ICallerInformationView callerInformationView, Context context) {
        this.callerInformationView = callerInformationView;
        this.context = context;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public void getProducts() {
        callerInformationView.OnProductsListGenerated(getAllProducts());
    }

    @Override
    public void Save(String phoneNumber, String ProductName,String Gender ,  String Name, String date) {
        // save
        if(phoneNumber.isEmpty() || ProductName.isEmpty() || Gender.isEmpty() || Name.isEmpty() || date.isEmpty())
            return;

        // save to db
        PotentialCustomerPhoneCall pcpc = new PotentialCustomerPhoneCall();
        pcpc.setPersianDate(date);
        pcpc.setUsername(Name);
        pcpc.setPhoneNumber(phoneNumber);
        pcpc.setProduct(ProductName);
        pcpc.setGender(Gender);

        String hashable = phoneNumber+ "|" + ProductName + "|" + date;
        pcpc.setUUID(Base64.encodeToString(hashable.getBytes() , Base64.DEFAULT).replaceAll("[\\n\\s=\\\\/]+",""));

        db.potentialCustomerPhoneCallDao().AddNewPhoneCall(pcpc);

        if(ConnectionHelper.isConnected(context)){
            HashMap<String,String> params = new HashMap<>();
            params.put("name",Name);
            params.put("product",ProductName);
            params.put("gender",Gender);
            params.put("persian_date", date);
            params.put("phone_number",phoneNumber);
            params.put("uuid",pcpc.getUUID());
            Http http = new Http(Constants.getPostUrl("add_new_potential_customer_call"), new Http.IHTTPResult() {
                @Override
                public void OnStarted() {

                }

                @Override
                public void OnSuccess(String Result) {
                    Log.d(TAG, "OnSuccess: " + Result);
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
        }else{
            Toast.makeText(context, "ارتباط با سرور برقرار نشد لطفا از قسمت لیست تماس ها اطلاعات را با سرور همگام نمایید", Toast.LENGTH_LONG).show();
        }

        callerInformationView.OnPotentialCallAdded();

    }

    private List<String> getAllProducts(){
        List<String> products = new ArrayList<>();
        String productsJson = db.settingDao().getSettingByName(Constants.ProductsList);
        if (productsJson.length() <= 0)
            return null;

        String[] arrayOfProducts = productsJson.split("\\|");
        return Arrays.asList(arrayOfProducts).stream().sorted().collect(Collectors.toList());
    }
}
