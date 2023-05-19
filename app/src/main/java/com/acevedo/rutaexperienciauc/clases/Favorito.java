package com.acevedo.rutaexperienciauc.clases;

public class Favorito {
    int idContenido;
    int idTipoMedia;
    String coTitulo;
    String coDescripcion;
    String coUrlMedia;
    boolean isFavorite;

    public Favorito(int idContenido, int idTipoMedia, String coTitulo, String coDescripcion, String coUrlMedia, boolean isFavorite) {
        this.idContenido = idContenido;
        this.idTipoMedia = idTipoMedia;
        this.coTitulo = coTitulo;
        this.coDescripcion = coDescripcion;
        this.coUrlMedia = coUrlMedia;
        this.isFavorite = isFavorite;
    }

    public int getIdContenido() {
        return idContenido;
    }

    public void setIdContenido(int idContenido) {
        this.idContenido = idContenido;
    }

    public int getIdTipoMedia() {
        return idTipoMedia;
    }

    public void setIdTipoMedia(int idTipoMedia) {
        this.idTipoMedia = idTipoMedia;
    }

    public String getCoTitulo() {
        return coTitulo;
    }

    public void setCoTitulo(String coTitulo) {
        this.coTitulo = coTitulo;
    }

    public String getCoDescripcion() {
        return coDescripcion;
    }

    public void setCoDescripcion(String coDescripcion) {
        this.coDescripcion = coDescripcion;
    }

    public String getCoUrlMedia() {
        return coUrlMedia;
    }

    public void setCoUrlMedia(String coUrlMedia) {
        this.coUrlMedia = coUrlMedia;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}