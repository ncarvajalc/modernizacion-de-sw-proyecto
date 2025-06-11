package uniandes.isis2304.vacuandes.negocio;

public class TalentoHumano  implements VOTalentoHumano{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	private String TipoDeIdentificacion;
	
	private String IdentificacionCiudadano;
	
	private String FuncionTalentoHumano;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public TalentoHumano() {
		TipoDeIdentificacion = "";
		IdentificacionCiudadano = "";
		FuncionTalentoHumano = "";
	}
	
	public TalentoHumano(String tipoDeIdentificacion, String identificacionCiudadano, String funcionTalentoHumano) {
		TipoDeIdentificacion = tipoDeIdentificacion;
		IdentificacionCiudadano = identificacionCiudadano;
		FuncionTalentoHumano = funcionTalentoHumano;
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

	public String getFuncionTalentoHumano() {
		return FuncionTalentoHumano;
	}

	public void setFuncionTalentoHumano(String funcionTalentoHumano) {
		FuncionTalentoHumano = funcionTalentoHumano;
	}
	
	@Override
	public String toString() {
		return "TalentoHumano [id=" + TipoDeIdentificacion+IdentificacionCiudadano + ", funcion=" + FuncionTalentoHumano + "]";
	}
		
}