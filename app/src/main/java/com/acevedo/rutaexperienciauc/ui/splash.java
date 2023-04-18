package com.acevedo.rutaexperienciauc.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.MainActivity;
import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.ui.inicio.InicioFragment;
import com.bumptech.glide.Glide;

import java.util.Calendar;

public class splash extends AppCompatActivity {
    TextView Txtsplash_año;
    ImageView imgSplashFondo,imgSplashLogo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Txtsplash_año = findViewById(R.id.Txtsplash_año);
        imgSplashLogo = findViewById(R.id.imgSplashLogo);
        imgSplashFondo = findViewById(R.id.imgSplashFondo);

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

//        //Encontramos la imagen del logo de la universidad
//        Glide.with(this)
//                .load("https://scontent.flim2-2.fna.fbcdn.net/v/t1.6435-9/40160004_1820551124696091_2401308222259462144_n.jpg?_nc_cat=106&ccb=1-7&_nc_sid=174925&_nc_eui2=AeHN_Oua9_w6G_vTcE24I-USx9mPdfQA97HH2Y919AD3sQgwhwQ1gv80r4sBqgkjhSzD_RI4Nd_ZyzzNdD6-jrQ6&_nc_ohc=yk_Fg2hgZWsAX9WftJg&_nc_ht=scontent.flim2-2.fna&oh=00_AfCUpJqs6E4oFZ7vhi9HGjX2YOFWNaTSUTPDhhUqtPIHSA&oe=64661898")
//                .into(imgSplashLogo);
//
        //Encontramos la imagen del fondo de la universidad
//        Glide.with(this)
//                .load("https://ucontinental.edu.pe/www/wp-content/uploads/2023/03/campus-huancayo-11.jpg")
//                .into(imgSplashFondo);
    }
}