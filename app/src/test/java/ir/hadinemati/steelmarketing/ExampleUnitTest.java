package ir.hadinemati.steelmarketing;

import android.util.Base64;
import android.util.Log;

import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;

import ir.hadinemati.steelmarketing.Lib.Encryption;
import ir.hadinemati.steelmarketing.Lib.Http;
import ir.hadinemati.steelmarketing.Lib.Num2CharConverter;
import saman.zamani.persiandate.PersianDate;
import saman.zamani.persiandate.PersianDateFormat;

import static org.junit.Assert.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        PersianDate p = new PersianDate();
        PersianDateFormat pform = new PersianDateFormat("Y-m-d");
        System.out.println(pform.format(p));
        assertEquals(pform.format(p), "1400-04-13");
    }

    @Test
    public void MillionTest() {
        assertEquals("یکهزار", Num2CharConverter.onWork((BigDecimal.valueOf(1000)), "").replaceAll(" ", ""));
    }

    @Test
    public void Base64Test() {
        String hashable = "Working is good";
        String base64 = Arrays.toString(Base64.encode(hashable.getBytes(), Base64.DEFAULT));
        System.out.println(base64);
        assertEquals("", "");

    }

    @Test
    public void HttpTest(){


    }

    @Test
    public void EncDecTest(){
        String original = "this is a test of + and - signs";
        String Enc = Encryption.Encrypt(original);
        String Dec = Encryption.Decrypt(Enc);

        System.out.println(Enc);
        System.out.println(Dec);

        assertEquals(Dec,original);
    }
}