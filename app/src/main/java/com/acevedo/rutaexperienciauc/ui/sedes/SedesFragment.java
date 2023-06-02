package com.acevedo.rutaexperienciauc.ui.sedes;


import android.app.ProgressDialog;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.SedeAdapter;
import com.acevedo.rutaexperienciauc.clases.Sede;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.ListaCarrerasActivity;
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


public class SedesFragment extends Fragment {

    RecyclerView rvSedesAll;
    List<Sede> listaSede;
    RequestQueue requestQueue;

    ProgressDialog progreso;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_sedes, container, false);
        rvSedesAll = vista.findViewById(R.id.rvSedesAll);
        rvSedesAll.setHasFixedSize(true);
        rvSedesAll.setLayoutManager(new LinearLayoutManager(getContext()));
        requestQueue = Volley.newRequestQueue(getContext());
        listaSede = new ArrayList<>();
        cargarSedes();
        return vista;
    }


    private void cargarSedes() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Buscando Sedes");
        progreso.setCancelable(false);
        progreso.show();

        String url = Util.RUTA_SEDE;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progreso.dismiss();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id =jsonObject.getInt("IdSede");
                                String nombre = jsonObject.getString("SeNombre");
                                String adress =jsonObject.getString("SeDireccion");
                                String referencia = jsonObject.getString("SeReferencia");
                                String telefono = jsonObject.getString("SeTelefono");
                                String image_url = jsonObject.getString("SeUrlImagen");
                                Sede sede = new Sede(id, nombre, adress,referencia,telefono, image_url);
                                listaSede.add(sede);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SedeAdapter adapter = new SedeAdapter(getContext(),listaSede);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectSede(view);
                            }
                        });

                        rvSedesAll.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progreso.hide();
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }

    private void selectSede(View view) {
        int id = listaSede.get(rvSedesAll.getChildAdapterPosition(view)).getId();
        Intent i = new Intent(getContext(), ListaCarrerasActivity.class);
        i.putExtra("idSede",id);
        startActivity(i);
    }
}