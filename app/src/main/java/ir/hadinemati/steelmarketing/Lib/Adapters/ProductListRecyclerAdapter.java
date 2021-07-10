package ir.hadinemati.steelmarketing.Lib.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.hadinemati.steelmarketing.Presenters.Interfaces.ISettingsPresenter;
import ir.hadinemati.steelmarketing.R;

public class ProductListRecyclerAdapter extends RecyclerView.Adapter<ProductListRecyclerAdapter.ProductListViewHolder> {


    Context context;
    List<String> _productList;
    List<String> defaultProductList ;
    LayoutInflater layoutInflater ;
    ISettingsPresenter settingsPresenter;

    public ProductListRecyclerAdapter(Context context, List<String> _productList, List<String> defaultProductList, ISettingsPresenter settingsPresenter) {
        this.context = context;
        this._productList = _productList;
        this.defaultProductList = defaultProductList;
        this.layoutInflater = LayoutInflater.from(context);
        this.settingsPresenter = settingsPresenter;
    }

    @NonNull
    @Override
    public ProductListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_product_recycler , parent,false);
        return new ProductListViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductListViewHolder holder, int position) {
        holder.btnRemove.setVisibility(View.VISIBLE);
        if(defaultProductList.contains(_productList.get(position)))
            holder.btnRemove.setVisibility(View.INVISIBLE);

        holder.tvName.setText(_productList.get(position));
    }

    @Override
    public int getItemCount() {
        return _productList.size();
    }

    public class ProductListViewHolder extends RecyclerView.ViewHolder{

        TextView tvName;
        ImageButton btnRemove;

        public ProductListViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.ItemProductRecyclerTvName);
            btnRemove = itemView.findViewById(R.id.ItemProductRecyclerIBDelete);
            btnRemove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingsPresenter.RemoveProductByName(tvName.getText().toString());
                }
            });
        }
    }
}
