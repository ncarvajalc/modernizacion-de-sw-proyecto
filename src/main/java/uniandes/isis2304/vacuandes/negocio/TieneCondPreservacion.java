package uniandes.isis2304.vacuandes.negocio;

public class TieneCondPreservacion implements VOTieneCondPreservacion{
	
	private long IdentificadorCondicion;
	
	private long IdentificadorVacuna;

	
	public TieneCondPreservacion() {
		IdentificadorCondicion = 0;
		IdentificadorVacuna = 0;
	}
	
	public TieneCondPreservacion(long identificadorCondicion, long identificadorVacuna) {
		IdentificadorCondicion = identificadorCondicion;
		IdentificadorVacuna = identificadorVacuna;
	}

	public long getIdentificadorCondicion() {
		return IdentificadorCondicion;
	}

	public void setIdentificadorCondicion(long identificadorCondicion) {
		IdentificadorCondicion = identificadorCondicion;
	}

	public long getIdentificadorVacuna() {
		return IdentificadorVacuna;
	}

	public void setIdentificadorVacuna(long identificadorVacuna) {
		IdentificadorVacuna = identificadorVacuna;
	}

	@Override
	public String toString() {
		return "TieneCondPreservacion [IdentificadorCondicion=" + IdentificadorCondicion + ", IdentificadorVacuna="
				+ IdentificadorVacuna + "]";
	}
	

}
