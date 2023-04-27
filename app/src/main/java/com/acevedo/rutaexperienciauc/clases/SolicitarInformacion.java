package com.acevedo.rutaexperienciauc.clases;

import java.util.Date;

public class SolicitarInformacion {
    int IdContacto;
    String CcNombre;
    String CcApellidoPaterno;
    String CcApellidoMaterno;
    String CcCorreo;
    String CcTelefono;
    Date CcFechaNacimiento;
    Date CcFechaContaco;
    int IdTipoContacto;
    int IdTipoModalidad;

    public SolicitarInformacion() {
    }

    public int getIdContacto() {
        return IdContacto;
    }

    public void setIdContacto(int idContacto) {
        IdContacto = idContacto;
    }

    public String getCcNombre() {
        return CcNombre;
    }

    public void setCcNombre(String ccNombre) {
        CcNombre = ccNombre;
    }

    public String getCcApellidoPaterno() {
        return CcApellidoPaterno;
    }

    public void setCcApellidoPaterno(String ccApellidoPaterno) {
        CcApellidoPaterno = ccApellidoPaterno;
    }

    public String getCcApellidoMaterno() {
        return CcApellidoMaterno;
    }

    public void setCcApellidoMaterno(String ccApellidoMaterno) {
        CcApellidoMaterno = ccApellidoMaterno;
    }

    public String getCcCorreo() {
        return CcCorreo;
    }

    public void setCcCorreo(String ccCorreo) {
        CcCorreo = ccCorreo;
    }

    public String getCcTelefono() {
        return CcTelefono;
    }

    public void setCcTelefono(String ccTelefono) {
        CcTelefono = ccTelefono;
    }

    public Date getCcFechaNacimiento() {
        return CcFechaNacimiento;
    }

    public void setCcFechaNacimiento(Date ccFechaNacimiento) {
        CcFechaNacimiento = ccFechaNacimiento;
    }

    public Date getCcFechaContaco() {
        return CcFechaContaco;
    }

    public void setCcFechaContaco(Date ccFechaContaco) {
        CcFechaContaco = ccFechaContaco;
    }

    public int getIdTipoContacto() {
        return IdTipoContacto;
    }

    public void setIdTipoContacto(int idTipoContacto) {
        IdTipoContacto = idTipoContacto;
    }

    public int getIdTipoModalidad() {
        return IdTipoModalidad;
    }

    public void setIdTipoModalidad(int idTipoModalidad) {
        IdTipoModalidad = idTipoModalidad;
    }

    @Override
    public String toString() {
        return "SolicitarInformacion{" +
                "IdContacto=" + IdContacto +
                ", CcNombre='" + CcNombre + '\'' +
                ", CcApellidoPaterno='" + CcApellidoPaterno + '\'' +
                ", CcApellidoMaterno='" + CcApellidoMaterno + '\'' +
                ", CcCorreo='" + CcCorreo + '\'' +
                ", CcTelefono='" + CcTelefono + '\'' +
                ", CcFechaNacimiento=" + CcFechaNacimiento +
                ", CcFechaContaco=" + CcFechaContaco +
                ", IdTipoContacto=" + IdTipoContacto +
                ", IdTipoModalidad=" + IdTipoModalidad +
                '}';
    }
}

