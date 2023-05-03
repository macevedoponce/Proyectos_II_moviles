package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;
import com.bumptech.glide.Glide;

public class DetalleExperienciaActivity extends AppCompatActivity {

    ImageView ivIcono, ivContenido;
    TextView tvTitulo, tvDescripcion, tvVermas;

    Integer IdExperiencia;
    String ExIconoUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_experiencia);

        Intent intent = getIntent();
        String ExIconoUrl = intent.getStringExtra("exIconoUrl");
        int IdExperiencia = intent.getIntExtra("idExperiencia", 0);


        ivIcono = findViewById(R.id.ivIcono);
        tvTitulo = findViewById(R.id.tvTitulo);
        tvTitulo.setText(IdExperiencia+"");
        Glide.with(this).load(ExIconoUrl).into(ivIcono);
    }
}