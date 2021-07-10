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

import ir.hadinemati.steelmarketing.Models.Entity.ContactDO;
import ir.hadinemati.steelmarketing.Presenters.Interfaces.ISettingsPresenter;
import ir.hadinemati.steelmarketing.R;

public class IgnoreContactListRecyclerAdapter extends RecyclerView.Adapter<IgnoreContactListRecyclerAdapter.IgnoredContactViewHolder> {

    List<ContactDO> contactDOList;
    Context context;
    LayoutInflater layoutInflater;
    ISettingsPresenter settingsPresenter;
    public IgnoreContactListRecyclerAdapter(List<ContactDO> contactDOList, Context context , ISettingsPresenter presenter) {
        this.contactDOList = contactDOList;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        settingsPresenter = presenter;
    }

    @NonNull
    @Override
    public IgnoredContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = layoutInflater.inflate(R.layout.item_ignored_contact , parent,false);
        return new IgnoredContactViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull IgnoredContactViewHolder holder, int position) {
            holder.tvName.setText(contactDOList.get(position).getName());
            holder.tvNumber.setText(contactDOList.get(position).getPhoneNumber());
    }

    @Override
    public int getItemCount() {
        return contactDOList.size();
    }

    public class IgnoredContactViewHolder extends RecyclerView.ViewHolder{
        TextView tvName ,tvNumber;
        ImageButton IBDelete;

        public IgnoredContactViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.ItemIgnoredContactTvName);
            tvNumber = itemView.findViewById(R.id.ItemIgnoredContactTvNumber);
            IBDelete = itemView.findViewById(R.id.ItemIgnoredContactIBDelete);


            IBDelete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    settingsPresenter.RemoveIgnoredContact(tvNumber.getText().toString());
                }
            });
        }
    }
}
