package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.acevedo.rutaexperienciauc.R;
import com.bumptech.glide.Glide;

public class DetalleExperienciaFragment extends Fragment {

    ImageView ivIcono, ivContenido;
    TextView tvTitulo, tvDescripcion, tvVermas;

    Integer idExperiencia;
    String exIconoUrl;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idExperiencia = getArguments().getInt("idCarrera");
        exIconoUrl = getArguments().getString("iconoUrl");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_detalle_experiencia, container, false);
        ivIcono = vista.findViewById(R.id.ivIcono);
        tvTitulo = vista.findViewById(R.id.tvTitulo);
        tvTitulo.setText(idExperiencia+"");
        Glide.with(this).load(exIconoUrl).into(ivIcono);
        return vista;
    }
}