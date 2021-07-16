package ir.hadinemati.steelmarketing.Lib;


import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
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
import java.util.zip.GZIPInputStream;

public class Http {

    final String TAG = "http";

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
        if(outData.length() > 0)
            outData.deleteCharAt(outData.length() - 1);
        return outData.toString();
    }

    public String PrepareDataToSend(HashMap<String, String> data, boolean doEncrypt) {
        try {
            return "data=" + URLEncoder.encode(Encryption.Encrypt(this.PrepareDataToSend(data)), "UTF-8") + "&enc=true";
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
                byte[] data_bytes = PrepareDataToSend(param, true).getBytes();
                try {
                    URL uri = new URL(url);
                    http = (HttpURLConnection) uri.openConnection();

//                    http.setRequestProperty("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9");
//                    http.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
//                    http.setRequestProperty("Accept-Encoding", "identity");
//                    http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
//                    http.setRequestProperty("Content-Type", "multipart/form-data");
//                    http.setRequestProperty("Connection", "keep-alive");
                    http.setRequestMethod("POST");

                    http.setConnectTimeout(100 * 1000);
                    http.setReadTimeout(100 * 1000);
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
                        httpResult.OnProgress(bytesRead * 100 / length);

                    } while (bytesRead < length);
                    // reset memory
                    buffer = null;
                    byteBuffer = null;

                    os.flush();
                    os.close();

                    if (http.getResponseCode() == HttpURLConnection.HTTP_OK) {

                        Log.d(TAG, "run: " + http.getContentEncoding());

                        Reader reader = null;
                        if (http.getContentEncoding() != null) {
                            if (http.getContentEncoding().equalsIgnoreCase("gzip")) {
                                reader = new InputStreamReader(new GZIPInputStream(http.getInputStream()));
                            } else {
                                reader = new InputStreamReader(http.getInputStream());
                            }
                        } else {
                            reader = new InputStreamReader(http.getInputStream());
                        }

                        StringBuilder Result = new StringBuilder();

                        while (true) {
                            int ch = reader.read();
                            if (ch == -1)
                                break;
                            Result.append((char) ch);
                        }
                        Log.d(TAG, "run: success" + Result.toString());
                        if(!Result.toString().equalsIgnoreCase(""))
                           httpResult.OnSuccess(Encryption.Decrypt(Result.toString()));
                        else
                            httpResult.OnSuccess("");


                    } else {
                        Log.d("http", "run: refused " + http.getResponseCode() + http.getResponseMessage());
                        httpResult.OnFailed("Connection refused"+ http.getResponseMessage() + http.getResponseCode() );
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("http", "run: " + e.getMessage(), e.getCause());
                    httpResult.OnFailed(e.getMessage());
                } finally {
                   // http.disconnect();
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

