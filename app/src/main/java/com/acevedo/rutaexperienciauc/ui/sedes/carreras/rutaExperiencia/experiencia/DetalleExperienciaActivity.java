package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.animation.ObjectAnimator;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.ContenidoAdapter;
import com.acevedo.rutaexperienciauc.clases.Contenido;
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


public class DetalleExperienciaActivity extends AppCompatActivity {

    LinearLayout llVolver;
    RecyclerView rvContenido;
    ProgressBar pbCantidadItems;
    RequestQueue requestQueue;
    List<Contenido> listaContenido;
    ProgressDialog progreso;
    ObjectAnimator progressAnimator;
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

    private void cargarExperiencia() {
        int idExperiencia = getIntent().getIntExtra("idExperiencia",0);

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
                        Contenido contenido = new Contenido(idExperiencia,idContenido, idTipoMedia,coTitulo, coDescripcion, coUrlMedia);
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