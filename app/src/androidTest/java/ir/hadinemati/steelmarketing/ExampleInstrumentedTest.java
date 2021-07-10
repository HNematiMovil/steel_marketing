package ir.hadinemati.steelmarketing;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;

import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Lib.Encryption;
import ir.hadinemati.steelmarketing.Lib.Http;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class ExampleInstrumentedTest {
    @Test
    public void useAppContext() {
        // Context of the app under test.
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();
        assertEquals("ir.hadinemati.steelmarketing", appContext.getPackageName());
    }


    @Test
    public void EncDecTest(){
        String original = "this is a test of + and - signs22";
        String Enc = Encryption.Encrypt(original);
        String Dec = Encryption.Decrypt(Enc);

        System.out.println(Enc);
        System.out.println(Dec);
        Log.d("testoftest", "EncDecTest: " + Enc + Dec);

        assertEquals(Dec,original);
    }

    @Test
    public void httpTest(){
        Http http = new Http(Constants.getPostUrl("check"), new Http.IHTTPResult() {
            @Override
            public void OnStarted() {
                Log.d("testoftest", "OnStarted: ");
            }

            @Override
            public void OnSuccess(String Result) {
                Log.d("testoftest", "OnSuccess: " + Result);
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

        HashMap<String,String> params = new HashMap<>();
        params.put("username","this is a big username");
        params.put("pram2","data2");
        params.put("test","data3");

        http.BufferedPost(params);

    }
}