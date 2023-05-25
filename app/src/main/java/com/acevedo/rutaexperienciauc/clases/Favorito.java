package com.acevedo.rutaexperienciauc.clases;

public class Favorito {
    int idExperiencia;
    int idContenido;
    String coTitulo;
    boolean isFavorite;

    public Favorito(int idExperiencia ,int idContenido, String coTitulo, boolean isFavorite) {
        this.idExperiencia = idExperiencia;
        this.idContenido = idContenido;
        this.coTitulo = coTitulo;
        this.isFavorite = isFavorite;
    }

    public int getIdExperiencia() {
        return idExperiencia;
    }

    public void setIdExperiencia(int idExperiencia) {
        this.idExperiencia = idExperiencia;
    }

    public int getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(int idContenido) {
        this.idContenido = idContenido;
    }

    public String getCoTitulo() {
        return coTitulo;
    }

    public void setCoTitulo(String coTitulo) {
        this.coTitulo = coTitulo;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}