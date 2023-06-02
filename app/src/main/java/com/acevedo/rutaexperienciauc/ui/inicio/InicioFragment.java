package com.acevedo.rutaexperienciauc.ui.inicio;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import android.os.Handler;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.SedeAdapter;
import com.acevedo.rutaexperienciauc.adapter.SliderSedeAdapter;
import com.acevedo.rutaexperienciauc.clases.Sede;


import com.acevedo.rutaexperienciauc.ui.sedes.carreras.ListaCarrerasActivity;

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


public class InicioFragment extends Fragment {
    RecyclerView rvSedes,imageSlider;
    CardView cvPensamiento, cvComunidades, cvBienestar, cvEscribenos;
    List<Sede> listaSede;
    List<Sede> listaSlider;

    RequestQueue requestQueue;

    ProgressDialog progreso;

    SliderSedeAdapter sliderAdapter;
    private Handler handler = new Handler();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View vista =  inflater.inflate(R.layout.fragment_inicio, container, false);


        cvPensamiento = vista.findViewById(R.id.cvPensamiento);
        cvComunidades = vista.findViewById(R.id.cvComunidades);
        cvBienestar = vista.findViewById(R.id.cvBienestar);
        cvEscribenos = vista.findViewById(R.id.cvEscribenos);


        cvPensamiento.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDiferenciales("pensamiento");
            }
        });
        cvBienestar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDiferenciales("bienestar");
            }
        });
        cvComunidades.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogDiferenciales("comunidades");
            }
        });

        cvEscribenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getContext(), SolicitarInformacionActivity.class);
                startActivity(i);
            }
        });

        //sedes
        rvSedes = vista.findViewById(R.id.rvSedes);
        rvSedes.setHasFixedSize(true);
        rvSedes.setLayoutManager(new GridLayoutManager(getContext(),2));

        //slider
        imageSlider = vista.findViewById(R.id.imageSlider);
        LinearLayoutManager manager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        imageSlider.setLayoutManager(manager);
        imageSlider.setHasFixedSize(true);
        manager.setSmoothScrollbarEnabled(true); // Para un desplazamiento más suave
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(imageSlider);

        listaSlider = new ArrayList<>();

        requestQueue = Volley.newRequestQueue(getContext());
        listaSede = new ArrayList<>();

        cargarSedes();
        obtenerImagenesSlider();

        return vista;

    }


    private void dialogDiferenciales(String diferencial) {

        final Dialog dialog = new Dialog(getContext());
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_diferencial);

        TextView txtDesc = dialog.findViewById(R.id.txtDescripcion);
        TextView tvTitle1 = dialog.findViewById(R.id.tvTitle1);
        TextView tvTitle2 = dialog.findViewById(R.id.tvTitle2);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);


        String t1 = "dif_"+diferencial+"_title_1";
        String t2 = "dif_"+diferencial+"_title_2";
        String desc = "dif_"+diferencial+"_desc";

        int title1 = getResources().getIdentifier(t1, "string", getContext().getPackageName());
        int title2 = getResources().getIdentifier(t2, "string", getContext().getPackageName());
        int description = getResources().getIdentifier(desc, "string", getContext().getPackageName());

        tvTitle1.setText(getString(title1));
        tvTitle2.setText(getString(title2));
        txtDesc.setText(getString(description));

        btnAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {dialog.dismiss();
            }
        });

        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);
    }

    private void cargarSedes() {

        progreso = new ProgressDialog(getContext());
        progreso.setMessage("Buscando Sedes");
        progreso.setCancelable(false);
        progreso.show();
        String url = Util.RUTA_SEDE;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progreso.dismiss();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id =jsonObject.getInt("IdSede");
                                String nombre = jsonObject.getString("SeNombre");
                                String adress =jsonObject.getString("SeDireccion");
                                String referencia = jsonObject.getString("SeReferencia");
                                String telefono = jsonObject.getString("SeTelefono");
                                String image_url = jsonObject.getString("SeUrlImagen");
                                Sede sede = new Sede(id, nombre, adress, referencia, telefono, image_url);
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

                        rvSedes.setAdapter(adapter);
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
        int id = listaSede.get(rvSedes.getChildAdapterPosition(view)).getId();
        Intent i = new Intent(getContext(), ListaCarrerasActivity.class);
        i.putExtra("idSede",id);
        startActivity(i);
    }

    private void obtenerImagenesSlider() {


        String url = Util.RUTA_SEDE;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        progreso.dismiss();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id =jsonObject.getInt("IdSede");
                                String nombre = jsonObject.getString("SeNombre");
                                String adress =jsonObject.getString("SeDireccion");
                                String referencia = jsonObject.getString("SeReferencia");
                                String telefono = jsonObject.getString("SeTelefono");
                                String image_url = jsonObject.getString("SeUrlImagen");
                                Sede sede = new Sede(id, nombre, adress, referencia, telefono, image_url);
                                listaSlider.add(sede);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        sliderAdapter = new SliderSedeAdapter(getContext(),listaSlider);
                        imageSlider.setAdapter(sliderAdapter);
                        sliderAdapter.notifyDataSetChanged();

                        // Configurar el desplazamiento automático cada 3 segundos
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                int currentPosition = ((LinearLayoutManager) imageSlider.getLayoutManager()).findFirstVisibleItemPosition();
                                int newPosition = (currentPosition + 1) % sliderAdapter.getItemCount();
                                if (newPosition == 0 && currentPosition != newPosition) {
                                    // Si es el último elemento, volver al primer elemento
                                    imageSlider.smoothScrollToPosition(0);
                                } else {
                                    imageSlider.smoothScrollToPosition(newPosition);
                                }
                                handler.postDelayed(this, 3000);
                            }
                        }, 3000);
                        sliderAdapter.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selectSede(view);
                            }
                        });

                        imageSlider.setAdapter(sliderAdapter);
                        sliderAdapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        requestQueue.add(request);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        // Detener el desplazamiento automático al salir del fragmento
        handler.removeCallbacksAndMessages(null);
    }
}