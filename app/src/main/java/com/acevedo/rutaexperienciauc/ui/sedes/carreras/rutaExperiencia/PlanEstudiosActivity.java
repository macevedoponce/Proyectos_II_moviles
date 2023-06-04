package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;

public class PlanEstudiosActivity extends AppCompatActivity {
    ProgressBar progressBar;
    WebView wvPlanEstudios;

    LinearLayout llVolver;
    //ProgressDialog progreso;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_estudios);

//        progreso = new ProgressDialog(this);
//        progreso.setMessage("Cargando Plan de Estudios");
//        progreso.setCancelable(false);

        wvPlanEstudios = findViewById(R.id.wvPlanEstudios);
        progressBar = findViewById(R.id.progressBar);
        llVolver = findViewById(R.id.llVolver);
        String pdfUrl = getIntent().getStringExtra("planEstudiosUrl");
        wvPlanEstudios.getSettings().setJavaScriptEnabled(true);
        wvPlanEstudios.getSettings().setBuiltInZoomControls(true);
        wvPlanEstudios.getSettings().setDisplayZoomControls(false);
        progressBar.setVisibility(View.VISIBLE);
        wvPlanEstudios.setVisibility(View.GONE);
        wvPlanEstudios.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                progressBar.setVisibility(View.VISIBLE);
                //progreso.show(); // Muestra el ProgressBar cuando se inicia la carga de la página
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                progressBar.setVisibility(View.GONE);
                wvPlanEstudios.setVisibility(View.VISIBLE);
                //progreso.dismiss(); // Oculta el ProgressBar cuando la página ha terminado de cargarse
            }
        });

        wvPlanEstudios.loadUrl("https://docs.google.com/gview?embedded=true&url="+pdfUrl);

        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
    }
}