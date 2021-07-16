package ir.hadinemati.steelmarketing.Lib.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ir.hadinemati.steelmarketing.Models.Entity.PotentialCustomerPhoneCall;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.ICallsListPresenter;
import ir.hadinemati.steelmarketing.R;


public class CallsListRecyclerAdapter extends RecyclerView.Adapter<CallsListRecyclerAdapter.CallsViewHolder> {


    List<PotentialCustomerPhoneCall> _list;
    Context context;
    ICallsListPresenter callsListPresenter;
    LayoutInflater _inflater;

    public CallsListRecyclerAdapter(List<PotentialCustomerPhoneCall> _list, Context context, ICallsListPresenter callsListPresenter) {
        this._list = _list;
        this.context = context;
        this.callsListPresenter = callsListPresenter;
        _inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CallsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = _inflater.inflate(R.layout.item_call, parent, false);
        return new CallsViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull CallsViewHolder holder, int position) {
        PotentialCustomerPhoneCall _call = _list.get(position);
        String Gender = _call.Gender + (_call.Gender.equalsIgnoreCase("آقا") ? "ی" : "") ;
        holder.tvName.setText( Gender + " " + _call.Username);
        holder.tvDate.setText(_call.PersianDate);
        holder.tvProduct.setText(_call.Product);
        holder.tvPhone.setText(_call.PhoneNumber);
        holder.btnShowinformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                callsListPresenter.getExtraInformation(_call.getUUID());
            }
        });
    }

    @Override
    public int getItemCount() {
        return _list.size();
    }

    public class CallsViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvProduct, tvDate, tvPhone;
        Button btnShowinformation;

        public CallsViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvPhone = itemView.findViewById(R.id.tvPhoneNumber);
            tvProduct = itemView.findViewById(R.id.tvProduct);
            tvDate = itemView.findViewById(R.id.tvPersianDate);

            btnShowinformation = itemView.findViewById(R.id.btnShowInformation);

        }
    }
}
