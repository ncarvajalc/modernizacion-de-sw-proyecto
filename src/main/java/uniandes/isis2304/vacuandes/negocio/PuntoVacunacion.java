package uniandes.isis2304.vacuandes.negocio;

public class PuntoVacunacion implements VOPuntoVacunacion{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	
	private String DireccionPuntoVacunacion;
	
	private String NombrePuntoVacunacion;
	
	private int CapacidadSimultanea;
	
	private int CapacidadDiaria;
	
	private int DisponibilidadDeDosis;
	
	private String NombreOficina;
	
	private int RegionOficina;
	
	private String SoloMayores;
	
	private String SoloSalud;
	
	private String EstaHabilitado;

	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public PuntoVacunacion() {
		DireccionPuntoVacunacion = "";
		NombrePuntoVacunacion = "";
		CapacidadSimultanea = 1;
		CapacidadDiaria = 1;
		DisponibilidadDeDosis = 1;
		NombreOficina = "";
		RegionOficina = 0;
		SoloMayores = "";
		SoloSalud = "";
		EstaHabilitado = "";
	}
	
	public PuntoVacunacion(String direccionPuntoVacunacion, String nombrePuntoVacunacion, int capacidadSimultanea,
			int capacidadDiaria, int disponibilidadDeDosis, String nombreOficina, int regionOficina, String soloMayores, String soloSalud, String estaHabilitado) {
		DireccionPuntoVacunacion = direccionPuntoVacunacion;
		NombrePuntoVacunacion = nombrePuntoVacunacion;
		CapacidadSimultanea = capacidadSimultanea;
		CapacidadDiaria = capacidadDiaria;
		DisponibilidadDeDosis = disponibilidadDeDosis;
		NombreOficina = nombreOficina;
		RegionOficina = regionOficina;
		SoloMayores = soloMayores;
		SoloSalud = soloSalud;
		EstaHabilitado = estaHabilitado;
	}

	public String getDireccionPuntoVacunacion() {
		return DireccionPuntoVacunacion;
	}

	public void setDireccionPuntoVacunacion(String direccionPuntoVacunacion) {
		DireccionPuntoVacunacion = direccionPuntoVacunacion;
	}

	public String getNombrePuntoVacunacion() {
		return NombrePuntoVacunacion;
	}

	public void setNombrePuntoVacunacion(String nombrePuntoVacunacion) {
		NombrePuntoVacunacion = nombrePuntoVacunacion;
	}

	public int getCapacidadSimultanea() {
		return CapacidadSimultanea;
	}

	public void setCapacidadSimultanea(int capacidadSimultanea) {
		CapacidadSimultanea = capacidadSimultanea;
	}

	public int getCapacidadDiaria() {
		return CapacidadDiaria;
	}

	public void setCapacidadDiaria(int capacidadDiaria) {
		CapacidadDiaria = capacidadDiaria;
	}

	public int getDisponibilidadDeDosis() {
		return DisponibilidadDeDosis;
	}

	public void setDisponibilidadDeDosis(int disponibilidadDeDosis) {
		DisponibilidadDeDosis = disponibilidadDeDosis;
	}

	public String getNombreOficina() {
		return NombreOficina;
	}

	public void setNombreOficina(String nombreOficina) {
		NombreOficina = nombreOficina;
	}

	public int getRegionOficina() {
		return RegionOficina;
	}

	public void setRegionOficina(int regionOficina) {
		RegionOficina = regionOficina;
	}
	
	public String getSoloMayores() {
		return SoloMayores;
	}

	public void setSoloMayores(String soloMayores) {
		SoloMayores = soloMayores;
	}

	public String getSoloSalud() {
		return SoloSalud;
	}

	public void setSoloSalud(String soloSalud) {
		SoloSalud = soloSalud;
	}

	public String getEstaHabilitado() {
		return EstaHabilitado;
	}

	public void setEstaHabilitado(String estaHabilitado) {
		EstaHabilitado = estaHabilitado;
	}

	@Override
	public String toString() 
	{
		return "PuntoVacunacion [Direccion=" + DireccionPuntoVacunacion + ", Nombre=" + NombrePuntoVacunacion + "]";
	}
}
