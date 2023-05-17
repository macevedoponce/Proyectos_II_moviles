package com.acevedo.rutaexperienciauc.ui.ayuda;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.CarreraAdapter;
import com.acevedo.rutaexperienciauc.adapter.PreguntasFrecuentesAdapter;
import com.acevedo.rutaexperienciauc.clases.Carrera;
import com.acevedo.rutaexperienciauc.clases.PreguntasFrecuentes;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.ListaCarrerasActivity;
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

public class AyudaFragment extends Fragment {
    RecyclerView rvPreguntasFrecuentes;
    List<PreguntasFrecuentes> listPreguntasFrecuentes;
    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_ayuda, container, false);
        rvPreguntasFrecuentes = vista.findViewById(R.id.rvPreguntasFrecuentes);
        listPreguntasFrecuentes = new ArrayList<>();
        rvPreguntasFrecuentes.setLayoutManager(new LinearLayoutManager(getContext()));
        requestQueue = Volley.newRequestQueue(getContext());
        cargarPreguntasFrecuentes();

        return vista;
    }

    private void cargarPreguntasFrecuentes() {

        String url = "http://192.168.100.10:3000/preguntasfrecuentes";

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int IdPreguntaFrecuente = jsonObject.getInt("IdPreguntaFrecuente");
                                String PfPregunta = jsonObject.getString("PfPregunta");
                                String PfRespuesta =jsonObject.getString("PfRespuesta");
                                PreguntasFrecuentes preguntasFrecuentes = new PreguntasFrecuentes(IdPreguntaFrecuente, PfPregunta, PfRespuesta);
                                listPreguntasFrecuentes.add(preguntasFrecuentes);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        PreguntasFrecuentesAdapter adapter = new PreguntasFrecuentesAdapter(getContext(),listPreguntasFrecuentes);
//                        adapter.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                selectPregunta();
//                            }
//                        });
                        rvPreguntasFrecuentes.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }
}