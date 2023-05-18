package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.ListaRutaExperiencia;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia.ListExperienciasActivity;

import java.util.List;

public class ListaRutaExperienciaAdapter extends RecyclerView.Adapter<ListaRutaExperienciaAdapter.ViewHolder> {
    private List<ListaRutaExperiencia> ListaRuta;

    private int idCarrera;

    public ListaRutaExperienciaAdapter(List<ListaRutaExperiencia> itemList, int idCarrera){
        this.ListaRuta = itemList;
        this.idCarrera = idCarrera;
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
        holder.setOnClickListener(position);
    }

    @Override
    public int getItemCount(){ return ListaRuta.size();}

    public void setItems(List<ListaRutaExperiencia> items){ListaRuta = items;}


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        ImageView ivCiclo;

        Context context;
        Button btnExperienciaAleatoria, btnExperienciaMasInfo;


        ViewHolder(@NonNull View itemView){
            super(itemView);
            context = itemView.getContext();
            ivCiclo = itemView.findViewById(R.id.ivciclo);
            btnExperienciaAleatoria = itemView.findViewById(R.id.btnExperienciaAleatoria);
            btnExperienciaMasInfo = itemView.findViewById(R.id.btnExperienciaMasInfo);
        }
        void setOnClickListener(int position){
            btnExperienciaMasInfo.setTag(position);
            btnExperienciaAleatoria.setTag(position);
            btnExperienciaAleatoria.setOnClickListener(this);
            btnExperienciaMasInfo.setOnClickListener(this);
        }
        @Override
        public void onClick(View view){
            int position = (int) view.getTag() +1;
            switch (view.getId()){
                case R.id.btnExperienciaAleatoria:
                    Toast.makeText(context, "idCarrera: "+idCarrera +"idCiclo: "+position, Toast.LENGTH_SHORT).show();
                    break;
                case R.id.btnExperienciaMasInfo:
                    Intent intent = new Intent(context, ListExperienciasActivity.class);
                    intent.putExtra("idCarrera", idCarrera);
                    intent.putExtra("idCiclo", position);
                    context.startActivity(intent);
                    break;
            }
        }
    }
}