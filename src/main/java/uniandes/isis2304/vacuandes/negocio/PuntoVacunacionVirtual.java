package uniandes.isis2304.vacuandes.negocio;

public class PuntoVacunacionVirtual implements VOPuntoVacunacionVirtual{
	private String DirPuntoVacunacionVirtual;
	
	private String NombreOficina;
	
	private int RegionOficina;
	
	private long VacunaAsignada;
	
	
	public PuntoVacunacionVirtual() {
		
		DirPuntoVacunacionVirtual = "";
		NombreOficina = "";
		RegionOficina = 0;
		VacunaAsignada = 0;
	}
	public PuntoVacunacionVirtual(String dirPuntoVacunacionVirtual, String nombreOficina, int regionOficina,
			long vacunaAsignada) {
		
		DirPuntoVacunacionVirtual = dirPuntoVacunacionVirtual;
		NombreOficina = nombreOficina;
		RegionOficina = regionOficina;
		VacunaAsignada = vacunaAsignada;
	}

	public String getDirPuntoVacunacionVirtual() {
		return DirPuntoVacunacionVirtual;
	}

	public void setDirPuntoVacunacionVirtual(String dirPuntoVacunacionVirtual) {
		DirPuntoVacunacionVirtual = dirPuntoVacunacionVirtual;
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

	public long getVacunaAsignada() {
		return VacunaAsignada;
	}

	@Override
	public String toString() {
		return "PuntoVacunacionVirtual [DirPuntoVacunacionVirtual=" + DirPuntoVacunacionVirtual + ", NombreOficina="
				+ NombreOficina + ", RegionOficina=" + RegionOficina + ", VacunaAsignada=" + VacunaAsignada + "]";
	}
	public void setVacunaAsignada(long vacunaAsignada) {
		VacunaAsignada = vacunaAsignada;
	}

	
	
	
	
	
	
	
	
	
	
	

}
