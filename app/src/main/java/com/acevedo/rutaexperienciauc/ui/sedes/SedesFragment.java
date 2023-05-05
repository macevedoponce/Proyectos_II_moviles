package com.acevedo.rutaexperienciauc.ui.sedes;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.SedeAdapter;
import com.acevedo.rutaexperienciauc.clases.Sede;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.ListaCarrerasFragment;
import com.acevedo.rutaexperienciauc.util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class SedesFragment extends Fragment {

    RecyclerView rvSedesAll;
    List<Sede> listaSede;
    RequestQueue requestQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
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
        String url = Util.RUTA_SEDE;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<SlideModel> slideModels = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id =jsonObject.getInt("IdSede");
                                String nombre = jsonObject.getString("SeNombre");
                                String adress =jsonObject.getString("SeDireccion");
                                String image_url = jsonObject.getString("SeUrlImagen");
                                Sede sede = new Sede(id, nombre, adress, image_url);
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
                error.printStackTrace();
            }
        });
        requestQueue.add(request);

    }

    private void selectSede(View view) {
        int id = listaSede.get(rvSedesAll.getChildAdapterPosition(view)).getId();
        String nombre = listaSede.get(rvSedesAll.getChildAdapterPosition(view)).getNombre();

      //  Toast.makeText(getContext(), id+"", Toast.LENGTH_SHORT).show();

//        Intent i = new Intent(getContext(), ListaCarrerasFragment.class);
//        i.putExtra("sede_id",id);
//        startActivity(i);
        //codigo que reemplaza el fragment inicio por el fragment solicitar información
        ListaCarrerasFragment listaCarrerasFragment = new ListaCarrerasFragment(); // inicializa el fragment
        Bundle args = new Bundle();
        args.putInt("idSede",id);
        listaCarrerasFragment.setArguments(args);

        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,listaCarrerasFragment).addToBackStack(null).commit();
    }
}