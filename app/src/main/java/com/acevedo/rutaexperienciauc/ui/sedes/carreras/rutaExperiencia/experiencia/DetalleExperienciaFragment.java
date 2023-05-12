package com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.bumptech.glide.Glide;

public class DetalleExperienciaFragment extends Fragment {

    ImageView ivIcono, ivContenido;
    TextView tvTitulo, tvDescripcion, tvVermas;

    Integer idExperiencia;
    String exIconoUrl;
    ImageButton customFavoriteButton;
    SharedPreferences sharedPreferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        idExperiencia = getArguments().getInt("idCarrera");
        exIconoUrl = getArguments().getString("iconoUrl");
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
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
        customFavoriteButton = vista.findViewById(R.id.custom_favorite_button);
        customFavoriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isFavorite = sharedPreferences.getBoolean("rutaExperiencia" + idExperiencia, false);
                if (isFavorite) {
                    customFavoriteButton.setSelected(false);
                    sharedPreferences.edit().remove("rutaExperiencia" + idExperiencia).apply();
                    String miValor = sharedPreferences.getString("rutaExperiencia","rutaExperiencia");
                    Toast.makeText(getActivity(),miValor , Toast.LENGTH_SHORT).show();
                } else {
                    customFavoriteButton.setSelected(true);
                    sharedPreferences.edit().putBoolean("rutaExperiencia" + idExperiencia, true).apply();
                }
            }
        });
        boolean isFavorite = sharedPreferences.getBoolean("rutaExperiencia" + idExperiencia, false);
        customFavoriteButton.setSelected(isFavorite);
        return vista;
    }
}