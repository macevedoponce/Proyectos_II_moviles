package com.acevedo.rutaexperienciauc.ui.sedes.carreras;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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

public class ListaCarrerasActivity extends AppCompatActivity {

    RecyclerView rvCarrerasAll;
    List<Carrera> listaCarrera;
    RequestQueue requestQueue;
    LinearLayout llVolver;
    private EditText edtBuscarCarreraNombre;
    private Button btnBuscarCarrera;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_carreras);
        rvCarrerasAll = findViewById(R.id.rvCarrerasall);
        edtBuscarCarreraNombre = findViewById(R.id.edtBuscarCarreraNombre);
        btnBuscarCarrera = findViewById(R.id.btnBuscarCarrera);
        listaCarrera = new ArrayList<>();
        rvCarrerasAll.setHasFixedSize(true);
        rvCarrerasAll.setLayoutManager(new LinearLayoutManager(this));
        requestQueue = Volley.newRequestQueue(this);
        llVolver = findViewById(R.id.llVolver);

        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cargarCarreras();

        btnBuscarCarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el texto del EditText y filtrar la lista de carreras
                String query = edtBuscarCarreraNombre.getText().toString().toLowerCase();
                List<Carrera> filteredList = new ArrayList<>();
                for (Carrera carrera : listaCarrera) {
                    String nombre = carrera.getNombre().toLowerCase();
                    if (nombre.contains(query)) {
                        filteredList.add(carrera);
                    }
                }
                // Actualizar el RecyclerView con la lista filtrada o mostrar un Toast si no hay resultados
                if (filteredList.isEmpty()) {
                    Toast.makeText(ListaCarrerasActivity.this, "No se encontraron carreras", Toast.LENGTH_SHORT).show();
                } else {
                    CarreraAdapter adapter = new CarreraAdapter(ListaCarrerasActivity.this, filteredList);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            selectCarrera(view);
                        }
                    });
                    rvCarrerasAll.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                }
            }
        });

        setUpSearch();
    }

    private void cargarCarreras() {

        int idSedeRecibido = getIntent().getIntExtra("idSede",0);

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

                        CarreraAdapter adapter = new CarreraAdapter(ListaCarrerasActivity.this,listaCarrera);
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
    private void setUpSearch() {
        edtBuscarCarreraNombre.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                String query = charSequence.toString().toLowerCase();
                List<Carrera> filteredList = new ArrayList<>();
                for (Carrera carrera : listaCarrera) {
                    String nombre = carrera.getNombre().toLowerCase();
                    if (nombre.contains(query)) {
                        filteredList.add(carrera);
                    }
                }
                // Actualizar el RecyclerView con la lista filtrada
                CarreraAdapter adapter = new CarreraAdapter(ListaCarrerasActivity.this, filteredList);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        selectCarrera(view);
                    }
                });
                rvCarrerasAll.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }
    private void selectCarrera(View view) {
        int id = listaCarrera.get(rvCarrerasAll.getChildAdapterPosition(view)).getId();
        int cantidadCiclos = listaCarrera.get(rvCarrerasAll.getChildAdapterPosition(view)).getCantidadCiclos();
        int idSede = listaCarrera.get(rvCarrerasAll.getChildAdapterPosition(view)).getIdSede();
        String planEstudiosUrl = listaCarrera.get(rvCarrerasAll.getChildAdapterPosition(view)).getPlanEstudios_url();
        String nombre = listaCarrera.get(rvCarrerasAll.getChildAdapterPosition(view)).getNombre();

        Intent i = new Intent(this, RutaExperiencia.class);
        i.putExtra("idCarrera",id);
        i.putExtra("cantidadCiclos", cantidadCiclos);
        i.putExtra("idSede", idSede);
        i.putExtra("planEstudiosUrl", planEstudiosUrl);

        startActivity(i);

    }
}