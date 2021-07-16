package ir.hadinemati.steelmarketing;

import android.content.Context;
import android.util.Log;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Lib.Encryption;
import ir.hadinemati.steelmarketing.Lib.Http;
import ir.hadinemati.steelmarketing.Lib.StringSplitHelpera;

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
        String original = "data=hadi";
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
                Log.d("http 1", "OnStarted: ");
            }

            @Override
            public void OnSuccess(String Result) {
                Log.d("http 1", "OnSuccess: " + Result);
            }

            @Override
            public void OnProgress(int percent) {

            }

            @Override
            public void OnTimeOut() {

            }

            @Override
            public void OnFailed(String message) {
                Log.d("http 1", "OnFailed: " + message);
            }
        });

        HashMap<String,String> params = new HashMap<>();
        params.put("username","this is a big username");
        params.put("pram2","data2");
        params.put("test","data3");

        http.BufferedPost(params);

    }


    @Test
    void SplitHelper() {
        String input = "this is a long splittable text that is ";
       List<String> parts =  StringSplitHelpera.SplitString(input , 10);
        Log.d("asdasd", "SplitHelper: "  + parts.stream().collect(Collectors.joining("")));
       assertEquals(parts.stream().collect(Collectors.joining("")), input );

    }
}