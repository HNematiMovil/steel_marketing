package ir.hadinemati.steelmarketing.Views.Widgets;


import android.content.Context;
import android.util.AttributeSet;

import ir.hadinemati.steelmarketing.Lib.TypeFaceHelper;

public class ButtonPersian extends androidx.appcompat.widget.AppCompatButton {

    public ButtonPersian(Context context) {
        super(context);
        changeFont();
    }

    public ButtonPersian(Context context, AttributeSet attrs) {
        super(context, attrs);
        changeFont();
    }

    public ButtonPersian(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        changeFont();
    }
    void changeFont(){
        if(!isInEditMode())
            setTypeface(TypeFaceHelper.getPersianTypeFace(getContext()));
    }
}
