package uniandes.isis2304.vacuandes.negocio;

public interface VOPerteneceA {
	public String getTipoIdCiudadano();
	
	public String getIdCiudadano();
	
	public long getIdentificadorCondicion();
	
	public String getDescripcionCondicion();
	
	@Override
	public String toString();
}
