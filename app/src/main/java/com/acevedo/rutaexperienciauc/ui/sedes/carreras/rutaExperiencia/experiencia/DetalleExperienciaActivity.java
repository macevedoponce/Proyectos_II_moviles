package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.acevedo.rutaexperienciauc.R;

public class DetalleExperienciaActivity extends AppCompatActivity {

    TextView tvTitulo, tvDescripcion, tvVermas;
    CardView cvFullScreen;
    LinearLayout llVolver;

    ImageView ivContenido;
    WebView wvContenido;

    RatingBar ratingBar;

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

        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        cargarExperiencia();

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

                //Toast.makeText(this, "Imagen", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                wvContenido.setVisibility(View.VISIBLE);
                wvContenido.getSettings().setJavaScriptEnabled(true);
                wvContenido.loadUrl(coUrlMedia);
                //Toast.makeText(this, "Video", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(this, "Imagen 360", Toast.LENGTH_SHORT).show();
                break;
        }

    }
}