package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.ListaRutaExperienciaAdapter;
import com.acevedo.rutaexperienciauc.clases.Carrera;
import com.acevedo.rutaexperienciauc.clases.ListaRutaExperiencia;
import com.acevedo.rutaexperienciauc.ui.solicitarInformacion.SolicitarInformacionActivity;
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
    ArrayAdapter<String> spinnerAdapter;
    List<ListaRutaExperiencia> items;

    Spinner spCarreras;
    RequestQueue requestQueue;

    int cantidadCiclos;
    int idCarrera;
    String planEstudiosUrl;

    int idSede;

    LinearLayout llVolver;

    Button btnPlanEstudios;

    //spinner
    final List<Integer> idsCarrera = new ArrayList<>();
    final List<Integer> cantCiclosCarrera = new ArrayList<>();
    List<String> nombresCarrera = new ArrayList<>();
    boolean spinnerClicked = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_experiencia);

        spCarreras = findViewById(R.id.spListaCarreras);
        llVolver = findViewById(R.id.llVolver);
        btnPlanEstudios = findViewById(R.id.btnPlanEstudios);

        //recibimos los datos enviados de la lista de carreras
        idCarrera = getIntent().getIntExtra("idCarrera", 0);
        cantidadCiclos = getIntent().getIntExtra("cantidadCiclos", 0);
        planEstudiosUrl = getIntent().getStringExtra("planEstudiosUrl");
        idSede = getIntent().getIntExtra("idSede",0);

        requestQueue = Volley.newRequestQueue(this);

        nombresCarrera.add(0, "Selecciona una carrera");

        cargarCarreras();

        // Crear adaptador del spinner con la lista actualizada de nombres de carreras
        spinnerAdapter = new ArrayAdapter<>(RutaExperiencia.this, R.layout.spinner_item, nombresCarrera);
        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
        spCarreras.setAdapter(spinnerAdapter);

        spCarreras.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //obteniendo id carrera seleccionada
                if (i == 0) {
                    return;
                }
                idCarrera = idsCarrera.get(i-1);
                //obteniendo cantidad de ciclos de la carrera seleccioanda
                cantidadCiclos = cantCiclosCarrera.get(i-1);
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
//                Toast.makeText(RutaExperiencia.this, "plan" + planEstudiosUrl, Toast.LENGTH_SHORT).show();
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
                                idsCarrera.add(id);
                                cantCiclosCarrera.add(cantidadCiclos);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
//                        spinnerAdapter = new ArrayAdapter<>(RutaExperiencia.this, R.layout.spinner_item, nombresCarrera);
//                        spinnerAdapter.setDropDownViewResource(R.layout.spinner_item);
//                        spCarreras.setAdapter(spinnerAdapter);
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

        int[] images = {R.drawable.ciclo_uno, R.drawable.ciclo_dos, R.drawable.ciclo_tres, R.drawable.ciclo_cuatro, R.drawable.ciclo_cinco, R.drawable.ciclo_seis, R.drawable.ciclo_siete, R.drawable.ciclo_ocho, R.drawable.ciclo_nueve, R.drawable.ciclo_diez, R.drawable.ciclo_uno, R.drawable.ciclo_dos, R.drawable.ciclo_tres, R.drawable.ciclo_cuatro};

        List<ListaRutaExperiencia> rutaExperienciaList = new ArrayList<>();
        for (int i = 0; i < cantCiclos; i++) {
            rutaExperienciaList.add(new ListaRutaExperiencia(images[i]));
        }
        return rutaExperienciaList;
    }
}