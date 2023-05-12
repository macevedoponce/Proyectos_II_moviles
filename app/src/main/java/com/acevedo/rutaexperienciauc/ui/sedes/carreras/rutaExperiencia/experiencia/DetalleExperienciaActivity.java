package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DetalleExperienciaActivity extends AppCompatActivity {

    TextView tvTitulo, tvDescripcion, tvVermas;
    CardView cvFullScreen;
    LinearLayout llVolver;

    ImageView ivContenido;
    WebView wvContenido;
    YouTubePlayerView ypContenido;

    RatingBar ratingBar;
    ImageButton customFavoriteButton;
    SharedPreferences sharedPreferences;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_experiencia);

        tvTitulo = findViewById(R.id.tvTitulo);
        tvDescripcion = findViewById(R.id.tvDescripcion);
        tvVermas = findViewById(R.id.tvVermas);
        ratingBar = findViewById(R.id.ratingBar);
        cvFullScreen = findViewById(R.id.cvFullScreen);
        llVolver = findViewById(R.id.llVolver);
        ivContenido = findViewById(R.id.ivContenido);
        wvContenido = findViewById(R.id.wvContenido);
        //ypContenido = findViewById(R.id.ypContenido);
        customFavoriteButton = findViewById(R.id.custom_favorite_button);
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

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
        int idContenido = getIntent().getIntExtra("idContenido",0);
        int idTipoMedia = getIntent().getIntExtra("idTipoMedia",0);
        String coTitulo = getIntent().getStringExtra("coTitulo");
        String coDescripcion = getIntent().getStringExtra("coDescripcion");
        String coUrlMedia = getIntent().getStringExtra("coUrlMedia");

        tvTitulo.setText(coTitulo);
        tvDescripcion.setText(coDescripcion);
        switch(idTipoMedia){
            case 1:
                ivContenido.setVisibility(View.VISIBLE);
                Glide.with(this).load(coUrlMedia).into(ivContenido);
                //Toast.makeText(this, "Imagen", Toast.LENGTH_SHORT).show();
                break;
            case 2:

                //ypContenido.setVisibility(View.VISIBLE);
                wvContenido.setVisibility(View.VISIBLE);
                ypContenido = new YouTubePlayerView(this);
                wvContenido.getSettings().setJavaScriptEnabled(true);
                wvContenido.getSettings().setDomStorageEnabled(true);
                wvContenido.setWebChromeClient(new WebChromeClient());

                wvContenido.addJavascriptInterface(ypContenido,"YouTubePlayer"); //contenido 360
                String idVideo = getYoutubeId(coUrlMedia);
                wvContenido.loadDataWithBaseURL("https://www.youtube.com", getHTMLString(idVideo), "text/html", "UTF-8", null);


//                wvContenido.setVisibility(View.VISIBLE);
//                wvContenido.getSettings().setJavaScriptEnabled(true);
//                wvContenido.loadUrl("https://www.youtube.com/watch?v=SeqgxNQxubo");
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

    private String getHTMLString(String mVideoId) {
        return "<html><head>" +
                "<style>body{margin:0;padding:0;}</style>" +
                "</head><body>" +
                "<iframe id=\"player\" type=\"text/html\" width=\"100%\" height=\"100%\" src=\"https://www.youtube.com/embed/" + mVideoId + "?enablejsapi=1&playsinline=1&iv_load_policy=3&modestbranding=1&fs=0&controls=1&rel=0&showinfo=0&start=0&end=0&autoplay=1\" frameborder=\"0\"></iframe>" +
                "<script src=\"https://www.youtube.com/iframe_api\"></script>" +
                "<script>" +
                "var player;" +
                "function onYouTubeIframeAPIReady() {" +
                "player = new YT.Player('player', {" +
                "events: {" +
                "onReady: onPlayerReady," +
                "onStateChange: onPlayerStateChange" +
                "}" +
                "});" +
                "}" +
                "function onPlayerReady(event) {" +
                "event.target.playVideo();" +
                "}" +
                "function onPlayerStateChange(event) {" +
                "if(event.data == YT.PlayerState.PLAYING) {" +
                "YouTubePlayer.onPlaying();" +
                "}" +
                "}" +
                "</script>" +
                "</body></html>";
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