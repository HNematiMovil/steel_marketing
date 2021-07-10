package ir.hadinemati.steelmarketing.Lib;

import android.content.Context;
import android.graphics.Typeface;
import android.view.View;

public class TypeFaceHelper {


    public static Typeface getPersianTypeFace(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/Vazir.ttf");
    }

    public static Typeface getFontAwesomeTypeFace(Context context){
        return Typeface.createFromAsset(context.getAssets(),"fonts/fontawesome-webfont.ttf");
    }



}
