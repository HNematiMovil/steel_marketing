package ir.hadinemati.steelmarketing.Services;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.AudioAttributes;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Base64;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.FileProvider;

import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;

import ir.hadinemati.steelmarketing.BuildConfig;
import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Lib.Http;
import ir.hadinemati.steelmarketing.R;

public class Uploading extends IntentService {
    private String TAG = "UPLOADGIN";
    private static int FOREGROUND_ID = 1338;


    String CHANNEL_ID = "thisIsBigId";

    private NotificationCompat.Builder notificationBuilder;
    private NotificationManagerCompat notificationManager;

    private String data;
    private int UploadingIndex = 0;
    private Handler Threadhandler;
    private boolean isUploading = false;
    private JSONArray jaData;
    int notificationId = 5165;

    public Uploading() {
        super("uploading");
    }

    /**
     * @param name
     * @deprecated
     */
    public Uploading(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "test";
            String description = "tost";
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            channel.setSound(null,null);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }




        notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_notification_overlay)
                .setContentTitle("title")
                .setContentText("content")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
         notificationManager = NotificationManagerCompat.from(this);



        Bundle b = intent.getExtras();
        data = b.getString("ImagesPath");
        Log.d(TAG, "onHandleIntent: Started" + data);

        if (data.equalsIgnoreCase(""))
            return;

        try {
            jaData = new JSONArray(data);
            NextUpload();
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void NextUpload() {
        if (UploadingIndex >= jaData.length()) {
            notificationBuilder.setContentText("تصاویر ارسال شدند")
                    // Removes the progress bar
                    .setProgress(0, 0, false);
            notificationManager.notify(notificationId, notificationBuilder.build());

            return;
        }

        try {
            Log.i(TAG, "NextUpload: JsonProcess " + UploadingIndex);
            UploadSingleFile((JSONObject) jaData.get(UploadingIndex));
            UploadingIndex++;
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void UploadSingleFile(JSONObject jo) {
        try {
            String ImagePath = jo.getString("ImagePath");
            String category = jo.getString("category");
            File file = new File(ImagePath);

            Bitmap image = BitmapFactory.decodeFile(file.getAbsolutePath());
            ByteArrayOutputStream BAOS = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.JPEG, 100, BAOS);
            image = null;
            String decoded = Base64.encodeToString(BAOS.toByteArray(), Base64.DEFAULT);
            BAOS.reset();
            HashMap<String, String> param = new HashMap<>();
            param.put("imageData", decoded);
            param.put("category", category);
            param.put("filename", file.getName());
            decoded = "";

            Http http = new Http(Constants.getPostUrl("upload_images"), new Http.IHTTPResult() {
                @Override
                public void OnStarted() {
                    Log.i(TAG, "OnStarted: HTTP Started");
                    notificationBuilder.setContentTitle("ارسال تصویر " + UploadingIndex + " از " + jaData.length()).setContentText("")
                            // Removes the progress bar
                            .setProgress(0, 0, false);

                    notificationManager.notify(notificationId, notificationBuilder.build());

                }

                @Override
                public void OnSuccess(String Result) {
                    notificationBuilder.setContentText("تصویر ارسال شد")
                            // Removes the progress bar
                            .setProgress(0, 0, false);
                    notificationManager.notify(notificationId, notificationBuilder.build());
                    Log.i(TAG, "OnSuccess: " + Result);
                    NextUpload();
                }

                @Override
                public void OnProgress(int percent) {
                    notificationBuilder.setProgress(100, percent, false);
                    notificationManager.notify(notificationId, notificationBuilder.build());
                    Log.d(TAG, "OnProgress: " + percent);
                }

                @Override
                public void OnTimeOut() {

                }

                @Override
                public void OnFailed(String message) {
                    notificationBuilder.setContentTitle("خطا در برقراری ارتباط").setContentText("لطفا اینترنت خود را بررسی نموده و مجددا تلاش نمایید").setProgress(0, 0, false);
                    notificationManager.notify(notificationId, notificationBuilder.build());
                }
            });
            http.BufferedPost(param);
            param = null;
            BAOS = null;
        } catch (JSONException e) {

        }


    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(notificationId);
        super.onDestroy();

    }
}
