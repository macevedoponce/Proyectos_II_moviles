package com.acevedo.rutaexperienciauc.clases;

public class Sede {
    int id;
    String nombre;
    String adress;
    String SeReferencia;
    String SeTelefono;
    String imagen_url;

    public Sede(){

    }

    public Sede(int id, String nombre, String adress, String seReferencia, String seTelefono, String imagen_url) {
        this.id = id;
        this.nombre = nombre;
        this.adress = adress;
        this.SeReferencia = seReferencia;
        this.SeTelefono = seTelefono;
        this.imagen_url = imagen_url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getAdress() {
        return adress;
    }

    public void setAdress(String adress) {
        this.adress = adress;
    }

    public String getSeReferencia() {
        return SeReferencia;
    }

    public void setSeReferencia(String seReferencia) {
        SeReferencia = seReferencia;
    }

    public String getSeTelefono() {
        return SeTelefono;
    }

    public void setSeTelefono(String seTelefono) {
        SeTelefono = seTelefono;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }
}
