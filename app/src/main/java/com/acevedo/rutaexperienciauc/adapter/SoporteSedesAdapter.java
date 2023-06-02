package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.PreguntasFrecuentes;
import com.acevedo.rutaexperienciauc.clases.Sede;

import java.util.ArrayList;
import java.util.List;

public class SoporteSedesAdapter extends RecyclerView.Adapter<SoporteSedesAdapter.ViewHolder>{
    Context context;
    List<Sede> sedeList;


    public SoporteSedesAdapter (Context context,List<Sede> sedeList){
        this.context = context;
        this.sedeList = sedeList;
    }

    @NonNull
    @Override
    public SoporteSedesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(context).inflate(R.layout.soporte_sedes_item, parent, false);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SoporteSedesAdapter.ViewHolder holder, int position) {
        Sede sede = sedeList.get(position);
        holder.setNombre(sede.getNombre());
        holder.setAdress(sede.getAdress());
        holder.setSeReferencia(sede.getSeReferencia());
        holder.setSeTelefono(sede.getSeTelefono());
    }

    @Override
    public int getItemCount() {
        return sedeList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNombreSede, tvDireccionSede,tvReferenciaSede, tvTelefonoSede;

        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvNombreSede = view.findViewById(R.id.tvNombreSede);
            tvDireccionSede = view.findViewById(R.id.tvDireccionSede);
            tvReferenciaSede = view.findViewById(R.id.tvReferenciaSede);
            tvTelefonoSede = view.findViewById(R.id.tvTelefonoSede);

            //telefono
            tvTelefonoSede.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        String telefono = sedeList.get(position).getSeTelefono();
                        llamarTelefono(telefono);
                    }
                }
            });
            //maps
            tvNombreSede.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION) {
                        Sede sede = sedeList.get(position);
                        String direccion = sede.getAdress();
                        abrirGoogleMaps(direccion);
                    }
                }
            });

        }

        public void setNombre(String Nombre){
            tvNombreSede.setText(Nombre);
        }

        public void setAdress(String Adress){
            tvDireccionSede.setText(Adress);
        }

        public void setSeReferencia(String SeReferencia){
            tvReferenciaSede.setText(SeReferencia);
        }

        public void setSeTelefono(String SeTelefono){
            tvTelefonoSede.setText(SeTelefono);
            tvTelefonoSede.setPaintFlags(tvTelefonoSede.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        }

    }
    private void llamarTelefono(String telefono) {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + telefono));
        context.startActivity(intent);
    }

    private void abrirGoogleMaps(String direccion) {
        Uri gmmIntentUri = Uri.parse("geo:0,0?q=" + Uri.encode(direccion));
        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
        mapIntent.setPackage("com.google.android.apps.maps");
        context.startActivity(mapIntent);
    }
}
