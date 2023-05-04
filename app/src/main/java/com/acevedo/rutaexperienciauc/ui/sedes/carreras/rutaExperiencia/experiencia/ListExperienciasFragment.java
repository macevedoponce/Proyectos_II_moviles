package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.ExperienciaAdapter;
import com.acevedo.rutaexperienciauc.clases.Experiencia;
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


public class ListExperienciasFragment extends Fragment {

    RecyclerView rvExperiencias;
    List<Experiencia> listaExperiencia;
    RequestQueue requestQueue;

    ImageView ivCiclo;

    int idCarrera, exCiclo;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        idCarrera = getArguments().getInt("idCarrera");
        exCiclo = getArguments().getInt("exCiclo");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_list_experiencias, container, false);
        ivCiclo = vista.findViewById(R.id.ivCiclo);
        rvExperiencias = vista.findViewById(R.id.rvExperiencias);
        rvExperiencias.setHasFixedSize(true);
        rvExperiencias.setLayoutManager(new LinearLayoutManager(getContext()));
        requestQueue = Volley.newRequestQueue(getContext());
        listaExperiencia = new ArrayList<>();
        cargarExperiencias();

        String cicloName = getCicloName(exCiclo);
        int drawableId = getResources().getIdentifier(cicloName, "drawable",getContext().getPackageName());
        Drawable drawable = getResources().getDrawable(drawableId, null);

        ivCiclo.setImageDrawable(drawable);

        return vista;
    }

    private String getCicloName(int exCiclo) {
        String[] cicloNames = {"ciclo_uno", "ciclo_dos", "ciclo_tres", "ciclo_cuatro", "ciclo_cinco", "ciclo_seis", "ciclo_siete", "ciclo_ocho", "ciclo_nueve", "ciclo_diez", "ciclo_once", "ciclo_doce", "ciclo_trece", "ciclo_catorce"};
        // Restar 1 al número para que se corresponda con el índice del array
        int index = exCiclo - 1;
        if (index >= 0 && index < cicloNames.length) {
            return cicloNames[index];
        } else {
            return "Número fuera de rango";
        }
    }

    private void cargarExperiencias() {
        String url = Util.RUTA_EXPERIENCIA + "/"+ idCarrera + "/" + exCiclo;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int idExperiencia =jsonObject.getInt("IdExperiencia");
                                String ExNombre = jsonObject.getString("ExNombre");
                                String ExIconoUrl =jsonObject.getString("ExIconoUrl");
                                Experiencia experiencia = new Experiencia(idExperiencia, ExNombre, ExIconoUrl);
                                listaExperiencia.add(experiencia);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        ExperienciaAdapter adapter = new ExperienciaAdapter(getContext(),listaExperiencia);
                        adapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                selectExperiencia(v);
                            }
                        });

                        rvExperiencias.setAdapter(adapter);
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

    private void selectExperiencia(View view) {
        int idExperiencia = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getIdExperiencia();
        String iconoUrl = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getExIconoUrl();

        Toast.makeText(getContext(), "urra" + idExperiencia, Toast.LENGTH_SHORT).show();

        //codigo que reemplaza el fragment inicio por el fragment solicitar información
        DetalleExperienciaFragment detalleExperienciaFragment = new DetalleExperienciaFragment(); // inicializa el fragment

        Bundle args = new Bundle();
        args.putInt("idCarrera",idExperiencia);
        args.putString("iconoUrl", iconoUrl);
        detalleExperienciaFragment.setArguments(args);

        getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,detalleExperienciaFragment).addToBackStack(null).commit(); // reemplaza el contenedor del fragment con el nuevo fragment


    }
}