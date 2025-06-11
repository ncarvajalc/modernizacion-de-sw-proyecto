package uniandes.isis2304.vacuandes.negocio;

public interface VOVacuna {
	
	public long getIdentificadorVacuna();
	
	public String getHaSidoAplicado();
	
	public long getLoteVacuna();
	
	public Long getSegundaDosis();
	
	public String getPuntoVacunacion();
	
	public String getTipoIdCiudadano();
	
	@Override
	public String toString();
	

}
