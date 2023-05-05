package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.ListaRutaExperiencia;

import java.util.List;

public class ListaRutaExperienciaAdapter extends RecyclerView.Adapter<ListaRutaExperienciaAdapter.ViewHolder> {
    private List<ListaRutaExperiencia> ListaRuta;

    public ListaRutaExperienciaAdapter(List<ListaRutaExperiencia> itemList){
        this.ListaRuta = itemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ruta_experiencia_item,parent,false);
        return new  ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position){
        ListaRutaExperiencia item = ListaRuta.get(position);
        holder.ivCiclo.setImageResource(item.getImgCiclos());
    }

    @Override
    public int getItemCount(){ return ListaRuta.size();}

    public void setItems(List<ListaRutaExperiencia> items){ListaRuta = items;}

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivCiclo;
        ViewHolder(@NonNull View itemView){
            super(itemView);
            ivCiclo = itemView.findViewById(R.id.ivciclo);
        }
    }
}
