package com.acevedo.rutaexperienciauc.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.acevedo.rutaexperienciauc.R;
import com.acevedo.rutaexperienciauc.clases.Favorito;

import java.util.List;

public class FavoritosAdapter extends RecyclerView.Adapter<FavoritosAdapter.FavoritosHolder> implements View.OnClickListener {
    private Context context;
    private List<Favorito> favoritosList;
    private View.OnClickListener listener;

    public FavoritosAdapter(Context context, List<Favorito> favoritosList) {
        this.context = context;
        this.favoritosList = favoritosList;
    }

    @NonNull
    @Override
    public FavoritosHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View mView = LayoutInflater.from(context).inflate(R.layout.favorito_item, parent, false);
        mView.setOnClickListener(this);
        return new FavoritosHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull FavoritosHolder holder, int position) {
        Favorito favoritos = favoritosList.get(position);
        holder.setNombreExperiencia(favoritos.getNombreExperiencia());
        holder.setFavorite(favoritos.isFavorite());
    }

    @Override
    public int getItemCount() {
        return favoritosList.size();
    }

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void onClick(View v) {
        if (listener != null) {
            listener.onClick(v);
        }
    }

    public class FavoritosHolder extends RecyclerView.ViewHolder {
        private TextView tvNombreExperiencia;
        private ImageView ivFavorite;

        public FavoritosHolder(@NonNull View itemView) {
            super(itemView);
            tvNombreExperiencia = itemView.findViewById(R.id.tvNombreExperiencia);
            ivFavorite = itemView.findViewById(R.id.favorite_button_fragment);
        }

        public void setNombreExperiencia(String nombreExperiencia) {
            tvNombreExperiencia.setText(nombreExperiencia);
        }

        public void setFavorite(boolean isFavorite) {
            if (isFavorite) {
                ivFavorite.setImageResource(R.drawable.ic_favorite);
            } else {
                ivFavorite.setImageResource(R.drawable.ic_favorite_border);
            }
        }
    }
}