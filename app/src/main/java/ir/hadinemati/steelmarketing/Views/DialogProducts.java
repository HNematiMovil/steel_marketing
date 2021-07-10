package ir.hadinemati.steelmarketing.Views;

import android.app.Dialog;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;
import java.util.stream.Collectors;

import ir.hadinemati.steelmarketing.Presenters.Interfaces.IProductsSelectionResult;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Views.Widgets.TextViewPersian;


public class DialogProducts extends Dialog {

    Context context;
    List<String> list;
    IProductsSelectionResult productsSelectionResult;

    Handler _handler ;

    public DialogProducts(@NonNull Context context , List<String> items , IProductsSelectionResult productsSelectionResult) {
        super(context);
        this.context = context;
        this.list = items;
        this.productsSelectionResult = productsSelectionResult;

    }

    EditText etSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_products);
        // init rendering
        RenderView(list);

        etSearch =  ((EditText) findViewById(R.id.etSearchProduct));
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                _handler = new Handler();
                _handler.removeCallbacksAndMessages(null);
                _handler.postDelayed(new Runnable() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void run() {
                        List<String> subList = list.stream().filter(x->x.contains(etSearch.getText())).collect(Collectors.toList());
                        RenderView(subList);
                    }
                },1000);
            }
        });




    }

    void RenderView(List<String> ItemsList){

        LinearLayout llproducts = findViewById(R.id.llProducts);
        llproducts.removeAllViews();
        for (String item : ItemsList){

            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view= inflater.inflate(R.layout.item_product , null );
            TextViewPersian tv = view.findViewById(R.id.itemProductTVText);
            tv.setText(item);
            tv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    productsSelectionResult.selected(item);
                    DialogProducts.this.dismiss();
                }
            });
//            tv.setPadding(10,10,10,10);
            llproducts.addView(view);
        }

    }
}
