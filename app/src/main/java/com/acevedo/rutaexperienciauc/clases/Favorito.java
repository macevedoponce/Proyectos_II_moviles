package com.acevedo.rutaexperienciauc.clases;

public class Favorito {
    private int idExperiencia;
    private String nombreExperiencia;
    private boolean isFavorite;

    public Favorito() {

    }

    public Favorito(int idExperiencia, String nombreExperiencia, boolean isFavorite) {
        this.idExperiencia = idExperiencia;
        this.nombreExperiencia = nombreExperiencia;
        this.isFavorite = isFavorite;
    }

    public int getIdExperiencia() {
        return idExperiencia;
    }

    public void setIdExperiencia(int idExperiencia) {
        this.idExperiencia = idExperiencia;
    }

    public String getNombreExperiencia() {
        return nombreExperiencia;
    }

    public void setNombreExperiencia(String nombreExperiencia) {
        this.nombreExperiencia = nombreExperiencia;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}