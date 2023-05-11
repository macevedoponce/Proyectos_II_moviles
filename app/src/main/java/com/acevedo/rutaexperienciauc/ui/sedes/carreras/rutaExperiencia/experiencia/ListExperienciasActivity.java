package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

public class ListExperienciasActivity extends AppCompatActivity {


    RecyclerView rvExperiencias;
    List<Experiencia> listaExperiencia;
    RequestQueue requestQueue;

    ImageView ivCiclo;
    LinearLayout llVolver;

    int idCarrera, exCiclo;

    ProgressDialog progreso;

    //progressBeneficio
    ProgressBar pbBeneficio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_experiencias);

        idCarrera = getIntent().getIntExtra("idCarrera",0);
        exCiclo = getIntent().getIntExtra("idCiclo",0);


        llVolver = findViewById(R.id.llVolver);
        ivCiclo = findViewById(R.id.ivCiclo);
        rvExperiencias = findViewById(R.id.rvExperiencias);
        //pbBeneficio = findViewById(R.id.pbBeneficio);
        rvExperiencias.setHasFixedSize(true);
        rvExperiencias.setLayoutManager(new GridLayoutManager(this,2));
        requestQueue = Volley.newRequestQueue(this);
        listaExperiencia = new ArrayList<>();
        cargarExperiencias();

        //animacion al progressbarBeneficio
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(pbBeneficio, "progress",0,100);//el ultimo 100 es el progresso
        progressAnimator.setDuration(4000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();

        //icono de ciclo en activity
        String cicloName = getCicloName(exCiclo);
        int drawableId = getResources().getIdentifier(cicloName, "drawable",getPackageName());
        Drawable drawable = getResources().getDrawable(drawableId, null);
        ivCiclo.setImageDrawable(drawable);

        //volver
        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

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

        progreso = new ProgressDialog(this);
        progreso.setMessage("Buscando Experiencias");
        progreso.setCancelable(false);
        progreso.show();


        String url = Util.RUTA_EXPERIENCIA + "/"+ idCarrera + "/" + exCiclo+ "/" + exCiclo;
        //String url = Util.RUTA_EXPERIENCIA;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progreso.dismiss();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idExperiencia =jsonObject.getInt("IdExperiencia");
                        String ExNombre = jsonObject.getString("ExNombre");
                        String ExIconoUrl =jsonObject.getString("ExIconoUrl");
                        int idContenido =jsonObject.getInt("IdContenido");
                        int IdTipoMedia =jsonObject.getInt("IdTipoMedia");
                        String CoTitulo =jsonObject.getString("CoTitulo");
                        String CoDescripcion =jsonObject.getString("CoDescripcion");
                        String CoUrlMedia =jsonObject.getString("CoUrlMedia");
                        Experiencia experiencia = new Experiencia(idExperiencia, ExNombre, ExIconoUrl,idContenido,IdTipoMedia,CoTitulo,CoDescripcion,CoUrlMedia);
                        listaExperiencia.add(experiencia);

                    } catch (JSONException e) {
                        progreso.hide();
                        e.printStackTrace();
                    }
                }

                ExperienciaAdapter adapter = new ExperienciaAdapter(ListExperienciasActivity.this,listaExperiencia);
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
        int idContenido = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getIdContenido();
        int idTipoMedia = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getIdTipoMedia();
        String coTitulo = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getCoTitulo();
        String coDescripcion = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getCoDescripcion();
        String coUrlMedia = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getCoUrlMedia();

        Intent i = new Intent(this, DetalleExperienciaActivity.class);
        i.putExtra("idContenido",idContenido);
        i.putExtra("idTipoMedia",idTipoMedia);
        i.putExtra("coTitulo",coTitulo);
        i.putExtra("coDescripcion",coDescripcion);
        i.putExtra("coUrlMedia",coUrlMedia);
        startActivity(i);

    }
}