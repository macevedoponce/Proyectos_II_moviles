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

import java.util.List;

public class PreguntasFrecuentesAdapter extends RecyclerView.Adapter<PreguntasFrecuentesAdapter.ViewHolder> implements View.OnClickListener {
    Context context;
    List<PreguntasFrecuentes>  preguntasFrecuentesList;
    View.OnClickListener listener;

    public PreguntasFrecuentesAdapter (Context context, List<PreguntasFrecuentes> preguntasFrecuentesList){
        this.context = context;
        this.preguntasFrecuentesList = preguntasFrecuentesList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        View mView = LayoutInflater.from(context).inflate(R.layout.preguntas_frecuentes_item, parent, false);
        mView.setOnClickListener(this);
        return new ViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        PreguntasFrecuentes preguntasFrecuentes = preguntasFrecuentesList.get(position);
        holder.setPfPregunta(preguntasFrecuentes.getPfPregunta());
        holder.setPfRespuesta(preguntasFrecuentes.getPfRespuesta());

    }

    @Override
    public int getItemCount() {
        return preguntasFrecuentesList.size();
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

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvPreguntaFrecuente, tvRespuestaPregunta;

        View view;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }

        public void setPfPregunta(String PfPregunta){
            tvPreguntaFrecuente = view.findViewById(R.id.tvPreguntaFrecuente);
            tvPreguntaFrecuente.setText(PfPregunta);
        }

        public void setPfRespuesta(String PfRespuesta){
            tvRespuestaPregunta = view.findViewById(R.id.tvRespuestaPregunta);
            tvRespuestaPregunta.setText(PfRespuesta);
        }
    }
}
