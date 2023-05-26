package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.BeneficioAdapter;
import com.acevedo.rutaexperienciauc.adapter.ListaRutaExperienciaAdapter;
import com.acevedo.rutaexperienciauc.adapter.SpinnerAdapter;
import com.acevedo.rutaexperienciauc.clases.Beneficio;
import com.acevedo.rutaexperienciauc.clases.Carrera;
import com.acevedo.rutaexperienciauc.clases.ListaRutaExperiencia;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia.ListExperienciasActivity;
import com.acevedo.rutaexperienciauc.ui.solicitarInformacion.SolicitarInformacionActivity;
import com.acevedo.rutaexperienciauc.util.Util;
import com.airbnb.lottie.LottieAnimationView;
import com.airbnb.lottie.LottieDrawable;
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
    RecyclerView rvListaRutaExperiencia,rvBeneficios;

    List<Beneficio> listaBeneficio;
    ListaRutaExperienciaAdapter adapter;
    SpinnerAdapter spinnerAdapter;
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
    final List<String> planEstudiosUrls = new ArrayList<>();
    List<String> nombresCarrera = new ArrayList<>();

    ProgressDialog progreso;
    int porcentajeBeneficio=0;


    BeneficioAdapter adapterb;

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

        //beneficio
        rvBeneficios= findViewById(R.id.rvBeneficios);
        rvBeneficios.setHasFixedSize(true);
        rvBeneficios.setLayoutManager(new LinearLayoutManager(this));
        listaBeneficio = new ArrayList<>();
        cargarBeneficios();

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

    //beneficio


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

                rvListaRutaExperiencia.addOnScrollListener(new RecyclerView.OnScrollListener() {
                    @Override
                    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                        super.onScrolled(recyclerView, dx, dy);

                        // Obtén información sobre el RecyclerView
                        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                        int totalItems = layoutManager.getItemCount();
                        int visibleItems = layoutManager.getChildCount();
                        int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                        // Calcula el progreso
                        porcentajeBeneficio = (int) ((firstVisibleItemPosition + visibleItems) * 100.0 / totalItems);
                        adapterb.updateProgress(porcentajeBeneficio);

                    }
                });

                BeneficioAdapter adapter = new BeneficioAdapter(RutaExperiencia.this,listaBeneficio);
                adapterb = adapter;
                //adapter.updateProgress(porcentajeBeneficio);
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
        RequestQueue requestQueue1 = Volley.newRequestQueue(this);
        requestQueue1.add(request);
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

    //fin beneficio

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
        LinearLayoutManager manager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvListaRutaExperiencia.setLayoutManager(manager);
        rvListaRutaExperiencia.setHasFixedSize(true);
        manager.setSmoothScrollbarEnabled(true); // Para un desplazamiento más suave
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvListaRutaExperiencia);
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