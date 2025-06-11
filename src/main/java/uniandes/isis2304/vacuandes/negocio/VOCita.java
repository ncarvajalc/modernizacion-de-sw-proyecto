package uniandes.isis2304.vacuandes.negocio;

import java.sql.Timestamp;

public interface VOCita {
	public long getIdentificadorCita();
	
	public Timestamp getFechaCita();
	
	public String getTipoIdCiudadano();
	
	public String getIdCiudadano();
	
	@Override
	public String toString();
}
