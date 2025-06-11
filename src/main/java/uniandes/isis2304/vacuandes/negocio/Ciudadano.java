package uniandes.isis2304.vacuandes.negocio;

import java.sql.Timestamp;

public class Ciudadano implements VOCiudadano{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	private String TipoDeIdentificacion;
	
	private String IdentificacionCiudadano;
	
	private String NombreCiudadano;
	
	private String ApellidoCiudadano;
	
	private String EsVacunable;
	
	private Timestamp FechaDeNacimiento;
	
	private long TelefonoDeContacto;

	private long EstadoVacunacion;
	
	private long Etapa;
	
	private String NombreOficina;
	
	private String PuntoVacunacion;
	
	private int RegionOficina;
	
	private String genero;
	
	private String rol;
	
	private String ciudad;
	
	private String localidad;

	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	public Ciudadano(String tipoDeIdentificacion, String identificacionCiudadano, String nombreCiudadano,
			String apellidoCiudadano, String esVacunable, Timestamp fechaDeNacimiento, long telefonoDeContacto,
			long estadoVacunacion, long etapa, String nombreOficina, String puntoVacunacion, int regionOficina, String pGenero, String pRol, String pCiudad, String pLocalidad) {
		TipoDeIdentificacion = tipoDeIdentificacion;
		IdentificacionCiudadano = identificacionCiudadano;
		NombreCiudadano = nombreCiudadano;
		ApellidoCiudadano = apellidoCiudadano;
		EsVacunable = esVacunable;
		FechaDeNacimiento = fechaDeNacimiento;
		TelefonoDeContacto = telefonoDeContacto;
		EstadoVacunacion = estadoVacunacion;
		Etapa = etapa;
		NombreOficina = nombreOficina;
		PuntoVacunacion = null;
		RegionOficina = regionOficina;
		genero = pGenero;
		rol = pRol;
		ciudad = pCiudad;
		localidad = pLocalidad;
	}
	
	public Ciudadano() {
		TipoDeIdentificacion = "";
		IdentificacionCiudadano = "";
		NombreCiudadano = "";
		ApellidoCiudadano = "";
		EsVacunable = "0";
		FechaDeNacimiento = null;
		TelefonoDeContacto = 0;
		EstadoVacunacion = 0;
		Etapa = 0;
		NombreOficina = "";
		PuntoVacunacion = null;
		RegionOficina = 0;
		genero = "";
		rol = "";
		ciudad = "";
		localidad = "";
	}

	public String getTipoDeIdentificacion() {
		return TipoDeIdentificacion;
	}

	public void setTipoDeIdentificacion(String tipoDeIdentificacion) {
		TipoDeIdentificacion = tipoDeIdentificacion;
	}

	public String getIdentificacionCiudadano() {
		return IdentificacionCiudadano;
	}

	public void setIdentificacionCiudadano(String identificacionCiudadano) {
		IdentificacionCiudadano = identificacionCiudadano;
	}

	public String getNombreCiudadano() {
		return NombreCiudadano;
	}

	public void setNombreCiudadano(String nombreCiudadano) {
		NombreCiudadano = nombreCiudadano;
	}

	public String getApellidoCiudadano() {
		return ApellidoCiudadano;
	}

	public void setApellidoCiudadano(String apellidoCiudadano) {
		ApellidoCiudadano = apellidoCiudadano;
	}

	public String getEsVacunable() {
		return EsVacunable;
	}

	public void setEsVacunable(String esVacunable) {
		EsVacunable = esVacunable;
	}

	public Timestamp getFechaDeNacimiento() {
		return FechaDeNacimiento;
	}

	public void setFechaDeNacimiento(Timestamp fechaDeNacimiento) {
		FechaDeNacimiento = fechaDeNacimiento;
	}

	public long getTelefonoDeContacto() {
		return TelefonoDeContacto;
	}

	public void setTelefonoDeContacto(long telefonoDeContacto) {
		TelefonoDeContacto = telefonoDeContacto;
	}

	public long getEstadoVacunacion() {
		return EstadoVacunacion;
	}

	public void setEstadoVacunacion(long estadoVacunacion) {
		EstadoVacunacion = estadoVacunacion;
	}

	public long getEtapa() {
		return Etapa;
	}

	public void setEtapa(long etapa) {
		Etapa = etapa;
	}

	public String getNombreOficina() {
		return NombreOficina;
	}

	public void setNombreOficina(String nombreOficina) {
		NombreOficina = nombreOficina;
	}

	public String getPuntoVacunacion() {
		return PuntoVacunacion;
	}

	public void setPuntoVacunacion(String puntoVacunacion) {
		PuntoVacunacion = puntoVacunacion;
	}

	public int getRegionOficina() {
		return RegionOficina;
	}

	public void setRegionOficina(int regionOficina) {
		RegionOficina = regionOficina;
	}
	
	public String getGenero() {
		return genero;
	}

	public void setGenero(String genero) {
		this.genero = genero;
	}

	public String getRol() {
		return rol;
	}

	public void setRol(String rol) {
		this.rol = rol;
	}

	public String getCiudad() {
		return ciudad;
	}

	public void setCiudad(String ciudad) {
		this.ciudad = ciudad;
	}

	public String getLocalidad() {
		return localidad;
	}

	public void setLocalidad(String localidad) {
		this.localidad = localidad;
	}

	@Override
	public String toString() {
		return "Ciudadano [id=" + TipoDeIdentificacion+IdentificacionCiudadano + ", Nombre=" + NombreCiudadano + " " + ApellidoCiudadano + "]";
	}
	
}
