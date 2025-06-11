package uniandes.isis2304.vacuandes.negocio;

public interface VOPuntoVacunacion {
	public String getDireccionPuntoVacunacion();
	
	public String getNombrePuntoVacunacion();
	
	public int getCapacidadSimultanea();
	
	public int getCapacidadDiaria();
	
	public int getDisponibilidadDeDosis();
	
	public String getNombreOficina();
	
	public int getRegionOficina();
	public String getSoloMayores();
	public String getSoloSalud();
	public String getEstaHabilitado();
}
