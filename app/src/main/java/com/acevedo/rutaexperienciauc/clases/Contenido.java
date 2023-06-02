package com.acevedo.rutaexperienciauc.clases;

public class Contenido {
    int idExperiencia;
    int idContenido;
    int idTipoMedia;
    String coTitulo;
    String coDescripcion;
    String coUrlMedia;
    String exCicloInicio;
    String exCicloFin;

    public Contenido(int idExperiencia, int idContenido, int idTipoMedia, String coTitulo, String coDescripcion, String coUrlMedia, String exCicloInicio, String exCicloFin) {
        this.idExperiencia = idExperiencia;
        this.idContenido = idContenido;
        this.idTipoMedia = idTipoMedia;
        this.coTitulo = coTitulo;
        this.coDescripcion = coDescripcion;
        this.coUrlMedia = coUrlMedia;
        this.exCicloInicio = exCicloInicio;
        this.exCicloFin = exCicloFin;
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

    public String getExCicloInicio() {
        return exCicloInicio;
    }

    public void setExCicloInicio(String exCicloInicio) {
        this.exCicloInicio = exCicloInicio;
    }

    public String getExCicloFin() {
        return exCicloFin;
    }

    public void setExCicloFin(String exCicloFin) {
        this.exCicloFin = exCicloFin;
    }
}
