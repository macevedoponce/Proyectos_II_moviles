package com.acevedo.rutaexperienciauc.ui.sedes.experiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
    List<ListaRutaExperiencia> items;

    Spinner spCarreras;
    RequestQueue requestQueue;

    int cantidadCiclos;
    int idCarrera;
    String planEstudiosUrl;

    int idSede;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_experiencia);

        spCarreras = findViewById(R.id.spListaCarreras);

        //recibimos los datos enviados de la lista de carreras
        idCarrera = getIntent().getIntExtra("idCarrera", 0);
        cantidadCiclos = getIntent().getIntExtra("cantidadCiclos", 0);
        planEstudiosUrl = getIntent().getStringExtra("planEstudiosUrl");
        idSede = getIntent().getIntExtra("idSede",0);

        requestQueue = Volley.newRequestQueue(this);
        cargarCarreras();

        initView();
        initValues();
    }

    private void cargarCarreras() {
        String url = Util.RUTA_CARRERAS + "/" + idSede;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<String> nombresCarrera = new ArrayList<>();
                        for(int i = 0; i < response.length(); i++){
                            try{
                                JSONObject jsonObject = response.getJSONObject(i);
                                String nombreCarrera = jsonObject.getString("CaNombre");
                                nombresCarrera.add(nombreCarrera);
                            }catch (JSONException e){
                                e.printStackTrace();
                            }
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(RutaExperiencia.this, R.layout.spinner_item, nombresCarrera);
                        adapter.setDropDownViewResource(R.layout.spinner_item);
                        spCarreras.setAdapter(adapter);

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

        int[] images = {R.drawable.ciclo_uno, R.drawable.ciclo_dos, R.drawable.ciclo_tres, R.drawable.ciclo_cuatro, R.drawable.ciclo_cinco, R.drawable.ciclo_seis, R.drawable.ciclo_siete, R.drawable.ciclo_ocho, R.drawable.ciclo_nueve, R.drawable.ciclo_diez};

        List<ListaRutaExperiencia> rutaExperienciaList = new ArrayList<>();
        for (int i = 0; i < cantCiclos; i++) {
            rutaExperienciaList.add(new ListaRutaExperiencia(images[i]));
        }
        return rutaExperienciaList;
    }
}