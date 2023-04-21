package com.acevedo.rutaexperienciauc.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.MainActivity;
import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.ui.inicio.InicioFragment;
import com.bumptech.glide.Glide;

import java.util.Calendar;

public class splash extends AppCompatActivity {
    TextView Txtsplash_año,txtDerechosReservados;
    ImageView imgSplashLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //animacion
        Animation animacion1 = AnimationUtils.loadAnimation(this, R.anim.splash_top);
        Animation animacion2 = AnimationUtils.loadAnimation(this, R.anim.splash_bottom);

        Txtsplash_año = findViewById(R.id.Txtsplash_año);
        imgSplashLogo = findViewById(R.id.imgSplashLogo);
        txtDerechosReservados = findViewById(R.id.txtDerechosReservados);

        Txtsplash_año.setAnimation(animacion2);
        imgSplashLogo.setAnimation(animacion1);
        txtDerechosReservados.setAnimation(animacion2);




        //Añadimos un temporizador para pasar al inicio
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(splash.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        }, 3000); //El número es el tiempo en milisegundos

        //Obtenemos el año
        int year = Calendar.getInstance().get(Calendar.YEAR);

        //Enviamos el año al textView
        Txtsplash_año.setText(String.valueOf(year));

    }
}