package ir.hadinemati.steelmarketing.Views.Widgets;

import android.content.Context;
import android.util.AttributeSet;

import ir.hadinemati.steelmarketing.Lib.TypeFaceHelper;


public class FontAwesomeIcon extends androidx.appcompat.widget.AppCompatTextView {
    public FontAwesomeIcon(Context context) {
        super(context);
        ChanegFont();
    }

    public FontAwesomeIcon(Context context, AttributeSet attrs) {
        super(context, attrs);
        ChanegFont();
    }

    public FontAwesomeIcon(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        ChanegFont();
    }

    private void ChanegFont(){
        if(isInEditMode())
            return;
        setTypeface(TypeFaceHelper.getFontAwesomeTypeFace(getContext()));
    }
}
