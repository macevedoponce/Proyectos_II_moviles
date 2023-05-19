package com.acevedo.rutaexperienciauc.ui.favoritos;

import static com.acevedo.rutaexperienciauc.R.id.favorite_button_frag;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.adapter.FavoritosAdapter;
import com.acevedo.rutaexperienciauc.clases.Favorito;
import com.acevedo.rutaexperienciauc.clases.Sede;
import com.acevedo.rutaexperienciauc.ui.sedes.carreras.rutaExperiencia.experiencia.DetalleExperienciaActivity;
import com.acevedo.rutaexperienciauc.util.sqlite.FavoritosDatabaseHelper;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class FavoritosFragment extends Fragment {

    RecyclerView rvFavoritosAll;
    List<Favorito> listaFavoritos;
    FavoritosAdapter favoritosAdapter;
    FavoritosDatabaseHelper databaseHelper;
    TextView emptyTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_favoritos, container, false);
        rvFavoritosAll = vista.findViewById(R.id.rvFavoritosAll);
        rvFavoritosAll.setHasFixedSize(true);
        rvFavoritosAll.setLayoutManager(new LinearLayoutManager(getContext()));
        emptyTextView = vista.findViewById(R.id.emptyTextView);

        // Inicializa FavoritosDatabaseHelper
        databaseHelper = new FavoritosDatabaseHelper(getContext());

        // Obtiene la lista de favoritos desde la base de datos
        listaFavoritos = databaseHelper.getExperienciasFavoritas();

        // Configura el RecyclerView con el adaptador
        favoritosAdapter = new FavoritosAdapter(getContext(), listaFavoritos);
        rvFavoritosAll.setAdapter(favoritosAdapter);

        favoritosAdapter.setOnItemClickListener(new FavoritosAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Favorito favorito = listaFavoritos.get(position);
                // aquí poner para que vaya al detalleExperienciaActivity
                Intent i = new Intent(getContext(), DetalleExperienciaActivity.class);
                i.putExtra("idContenido",favorito.getIdContenido());
                i.putExtra("idTipoMedia",favorito.getIdTipoMedia());
                i.putExtra("coTitulo",favorito.getCoTitulo());
                i.putExtra("coDescripcion",favorito.getCoDescripcion());
                i.putExtra("coUrlMedia",favorito.getCoUrlMedia());
                startActivity(i);
            }

            @Override
            public void onFavoriteButtonClick(int position) {
                Favorito favorito = listaFavoritos.get(position);

                // Elimina el favorito de la lista y de la base de datos
                listaFavoritos.remove(position);
                databaseHelper.eliminarExperienciaFavorita(favorito.getIdContenido());

                // Notifica al adaptador que se ha eliminado un elemento
                favoritosAdapter.notifyItemRemoved(position);
                favoritosAdapter.notifyItemRangeChanged(position, listaFavoritos.size());

                // Si no hay más elementos en la lista
                if (listaFavoritos.isEmpty()) {
                    showEmptyState();
                }
            }
        });

        // Si no hay elementos en la lista
        if (listaFavoritos.isEmpty()) {
            showEmptyState();
        }

        return vista;
    }
    @Override
    public void onResume() {
        super.onResume();
        // Actualiza la lista de favoritos en onResume
        listaFavoritos = databaseHelper.getExperienciasFavoritas();
        favoritosAdapter.setData(listaFavoritos);

        if (listaFavoritos.isEmpty()) {
            showEmptyState();
        } else {
            hideEmptyState();
        }
    }
    private void showEmptyState() {
        rvFavoritosAll.setVisibility(View.GONE);
        emptyTextView.setVisibility(View.VISIBLE);
        emptyTextView.setText("La lista de favoritos está vacía");
    }
    private void hideEmptyState() {
        rvFavoritosAll.setVisibility(View.VISIBLE);
        emptyTextView.setVisibility(View.GONE);
    }

}