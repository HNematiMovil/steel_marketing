package ir.hadinemati.steelmarketing.Lib;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;

import java.util.List;

import ir.hadinemati.steelmarketing.Models.Entity.ContactDO;

public class ContactsHelper {

    public static String getContactNameByPhoneNumber(Context context , String phoneNumber){
        String contactName = "";


            Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phoneNumber));
            String[] projection = new String[]{ContactsContract.PhoneLookup.DISPLAY_NAME};

            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);

            if (cursor!=null){
                if(cursor.moveToFirst()){
                    contactName = cursor.getString(0);
                }
                cursor.close();
            }


        return  contactName;
    }

    public static String ContactDOListToJson(List<ContactDO> _list){
        return "";
    }

}
