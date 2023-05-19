package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.BeneficioAdapter;
import com.acevedo.rutaexperienciauc.adapter.ExperienciaAdapter;
import com.acevedo.rutaexperienciauc.clases.Beneficio;
import com.acevedo.rutaexperienciauc.clases.Experiencia;
import com.acevedo.rutaexperienciauc.util.Util;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListExperienciasActivity extends AppCompatActivity {


    RecyclerView rvExperiencias,rvBeneficios;
    List<Experiencia> listaExperiencia;
    List<Beneficio> listaBeneficio;
    RequestQueue requestQueue;

    ImageView ivCiclo;
    LinearLayout llVolver;

    int idCarrera, exCiclo, cantCiclos;

    ProgressDialog progreso;
    int porcentajeBeneficio;

    //progressBeneficio
    //ProgressBar pbBeneficio; // ya no se usa


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_experiencias);

        idCarrera = getIntent().getIntExtra("idCarrera",0);
        exCiclo = getIntent().getIntExtra("idCiclo",0);
        cantCiclos = getIntent().getIntExtra("cantCiclos",0); //cantidad de ciclos


        llVolver = findViewById(R.id.llVolver);
        ivCiclo = findViewById(R.id.ivCiclo);
        rvExperiencias = findViewById(R.id.rvExperiencias);
        //pbBeneficio = findViewById(R.id.pbBeneficio); //ya no se usa
        rvExperiencias.setHasFixedSize(true);
        rvExperiencias.setLayoutManager(new GridLayoutManager(this,2));
        requestQueue = Volley.newRequestQueue(this);
        listaExperiencia = new ArrayList<>();
        cargarExperiencias();

        rvBeneficios= findViewById(R.id.rvBeneficios);
        rvBeneficios.setHasFixedSize(true);
        rvBeneficios.setLayoutManager(new LinearLayoutManager(this));
        listaBeneficio = new ArrayList<>();
        cargarBeneficios();

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

    private int calcularPorcentajeBeneficio(int cantCiclos, int exCiclo) {
        if (cantCiclos <= 0 || exCiclo <= 0) {
            return 0; // Devolver 0 si los valores no son válidos
        }

        float porcentajeFloat = ((float) exCiclo / cantCiclos) * 100.0f;
        int porcentajeEntero = Math.round(porcentajeFloat);
        return porcentajeEntero;
    }

    private void cargarBeneficios() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Buscando Beneficios");
        progreso.setCancelable(false);
        progreso.show();


        String url = Util.RUTA_BENEFICIO + "/"+ idCarrera ;
        //String url = Util.RUTA_EXPERIENCIA;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progreso.dismiss();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idBeneficio =jsonObject.getInt("IdBeneficio");
                        String beDescripcion = jsonObject.getString("BeDescripcion");
                        Beneficio beneficio = new Beneficio(idBeneficio, beDescripcion);
                        listaBeneficio.add(beneficio);

                    } catch (JSONException e) {
                        progreso.hide();
                        e.printStackTrace();
                    }
                }
                //obtener porcentaje de beneficio en un determinado ciclo
                porcentajeBeneficio = calcularPorcentajeBeneficio(cantCiclos,exCiclo);

                BeneficioAdapter adapter = new BeneficioAdapter(ListExperienciasActivity.this,listaBeneficio,porcentajeBeneficio);
                adapter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        selectBeneficio(v);
                        //Toast.makeText(ListExperienciasActivity.this, "muy bien", Toast.LENGTH_SHORT).show();
                    }
                });

                rvBeneficios.setAdapter(adapter);
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

    private void selectBeneficio(View v) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_beneficio_detalle);

        String beneficioDetalle = listaBeneficio.get(rvBeneficios.getChildAdapterPosition(v)).getBeDescripcion();
        LottieAnimationView avBeneficioDetalle = dialog.findViewById(R.id.avBeneficioDetalle);
        ProgressBar pbBeneficioDetalle = dialog.findViewById(R.id.pbBeneficioDetalle);
        TextView tvProgressBeneficioDetalle = dialog.findViewById(R.id.tvProgressBeneficioDetalle);
        TextView tvBeneficioDetalle = dialog.findViewById(R.id.tvBeneficioDetalle);
        CardView cvAceptar = dialog.findViewById(R.id.cvAceptar);

        //pbBeneficioDetalle.setProgress(porcentajeBeneficio);
        ObjectAnimator progressAnimator = ObjectAnimator.ofInt(pbBeneficioDetalle, "progress",0,porcentajeBeneficio);//el ultimo 100 es el progresso
        progressAnimator.setDuration(2000);
        progressAnimator.setInterpolator(new LinearInterpolator());
        progressAnimator.start();



        tvProgressBeneficioDetalle.setText(porcentajeBeneficio + "% completado");
        tvBeneficioDetalle.setText(beneficioDetalle);
        avBeneficioDetalle.setAnimation(R.raw.joy_in_education);
        avBeneficioDetalle.setRepeatCount(LottieDrawable.INFINITE);
        //avBeneficioDetalle.loop(true);
        avBeneficioDetalle.playAnimation();

        cvAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

        dialog.show();
        dialog.setCancelable(false);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.CENTER);
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

        //progreso = new ProgressDialog(this);
        //progreso.setMessage("Buscando Experiencias");
        //progreso.setCancelable(false);
        //progreso.show();


        String url = Util.RUTA_EXPERIENCIA + "/"+ idCarrera + "/" + exCiclo+ "/" + exCiclo;
        //String url = Util.RUTA_EXPERIENCIA;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                //progreso.dismiss();
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
                        //progreso.hide();
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
        int idExperiencia = listaExperiencia.get(rvExperiencias.getChildAdapterPosition(view)).getIdExperiencia();

        Intent i = new Intent(ListExperienciasActivity.this, DetalleExperienciaActivity.class);
        i.putExtra("idContenido",idContenido);
        i.putExtra("idTipoMedia",idTipoMedia);
        i.putExtra("coTitulo",coTitulo);
        i.putExtra("coDescripcion",coDescripcion);
        i.putExtra("coUrlMedia",coUrlMedia);
        i.putExtra("idExperiencia",idExperiencia);
        startActivity(i);

    }
}