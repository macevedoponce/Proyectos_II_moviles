package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
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
        if (convertView == null) {
            convertView = inflater.inflate(resource, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.txtItemsSpinner);
        String nombreCarrera = getItem(position);

        int cantidadMaximaLetras = 30; // Cantidad máxima de letras que deseas mostrar
        String textoSeleccionado;

        if (nombreCarrera.length() > cantidadMaximaLetras) {
            String subcadena = nombreCarrera.substring(0, cantidadMaximaLetras);
            textoSeleccionado = subcadena + "...";
        } else {
            textoSeleccionado = nombreCarrera;
        }

        SpannableString spannableString = new SpannableString(textoSeleccionado);
        spannableString.setSpan(new ForegroundColorSpan(Color.BLACK), 0, textoSeleccionado.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textView.setText(spannableString);

        return convertView;
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
        String nombreCarrera = getItem(position);
        textView.setText(getItem(position));

        return convertView;
    }

}
