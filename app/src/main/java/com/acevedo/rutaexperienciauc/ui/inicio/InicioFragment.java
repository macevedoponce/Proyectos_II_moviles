package com.acevedo.rutaexperienciauc.ui.inicio;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.acevedo.rutaexperienciauc.util.Util;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
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

    CardView cvPensamiento, cvComunidades, cvBienestar;
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
        slideModels.add(new SlideModel("https://scontent.flim26-1.fna.fbcdn.net/v/t39.30808-6/341272484_2047433308981227_7964840724640227744_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=e3f864&_nc_eui2=AeHIrSGu_1VNJ1C-aFWLWBFve379lvGnHaF7fv2W8acdoZBo3dLiCCXUDAUeKCK9dHsPThsQluBmqrrzFwRQN7dN&_nc_ohc=Yjw7X3kPTPkAX_WQeKu&_nc_ht=scontent.flim26-1.fna&oh=00_AfB5I4AxK3DN_E5i-X5QPXmQ49owkf7ZylzMMavVwoklpQ&oe=6440EBFD", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.flim26-1.fna.fbcdn.net/v/t39.30808-6/327419865_510473061074229_8059979412883123201_n.png?_nc_cat=110&ccb=1-7&_nc_sid=e3f864&_nc_eui2=AeECIzlOwXjkNFg2W9Q7hiC0zS5LL5xP0SDNLksvnE_RIE6D3PCyVTyhJr5QDUsny8OL0dc2JB4fV7Px4kQgIDaS&_nc_ohc=fSEKfeaNkngAX8OHzDk&_nc_ht=scontent.flim26-1.fna&oh=00_AfCn_NcBlNC8AmNowRje_M4ZBaiiMCQ9v1OtjkzT2MmFHg&oe=6441CE50", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.flim26-1.fna.fbcdn.net/v/t39.30808-6/313326143_5566324050118761_1187009842661087591_n.jpg?_nc_cat=104&ccb=1-7&_nc_sid=e3f864&_nc_eui2=AeG63qJoSayNZF4Y3Y88JKVCZmt2qBm0Shtma3aoGbRKG6RWfU-1lBKlke_0D6QyIinr1I_R25O8wNmGilmfrJgp&_nc_ohc=MZvxZmYHKxoAX9XzYPF&_nc_ht=scontent.flim26-1.fna&oh=00_AfDQbhr4gUSkehyzo7m_ipS2sOTHVUeRaWUE-JHwEw5aaQ&oe=644203C3", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.flim26-1.fna.fbcdn.net/v/t39.30808-6/292737338_5267626859988483_6971671227727438531_n.jpg?_nc_cat=102&ccb=1-7&_nc_sid=e3f864&_nc_eui2=AeEofyOIOQMG_BCkP9FNw-AeSdwfS6li1zdJ3B9LqWLXN_SDoLLLQ1zvCQNKFZ7CjrLcD9Ce0VZ5eAtpw5vLZi0V&_nc_ohc=JHvpHuFUsmgAX9_QcHQ&_nc_ht=scontent.flim26-1.fna&oh=00_AfDk3JJ0hNjL2CM2umVMdmGCzLO7zKDlyNCGxr1nM9zZ8g&oe=64418709", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.flim26-1.fna.fbcdn.net/v/t39.30808-6/272763466_4788726011211906_2249349560118037732_n.jpg?_nc_cat=105&ccb=1-7&_nc_sid=e3f864&_nc_eui2=AeFVRB32_CPz5rctWvINGw9Tu3E6Zm5tScK7cTpmbm1Jwsjmx4DScsC4ZfxSq--bMj0ggtpOHeXiuxfta8TUgMMR&_nc_ohc=dqQuXCvn1AAAX8qARCW&_nc_ht=scontent.flim26-1.fna&oh=00_AfBhv9b8Hu8CbYjuYjbmFTnHQoIidElCSfLoMAiaI2h8VA&oe=6442421C", ScaleTypes.FIT));
        slideModels.add(new SlideModel("https://scontent.flim26-1.fna.fbcdn.net/v/t39.30808-6/246747911_4435060763245101_373665842614507042_n.jpg?_nc_cat=111&ccb=1-7&_nc_sid=e3f864&_nc_eui2=AeEXqw4ypouzqYr8mcYO8sw5j0y14BctGeyPTLXgFy0Z7Phj8HlauUUuxQpWABhPSZ2QzPwUxev5MKvM3evFX1Ux&_nc_ohc=LwiHYdGVmuAAX_2C3mf&_nc_oc=AQnXvTr7TKptXUhlff9usPsgS5xkZCkrHCFl5XMhNC1dsaCsOJc1rlEK5ESCZKTNqB8&_nc_ht=scontent.flim26-1.fna&oh=00_AfAyyOXTlLvOALxS9hmtWmrNKKcrMlP27zs1-NSc09EP2w&oe=644238C4", ScaleTypes.FIT));

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