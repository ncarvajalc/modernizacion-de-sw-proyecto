package uniandes.isis2304.vacuandes.negocio;

public class TieneInfraestructura implements VOTieneInfraestructura {
	
	
	private long IdentificadorInfraestructura;
	
	private String DireccionPuntoVacunacion;
	
	public TieneInfraestructura() {
		IdentificadorInfraestructura = 0;
		DireccionPuntoVacunacion = "";
	}

	public TieneInfraestructura(long identificadorInfraestructura, String direccionPuntoVacunacion) {
		IdentificadorInfraestructura = identificadorInfraestructura;
		DireccionPuntoVacunacion = direccionPuntoVacunacion;
	}

	public long getIdentificadorInfraestructura() {
		return IdentificadorInfraestructura;
	}

	public void setIdentificadorInfraestructura(long identificadorInfraestructura) {
		IdentificadorInfraestructura = identificadorInfraestructura;
	}

	public String getDireccionPuntoVacunacion() {
		return DireccionPuntoVacunacion;
	}

	public void setDireccionPuntoVacunacion(String direccionPuntoVacunacion) {
		DireccionPuntoVacunacion = direccionPuntoVacunacion;
	}

	@Override
	public String toString() {
		return "TieneInfraestructura [IdentificadorInfraestructura=" + IdentificadorInfraestructura
				+ ", DireccionPuntoVacunacion=" + DireccionPuntoVacunacion + "]";
	}
	
	
	
	
	
	

}
