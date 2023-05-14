package com.acevedo.rutaexperienciauc.clases;

public class Beneficio {
    int idBeneficio;
    String beDescripcion;

    public Beneficio() {
    }

    public Beneficio(int idBeneficio, String beDescripcion) {
        this.idBeneficio = idBeneficio;
        this.beDescripcion = beDescripcion;
    }

    public int getIdBeneficio() {
        return idBeneficio;
    }

    public void setIdBeneficio(int idBeneficio) {
        this.idBeneficio = idBeneficio;
    }

    public String getBeDescripcion() {
        return beDescripcion;
    }

    public void setBeDescripcion(String beDescripcion) {
        this.beDescripcion = beDescripcion;
    }
}
