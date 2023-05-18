package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.PreguntasFrecuentes;
import com.acevedo.rutaexperienciauc.clases.Sede;

import java.util.List;

public class SoporteSedesAdapter extends RecyclerView.Adapter<SoporteSedesAdapter.ViewHolder>{
    Context context;
    List<Sede> sedeList;


    public SoporteSedesAdapter (Context context, List<Sede> sedeList){
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
        }

        public void setNombre(String Nombre){
            tvNombreSede = view.findViewById(R.id.tvNombreSede);
            tvNombreSede.setText(Nombre);
        }

        public void setAdress(String Adress){
            tvDireccionSede = view.findViewById(R.id.tvDireccionSede);
            tvDireccionSede.setText(Adress);
        }

        public void setSeReferencia(String SeReferencia){
            tvReferenciaSede = view.findViewById(R.id.tvReferenciaSede);
            tvReferenciaSede.setText(SeReferencia);
        }

        public void setSeTelefono(String SeTelefono){
            tvTelefonoSede = view.findViewById(R.id.tvTelefonoSede);
            tvTelefonoSede.setText(SeTelefono);
        }
    }
}
