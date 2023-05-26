package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.Sede;
import com.bumptech.glide.Glide;

import java.util.List;

public class SliderSedeAdapter extends RecyclerView.Adapter<SliderSedeAdapter.SedeHolder> implements View.OnClickListener {

    Context context;
    List<Sede> sedeList;
    View.OnClickListener listener;


    public SliderSedeAdapter(Context context, List<Sede> sedeList) {
        this.context = context;
        this.sedeList = sedeList;
    }


    @NonNull
    @Override
    public SedeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.slider_item, parent, false);
        mView.setOnClickListener(this);
        return new SedeHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull SedeHolder holder, int position) {
        Sede sede = sedeList.get(position);
        holder.setImage_ruta(sede.getImagen_url());
        holder.setNombre(sede.getNombre());

    }

    @Override
    public int getItemCount() {
        return sedeList.size();
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


    public class SedeHolder extends RecyclerView.ViewHolder {

        ImageView ivSede;
        TextView tvSedeName;

        View view;

        public SedeHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setImage_ruta(String imagen_ruta){
            ivSede = view.findViewById(R.id.ivSede);
            Glide.with(context).load(imagen_ruta).into(ivSede);

        }

        public void setNombre(String nombre){
            tvSedeName = view.findViewById(R.id.tvSedeName);
            tvSedeName.setText(nombre);
        }
    }
}
