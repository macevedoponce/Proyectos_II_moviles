package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;
import com.bumptech.glide.Glide;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FullScreenActivity extends AppCompatActivity {

    CardView cvVolver,cvX;
    ImageView ivFullScreen;
    WebView wvFullScreen;
    YouTubePlayerView ypContenido;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_full_screen);
        cvVolver = findViewById(R.id.cvVolver);
        wvFullScreen = findViewById(R.id.wvFullScreen);
        ivFullScreen = findViewById(R.id.ivFullScreen);
        cvX = findViewById(R.id.cvX);
        cargarContenido();

        cvX.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        cvVolver.setOnClickListener(new View.OnClickListener() {
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
                cvVolver.setVisibility(View.VISIBLE);
                wvFullScreen.setVisibility(View.VISIBLE);
                ypContenido = new YouTubePlayerView(this);
                wvFullScreen.getSettings().setJavaScriptEnabled(true);
                wvFullScreen.getSettings().setDomStorageEnabled(true);
                wvFullScreen.setWebChromeClient(new WebChromeClient());

                wvFullScreen.addJavascriptInterface(ypContenido,"YouTubePlayer");
                String idVideo = getYoutubeId(coUrlMedia);
                wvFullScreen.loadDataWithBaseURL("https://www.youtube.com", getHTMLString(idVideo), "text/html", "UTF-8", null);

                break;
            case 3:
                wvFullScreen.setVisibility(View.VISIBLE);
                wvFullScreen.getSettings().setJavaScriptEnabled(true);
                wvFullScreen.loadUrl(coUrlMedia);
                break;
        }

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