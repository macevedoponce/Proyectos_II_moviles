package com.acevedo.rutaexperienciauc;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.acevedo.rutaexperienciauc.databinding.ActivityMainBinding;

import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    ImageButton whatsAppButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        whatsAppButton = findViewById(R.id.whatsapp_button);

        whatsAppButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phoneNumberWithCountryCode = "+51989485010"; //número de teléfono incluyendo el código de país 51
                String message = "Solicito informacion"; // Mensaje que se va enviar a whatsapp

                try {
                    String url = "https://api.whatsapp.com/send?phone=" + phoneNumberWithCountryCode + "&text=" + URLEncoder.encode(message, "UTF-8");
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse(url));
                    startActivity(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, "No se puede abrir WhatsApp", Toast.LENGTH_SHORT).show();
                }
            }
        });


        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_favoritos, R.id.navigation_sedes,R.id.navigation_inicio, R.id.navigation_ayuda, R.id.navigation_soporte)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_activity_main);
        //NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(binding.navView, navController);
    }

}