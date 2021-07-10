package ir.hadinemati.steelmarketing.Views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.List;

import ir.hadinemati.steelmarketing.Lib.Constants;
import ir.hadinemati.steelmarketing.Lib.Num2CharConverter;
import ir.hadinemati.steelmarketing.Presenters.CallerInformationPresenter;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.ICallerInformationPresenter;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.IDialogResult;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.IProductsSelectionResult;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Views.Interfaces.ICallerInformationView;

public class CallerInformationView extends AppCompatActivity implements ICallerInformationView {


    private EditText etUserName, etPhoneNumber, etDateTime, etPriceOffer;
    private TextView tvSelectedProduct;
    private Button btnWoman,btnMan;
    private String Gender = "آقا";

    ICallerInformationPresenter _callerInformationPresenter;

    List<String> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_FULLSCREEN |
                        WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED |
                        WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);

        setContentView(R.layout.activity_caller_information_view);

        _callerInformationPresenter = new CallerInformationPresenter(this, getApplicationContext());
        _callerInformationPresenter.getProducts();

        initView();

        Bundle b = getIntent().getExtras();
        boolean showDialog = true;
        if (b != null)
            showDialog = b.getBoolean(Constants.ShowDialog);

        DialogQuestion dq = new DialogQuestion(this, new IDialogResult() {
            @Override
            public void closed(DialogQuestion.DialogCloseStates state) {
                if (state == DialogQuestion.DialogCloseStates.CLOSE_STATES_NO)
                    CallerInformationView.this.finish();
            }
        }, "آیا تماس کاری داشتید؟");

        dq.setCanceledOnTouchOutside(false);

        if (showDialog)
            dq.show();
    }


    private void initView() {
        etPhoneNumber = findViewById(R.id.etPhoneNumber);
        etUserName = findViewById(R.id.etUserName);
        etDateTime = findViewById(R.id.etTime);
        tvSelectedProduct = findViewById(R.id.tvSelectedProduct);

        etPriceOffer = findViewById(R.id.etPriceOffer);

        btnMan = findViewById(R.id.btnMan);
        btnWoman = findViewById(R.id.btnWoman);

        btnWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMan.setBackgroundColor(getColor(R.color.white));
                btnWoman.setBackgroundColor(getColor(R.color.green_50));
                Gender = "خانم";
            }
        });
        btnMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnWoman.setBackgroundColor(getColor(R.color.white));
                btnMan.setBackgroundColor(getColor(R.color.green_50));
                Gender = "آقا";
            }
        });

        etPriceOffer.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(etPriceOffer.getText().toString().isEmpty())
                    return;


                etPriceOffer.removeTextChangedListener(this);
                String cleanString = etPriceOffer.getText().toString().replace(",", "");



                int number = Integer.parseInt(cleanString);
                ((TextView) findViewById(R.id.tvPricePersian)).setText(Num2CharConverter.onWork(BigDecimal.valueOf(number), "تومان"));

               String formatted = NumberFormat.getCurrencyInstance().format(number);


                etPriceOffer.setText(formatted.replaceAll("[$.]","").substring(0,formatted.length()-4));

                etPriceOffer.setSelection(etPriceOffer.getText().length());
                etPriceOffer.addTextChangedListener(this);
            }
        });


        etUserName.requestFocus();

        SharedPreferences _shared = getSharedPreferences(Constants.sharedPreferenceName, MODE_PRIVATE);

        etPhoneNumber.setText(_shared.getString(Constants.phoneNumber, ""));
        etUserName.setText(_shared.getString(Constants.userName, ""));
        etDateTime.setText(_shared.getString(Constants.DateTime, ""));

        (findViewById(R.id.btnSave)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                _callerInformationPresenter.Save(etPhoneNumber.getText().toString(), tvSelectedProduct.getText().toString(), Gender ,etUserName.getText().toString(), etDateTime.getText().toString());
            }
        });

        ((Button) findViewById(R.id.btnChoose)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogProducts dp = new DialogProducts(CallerInformationView.this, list, new IProductsSelectionResult() {
                    @Override
                    public void selected(String name) {
//                        Toast.makeText(CallerInformationView.this, name, Toast.LENGTH_SHORT).show();
                        tvSelectedProduct.setText(name);
                    }
                });
                dp.setCanceledOnTouchOutside(false);
                dp.show();
            }
        });

    }

    @Override
    public void OnProductsListGenerated(List<String> productsList) {
        list = productsList;
    }

    @Override
    public void OnPotentialCallAdded() {
        Toast.makeText(this, "تماس ذخیره شد", Toast.LENGTH_SHORT).show();
        CallerInformationView.this.finish();
    }
}