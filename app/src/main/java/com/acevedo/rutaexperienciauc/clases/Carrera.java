package com.acevedo.rutaexperienciauc.clases;

public class Carrera {
    int id;
    String nombre;
    String descripcion;
    String planEstudios_url;
    int cantidadCiclos;
    int idSede;

    public Carrera() {

    }

    public Carrera(int id, String nombre, String descripcion, String planEstudios_url, int cantidadCiclos, int idSede) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.planEstudios_url = planEstudios_url;
        this.cantidadCiclos = cantidadCiclos;
        this.idSede = idSede;
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

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getPlanEstudios_url() {
        return planEstudios_url;
    }

    public void setPlanEstudios_url(String planEstudios_url) {
        this.planEstudios_url = planEstudios_url;
    }

    public int getCantidadCiclos() {
        return cantidadCiclos;
    }

    public void setCantidadCiclos(int cantidadCiclos) {
        this.cantidadCiclos = cantidadCiclos;
    }

    public int getIdSede() {
        return idSede;
    }

    public void setIdSede(int idSede) {
        this.idSede = idSede;
    }
}