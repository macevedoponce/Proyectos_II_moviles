package com.acevedo.rutaexperienciauc.ui.sedes.carreras;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.CarreraAdapter;
import com.acevedo.rutaexperienciauc.clases.Carrera;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.RutaExperiencia;
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

public class ListaCarrerasFragment extends Fragment {

    RecyclerView rvCarrerasAll;
    List<Carrera> listaCarrera;
    private List<Carrera> listaFiltrada;
    RequestQueue requestQueue;
    private int sedeId;
    private EditText edtBuscarCarreraNombre;
    private Button btnBuscarCarrera;
    private CarreraAdapter adapter;


    public ListaCarrerasFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_lista_carreras, container, false);
        rvCarrerasAll = vista.findViewById(R.id.rvCarrerasall);
        edtBuscarCarreraNombre = vista.findViewById(R.id.edtBuscarCarreraNombre);
        btnBuscarCarrera = vista.findViewById(R.id.btnBuscarCarrera);
        listaCarrera = new ArrayList<>();

        listaFiltrada = new ArrayList<>();

        btnBuscarCarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String searchText = edtBuscarCarreraNombre.getText().toString().toLowerCase();
                filtrarCarreras(searchText);
            }
        });
//
        rvCarrerasAll.setHasFixedSize(true);
        rvCarrerasAll.setLayoutManager(new LinearLayoutManager(getContext()));
        requestQueue = Volley.newRequestQueue(getContext());

        cargarCarreras();
        return vista;
    }

    private void filtrarCarreras(String texto) {
        listaFiltrada.clear();
        for (Carrera carrera : listaCarrera) {
            if (carrera.getNombre().toLowerCase().contains(texto.toLowerCase())) {
                listaFiltrada.add(carrera);
            }
        }
    }
    private void cargarCarreras() {
        Bundle args = getArguments();
        int idSedeRecibido = 0;
        if (args != null) {
            idSedeRecibido = args.getInt("idSede");
            // Usa el texto como quieras
        }
        String url = Util.RUTA_CARRERAS + "/" + idSedeRecibido;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id =jsonObject.getInt("IdCarrera");
                                String nombre = jsonObject.getString("CaNombre");
                                String descripcion =jsonObject.getString("CaDescripcion");
                                String planEstudiosUrl = jsonObject.getString("CaPlanEstudiosUrl");
                                int cantidadCiclos = jsonObject.getInt("CaCantidadCiclos");
                                int idSede =jsonObject.getInt("IdSede");
                                Carrera carrera = new Carrera(id, nombre, descripcion, planEstudiosUrl, cantidadCiclos,idSede);
                                listaCarrera.add(carrera);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }



                        CarreraAdapter adapter = new CarreraAdapter(getContext(),listaCarrera);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectCarrera(view);
                            }
                        });

                        rvCarrerasAll.setAdapter(adapter);
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

    private void selectCarrera(View view) {
        int id = listaCarrera.get(rvCarrerasAll.getChildAdapterPosition(view)).getId();
        int cantidadCiclos = listaCarrera.get(rvCarrerasAll.getChildAdapterPosition(view)).getCantidadCiclos();
        String nombre = listaCarrera.get(rvCarrerasAll.getChildAdapterPosition(view)).getNombre();

        Intent i = new Intent(getContext(), RutaExperiencia.class);
        i.putExtra("idCarrera",id);
        i.putExtra("cantidadCiclos", cantidadCiclos);
        startActivity(i);
    }


}