package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.ExperienciaAdapter;
import com.acevedo.rutaexperienciauc.clases.Experiencia;
import com.acevedo.rutaexperienciauc.clases.ListaRutaExperiencia;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia.DetalleExperienciaActivity;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia.ListExperienciasActivity;
import com.acevedo.rutaexperienciauc.util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListaRutaExperienciaAdapter extends RecyclerView.Adapter<ListaRutaExperienciaAdapter.ViewHolder> {
    private List<ListaRutaExperiencia> listaRuta;
    private int idCarrera;
    private Context context;
    private RequestQueue requestQueue;

    public ListaRutaExperienciaAdapter(List<ListaRutaExperiencia> itemList, int idCarrera, Context context) {
        this.listaRuta = itemList;
        this.idCarrera = idCarrera;
        this.context = context;
        requestQueue = Volley.newRequestQueue(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.ruta_experiencia_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ListaRutaExperiencia item = listaRuta.get(position);
        holder.ivCiclo.setImageResource(item.getImgCiclos());
        holder.cargarExperiencias(position);
    }

    @Override
    public int getItemCount() {
        return listaRuta.size();
    }

    public void setItems(List<ListaRutaExperiencia> items) {
        listaRuta = items;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView ivCiclo;
        RecyclerView rvExperiencias;
        List<Experiencia> listaExperiencia;
        ExperienciaAdapter experienciaAdapter;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivCiclo = itemView.findViewById(R.id.ivciclo);
            rvExperiencias = itemView.findViewById(R.id.rvExperiencias);
            rvExperiencias.setHasFixedSize(true);
            rvExperiencias.setLayoutManager(new GridLayoutManager(context, 2));
            listaExperiencia = new ArrayList<>();
        }

        public void cargarExperiencias(int position) {
            int ciclo = position + 1;

            String url = Util.RUTA_EXPERIENCIA + "/" + idCarrera + "/" + ciclo + "/" + ciclo;
            JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                @Override
                public void onResponse(JSONArray response) {
                    listaExperiencia.clear();
                    for (int i = 0; i < response.length(); i++) {
                        try {
                            JSONObject jsonObject = response.getJSONObject(i);
                            int idExperiencia = jsonObject.getInt("IdExperiencia");
                            String exNombre = jsonObject.getString("ExNombre");
                            String exIconoUrl = jsonObject.getString("ExIconoUrl");
                            Experiencia experiencia = new Experiencia(idExperiencia, exNombre, exIconoUrl);
                            listaExperiencia.add(experiencia);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    experienciaAdapter = new ExperienciaAdapter(context, listaExperiencia);
                    experienciaAdapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            selectExperiencia(v);
                        }
                    });

                    rvExperiencias.setAdapter(experienciaAdapter);
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            requestQueue.add(request);
        }

        private void selectExperiencia(View view) {
            int idExperiencia = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getIdExperiencia();
            Intent intent = new Intent(context, DetalleExperienciaActivity.class);
            intent.putExtra("idExperiencia", idExperiencia);
            context.startActivity(intent);
        }
    }
}