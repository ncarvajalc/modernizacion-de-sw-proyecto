package uniandes.isis2304.vacuandes.interfazApp.dtos;

import java.sql.Timestamp;

import uniandes.isis2304.vacuandes.negocio.VOCiudadano;

public class CiudadanoResponse {
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
    public String puntoVacunacion;
    public int regionOficina;

    public CiudadanoResponse(VOCiudadano vo) {
        this.tipoDeIdentificacion = vo.getTipoDeIdentificacion();
        this.identificacionCiudadano = vo.getIdentificacionCiudadano();
        this.nombreCiudadano = vo.getNombreCiudadano();
        this.apellidoCiudadano = vo.getApellidoCiudadano();
        this.esVacunable = vo.getEsVacunable();
        this.fechaDeNacimiento = vo.getFechaDeNacimiento();
        this.telefonoDeContacto = vo.getTelefonoDeContacto();
        this.estadoVacunacion = vo.getEstadoVacunacion();
        this.etapa = vo.getEtapa();
        this.nombreOficina = vo.getNombreOficina();
        this.puntoVacunacion = vo.getPuntoVacunacion();
        this.regionOficina = vo.getRegionOficina();
    }
}
