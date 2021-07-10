package ir.hadinemati.steelmarketing.Lib.Adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ir.hadinemati.steelmarketing.Lib.TypeFaceHelper;

public class ProductsAdapter extends ArrayAdapter<String> {

    Context context;

    public ProductsAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.context = context;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position,convertView , parent);
        view.setTypeface(TypeFaceHelper.getPersianTypeFace(this.context));
        return  view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        TextView view = (TextView) super.getView(position,convertView , parent);
        view.setTypeface(TypeFaceHelper.getPersianTypeFace(this.context));
        return  view;
    }
}
