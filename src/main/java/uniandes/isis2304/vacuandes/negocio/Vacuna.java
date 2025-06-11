package uniandes.isis2304.vacuandes.negocio;

public class Vacuna implements VOVacuna{
	
	private long IdentificadorVacuna;
	
	private String HaSidoAplicado;
	
	private long LoteVacuna;
	
	private Long SegundaDosis;
	
	private String PuntoVacunacion;
	
	private String TipoIdCiudadano;
	
	private String IdCiudadano;
	
	private String tecnologia;
	
	public Vacuna() {
		IdentificadorVacuna = 0;
		HaSidoAplicado = "";
		LoteVacuna = 0;
		SegundaDosis = (long) 0;
		PuntoVacunacion = "";
		TipoIdCiudadano = "";
		IdCiudadano = "";
		tecnologia = "";
	}
	

	public Vacuna(long identificadorVacuna, String haSidoAplicado, long loteVacuna, Long segundaDosis,
			String puntoVacunacion, String tipoIdCiudadano, String idCiudadano, String pTecnologia) {
		IdentificadorVacuna = identificadorVacuna;
		HaSidoAplicado = haSidoAplicado;
		LoteVacuna = loteVacuna;
		SegundaDosis = segundaDosis;
		PuntoVacunacion = puntoVacunacion;
		TipoIdCiudadano = tipoIdCiudadano;
		IdCiudadano = idCiudadano;
		tecnologia = pTecnologia;
	}
	
	public long getIdentificadorVacuna() {
		return IdentificadorVacuna;
	}


	public void setIdentificadorVacuna(long identificadorVacuna) {
		IdentificadorVacuna = identificadorVacuna;
	}


	public String getHaSidoAplicado() {
		return HaSidoAplicado;
	}


	public void setHaSidoAplicado(String haSidoAplicado) {
		HaSidoAplicado = haSidoAplicado;
	}


	public long getLoteVacuna() {
		return LoteVacuna;
	}


	public void setLoteVacuna(long loteVacuna) {
		LoteVacuna = loteVacuna;
	}


	public Long getSegundaDosis() {
		return SegundaDosis;
	}


	public void setSegundaDosis(Long segundaDosis) {
		SegundaDosis = segundaDosis;
	}


	public String getPuntoVacunacion() {
		return PuntoVacunacion;
	}


	public void setPuntoVacunacion(String puntoVacunacion) {
		PuntoVacunacion = puntoVacunacion;
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


	public String getTecnologia() {
		return tecnologia;
	}


	public void setTecnologia(String tecnologia) {
		this.tecnologia = tecnologia;
	}


	@Override
	public String toString() {
		return "Vacuna [IdentificadorVacuna=" + IdentificadorVacuna + ", HaSidoAplicado=" + HaSidoAplicado
				+ ", LoteVacuna=" + LoteVacuna + ", SegundaDosis=" + SegundaDosis + ", PuntoVacunacion="
				+ PuntoVacunacion + ", TipoIdCiudadano=" + TipoIdCiudadano + ", IdCiudadano=" + IdCiudadano + "]";
	}
	

	

}
