package com.acevedo.rutaexperienciauc.clases;

public class Experiencia {

    int idExperiencia;
    String exNombre;
    String exIconoUrl;

    public Experiencia() {
    }

    public Experiencia(int idExperiencia, String exNombre, String exIconoUrl) {
        this.idExperiencia = idExperiencia;
        this.exNombre = exNombre;
        this.exIconoUrl = exIconoUrl;
    }

    public int getIdExperiencia() {
        return idExperiencia;
    }

    public void setIdExperiencia(int idExperiencia) {
        this.idExperiencia = idExperiencia;
    }

    public String getExNombre() {
        return exNombre;
    }

    public void setExNombre(String exNombre) {
        this.exNombre = exNombre;
    }

    public String getExIconoUrl() {
        return exIconoUrl;
    }

    public void setExIconoUrl(String exIconoUrl) {
        this.exIconoUrl = exIconoUrl;
    }
}
