package com.acevedo.rutaexperienciauc.ui.ayuda;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.acevedo.rutaexperienciauc.R;

public class AyudaFragment extends Fragment {

    Button btnExperiencias;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_ayuda, container, false);
        btnExperiencias = vista.findViewById(R.id.btnExperienciasCarrera1);

        btnExperiencias.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //codigo que reemplaza el fragment inicio por el fragment solicitar información
                //ListExperienciasFragment listExperienciasFragment = new ListExperienciasFragment(); // inicializa el fragment

//                Bundle args = new Bundle();
//                args.putInt("idCarrera",1);
//                args.putInt("exCiclo", 5);
//                listExperienciasFragment.setArguments(args);
//
//                getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,listExperienciasFragment).addToBackStack(null).commit(); // reemplaza el contenedor del fragment con el nuevo fragment

            }
        });
        return vista;
    }
}