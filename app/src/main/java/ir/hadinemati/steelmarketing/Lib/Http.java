package ir.hadinemati.steelmarketing.Lib;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;

import java.net.URL;
import java.net.URLEncoder;

import java.net.UnknownHostException;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class Http {

    HttpURLConnection http;

    String url;
    IHTTPResult httpResult;
    int MaxBufferSize = 8192;

    public Http(String url, IHTTPResult httpResult) {
        this.url = url;
        this.httpResult = httpResult;
    }

    public String PrepareDataToSend(HashMap<String, String> data) {
        StringBuilder outData = new StringBuilder();
        for (Map.Entry<String, String> param : data.entrySet()
        ) {

            try {
                outData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                outData.append("=");
                outData.append(URLEncoder.encode(param.getValue(), "UTF-8"));
                outData.append("&");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        outData.deleteCharAt(outData.length() - 1);
        return outData.toString();
    }

    public String PrepareDataToSend(HashMap<String, String> data,boolean doEncrypt){
        try {
            return "data="+URLEncoder.encode(Encryption.Encrypt(this.PrepareDataToSend(data)),"UTF-8")+"&enc=true";
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }




    public void BufferedPost(HashMap<String, String> param) {
        (new Thread(new Runnable() {
            @Override
            public void run() {
                httpResult.OnStarted();
                byte[] data_bytes = PrepareDataToSend(param,true).getBytes();
                try {
                    URL uri = new URL(url);
                    http = (HttpURLConnection) uri.openConnection();
                    http.setRequestMethod("POST");
                    http.setRequestProperty("Connection", "Keep-Alive");
                    http.setConnectTimeout(50 * 1000);
                    http.setReadTimeout(50 * 1000);
                    http.setDoOutput(true);
                    http.setDoInput(true);

//                    if(http.getResponseCode() != HttpURLConnection.HTTP_OK)
//                    {
//                        httpResult.OnFailed("NOConnection");
//                        return;
//                    }

                    OutputStream os = new BufferedOutputStream(http.getOutputStream());
                    ByteBuffer byteBuffer = ByteBuffer.wrap(data_bytes);
                    byte[] buffer;
                    int bytesRead = 0;
                    int length = data_bytes.length;

                    do {
                        int bufferSize = Math.min(byteBuffer.remaining(), MaxBufferSize);
                        buffer = new byte[bufferSize];
                        byteBuffer.get(buffer, 0, bufferSize);
                        bytesRead += bufferSize;
                        os.write(buffer);
                        httpResult.OnProgress(bytesRead*100 / length);

                    } while (bytesRead < length);
                    // reset memory
                    buffer = null;
                    byteBuffer = null;

                    os.flush();
                    os.close();


                    InputStream in = new BufferedInputStream(http.getInputStream());
                    String Result = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8)).lines().collect(Collectors.joining(""));
                    in = null;
                    httpResult.OnSuccess(Result);
                    Result = null;
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("http", "run: " + e.getMessage());
                    httpResult.OnFailed(e.getMessage());
                }



            }
        })).start();


    }


    public interface IHTTPResult {
        void OnStarted();
        void OnSuccess(String Result);
        void OnProgress(int percent);
        void OnTimeOut();

        void OnFailed(String message);
    }
}

