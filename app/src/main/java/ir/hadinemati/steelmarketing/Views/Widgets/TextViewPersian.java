package ir.hadinemati.steelmarketing.Views.Widgets;


import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import ir.hadinemati.steelmarketing.Lib.TypeFaceHelper;

public class TextViewPersian extends androidx.appcompat.widget.AppCompatTextView {

    public TextViewPersian(Context context) {
        super(context);
        changeFont();
    }

    public TextViewPersian(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        changeFont();
    }

    public TextViewPersian(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changeFont();
    }

    void changeFont(){
        if(!isInEditMode())
            setTypeface(TypeFaceHelper.getPersianTypeFace(getContext()));
    }


}
