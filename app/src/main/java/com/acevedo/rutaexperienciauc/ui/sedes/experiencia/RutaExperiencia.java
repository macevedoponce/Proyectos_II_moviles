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
        items = getItems();
        adapter = new ListaRutaExperienciaAdapter(items);
        rvListaRutaExperiencia.setAdapter(adapter);

    }
    private List<ListaRutaExperiencia> getItems(){
        List<ListaRutaExperiencia> itemList = new ArrayList<>();
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_uno));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_dos));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_tres));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_cuatro));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_cinco));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_seis));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_siete));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_ocho));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_nueve));
        itemList.add(new ListaRutaExperiencia(R.drawable.ciclo_diez));
        return itemList;
    }
}