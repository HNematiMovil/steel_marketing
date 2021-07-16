package ir.hadinemati.steelmarketing.Lib;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class Encryption {

    private static String CIPHER_NAME = "AES/CBC/PKCS5PADDING";

public static String Encrypt(String data){
    try {
        IvParameterSpec ivSpec = new IvParameterSpec(Constants.CypherIV.getBytes("UTF-8"));
        SecretKeySpec secretKey = new SecretKeySpec(Constants.CypherKey.getBytes("UTF-8"), "AES");

        Cipher cipher = Cipher.getInstance(CIPHER_NAME);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivSpec);

        byte[] encryptedData = cipher.doFinal(data.getBytes());

        String encryptedDataInBase64 = new String(android.util.Base64.encode(encryptedData, android.util.Base64.DEFAULT));
        String ivInBase64 = new String(android.util.Base64.encode("UJA584IKKJWN852f".getBytes("UTF-8"), android.util.Base64.DEFAULT));

        return encryptedDataInBase64 + ":" + ivInBase64;

    } catch (Exception ex) {
        throw new RuntimeException(ex);
    }
}
    public static String Decrypt(String data){
        try {
            IvParameterSpec ivSpec = new IvParameterSpec(Constants.CypherIV.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(Constants.CypherKey.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance(CIPHER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivSpec);

            String[] dataParts = data.split(":");

            byte[] decryptedData = cipher.doFinal(Base64.decode(dataParts[0],Base64.DEFAULT));

            return new String(decryptedData);

        } catch (Exception ex) {
            Log.d("http dec", "Decrypt: " + ex.getMessage() + ex.getCause());
            throw new RuntimeException(ex);
        }
    }

}
