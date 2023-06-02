package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.RecibirPDF;
import com.github.barteksc.pdfviewer.PDFView;

public class PlanEstudiosActivity extends AppCompatActivity {
    ProgressBar progressBar;
    PDFView pdfPlanEstudios;

    LinearLayout llVolver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_estudios);

        progressBar = findViewById(R.id.progressBar);
        pdfPlanEstudios = findViewById(R.id.pdfPlanEstudios);
        llVolver = findViewById(R.id.llVolver);
        String pdfUrl = getIntent().getStringExtra("planEstudiosUrl");
        new RecibirPDF(pdfPlanEstudios,progressBar).execute(pdfUrl);

        llVolver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}