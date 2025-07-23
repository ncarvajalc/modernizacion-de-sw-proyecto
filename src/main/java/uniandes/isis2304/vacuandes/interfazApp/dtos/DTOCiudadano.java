package uniandes.isis2304.vacuandes.interfazApp.dtos;

import java.sql.Timestamp;

public class DTOCiudadano {
    public String tipoDeIdentificacion;
    public String identificacionCiudadano;
    public String nombreCiudadano;
    public String apellidoCiudadano;
    public String esVacunable;
    public Timestamp fechaDeNacimiento;
    public long telefonoDeContacto;
    public long estadoVacunacion;
    public long etapa;
    public String nombreOficina;
    public int regionOficina;
    public String genero;
    public String rol;
    public String ciudad;
    public String localidad;
    
    @Override
    public String toString() {
        return "DTOCiudadano{" +
                "tipoDeIdentificacion='" + tipoDeIdentificacion + '\'' +
                ", identificacionCiudadano='" + identificacionCiudadano + '\'' +
                ", nombreCiudadano='" + nombreCiudadano + '\'' +
                ", apellidoCiudadano='" + apellidoCiudadano + '\'' +
                ", esVacunable='" + esVacunable + '\'' +
                ", fechaDeNacimiento=" + fechaDeNacimiento +
                ", telefonoDeContacto=" + telefonoDeContacto +
                ", estadoVacunacion=" + estadoVacunacion +
                ", etapa=" + etapa +
                ", nombreOficina='" + nombreOficina + '\'' +
                ", regionOficina=" + regionOficina +
                ", genero='" + genero + '\'' +
                ", rol='" + rol + '\'' +
                ", ciudad='" + ciudad + '\'' +
                ", localidad='" + localidad + '\'' +
                '}';
    }
}