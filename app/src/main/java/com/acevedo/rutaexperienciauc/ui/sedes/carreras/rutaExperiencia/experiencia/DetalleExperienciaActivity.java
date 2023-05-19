package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.util.Util;
import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DetalleExperienciaActivity extends AppCompatActivity {

    TextView tvTitulo, tvDescripcion, tvVermas;
    CardView cvFullScreen;
    LinearLayout llVolver;

    ImageView ivContenido;
    WebView wvContenido;
    YouTubePlayerView ypvContenido;
    RatingBar rbCalificarExperiencia;
    RequestQueue requestQueue;

    //mauricio
    ImageButton customFavoriteButton;
    SharedPreferences sharedPreferences;
    //mauricio


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_experiencia);

        tvTitulo = findViewById(R.id.tvTitulo);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvVermas = findViewById(R.id.tvVermas);
        rbCalificarExperiencia = findViewById(R.id.rbCalificarExperiencia);
        cvFullScreen = findViewById(R.id.cvFullScreen);
        llVolver = findViewById(R.id.llVolver);
        ivContenido = findViewById(R.id.ivContenido);
        wvContenido = findViewById(R.id.wvContenido);
        ypvContenido = findViewById(R.id.ypvContenido);
        requestQueue= Volley.newRequestQueue(this);
// mauricio
        customFavoriteButton = findViewById(R.id.custom_favorite_button);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
//fun mauricio

        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cargarExperiencia();

    }

    private void dialogDetalle(String titulo, String detalle) {

        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_diferencial);

        TextView txtDesc = dialog.findViewById(R.id.txtDescripcion);
        TextView tvTitle1 = dialog.findViewById(R.id.tvTitle1);
        TextView tvTitle2 = dialog.findViewById(R.id.tvTitle2);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        tvTitle2.setVisibility(View.GONE);


        tvTitle1.setText(titulo);
        txtDesc.setText(detalle);

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

    private void cargarExperiencia() {
        int idExperiencia = getIntent().getIntExtra("idExperiencia",0);
        int idTipoMedia = getIntent().getIntExtra("idTipoMedia",0);
        String coTitulo = getIntent().getStringExtra("coTitulo");
        String coDescripcion = getIntent().getStringExtra("coDescripcion");
        String coUrlMedia = getIntent().getStringExtra("coUrlMedia");
        int idContenido = getIntent().getIntExtra("idContenido",0);

        tvTitulo.setText(coTitulo);
        tvDescripcion.setText(coDescripcion);
        switch(idTipoMedia){
            case 1:
                ivContenido.setVisibility(View.VISIBLE);
                Glide.with(this).load(coUrlMedia).into(ivContenido);
                //Toast.makeText(this, "Imagen", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                ypvContenido.setVisibility(View.VISIBLE);
                getLifecycle().addObserver(ypvContenido);

                ypvContenido.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                    @Override
                    public void onReady(YouTubePlayer youTubePlayer) {
                        String videoId = getYoutubeId(coUrlMedia);
                        youTubePlayer.loadVideo(videoId, 0); // Carga el video y comienza a reproducirlo automáticamente
                    }
                });

                break;
            case 3:

                wvContenido.setVisibility(View.VISIBLE);
                wvContenido.getSettings().setJavaScriptEnabled(true);
                wvContenido.loadUrl(coUrlMedia);

                break;
        }

        tvVermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetalle(coTitulo, coDescripcion);
            }
        });

        cvFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DetalleExperienciaActivity.this, FullScreenActivity.class);
                intent.putExtra("coUrlMedia",coUrlMedia);
                intent.putExtra("idTipoMedia",idTipoMedia);
                startActivity(intent);
            }
        });
        GuardarExperienciaFavorito(Integer.toString(idContenido));

        //recuperar datos de calificación
        SharedPreferences sharedPreferences = getSharedPreferences("calificar_experiencia",MODE_PRIVATE);
        float ratingRecuperada = sharedPreferences.getFloat("rating"+idContenido,0);
        int idContenidoRecuperado = sharedPreferences.getInt("idContenido"+idContenido,0);

        //comparación de idExperiencia
        if(idContenido == idContenidoRecuperado){
            rbCalificarExperiencia.setIsIndicator(true); //ratingBar solo lectura
            rbCalificarExperiencia.setRating(ratingRecuperada);
        }

        rbCalificarExperiencia.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //cuando se tenga un cambio en RatingBar se llama a Dialog para confirmar la calificación
                dialogCalificarExperiencia(idExperiencia, rating, idContenido);
            }
        });

    }

    private void dialogCalificarExperiencia(int idExperiencia, float rating, int idContenido) {
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
                    datos.put("idExperiencia", idExperiencia);
                    datos.put("idCalificacion", (int) rating);
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
                                editor.putFloat("rating"+idContenido,rating);
                                editor.putInt("idContenido"+idContenido,idContenido);
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

    private void GuardarExperienciaFavorito(String rutaTitulo){
        customFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = sharedPreferences.getBoolean("rutaExperiencia" + rutaTitulo, false);
                if (isFavorite) {
                    customFavoriteButton.setSelected(false);
                    sharedPreferences.edit().remove("rutaExperiencia" + rutaTitulo).apply();
                } else {
                    customFavoriteButton.setSelected(true);
                    sharedPreferences.edit().putBoolean("rutaExperiencia" + rutaTitulo, true).apply();
                    Toast.makeText(getApplicationContext(),rutaTitulo , Toast.LENGTH_SHORT).show();
                }
            }
        });
        boolean isFavorite = sharedPreferences.getBoolean("rutaExperiencia" + rutaTitulo, false);
        customFavoriteButton.setSelected(isFavorite);
    }

    private String getYoutubeId(String videoUrl) {
        String videoId = null;
        if (videoUrl != null && videoUrl.trim().length() > 0 && (videoUrl.contains("youtube.com") || videoUrl.contains("youtu.be"))) {
            String expression = "^.*((youtu.be\\/)|(v\\/)|(\\/u\\/\\w\\/)|(embed\\/)|(watch\\?))\\??v?=?([^#&?]*).*";
            CharSequence input = videoUrl;
            Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(input);
            if (matcher.matches()) {
                videoId = matcher.group(7);
            }
        }
        return videoId;
    }
}