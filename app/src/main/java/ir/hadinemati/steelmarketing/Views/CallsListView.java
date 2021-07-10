package ir.hadinemati.steelmarketing.Views;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ir.hadinemati.steelmarketing.Lib.Adapters.CallsListRecyclerAdapter;
import ir.hadinemati.steelmarketing.Models.Entity.PotentialCustomerPhoneCall;
import ir.hadinemati.steelmarketing.Presenters.CallsViewPresenter;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.ICallsListPresenter;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.IProductsSelectionResult;
import ir.hadinemati.steelmarketing.R;
import ir.hadinemati.steelmarketing.Views.Interfaces.ICallsListView;

public class CallsListView extends AppCompatActivity implements ICallsListView {

    ICallsListPresenter callsListPresenter;

    RecyclerView rvCalls;
    TextView tvFilteredProducts;
    Button btnFilter;

    List<String> productList;

    final String SHOW_ALL = "نمایش همه";

    List<PotentialCustomerPhoneCall> callList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calls_list);

        callsListPresenter = new CallsViewPresenter(this, getApplicationContext());
        initView();

        callsListPresenter.getProductsList();
        callsListPresenter.getCallsList("");

    }

    void initView() {
        rvCalls = findViewById(R.id.rvCallsList);
        btnFilter = findViewById(R.id.btnFilter);
        tvFilteredProducts = findViewById(R.id.tvFilteredProduct);

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogProducts dlgProd = new DialogProducts(CallsListView.this, productList, new IProductsSelectionResult() {
                    @Override
                    public void selected(String name) {
                        tvFilteredProducts.setText(name);
                        if (name.equalsIgnoreCase(SHOW_ALL)){
                            callsListPresenter.getCallsList("");
                            return;
                        }
                        callsListPresenter.getCallsList(name);
                    }
                });

                dlgProd.show();
            }
        });

        findViewById(R.id.btnSync).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(callList.size() == 0)
                {
                    Toast.makeText(CallsListView.this, "داده ای برای همگام سازی وجود ندارد", Toast.LENGTH_SHORT).show();
                    return;
                }
                callsListPresenter.SyncCalls();
            }
        });

    }

    @Override
    public void OnCallsListGenerated(List<PotentialCustomerPhoneCall> callList) {
        this.callList = callList;
        rvCalls.setLayoutManager(new LinearLayoutManager(this));
        rvCalls.setAdapter(new CallsListRecyclerAdapter(callList, this, callsListPresenter));
    }

    @Override
    public void OnProductsListGenerated(List<String> products) {
        this.productList = products;
        this.productList.add(0, SHOW_ALL);
    }

    @Override
    public void SyncUpdated(int childIndex) {

    }
}
