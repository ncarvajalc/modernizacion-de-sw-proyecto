package uniandes.isis2304.vacuandes.negocio;

/**
 * Clase para modelar el concepto OficinaRegionalEPS del negocio de Vacuandes
 *
 */
public class OficinaRegionalEPS implements VOOficinaRegionalEPS {
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * El nombre de la EPS
	 */
	private String NombreEPS;
	
	/**
	 * La region de una oficina
	 */
	private int RegionEPS;
	
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto
	 */
	public OficinaRegionalEPS() {
		this.NombreEPS = "";
		this.RegionEPS = 0;
	}
	
	/**
	 * Constructor con valores
	 * @param NombreEPS - El nombre de la EPS
	 * @param RegionEPS - El numero de la region)
	 */
	public OficinaRegionalEPS(String nombreEPS, int regionEPS) {
		this.NombreEPS = nombreEPS;
		this.RegionEPS = regionEPS;
	}
	
	
	
	public String getNombreEPS() {
		return NombreEPS;
	}

	public void setNombreEPS(String nombreEPS) {
		NombreEPS = nombreEPS;
	}

	public int getRegionEPS() {
		return RegionEPS;
	}

	public void setRegionEPS(int regionEPS) {
		RegionEPS = regionEPS;
	}

	/**
	 * @return Una cadena con la información básica de la oficina
	 */
	@Override
	public String toString() 
	{
		return "Oficina [nombre=" + NombreEPS + ", region=" + RegionEPS + "]";
	}

}
