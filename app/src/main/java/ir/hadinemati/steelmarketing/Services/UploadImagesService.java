package ir.hadinemati.steelmarketing.Services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Base64;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Lib.Http;
import ir.hadinemati.steelmarketing.R;

public class UploadImagesService extends Service {
    String TAG = "UploadImagesService";
    NotificationManager notificationManager;
    NotificationCompat.Builder notificationBuilder;
    int id= 1;

    String data;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
              return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Bundle b = intent.getExtras();
        data = b.getString("ImagesPath");

        if(data.equalsIgnoreCase(""))
            return START_STICKY;

        try {
            JSONArray ja = new JSONArray(data);
            for(int i=0;i<ja.length();i++){
                JSONObject jo = (JSONObject) ja.get(i);
                String ImagePath = jo.getString("ImagePath");
                String category = jo.getString("category");
                File file = new File(ImagePath);

                Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
                ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
                image.compress(Bitmap.CompressFormat.JPEG , 100,BAOS);
                image = null;
                String decoded = Base64.encodeToString(BAOS.toByteArray() , Base64.DEFAULT);
                BAOS.reset();
                HashMap<String , String> param = new HashMap<>();
                param.put("image" , decoded);
                param.put("name" , file.getName());
                decoded = "";
                notificationManager = (NotificationManager) getApplicationContext().getSystemService(NOTIFICATION_SERVICE);
                notificationBuilder = new NotificationCompat.Builder(this);
                notificationBuilder.setContentTitle("آپلود تصویر") .setContentText("Download in progress").setSmallIcon(R.drawable.ic_launcher_background);
                Http http = new Http(Constants.BaseUrl, new Http.IHTTPResult() {
                    @Override
                    public void OnStarted() {

                    }

                    @Override
                    public void OnSuccess(String Result) {
                        notificationBuilder.setContentText("تصویر ارسال شد")
                                // Removes the progress bar
                                .setProgress(0,0,false);
                        notificationManager.notify(id, notificationBuilder.build());
                        notificationManager.cancel(id);
                    }

                    @Override
                    public void OnProgress(int percent) {
                        notificationBuilder.setProgress(100,percent,false);
                        notificationManager.notify(id,notificationBuilder.build());
                    }

                    @Override
                    public void OnTimeOut() {

                    }

                    @Override
                    public void OnFailed(String message) {

                    }
                });
                http.BufferedPost(param);
                param = null;
                BAOS = null;


            }
        } catch (JSONException e) {
            e.printStackTrace();
        }




        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        Intent i = new Intent(this,UploadImagesService.class);
        i.putExtra("ImagesPath" , data );
        startService(i);
    }
}
