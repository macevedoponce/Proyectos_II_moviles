package com.acevedo.rutaexperienciauc.clases;

import java.util.Date;

public class SolicitarInformacion {
    int IdSolicitarInfo;
    String SiNombre;
    String SiApellidoPaterno;
    String SiApellidoMaterno;
    String CaNombre;
    String SeNombre;
    String SiModalidad;
    String SiCorreo;
    String SiTelefono;
    Date SiFechaNacimiento;
    Date SiFechaContacto;
    String SiTipoContacto;

    public SolicitarInformacion(int idSolicitarInfo, String siNombre, String siApellidoPaterno, String siApellidoMaterno, String caNombre, String seNombre, String siModalidad, String siCorreo, String siTelefono, Date siFechaNacimiento, Date siFechaContacto, String siTipoContacto) {
        this.IdSolicitarInfo = idSolicitarInfo;
        this.SiNombre = siNombre;
        this.SiApellidoPaterno = siApellidoPaterno;
        this.SiApellidoMaterno = siApellidoMaterno;
        this.CaNombre = caNombre;
        this.SeNombre = seNombre;
        this.SiModalidad = siModalidad;
        this.SiCorreo = siCorreo;
        this.SiTelefono = siTelefono;
        this.SiFechaNacimiento = siFechaNacimiento;
        this.SiFechaContacto = siFechaContacto;
        this.SiTipoContacto = siTipoContacto;
    }

    public int getIdSolicitarInfo() {
        return IdSolicitarInfo;
    }

    public void setIdSolicitarInfo(int idSolicitarInfo) {
        IdSolicitarInfo = idSolicitarInfo;
    }

    public String getSiNombre() {
        return SiNombre;
    }

    public void setSiNombre(String siNombre) {
        SiNombre = siNombre;
    }

    public String getSiApellidoPaterno() {
        return SiApellidoPaterno;
    }

    public void setSiApellidoPaterno(String siApellidoPaterno) {
        SiApellidoPaterno = siApellidoPaterno;
    }

    public String getSiApellidoMaterno() {
        return SiApellidoMaterno;
    }

    public void setSiApellidoMaterno(String siApellidoMaterno) {
        SiApellidoMaterno = siApellidoMaterno;
    }

    public String getCaNombre() {
        return CaNombre;
    }

    public void setCaNombre(String caNombre) {
        CaNombre = caNombre;
    }

    public String getSeNombre() {
        return SeNombre;
    }

    public void setSeNombre(String seNombre) {
        SeNombre = seNombre;
    }

    public String getSiModalidad() {
        return SiModalidad;
    }

    public void setSiModalidad(String siModalidad) {
        SiModalidad = siModalidad;
    }

    public String getSiCorreo() {
        return SiCorreo;
    }

    public void setSiCorreo(String siCorreo) {
        SiCorreo = siCorreo;
    }

    public String getSiTelefono() {
        return SiTelefono;
    }

    public void setSiTelefono(String siTelefono) {
        SiTelefono = siTelefono;
    }

    public Date getSiFechaNacimiento() {
        return SiFechaNacimiento;
    }

    public void setSiFechaNacimiento(Date siFechaNacimiento) {
        SiFechaNacimiento = siFechaNacimiento;
    }

    public Date getSiFechaContacto() {
        return SiFechaContacto;
    }

    public void setSiFechaContacto(Date siFechaContacto) {
        SiFechaContacto = siFechaContacto;
    }

    public String getSiTipoContacto() {
        return SiTipoContacto;
    }

    public void setSiTipoContacto(String siTipoContacto) {
        SiTipoContacto = siTipoContacto;
    }

    @Override
    public String toString() {
        return "SolicitarInformacion{" +
                "IdSolicitarInfo=" + IdSolicitarInfo +
                ", SiNombre='" + SiNombre + '\'' +
                ", SiApellidoPaterno='" + SiApellidoPaterno + '\'' +
                ", SiApellidoMaterno='" + SiApellidoMaterno + '\'' +
                ", CaNombre='" + CaNombre + '\'' +
                ", SeNombre='" + SeNombre + '\'' +
                ", SiModalidad='" + SiModalidad + '\'' +
                ", SiCorreo='" + SiCorreo + '\'' +
                ", SiTelefono='" + SiTelefono + '\'' +
                ", SiFechaNacimiento=" + SiFechaNacimiento +
                ", SiFechaContacto=" + SiFechaContacto +
                ", SiTipoContacto='" + SiTipoContacto + '\'' +
                '}';
    }
}

