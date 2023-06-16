package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.ListaRutaExperienciaAdapter;
import com.acevedo.rutaexperienciauc.adapter.SpinnerAdapter;
import com.acevedo.rutaexperienciauc.clases.ListaRutaExperiencia;
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

public class RutaExperiencia extends AppCompatActivity {
    RecyclerView rvListaRutaExperiencia;
    ListaRutaExperienciaAdapter adapter;
    SpinnerAdapter spinnerAdapter;
    List<ListaRutaExperiencia> items;

    Spinner spCarreras;
    RequestQueue requestQueue;

    int cantidadCiclos;
    int idCarrera;
    String planEstudiosUrl, nombreCarrera;

    int idSede;

    LinearLayout llVolver;

    Button btnPlanEstudios;

    //spinner
    final List<Integer> idsCarrera = new ArrayList<>();
    final List<Integer> cantCiclosCarrera = new ArrayList<>();
    final List<String> planEstudiosUrls = new ArrayList<>();
    List<String> nombresCarrera = new ArrayList<>();

    TextView tvNombreCarrera;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_experiencia);

        spCarreras = findViewById(R.id.spListaCarreras);
        llVolver = findViewById(R.id.llVolver);
        btnPlanEstudios = findViewById(R.id.btnPlanEstudios);
        tvNombreCarrera = findViewById(R.id.tvNombreCarrera);

        //recibimos los datos enviados de la lista de carreras
        idCarrera = getIntent().getIntExtra("idCarrera", 0);
        cantidadCiclos = getIntent().getIntExtra("cantidadCiclos", 0);
        planEstudiosUrl = getIntent().getStringExtra("planEstudiosUrl");
        idSede = getIntent().getIntExtra("idSede",0);
        nombreCarrera = getIntent().getStringExtra("nombreCarrera");

        tvNombreCarrera.setText(nombreCarrera);

        requestQueue = Volley.newRequestQueue(this);

        nombresCarrera.add(0, "Selecciona una carrera");

        cargarCarreras();

        // Crear adaptador del spinner con la lista actualizada de nombres de carreras
        spinnerAdapter =new SpinnerAdapter(this, R.layout.spinner_item, nombresCarrera);
        spCarreras.setAdapter(spinnerAdapter);

        spCarreras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //obteniendo id carrera seleccionada
                if (i == 0) {
                    return;
                }
                //obteniendo el id de la carera seleccionada
                idCarrera = idsCarrera.get(i-1);
                //obteniendo cantidad de ciclos de la carrera seleccioanda
                cantidadCiclos = cantCiclosCarrera.get(i-1);
                //obteniendo el url de la carrera seleccionada
                planEstudiosUrl = planEstudiosUrls.get(i-1);
                //obteniendo el nombre de la carrera seleccionada
                nombreCarrera = nombresCarrera.get(i);
                tvNombreCarrera.setText(nombreCarrera);
                items = getItems(cantidadCiclos);
                adapter = new ListaRutaExperienciaAdapter(items,idCarrera);
                rvListaRutaExperiencia.setAdapter(adapter);

            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        initView();
        initValues();

        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        btnPlanEstudios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RutaExperiencia.this, PlanEstudiosActivity.class);
                intent.putExtra("planEstudiosUrl", planEstudiosUrl);
                startActivity(intent);
            }
        });
    }

    private void cargarCarreras() {
        String url = Util.RUTA_CARRERAS + "/" + idSede;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for(int i = 0; i < response.length(); i++){
                            try{
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id =jsonObject.getInt("IdCarrera");
                                String nombreCarrera = jsonObject.getString("CaNombre");
                                nombresCarrera.add(nombreCarrera);
                                int cantidadCiclos = jsonObject.getInt("CaCantidadCiclos");
                                String planEstudiosUrl = jsonObject.getString("CaPlanEstudiosUrl");
                                planEstudiosUrls.add(planEstudiosUrl);
                                idsCarrera.add(id);
                                cantCiclosCarrera.add(cantidadCiclos);

                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    private void initView(){
        rvListaRutaExperiencia = findViewById(R.id.rvListaRutaExperiencia);
    }
    private void initValues(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvListaRutaExperiencia.setLayoutManager(manager);
        items = getItems(cantidadCiclos);
        adapter = new ListaRutaExperienciaAdapter(items, idCarrera);
        rvListaRutaExperiencia.setAdapter(adapter);
    }
    private List<ListaRutaExperiencia> getItems(int cantCiclos){

        int[] images = {R.drawable.ciclo_uno, R.drawable.ciclo_dos, R.drawable.ciclo_tres, R.drawable.ciclo_cuatro, R.drawable.ciclo_cinco, R.drawable.ciclo_seis, R.drawable.ciclo_siete, R.drawable.ciclo_ocho, R.drawable.ciclo_nueve, R.drawable.ciclo_diez, R.drawable.ciclo_once, R.drawable.ciclo_doce, R.drawable.ciclo_trece, R.drawable.ciclo_catorce};

        List<ListaRutaExperiencia> rutaExperienciaList = new ArrayList<>();
        for (int i = 0; i < cantCiclos; i++) {
            rutaExperienciaList.add(new ListaRutaExperiencia(images[i]));
        }
        return rutaExperienciaList;
    }
}