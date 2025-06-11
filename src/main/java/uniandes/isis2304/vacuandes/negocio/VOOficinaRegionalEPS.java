package uniandes.isis2304.vacuandes.negocio;

/**
 * Interfaz para los métodos get de OficinaRegionalEPS
 * Sirve para proteger la información del negocio de posibles manipulaciones desde la interfaz 
 * 
 */
public interface VOOficinaRegionalEPS {
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	public String getNombreEPS();
	
	public int getRegionEPS();
	
	@Override
	public String toString() ;
}
