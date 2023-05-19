package com.acevedo.rutaexperienciauc.ui.soporte;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.MainActivity;
import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.PreguntasFrecuentesAdapter;
import com.acevedo.rutaexperienciauc.adapter.SoporteSedesAdapter;
import com.acevedo.rutaexperienciauc.clases.PreguntasFrecuentes;
import com.acevedo.rutaexperienciauc.clases.Sede;
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

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class SoporteFragment extends Fragment {
    RecyclerView rvSoporteSedes;
    List<Sede> sedeList;
    RequestQueue requestQueue;
    CardView cvEscribenos;

    ImageButton ibLinkedin;
    ImageButton ibFacebook;
    ImageButton ibWhatsapp;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_soporte, container, false);
        rvSoporteSedes = vista.findViewById(R.id.rvSoporteSedes);
        sedeList = new ArrayList<>();
        rvSoporteSedes.setLayoutManager(new LinearLayoutManager(getContext()));
        requestQueue = Volley.newRequestQueue(getContext());
        cvEscribenos = vista.findViewById(R.id.cvEscribenos);
        ibLinkedin = vista.findViewById(R.id.ibLinkedin);
        ibFacebook = vista.findViewById(R.id.ibFacebook);
        ibWhatsapp = vista.findViewById(R.id.ibWhatsapp);

        cargarSoporteSedes();

        cvEscribenos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(getContext(), SolicitarInformacionActivity.class);
                startActivity(i);
            }
        });

        seleccionarRedSocial(ibLinkedin,"https://pe.linkedin.com/school/universidad-continental/","LinkedIn");
        seleccionarRedSocial(ibFacebook,"https://www.facebook.com/ucontinental/?locale=es_LA","Facebook");
        seleccionarRedSocial(ibWhatsapp,"https://ucontinental.edu.pe/sin-categoria/bienvenido-whatsapp-conti/","WhatsApp");

        return vista;
    }

    private void cargarSoporteSedes() {
        String url = Util.RUTA_SEDE;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id =jsonObject.getInt("IdSede");
                                String nombre = jsonObject.getString("SeNombre");
                                String adress =jsonObject.getString("SeDireccion");
                                String referencia = jsonObject.getString("SeReferencia");
                                String telefono = jsonObject.getString("SeTelefono");
                                String image_url = jsonObject.getString("SeUrlImagen");
                                Sede sede = new Sede(id, nombre, adress,referencia,telefono, image_url);
                                sedeList.add(sede);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SoporteSedesAdapter adapter = new SoporteSedesAdapter(getContext(),sedeList);
                        rvSoporteSedes.setAdapter(adapter);
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

    private void seleccionarRedSocial(ImageButton imgbtn,String urlRedSocial,String nombre){
        imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    String url = urlRedSocial;
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "No se puede abrir " + nombre, Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}