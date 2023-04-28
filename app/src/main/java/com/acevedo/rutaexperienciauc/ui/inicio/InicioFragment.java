package com.acevedo.rutaexperienciauc.ui.inicio;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.SedeAdapter;
import com.acevedo.rutaexperienciauc.clases.Sede;
import com.acevedo.rutaexperienciauc.ui.solicitarInformacion.SolicitarInformacionFragment;
import com.acevedo.rutaexperienciauc.util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class InicioFragment extends Fragment {

    ImageSlider imageSlider;
    RecyclerView rvSedes;

    CardView cvPensamiento, cvComunidades, cvBienestar, cvEscribenos;
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
        View vista =  inflater.inflate(R.layout.fragment_inicio, container, false);

        imageSlider = vista.findViewById(R.id.imageSlider);
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
                // Obtener instancia del FragmentManager
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();

                //Crear instancia del Fragment que deseas mostrar

                SolicitarInformacionFragment fragment = new SolicitarInformacionFragment();

                //Crear una instancia de la clase FragmentTransaction
                FragmentTransaction transaction = fragmentManager.beginTransaction();

                // Reemplazar el contenido del contenedor de fragmentos con el Fragment que deseas mostrar
                transaction.replace(R.id.frameLayoutInicio, fragment);

                // Agregar el Fragment actual a la pila de retroceso
                transaction.addToBackStack("null");

                // Finalizar la transacción
                transaction.commit();
                imageSlider.setVisibility(View.GONE);
                cvPensamiento.setVisibility(View.GONE);
                cvComunidades.setVisibility(View.GONE);
                cvBienestar.setVisibility(View.GONE);
                cvEscribenos.setVisibility(View.GONE);
            }
        });

        //sedes
        rvSedes = vista.findViewById(R.id.rvSedes);
        rvSedes.setHasFixedSize(true);
        rvSedes.setLayoutManager(new GridLayoutManager(getContext(),2));
        requestQueue = Volley.newRequestQueue(getContext());
        listaSede = new ArrayList<>();

        cargarSedes();
        cargarSlider();


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
        String url = Util.RUTA_SEDE;

        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<SlideModel> slideModels = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                int id =jsonObject.getInt("sede_id");
                                String nombre = jsonObject.getString("sede_nombre");
                                String adress =jsonObject.getString("sede_adresss");
                                String phone = jsonObject.getString("sede_phone");
                                String image_url = jsonObject.getString("sede_image_url");
                                Sede sede = new Sede(id, nombre, adress, phone, Util.RUTA +image_url);
                                listaSede.add(sede);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        SedeAdapter adapter = new SedeAdapter(getContext(),listaSede);
                    adapter.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            mostrarSedes(view);
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






//        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//                try {
//                    JSONArray jsonArray = response.getJSONArray("");
//                    for(int i = 0; i<jsonArray.length();i++){
//                        JSONObject jsonObject = jsonArray.getJSONObject(i);
//                        int id =jsonObject.getInt("sede_id");
//                        String nombre = jsonObject.getString("sede_nombre");
//                        String adress =jsonObject.getString("sede_adresss");
//                        String phone = jsonObject.getString("sede_phone");
//                        String image_url = Util.RUTA_SEDE + jsonObject.getString("sede_image_url");
//                        Sede sede = new Sede(id, nombre, adress, phone, image_url);
//                        listaSede.add(sede);
//                    }
//                    SedeAdapter adapter = new SedeAdapter(getContext(),listaSede);
//                    adapter.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            mostrarSedes(view);
//                        }
//                    });
//
//                    rvSedes.setAdapter(adapter);
//                    adapter.notifyDataSetChanged();
//
//                } catch (JSONException e) {
//                    //e.printStackTrace();
//                    Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
//                    Log.v("error",e.getMessage());
//                }
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//        });
//        requestQueue.add(jsonObjectRequest);
    }

    private void mostrarSedes(View view) {
        int id = listaSede.get(rvSedes.getChildAdapterPosition(view)).getId();
        String nombre = listaSede.get(rvSedes.getChildAdapterPosition(view)).getNombre();

        Toast.makeText(getContext(), nombre+"", Toast.LENGTH_SHORT).show();

        // activar cuando se tenga lista la interface de facultades y se debe de enviar el id para hacer la consulta en el api

//        Intent i = new Intent(getContext(), ListProductosActivity.class);
//        i.putExtra("sede_id",id);
//        startActivity(i);
    }


    //funcion que muestra imagenes solo ingresando las urls en este apartado
    private void cargarSlider(){

        ArrayList<SlideModel> slideModels = new ArrayList<>();
        slideModels.add(new SlideModel("https://ucontinental.edu.pe/www/wp-content/uploads/2023/02/uc-017-16_02_2023.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://ucontinental.edu.pe/www/wp-content/uploads/2022/07/Fernando-Barrios-Ipenza-Universidad-Continental-24-anios-descentralizando-la-educacion-del-pais.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://ucontinental.edu.pe/www/wp-content/uploads/2023/01/uc-017-31_01_2023.jpg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://ucontinental.edu.pe/www/wp-content/uploads/2022/10/uc.jpeg", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://pbs.twimg.com/profile_banners/111604052/1680652709/1500x500", ScaleTypes.FIT));

        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
    }


    //función que recepciona imagenes desde api
    private void obtenerImagenesSlider() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        String url = Util.RUTA_SLIDER;
        JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        List<SlideModel> slideModels = new ArrayList<>();
                        for (int i = 0; i < response.length(); i++) {
                            try {
                                JSONObject jsonObject = response.getJSONObject(i);
                                String imageUrl = jsonObject.getString("slider_url");
                                slideModels.add(new SlideModel(Util.RUTA+imageUrl,ScaleTypes.FIT)); //agregamos la ruta ya que el API solo manda dirección
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        imageSlider.setImageList(slideModels, ScaleTypes.CENTER_CROP);
                        imageSlider.startSliding(3000);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(request);
    }
}