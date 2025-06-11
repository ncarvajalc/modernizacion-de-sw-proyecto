package uniandes.isis2304.vacuandes.negocio;

public class TrabajaEn implements VOTrabajaEn{
	
	private String TipoIdTalentoHumano;
	
	private String IdTalentoHumano;
	
	private String DireccionPuntoVacunacion;
	
	public TrabajaEn() {
		TipoIdTalentoHumano = "";
		IdTalentoHumano = "";
		DireccionPuntoVacunacion = "";
	}

	public TrabajaEn(String tipoIdTalentoHumano, String idTalentoHumano, String direccionPuntoVacunacion) {
		TipoIdTalentoHumano = tipoIdTalentoHumano;
		IdTalentoHumano = idTalentoHumano;
		DireccionPuntoVacunacion = direccionPuntoVacunacion;
	}

	public String getTipoIdTalentoHumano() {
		return TipoIdTalentoHumano;
	}

	public void setTipoIdTalentoHumano(String tipoIdTalentoHumano) {
		TipoIdTalentoHumano = tipoIdTalentoHumano;
	}

	public String getIdTalentoHumano() {
		return IdTalentoHumano;
	}

	public void setIdTalentoHumano(String idTalentoHumano) {
		IdTalentoHumano = idTalentoHumano;
	}

	public String getDireccionPuntoVacunacion() {
		return DireccionPuntoVacunacion;
	}

	public void setDireccionPuntoVacunacion(String direccionPuntoVacunacion) {
		DireccionPuntoVacunacion = direccionPuntoVacunacion;
	}

	@Override
	public String toString() {
		return "TrabajaEn [TipoIdTalentoHumano=" + TipoIdTalentoHumano + ", IdTalentoHumano=" + IdTalentoHumano
				+ ", DireccionPuntoVacunacion=" + DireccionPuntoVacunacion + "]";
	}
	
	
	
	
	
	
	

}
