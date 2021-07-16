package ir.hadinemati.steelmarketing.Views;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

import ir.hadinemati.steelmarketing.Lib.ConnectionHelper;
import ir.hadinemati.steelmarketing.Lib.CurrencyTextWatcher;
import ir.hadinemati.steelmarketing.Models.Entity.ProductPriceDO;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.INewSellPresenter;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.IProductsSelectionResult;
import ir.hadinemati.steelmarketing.Presenters.NewSellPresenter;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Views.Interfaces.ISellView;

public class NewSellView extends AppCompatActivity implements ISellView {

    final String TAG = "NewSellView";

    EditText etCustomerPhoneNumber, etCustomerName, etDateTime;
    Button btnGenderMan, btnGenderWoman, btnAddOrder, btnAddNewProductPrice;
    ImageButton ibChooseDate;
    LinearLayout llProductsPricesContainer;

    LayoutInflater inflater;


    List<String> productsList;


    INewSellPresenter sellPresenter;

    List<ProductPriceDO> productPriceDOS;

    String Gender = "آقا";

    DialogWait dgw;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_sell);


        if(!ConnectionHelper.isConnected(getApplicationContext())){
            Toast.makeText(this, "لطفا ابتدا به اینترنت وصل شوید", Toast.LENGTH_LONG).show();
            this.finish();
        }


        sellPresenter = new NewSellPresenter(this, getApplicationContext());
        sellPresenter.getProductsList();

        inflater = LayoutInflater.from(this);

        productPriceDOS = new ArrayList<>();

        initView();
    }

    private void initView() {
        etCustomerName = findViewById(R.id.etUserName);
        etCustomerPhoneNumber = findViewById(R.id.etPhoneNumber);
        etDateTime = findViewById(R.id.etTime);
        btnGenderMan = findViewById(R.id.btnMan);
        btnGenderWoman = findViewById(R.id.btnWoman);
        btnAddOrder = findViewById(R.id.btnAddOrder);
        btnAddNewProductPrice = findViewById(R.id.btnAddProductPrice);
        ibChooseDate = findViewById(R.id.ibChooseDate);
        llProductsPricesContainer = findViewById(R.id.llProducts);




        btnGenderWoman.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGenderMan.setBackgroundColor(getColor(R.color.white));
                btnGenderWoman.setBackgroundColor(getColor(R.color.green_50));
                Gender = "خانم";
            }
        });
        btnGenderMan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnGenderWoman.setBackgroundColor(getColor(R.color.white));
                btnGenderMan.setBackgroundColor(getColor(R.color.green_50));
                Gender = "آقا";
            }
        });


        btnAddNewProductPrice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int _index = productPriceDOS.size();
                productPriceDOS.add(new ProductPriceDO("", 0, 0, 1));

                View v = inflater.inflate(R.layout.item_new_product_price, null);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(

                        ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT
                );
                lp.setMargins(0,10,0,10);
                v.setLayoutParams(lp);
                v.findViewById(R.id.ibSearch).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        DialogProducts dlg = new DialogProducts(NewSellView.this, productsList, new IProductsSelectionResult() {
                            @Override
                            public void selected(String name) {
                                ((TextView) v.findViewById(R.id.tvProductName)).setText(name);
                                productPriceDOS.get(_index).setName(name);
                            }
                        });

                        dlg.show();
                    }
                });

                EditText etBuyPrice = v.findViewById(R.id.etBuyPrice);
                TextView tvBuyPricePersian = v.findViewById(R.id.tvBuyPricePersian);
                etBuyPrice.addTextChangedListener(new CurrencyTextWatcher(etBuyPrice, tvBuyPricePersian, new CurrencyTextWatcher.CurrencyTextWatcherCallBack() {
                    @Override
                    public void OnEnd(String FormattedPrice, int Price) {
                        productPriceDOS.get(_index).setBuyPrice(Price);
                    }
                }));

                EditText etSellPrice = v.findViewById(R.id.etSellPrice);
                TextView tvSellPricePersian = v.findViewById(R.id.tvSellPricePersian);
                etSellPrice.addTextChangedListener(new CurrencyTextWatcher(etSellPrice, tvSellPricePersian, new CurrencyTextWatcher.CurrencyTextWatcherCallBack() {
                    @Override
                    public void OnEnd(String FormattedPrice, int Price) {
                        productPriceDOS.get(_index).setSellPrice(Price);
                    }
                }));
                EditText etCount = v.findViewById(R.id.etCount);
                etCount.addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                    }

                    @Override
                    public void afterTextChanged(Editable editable) {

                        if (etCount.getText().toString().isEmpty())
                            return;

                        etCount.removeTextChangedListener(this);

                        int number = Integer.parseInt(etCount.getText().toString());

                        if (number < 1) {
                            etCount.setText("1");
                            productPriceDOS.get(_index).setCount(1);
                        } else
                            productPriceDOS.get(_index).setCount(number);

                        etCount.addTextChangedListener(this);
                    }
                });

                v.findViewById(R.id.ibDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        llProductsPricesContainer.removeView(v);
                        productPriceDOS.remove(_index);

                    }
                });

                llProductsPricesContainer.addView(v);
            }
        });


        btnAddOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dgw = new DialogWait(NewSellView.this);
                dgw.show();
                sellPresenter.addNewSell(etCustomerPhoneNumber.getText().toString(),Gender,etCustomerName.getText().toString(),productPriceDOS,etDateTime.getText().toString());
            }
        });


    }

    @Override
    public void OnProductsListGenerated(List<String> products) {
        this.productsList = products;
    }

    @Override
    public void OnFormCompletionError() {
        Toast.makeText(this, "لطفا همه موارد خواسته شده را وارد نمایید", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void WaitToSync() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {

            }
        });

    }

    @Override
    public void SyncDone() {
        dgw.dismiss();
    }
}
