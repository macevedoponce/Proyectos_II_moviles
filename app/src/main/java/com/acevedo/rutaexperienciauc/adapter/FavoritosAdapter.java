package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.Favorito;
import com.acevedo.rutaexperienciauc.util.sqlite.FavoritosDatabaseHelper;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritoViewHolder> {
    private List<Favorito> favoritosList;
    private Context context;
    private FavoritosDatabaseHelper databaseHelper;

    public FavoritosAdapter(Context context, List<Favorito> favoritosList) {
        this.context = context;
        this.favoritosList = favoritosList;
        databaseHelper = new FavoritosDatabaseHelper(context);
    }

    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorito_item, parent, false);
        return new FavoritoViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritoViewHolder holder, int position) {
        Favorito favorito = favoritosList.get(position);
        holder.bind(favorito);
    }

    @Override
    public int getItemCount() {
        return favoritosList.size();
    }

    public class FavoritoViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView nombreTextView;
        private ImageButton favoriteButton;

        public FavoritoViewHolder(@NonNull View itemView) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.tvNombreExperiencia);
            favoriteButton = itemView.findViewById(R.id.favoriteButtonFragment);

            favoriteButton.setOnClickListener(this);
        }

        public void bind(Favorito favorito) {
            nombreTextView.setText(favorito.getNombreExperiencia());
            favoriteButton.setSelected(favorito.isFavorite());
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            if (position != RecyclerView.NO_POSITION) {
                Favorito favorito = favoritosList.get(position);
                GuardarExperienciaFavorito(favorito.getIdExperiencia(), favorito.getNombreExperiencia());
            }
        }
    }

    private void GuardarExperienciaFavorito(int idExperiencia, String nombreExperiencia) {
        boolean isFavorite = databaseHelper.isExperienciaFavorita(idExperiencia);

        if (isFavorite) {
            databaseHelper.eliminarExperienciaFavorita(idExperiencia);
            Toast.makeText(context, "Experiencia eliminada de favoritos", Toast.LENGTH_SHORT).show();
        } else {
            databaseHelper.guardarExperienciaFavorita(idExperiencia, nombreExperiencia);
            Toast.makeText(context, "Experiencia añadida a favoritos", Toast.LENGTH_SHORT).show();
        }

        notifyDataSetChanged();
    }
}