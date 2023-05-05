package com.acevedo.rutaexperienciauc.ui.sedes.experiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.ListaRutaExperienciaAdapter;
import com.acevedo.rutaexperienciauc.clases.ListaRutaExperiencia;

import java.util.ArrayList;
import java.util.List;

public class RutaExperiencia extends AppCompatActivity {
    RecyclerView rvListaRutaExperiencia;
    ListaRutaExperienciaAdapter adapter;
    List<ListaRutaExperiencia> items;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ruta_experiencia);

        initView();
        initValues();

    }

    private void initView(){
        rvListaRutaExperiencia = findViewById(R.id.rvListaRutaExperiencia);
    }
    private void initValues(){
        LinearLayoutManager manager = new LinearLayoutManager(this);
        rvListaRutaExperiencia.setLayoutManager(manager);
        int cantidadCiclos = getIntent().getIntExtra("cantidadCiclos",0);
        items = getItems(cantidadCiclos);
        adapter = new ListaRutaExperienciaAdapter(items);
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