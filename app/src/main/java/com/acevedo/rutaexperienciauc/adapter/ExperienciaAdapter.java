package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.Experiencia;
import com.bumptech.glide.Glide;

import java.util.List;

public class ExperienciaAdapter extends RecyclerView.Adapter<ExperienciaAdapter.ExperienciaHolder> implements View.OnClickListener{

    Context context;
    List<Experiencia> experienciaList;

    View.OnClickListener listener;

    public ExperienciaAdapter(Context context,  List<Experiencia> experienciaList){
        this.context = context;
        this.experienciaList = experienciaList;
    }

    @NonNull
    @Override
    public ExperienciaHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.experiencia_item, parent, false);
        mView.setOnClickListener(this);
        return new ExperienciaHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ExperienciaAdapter.ExperienciaHolder holder, int position) {
        Experiencia experiencia = experienciaList.get(position);
        holder.setIcono(experiencia.getExIconoUrl());
        //holder.setNombre(experiencia.getExNombre());
    }

    @Override
    public int getItemCount() {
        return experienciaList.size();
    }

    public void setOnClickListener(View.OnClickListener listener){
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if(listener != null){
            listener.onClick(v);
        }
    }
    public class ExperienciaHolder extends RecyclerView.ViewHolder {

        ImageView ivIcono;
        TextView tvNombre;
        View view;
        public ExperienciaHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setIcono(String iconoUrl) {
            ivIcono = view.findViewById(R.id.ivIcono);
            Glide.with(context).load(iconoUrl).into(ivIcono);
        }

//        public void setNombre(String nombre){
//            tvNombre = view.findViewById(R.id.tvNombre);
//            tvNombre.setText(nombre);
//        }
    }
}
