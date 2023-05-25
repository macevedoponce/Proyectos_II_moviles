package com.acevedo.rutaexperienciauc.clases;

public class Contenido {
    int idExperiencia;
    int idContenido;
    int idTipoMedia;
    String coTitulo;
    String coDescripcion;
    String coUrlMedia;

    public Contenido(int idExperiencia,int idContenido, int idTipoMedia, String coTitulo, String coDescripcion, String coUrlMedia) {
        this.idExperiencia = idExperiencia;
        this.idContenido = idContenido;
        this.idTipoMedia = idTipoMedia;
        this.coTitulo = coTitulo;
        this.coDescripcion = coDescripcion;
        this.coUrlMedia = coUrlMedia;
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
}
