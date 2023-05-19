package com.acevedo.rutaexperienciauc.util;

public class Util {

    public static final String RUTA="http://192.168.1.14:3000"; // solo cambia la su IP

    public static final String RUTA_SEDE=RUTA+"/api/sedes"; // todas las sedes
    public static final String RUTA_SEDE_RANDOM=RUTA+"/api/sedes/sedesRandom"; // solo 4 sedes
    public static final String RUTA_CARRERAS=RUTA+"/api/carreras";// todas la experiencias de un ciclo
    public static final String RUTA_EXPERIENCIA=RUTA+"/api/experiencias"; // todas la experiencias de un ciclo
    public static final String RUTA_SOLICITAR_INFORMACION=RUTA+"/api/solicitar_informacion"; //envio de datos

    public static final String RUTA_PREGUNTAS_FRECUENTES=RUTA+"/api/preguntas_frecuentes"; //tolas preguntas frecuentes

    public static final String RUTA_CALIFICAR_EXPERIENCIA=RUTA+"/api/calificar_experiencia/"; //calificar experiencia
    public static final String RUTA_BENEFICIO=RUTA+"/api/beneficio/"; //calificar experiencia


}
