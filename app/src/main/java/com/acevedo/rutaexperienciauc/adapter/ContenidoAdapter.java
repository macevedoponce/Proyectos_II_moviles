package com.acevedo.rutaexperienciauc.adapter;

import android.animation.Animator;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ComponentActivity;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.Contenido;
import com.acevedo.rutaexperienciauc.clases.Experiencia;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia.DetalleExperienciaActivity;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia.FullScreenActivity;
import com.acevedo.rutaexperienciauc.util.Util;
import com.acevedo.rutaexperienciauc.util.sqlite.FavoritosDatabaseHelper;
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

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ContenidoAdapter extends RecyclerView.Adapter<ContenidoAdapter.ContenidoHolder> {

    Context context;
    List<Contenido> contenidoList;

    RequestQueue requestQueue;
    private FavoritosDatabaseHelper databaseHelper;


    public ContenidoAdapter(Context context, List<Contenido> contenidoList) {
        this.context = context;
        this.contenidoList = contenidoList;
    }

    @NonNull
    @Override
    public ContenidoAdapter.ContenidoHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.contenido_item, parent, false);
        return new ContenidoHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull ContenidoAdapter.ContenidoHolder holder, int position) {
        Contenido contenido = contenidoList.get(position);

        int idExperiencia = contenido.getIdExperiencia();
        int idContenido = contenido.getIdContenido();
        String coTitulo = contenido.getCoTitulo();
        String coDescripcion = contenido.getCoDescripcion();
        int coTipoMedia = contenido.getIdTipoMedia();
        String coUrlMedia = contenido.getCoUrlMedia();
        String exCicloInicio = contenido.getExCicloInicio();
        String exCicloFin = contenido.getExCicloFin();

        holder.setTitulo(coTitulo);
        holder.setDescripcion(coDescripcion);
        holder.setTipoMedia(coTipoMedia,coUrlMedia);
        holder.setCiclos(exCicloInicio,exCicloFin);

        holder.tvVermas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogDetalle(coTitulo, coDescripcion);
            }
        });

        holder.cvFullScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, FullScreenActivity.class);
                intent.putExtra("coUrlMedia",coUrlMedia);
                intent.putExtra("idTipoMedia",coTipoMedia);
                context.startActivity(intent);
            }
        });

        //recuperar datos de calificación
        SharedPreferences sharedPreferences = context.getSharedPreferences("calificar_experiencia",context.MODE_PRIVATE);
        float ratingRecuperada = sharedPreferences.getFloat("rating"+idContenido,0);
        int idContenidoRecuperado = sharedPreferences.getInt("idContenido"+idContenido,0);

        //comparación de idExperiencia
        if(idContenido == idContenidoRecuperado){
            holder.rbCalificarExperiencia.setIsIndicator(true); //ratingBar solo lectura
            holder.rbCalificarExperiencia.setRating(ratingRecuperada);
        }

        holder.rbCalificarExperiencia.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                //cuando se tenga un cambio en RatingBar se llama a Dialog para confirmar la calificación
                dialogCalificarExperiencia(idExperiencia, rating, idContenido);
            }
        });

        //favorito
        databaseHelper = new FavoritosDatabaseHelper(context);
        boolean isFavorite = databaseHelper.isExperienciaFavorita(idContenido);

        // Actualizar la apariencia del ImageButton
        holder.customFavoriteButton.setSelected(isFavorite);

        holder.customFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Obtener el estado actual del ImageButton
                boolean currentState = holder.customFavoriteButton.isSelected();

                // Invertir el estado de favorito
                boolean isFavorite = !currentState;

                // Actualizar la apariencia del ImageButton
                holder.customFavoriteButton.setSelected(isFavorite);

                // Guardar o eliminar la experiencia favorita según el estado
                if (isFavorite) {
                    databaseHelper.guardarExperienciaFavorita(idExperiencia,idContenido,coTitulo);
                    Toast.makeText(context, "Experiencia añadida a favoritos", Toast.LENGTH_SHORT).show();
                } else {
                    databaseHelper.eliminarExperienciaFavorita(idContenido);
                    Toast.makeText(context, "Experiencia eliminada de favoritos", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }


    private void dialogCalificarExperiencia(int idExperiencia, float rating, int idContenido) {
        final Dialog dialog = new Dialog(context);
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
                                SharedPreferences sharedPreferences = context.getSharedPreferences("calificar_experiencia",context.MODE_PRIVATE);
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
                                Toast.makeText(context, error + "", Toast.LENGTH_SHORT).show();
                            }
                        }
                );
                requestQueue= Volley.newRequestQueue(context);
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

    private void dialogDetalle(String coTitulo, String coDescripcion) {

        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(false);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_diferencial);

        TextView txtDesc = dialog.findViewById(R.id.txtDescripcion);
        TextView tvTitle1 = dialog.findViewById(R.id.tvTitle1);
        TextView tvTitle2 = dialog.findViewById(R.id.tvTitle2);
        ImageView btnClose = dialog.findViewById(R.id.btnClose);
        Button btnAceptar = dialog.findViewById(R.id.btnAceptar);
        tvTitle2.setVisibility(View.GONE);


        tvTitle1.setText(coTitulo);
        txtDesc.setText(coDescripcion);

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

    @Override
    public int getItemCount() {
        return contenidoList.size();
    }

    public class ContenidoHolder extends RecyclerView.ViewHolder {
        TextView tvTitulo, tvDescripcion, tvVermas, tvCicloInicioFin;
        CardView cvFullScreen;
        ImageView ivContenido;
        WebView wvContenido;
        YouTubePlayerView ypvContenido;
        RatingBar rbCalificarExperiencia;
        ImageButton customFavoriteButton;
        ProgressBar progressBar;


        View view;

        public ContenidoHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
            tvCicloInicioFin = view.findViewById(R.id.tvCicloInicioFin);
            tvTitulo = view.findViewById(R.id.tvTitulo);
            tvDescripcion = view.findViewById(R.id.tvDescripcion);
            tvVermas = view.findViewById(R.id.tvVermas);
            cvFullScreen = view.findViewById(R.id.cvFullScreen);
            rbCalificarExperiencia = view.findViewById(R.id.rbCalificarExperiencia);
            ivContenido = view.findViewById(R.id.ivContenido);
            wvContenido = view.findViewById(R.id.wvContenido);
            ypvContenido = view.findViewById(R.id.ypvContenido);
            customFavoriteButton = view.findViewById(R.id.custom_favorite_button);
            progressBar = view.findViewById(R.id.progressBar);
            progressBar.setVisibility(View.GONE);
        }

        public void setTitulo(String coTitulo) { tvTitulo.setText(coTitulo); }

        public void setDescripcion(String coDescripcion) {
            tvDescripcion.setText(coDescripcion);
        }

        public void setTipoMedia(int idTipoMedia, String coUrlMedia) {
            switch(idTipoMedia){
                case 1:
                    ivContenido.setVisibility(View.VISIBLE);
                    Glide.with(context).load(coUrlMedia).into(ivContenido);
                    break;
                case 2:
                    ypvContenido.setVisibility(View.VISIBLE);
                    ((DetalleExperienciaActivity) context).getLifecycle().addObserver(ypvContenido);

                    ypvContenido.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                        @Override
                        public void onReady(YouTubePlayer youTubePlayer) {
                            String videoId = getYoutubeId(coUrlMedia);
                            youTubePlayer.loadVideo(videoId, 0); // Carga el video y comienza a reproducirlo automáticamente
                        }
                    });

                    break;
                case 3:
                    //wvContenido.setVisibility(View.VISIBLE);
                    progressBar.setVisibility(View.VISIBLE);
                    wvContenido.getSettings().setJavaScriptEnabled(true);
                    wvContenido.setWebViewClient(new WebViewClient() {
                        @Override
                        public void onPageStarted(WebView view, String url, Bitmap favicon) {

                            //progreso.show(); // Muestra el ProgressBar cuando se inicia la carga de la página
                        }

                        @Override
                        public void onPageFinished(WebView view, String url) {
                            progressBar.setVisibility(View.GONE);
                            wvContenido.setVisibility(View.VISIBLE);
                            //progreso.dismiss(); // Oculta el ProgressBar cuando la página ha terminado de cargarse
                        }
                    });
                    wvContenido.loadUrl(coUrlMedia);

                    break;
            }
        }

        public void setCiclos(String exCicloInicio, String exCicloFin) {
            tvCicloInicioFin.setText("Del Ciclo: " + exCicloInicio +" al " + exCicloFin);
        }
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
