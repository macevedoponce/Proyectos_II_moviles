package com.acevedo.rutaexperienciauc.ui.solicitarInformacion;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;

public class PopupSolicitarInfo extends AppCompatActivity {

    Button btnPopupAceptar;
    TextView txtFelicitaciones;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_popup_solicitar_info);

        // Obtener referencias a los elementos de la interfaz
        btnPopupAceptar = findViewById(R.id.btnPopupAceptar);
        txtFelicitaciones = findViewById(R.id.txtFelicitaciones);

        //Establecer un degradado en el texto
        int startColor = Color.parseColor("#A304A3");
        int endColor = Color.parseColor("#0204C6");
        Shader textShader = new LinearGradient(270,270,txtFelicitaciones.getWidth(), txtFelicitaciones.getLineHeight(),
                new int []{startColor, endColor}, null, Shader.TileMode.CLAMP);
        txtFelicitaciones.getPaint().setShader(textShader);

        // Establecer el diseño y el comportamiento del diálogo emergente
        getWindow().setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        getWindow().setGravity(Gravity.CENTER);
        setFinishOnTouchOutside(true);

        // Configurar el clic del botón "Aceptar" para finalizar la actividad
        btnPopupAceptar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}