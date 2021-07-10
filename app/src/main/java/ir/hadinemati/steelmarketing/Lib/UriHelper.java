package ir.hadinemati.steelmarketing.Lib;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

public class UriHelper {

    public static String findImagePath(Context context , Uri uri){
        String path = "";

        String[] filepath = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri,filepath,null,null,null);
        cursor.moveToFirst();
        return cursor.getString(cursor.getColumnIndex(filepath[0]));
    }
}
