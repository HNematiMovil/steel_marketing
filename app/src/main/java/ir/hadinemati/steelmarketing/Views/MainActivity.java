package ir.hadinemati.steelmarketing.Views;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;


import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Presenters.MainPresenter;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Services.HandleCallReciverService;
import ir.hadinemati.steelmarketing.Services.Uploading;
import ir.hadinemati.steelmarketing.Views.Interfaces.IMainView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener, IMainView {


    MainPresenter _mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        _mainPresenter = new MainPresenter(this, getApplicationContext());
        _mainPresenter.initDatabase();

        // check permissions
        if (this.checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED ||
                this.checkSelfPermission(Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED ||
                this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED

        )
            requestPermissions(new String[]{Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        startService(new Intent(this, HandleCallReciverService.class));

        initView();




    }

    private void initView() {

        ((LinearLayout) findViewById(R.id.MainLLSettings)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.MainLLCallsList)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.MainLLStatistic)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.MainLLPictures)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.MainLLAccounting)).setOnClickListener(this);
        ((LinearLayout) findViewById(R.id.MainLLNewSell)).setOnClickListener(this);
        ((Button) findViewById(R.id.lastCallerInformationBtnSave)).setOnClickListener(this);


        SharedPreferences _shared = getSharedPreferences(Constants.sharedPreferenceName, MODE_PRIVATE);
        ((TextView) findViewById(R.id.lastCallerInformationTVDate)).setText(_shared.getString(Constants.DateTime, ""));
        ((TextView) findViewById(R.id.lastCallerInformationTVName)).setText(_shared.getString(Constants.userName, "بی نام"));
        ((TextView) findViewById(R.id.lastCallerInformationTVPhoneNumber)).setText(_shared.getString(Constants.phoneNumber, ""));
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.MainLLSettings:
                MainActivity.this.startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.MainLLCallsList:
                MainActivity.this.startActivity(new Intent(MainActivity.this, CallsListView.class));
                break;
            case R.id.MainLLStatistic:
                MainActivity.this.startActivity(new Intent(MainActivity.this, StatisticsActivity.class));
                break;
            case R.id.lastCallerInformationBtnSave:
                Intent CallerViewIntent = new Intent(MainActivity.this, CallerInformationView.class);
                CallerViewIntent.putExtra(Constants.ShowDialog, false);
                MainActivity.this.startActivity(CallerViewIntent);
                break;
            case R.id.MainLLPictures:
                startActivity(new Intent(MainActivity.this, PictureManagementView.class));
                break;
            case R.id.MainLLAccounting:
                startActivity(new Intent(MainActivity.this, AccountingView.class));
                break;
            case R.id.MainLLNewSell:
                startActivity(new Intent(MainActivity.this, NewSellView.class));
                break;

        }
    }
}