package com.acevedo.rutaexperienciauc.clases;

public class Sede {
    int id;
    String nombre;
    String adress;
    String phone;
    String imagen_url;

    public Sede(){

    }

    public Sede(int id, String nombre, String adress, String phone, String imagen_url) {
        this.id = id;
        this.nombre = nombre;
        this.adress = adress;
        this.phone = phone;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getImagen_url() {
        return imagen_url;
    }

    public void setImagen_url(String imagen_url) {
        this.imagen_url = imagen_url;
    }
}
