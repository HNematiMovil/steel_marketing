package ir.hadinemati.steelmarketing.Views;

import android.app.Dialog;
import android.content.Context;

import androidx.annotation.NonNull;

import ir.hadinemati.steelmarketing.R;

public class DialogWait extends Dialog {

    public DialogWait(@NonNull Context context) {
        super(context);
        setCanceledOnTouchOutside(false);
        setCancelable(false);
        setContentView(R.layout.dialog_wait);
    }
}
