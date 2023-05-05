package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.Carrera;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CarreraAdapter extends RecyclerView.Adapter<CarreraAdapter.CarreraHolder> implements View.OnClickListener{
    Context context;
    List<Carrera> carreraList;
//    List<Carrera> carreraListCopy;
    View.OnClickListener listener;


    public CarreraAdapter(Context context, List<Carrera> carreraList) {
        this.context = context;
        this.carreraList = carreraList;
//        this.carreraListCopy = new ArrayList<>(carreraList);
    }


    @NonNull
    @Override
    public CarreraHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.carrera_item, parent, false);
        mView.setOnClickListener(this);
        return new CarreraHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull CarreraHolder holder, int position) {
        Carrera carrera = carreraList.get(position);
        holder.setNombre(carrera.getNombre());
    }

    @Override
    public int getItemCount() {
        return carreraList.size();
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


    public class CarreraHolder extends RecyclerView.ViewHolder {

        TextView tvCarreraName;

        View view;

        public CarreraHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setNombre(String nombre){
            tvCarreraName = view.findViewById(R.id.tvCarreraName);
            tvCarreraName.setText(nombre);
        }
    }
}
