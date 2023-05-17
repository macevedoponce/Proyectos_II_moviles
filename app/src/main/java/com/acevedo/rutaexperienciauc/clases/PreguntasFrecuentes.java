package com.acevedo.rutaexperienciauc.clases;

public class PreguntasFrecuentes {
    int IdPreguntaFrecuente;
    String PfPregunta;
    String PfRespuesta;

    public PreguntasFrecuentes(int idPreguntaFrecuente, String pfPregunta, String pfRespuesta) {
        this.IdPreguntaFrecuente = idPreguntaFrecuente;
        this.PfPregunta = pfPregunta;
        this.PfRespuesta = pfRespuesta;
    }

    public int getIdPreguntaFrecuente() {
        return IdPreguntaFrecuente;
    }

    public void setIdPreguntaFrecuente(int idPreguntaFrecuente) {
        IdPreguntaFrecuente = idPreguntaFrecuente;
    }

    public String getPfPregunta() {
        return PfPregunta;
    }

    public void setPfPregunta(String pfPregunta) {
        PfPregunta = pfPregunta;
    }

    public String getPfRespuesta() {
        return PfRespuesta;
    }

    public void setPfRespuesta(String pfRespuesta) {
        PfRespuesta = pfRespuesta;
    }

    @Override
    public String toString() {
        return "PreguntasFrecuentes{" +
                "IdPreguntaFrecuente=" + IdPreguntaFrecuente +
                ", PfPregunta='" + PfPregunta + '\'' +
                ", PfRespuesta='" + PfRespuesta + '\'' +
                '}';
    }
}
