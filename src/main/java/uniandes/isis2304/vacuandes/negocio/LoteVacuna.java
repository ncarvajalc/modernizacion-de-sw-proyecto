package uniandes.isis2304.vacuandes.negocio;

public class LoteVacuna implements VOLoteVacuna{
	
	private long IdentificadorLote;
	
	private String NombreOficina;
	
	private int RegionOficina;
	
	public LoteVacuna() {
		IdentificadorLote = 0;
		NombreOficina = "";
		RegionOficina = 0;
	}

	public LoteVacuna(long identificadorLote, String nombreOficina, int regionOficina) {
		IdentificadorLote = identificadorLote;
		NombreOficina = nombreOficina;
		RegionOficina = regionOficina;
	}

	public long getIdentificadorLote() {
		return IdentificadorLote;
	}

	public void setIdentificadorLote(long identificadorLote) {
		IdentificadorLote = identificadorLote;
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
	@Override
	public String toString() {
		return "LoteVacuna [IdentificadorLote=" + IdentificadorLote + ", NombreOficina=" + NombreOficina
				+ ", RegionOficina=" + RegionOficina + "]";
	}
	
	

}
