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
    private OnItemClickListener listener;

    public FavoritosAdapter(Context context, List<Favorito> favoritosList) {
        this.context = context;
        this.favoritosList = favoritosList;
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onFavoriteButtonClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public FavoritoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.favorito_item, parent, false);
        return new FavoritoViewHolder(view, listener);
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

    public void setData(List<Favorito> favoritosList) {
        this.favoritosList = favoritosList;
        notifyDataSetChanged();
    }

    public class FavoritoViewHolder extends RecyclerView.ViewHolder {

        private TextView nombreTextView;
        private ImageButton favoriteButton;

        public FavoritoViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            nombreTextView = itemView.findViewById(R.id.tvNombreExperiencia);
            favoriteButton = itemView.findViewById(R.id.favorite_button_frag);

            // Asignar clic al elemento de la lista
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

            // Asignar clic al botón de favorito
            favoriteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onFavoriteButtonClick(position);
                        }
                    }
                }
            });
        }

        public void bind(Favorito favorito) {
            nombreTextView.setText(favorito.getCoTitulo());
        }
    }
}