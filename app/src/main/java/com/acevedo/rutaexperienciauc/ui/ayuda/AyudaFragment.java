package com.acevedo.rutaexperienciauc.ui.ayuda;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia.ListExperienciasActivity;

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
//                ListExperienciasFragment listExperienciasFragment = new ListExperienciasFragment(); // inicializa el fragment
//
//                Bundle args = new Bundle();
//                args.putInt("idCarrera",1);
//                args.putInt("exCiclo", 5);
//                listExperienciasFragment.setArguments(args);
//
//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                transaction.replace(R.id.nav_host_fragment_activity_main, listExperienciasFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();


//                FragmentTransaction transaction = getFragmentManager().beginTransaction();
//                ListExperienciasFragment listExperienciasFragment = new ListExperienciasFragment();
//                Bundle args = new Bundle();
//                args.putInt("idCarrera", 1);
//                args.putInt("exCiclo", 5);
//                listExperienciasFragment.setArguments(args);
//                transaction.replace(R.id.fragment_container_ayuda, listExperienciasFragment);
//                transaction.addToBackStack(null);
//                transaction.commit();

                //getFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_activity_main,listExperienciasFragment).addToBackStack(null).commit(); // reemplaza el contenedor del fragment con el nuevo fragment

                Intent i = new Intent(getContext(), ListExperienciasActivity.class);
                i.putExtra("idCarrera",1);
                i.putExtra("exCiclo",5);
                startActivity(i);

            }
        });
        return vista;
    }
}