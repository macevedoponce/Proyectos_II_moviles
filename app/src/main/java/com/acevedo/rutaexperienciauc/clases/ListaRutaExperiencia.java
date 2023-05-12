package com.acevedo.rutaexperienciauc.clases;

public class ListaRutaExperiencia {
    private int imgCiclos;

    public ListaRutaExperiencia(int imgCiclos) {
        this.imgCiclos = imgCiclos;
    }

    public int getImgCiclos() {
        return imgCiclos;
    }

    public void setImgCiclos(int imgCiclos) {
        this.imgCiclos = imgCiclos;
    }

    @Override
    public String toString() {
        return "RutaExperiencia{" +
                "imgCiclos=" + imgCiclos +
                '}';
    }
}
