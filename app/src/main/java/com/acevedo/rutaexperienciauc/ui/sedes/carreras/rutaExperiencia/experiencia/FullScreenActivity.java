package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullScreenActivity extends AppCompatActivity {

    CardView cvX;
    ImageView ivFullScreen;
    WebView wvFullScreen;
    YouTubePlayerView ypvContenido;

    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        wvFullScreen = findViewById(R.id.wvFullScreen);
        ivFullScreen = findViewById(R.id.ivFullScreen);
        ypvContenido = findViewById(R.id.ypvContenido);
        progressBar = findViewById(R.id.progressBar);
        cvX = findViewById(R.id.cvX);
        cargarContenido();

        cvX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void cargarContenido() {
        String coUrlMedia = getIntent().getStringExtra("coUrlMedia");
        int idTipoMedia = getIntent().getIntExtra("idTipoMedia",0);

        switch (idTipoMedia){
            case 1 :
                ivFullScreen.setVisibility(View.VISIBLE);
                Glide.with(this).load(coUrlMedia).into(ivFullScreen);
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
                //wvFullScreen.setVisibility(View.VISIBLE);
                progressBar.setVisibility(View.VISIBLE);
                wvFullScreen.getSettings().setJavaScriptEnabled(true);
                wvFullScreen.setWebViewClient(new WebViewClient() {
                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {

                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        progressBar.setVisibility(View.GONE);
                        wvFullScreen.setVisibility(View.VISIBLE);
                    }
                });
                wvFullScreen.loadUrl(coUrlMedia);
                break;
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