package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;

import java.util.List;

public class SpinnerAdapter extends ArrayAdapter<String> {

    private LayoutInflater inflater;
    private int resource;

    public SpinnerAdapter(Context context, int resource, List<String> items) {
        super(context, resource, items);
        inflater = LayoutInflater.from(context);
        this.resource = resource;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return getCustomView(position, convertView, parent);
    }

    private View getCustomView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.txtItemsSpinner);
        textView.setText(getItem(position));

        return convertView;
    }

}
