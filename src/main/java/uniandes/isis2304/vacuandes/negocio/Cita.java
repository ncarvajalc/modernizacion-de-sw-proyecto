package uniandes.isis2304.vacuandes.negocio;

import java.sql.Timestamp;

public class Cita implements VOCita{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	private long IdentificadorCita;
	
	private Timestamp FechaCita;
	
	private String TipoIdCiudadano;
	
	private String IdCiudadano;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	public Cita() {
		IdentificadorCita = 0;
		FechaCita = null;
		TipoIdCiudadano = "";
		IdCiudadano = "";
	}
	
	public Cita(long identificadorCita, Timestamp fechaCita, String tipoIdCiudadano, String idCiudadano) {
		IdentificadorCita = identificadorCita;
		FechaCita = fechaCita;
		TipoIdCiudadano = tipoIdCiudadano;
		IdCiudadano = idCiudadano;
	}

	public long getIdentificadorCita() {
		return IdentificadorCita;
	}

	public void setIdentificadorCita(long identificadorCita) {
		IdentificadorCita = identificadorCita;
	}

	public Timestamp getFechaCita() {
		return FechaCita;
	}

	public void setFechaCita(Timestamp fechaCita) {
		FechaCita = fechaCita;
	}

	public String getTipoIdCiudadano() {
		return TipoIdCiudadano;
	}

	public void setTipoIdCiudadano(String tipoIdCiudadano) {
		TipoIdCiudadano = tipoIdCiudadano;
	}

	public String getIdCiudadano() {
		return IdCiudadano;
	}

	public void setIdCiudadano(String idCiudadano) {
		IdCiudadano = idCiudadano;
	}
	

	@Override
	public String toString() 
	{
		return "Cita [IdentificadorCita=" + IdentificadorCita + ", FechaCita=" + FechaCita + ", TipoIdCiudadano=" + TipoIdCiudadano + ", IdCiudadano="
				+ IdCiudadano + "]";
	}
	
}
