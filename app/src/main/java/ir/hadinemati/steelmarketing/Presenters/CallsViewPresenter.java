package ir.hadinemati.steelmarketing.Presenters;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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
import ir.hadinemati.steelmarketing.Presenters.Interfaces.ICallsListPresenter;
import ir.hadinemati.steelmarketing.Views.Interfaces.ICallsListView;

public class CallsViewPresenter implements ICallsListPresenter {

    String TAG = "callsListPresenter";

    ICallsListView callsListView;
    Context context;
    AppDatabase db;

    public CallsViewPresenter(ICallsListView callsListView, Context context) {
        this.callsListView = callsListView;
        this.context = context;
        db = AppDatabase.getInstance(context);
    }

    @Override
    public void getCallsList(String filter) {
        List<PotentialCustomerPhoneCall> callList;

        if (filter.equalsIgnoreCase("")) {
            callList = db.potentialCustomerPhoneCallDao().getAll();
        } else {
            callList = db.potentialCustomerPhoneCallDao().getPhoneCallByProductName(filter);
        }

        callsListView.OnCallsListGenerated(callList);

    }

    @Override
    public void getExtraInformation(String uuid) {
        Toast.makeText(context, uuid, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void getProductsList() {
        callsListView.OnProductsListGenerated(getAllProducts());
    }

    @Override
    public void SyncCalls() {
        if (!ConnectionHelper.isConnected(context)) {
            Toast.makeText(context, "عدم دسترسی به اینترنت", Toast.LENGTH_SHORT).show();
            return;
        }


        // get all uuids
        List<String> uuids = new ArrayList<>();
        HashMap<String, String> params = new HashMap<>();
        params.put("username","cccc");
        Log.d(TAG, "SyncCalls: " + Constants.getPostUrl("get_all_call_uuids"));
        Http http = new Http(Constants.getPostUrl("get_all_call_uuids"), new Http.IHTTPResult() {
            @Override
            public void OnStarted() {

            }

            @Override
            public void OnSuccess(String Result) {
                if (Result.isEmpty())
                    return;
                try {
                    JSONArray ja = new JSONArray(Result);
                    for (int i = 0; i < ja.length(); i++)
                        uuids.add(((JSONObject) ja.get(i)).getString("uuid"));




                    if (uuids.size() == 0) {
                        Toast.makeText(context, "خطا در دریافت اطلاعات", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    // get db records
                    List<PotentialCustomerPhoneCall> potentialCustomerPhoneCalls = db.potentialCustomerPhoneCallDao().getAll();

                    for (int index = 0; index < potentialCustomerPhoneCalls.size(); index++) {
                        PotentialCustomerPhoneCall _call = potentialCustomerPhoneCalls.get(index);
                        if (uuids.contains(_call.getUUID())) {
                            callsListView.SyncUpdated(index);
                            continue;
                        }

                        SyncSingleCall(_call,index);

                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }
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


    private void SyncSingleCall(PotentialCustomerPhoneCall _call,int childIndex) {
        if(ConnectionHelper.isConnected(context)){
            HashMap<String,String> params = new HashMap<>();
            params.put("name",_call.getUsername());
            params.put("product",_call.getProduct());
            params.put("gender",_call.getGender());
            params.put("persian_date", _call.getPersianDate());
            params.put("phone_number",_call.getPhoneNumber());
            params.put("uuid",_call.getUUID());

            Log.d(TAG, "SyncSingleCall: " + params.toString());

            Http http = new Http(Constants.getPostUrl("add_new_potential_customer_call"), new Http.IHTTPResult() {
                @Override
                public void OnStarted() {

                }

                @Override
                public void OnSuccess(String Result) {
                    callsListView.SyncUpdated(childIndex);
                    Log.d(TAG, "OnSuccess: "  );
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
    }

    private List<String> getAllProducts() {
        List<String> products = new ArrayList<>();
        String productsJson = db.settingDao().getSettingByName(Constants.ProductsList);
        if (productsJson.length() <= 0)
            return null;

        String[] arrayOfProducts = productsJson.split("\\|");
        return Arrays.asList(arrayOfProducts).stream().sorted().collect(Collectors.toList());
    }
}
