package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.ContenidoAdapter;
import com.acevedo.rutaexperienciauc.clases.Contenido;
import com.acevedo.rutaexperienciauc.util.Util;
import com.airbnb.lottie.LottieAnimationView;
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


public class DetalleExperienciaActivity extends AppCompatActivity {

    LinearLayout llVolver;
    RecyclerView rvContenido;
    ProgressBar pbCantidadItems;
    RequestQueue requestQueue;
    List<Contenido> listaContenido;
    ProgressDialog progreso;
    ObjectAnimator progressAnimator;
    int idExperiencia;
    RatingBar rbCalificarExperiencia;
    TextView tvSinContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_experiencia);
        requestQueue = Volley.newRequestQueue(this);
        pbCantidadItems =findViewById(R.id.pbCantidadItems);
        rvContenido =findViewById(R.id.rvContenido);
        listaContenido = new ArrayList<>();
        llVolver = findViewById(R.id.llVolver);
        rbCalificarExperiencia = findViewById(R.id.rbCalificarExperiencia);
        tvSinContenido = findViewById(R.id.tvSinContenido);

        // Configuración del LayoutManager
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        rvContenido.setLayoutManager(layoutManager);
        layoutManager.setSmoothScrollbarEnabled(true); // Para un desplazamiento más suave
        rvContenido.setHasFixedSize(true);

        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvContenido);

        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        idExperiencia = getIntent().getIntExtra("idExperiencia",0);

        cargarCalifExperiencia();

        cargarExperiencia();

        rvContenido.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                // Obtén información sobre el RecyclerView
                LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                int totalItems = layoutManager.getItemCount();
                int visibleItems = layoutManager.getChildCount();
                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                // Calcula el progreso
                int progress = (int) ((firstVisibleItemPosition + visibleItems) * 100.0 / totalItems);

                // Cancela la animación existente
                if (progressAnimator != null) {
                    progressAnimator.cancel();
                }

                // Crea una nueva animación para el progreso
                progressAnimator = ObjectAnimator.ofInt(pbCantidadItems, "progress", pbCantidadItems.getProgress(), progress);
                progressAnimator.setDuration(1000);
                progressAnimator.start();

            }
        });
    }

    private void cargarCalifExperiencia() {
        //recuperar datos de calificación
        SharedPreferences sharedPreferences = getSharedPreferences("calificar_experiencia",MODE_PRIVATE);
        float ratingRecuperada = sharedPreferences.getFloat("rating"+idExperiencia,0);
        int idExperienciaRecuperado = sharedPreferences.getInt("idExperiencia"+idExperiencia,0);

        //comparación de idExperiencia
        if(idExperiencia == idExperienciaRecuperado){
            rbCalificarExperiencia.setIsIndicator(true); //ratingBar solo lectura
            rbCalificarExperiencia.setRating(ratingRecuperada);
        }

        rbCalificarExperiencia.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //cuando se tenga un cambio en RatingBar se llama a Dialog para confirmar la calificación
                dialogCalificarExperiencia(idExperiencia, rating);
            }
        });
    }

    private void dialogCalificarExperiencia(int idExperiencia, float rating) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.calificar_experiencia_dialogo);


        CardView cvAceptar = dialog.findViewById(R.id.cvAceptar);
        CardView cvCancelar = dialog.findViewById(R.id.cvCancelar);
        RatingBar rbCantidadEstrellas = dialog.findViewById(R.id.rbCantidadEstrellas);
        TextView tvGracias = dialog.findViewById(R.id.tvGracias);
        LottieAnimationView avCelebrate = dialog.findViewById(R.id.avCelebrate);
        CardView cvConfirmacion = dialog.findViewById(R.id.cvConfirmacion);

        rbCantidadEstrellas.setRating(rating);

        cvAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //enviar calificación
                JSONObject datos = new JSONObject();
                String url = Util.RUTA_CALIFICAR_EXPERIENCIA;

                try{
                    datos.put("IdExperiencia", idExperiencia);
                    datos.put("IdCalificacion", (int) rating);
                } catch (JSONException e){
                    e.printStackTrace();
                }
                //Enviar la petición al servidor
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, datos,
                        new Response.Listener<JSONObject>() {
                            @Override
                            public void onResponse(JSONObject response) {
                                //guardar calificacion del usuario
                                SharedPreferences sharedPreferences = getSharedPreferences("calificar_experiencia",MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.putFloat("rating"+idExperiencia,rating);
                                editor.putInt("idExperiencia"+idExperiencia,idExperiencia);
                                editor.commit();

                                // Ocultar elementos del diálogo
                                cvConfirmacion.setVisibility(View.GONE);

                                // Mostrar animación
                                avCelebrate.playAnimation();
                                avCelebrate.setVisibility(View.VISIBLE);
                                tvGracias.setVisibility(View.VISIBLE);

                                // Cerrar diálogo después de que la animación termine
                                avCelebrate.addAnimatorListener(new Animator.AnimatorListener() {
                                    @Override
                                    public void onAnimationStart(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationEnd(Animator animation) {
                                        dialog.dismiss();
                                    }

                                    @Override
                                    public void onAnimationCancel(Animator animation) {

                                    }

                                    @Override
                                    public void onAnimationRepeat(Animator animation) {

                                    }
                                });

                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                Toast.makeText(DetalleExperienciaActivity.this, error + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue= Volley.newRequestQueue(DetalleExperienciaActivity.this);
                requestQueue.add(jsonObjectRequest);
            }
        });

        cvCancelar.setOnClickListener(new View.OnClickListener() {
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

    private void cargarExperiencia() {
        progreso = new ProgressDialog(this);
        progreso.setMessage("Buscando Contenido");
        progreso.setCancelable(false);
        progreso.show();

        String url = Util.RUTA_CONTENIDO + "/"+ idExperiencia;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                progreso.dismiss();
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);
                        int idContenido =jsonObject.getInt("IdContenido");
                        int idTipoMedia =jsonObject.getInt("IdTipoMedia");
                        String coTitulo =jsonObject.getString("CoTitulo");
                        String coDescripcion =jsonObject.getString("CoDescripcion");
                        String coUrlMedia =jsonObject.getString("CoUrlMedia");
                        String exCicloInicio =jsonObject.getString("ExCicloInicio");
                        String exCicloFin =jsonObject.getString("ExCicloFin");
                        Contenido contenido = new Contenido(
                                idExperiencia,
                                idContenido,
                                idTipoMedia,
                                coTitulo,
                                coDescripcion,
                                coUrlMedia,
                                exCicloInicio,
                                exCicloFin
                        );
                        listaContenido.add(contenido);

                    } catch (JSONException e) {
                        progreso.dismiss();
                        e.printStackTrace();
                    }
                }
                if(listaContenido.size() == 0){
                    tvSinContenido.setVisibility(View.VISIBLE);
                    rvContenido.setVisibility(View.GONE);
                }else{
                    tvSinContenido.setVisibility(View.GONE);
                    rvContenido.setVisibility(View.VISIBLE);
                    ContenidoAdapter adapter = new ContenidoAdapter(DetalleExperienciaActivity.this,listaContenido);
                    rvContenido.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
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
}