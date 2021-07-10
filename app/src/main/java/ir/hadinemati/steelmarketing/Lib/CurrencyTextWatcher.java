package ir.hadinemati.steelmarketing.Lib;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.NumberFormat;

import ir.hadinemati.steelmarketing.R;

public class CurrencyTextWatcher implements TextWatcher {

    EditText et;
    TextView tvPricePersian;
    CurrencyTextWatcherCallBack callBack;


    public CurrencyTextWatcher(EditText et) {
        this.et = et;
    }

    public CurrencyTextWatcher(EditText et, CurrencyTextWatcherCallBack callBack) {
        this.et = et;
        this.callBack = callBack;
    }

    public CurrencyTextWatcher(EditText et, TextView tvPricePersian) {
        this.et = et;
        this.tvPricePersian = tvPricePersian;
    }

    public CurrencyTextWatcher(EditText et, TextView tvPricePersian, CurrencyTextWatcherCallBack callBack) {
        this.et = et;
        this.tvPricePersian = tvPricePersian;
        this.callBack = callBack;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        if (et.getText().toString().isEmpty())
            return;


        et.removeTextChangedListener(this);
        String cleanString = et.getText().toString().replace(",", "");


        int number = Integer.parseInt(cleanString);

        if (tvPricePersian != null)
            tvPricePersian.setText(Num2CharConverter.onWork(BigDecimal.valueOf(number), "تومان"));

        String formatted = NumberFormat.getCurrencyInstance().format(number);

        et.setText(formatted.replaceAll("[$.]", "").substring(0, formatted.length() - 4));

        et.setSelection(et.getText().length());
        // callback
        if (callBack != null)
            callBack.OnEnd(et.getText().toString(), number);
        et.addTextChangedListener(this);

    }


    public interface CurrencyTextWatcherCallBack {
        void OnEnd(String FormattedPrice, int Price);
    }
}
