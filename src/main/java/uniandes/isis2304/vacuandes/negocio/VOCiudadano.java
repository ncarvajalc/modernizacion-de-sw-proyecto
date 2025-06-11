package uniandes.isis2304.vacuandes.negocio;

import java.sql.Timestamp;

public interface VOCiudadano {
	public String getTipoDeIdentificacion();
	
	public String getIdentificacionCiudadano();
	
	public String getNombreCiudadano();
	
	public String getApellidoCiudadano();
	
	public String getEsVacunable();
	
	public Timestamp getFechaDeNacimiento();
	
	public long getTelefonoDeContacto();
	
	public long getEstadoVacunacion();
	
	public long getEtapa();
	
	public String getNombreOficina();
	
	public String getPuntoVacunacion();
	
	public int getRegionOficina();
}
