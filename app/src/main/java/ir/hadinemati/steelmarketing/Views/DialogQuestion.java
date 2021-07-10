package ir.hadinemati.steelmarketing.Views;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;

import ir.hadinemati.steelmarketing.Presenters.Interfaces.IDialogResult;
import ir.hadinemati.steelmarketing.R;

public class DialogQuestion extends Dialog implements View.OnClickListener {

    IDialogResult dialogResult ;
    String title ;
   public enum DialogCloseStates {
           CLOSE_STATE_YES,
           CLOSE_STATES_NO,

    };

    public DialogQuestion(@NonNull Context context) {

        super(context);
    }

    public DialogQuestion(@NonNull Context context , IDialogResult dialogResult ,String title) {
        super(context);
        this.dialogResult = dialogResult;
        this.title = title;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_question);
        ((TextView)findViewById(R.id.tvDialogMessage)).setText(this.title);
        ((Button) findViewById(R.id.btnYes)).setOnClickListener(this);
        ((Button) findViewById(R.id.btnNo)).setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        if(dialogResult == null)
            return;

        switch (view.getId()){
            case R.id.btnYes:
                dialogResult.closed(DialogCloseStates.CLOSE_STATE_YES);
                this.dismiss();
                break;
            case R.id.btnNo:
                dialogResult.closed(DialogCloseStates.CLOSE_STATES_NO);
                this.dismiss();
                break;
        }
    }
}
