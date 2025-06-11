package uniandes.isis2304.vacuandes.negocio;

public class PerteneceA implements VOPerteneceA{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	private String TipoIdCiudadano;
	
	private String IdCiudadano;
	
	private long IdentificadorCondicion;
	
	private String DescripcionCondicion;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	public PerteneceA() {
		TipoIdCiudadano = "";
		IdCiudadano = "";
		IdentificadorCondicion = 0;
		DescripcionCondicion = "";
	}
	
	public PerteneceA(String tipoIdCiudadano, String idCiudadano, long identificadorCondicion,
			String descripcionCondicion) {
		TipoIdCiudadano = tipoIdCiudadano;
		IdCiudadano = idCiudadano;
		IdentificadorCondicion = identificadorCondicion;
		DescripcionCondicion = descripcionCondicion;
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

	public long getIdentificadorCondicion() {
		return IdentificadorCondicion;
	}

	public void setIdentificadorCondicion(long identificadorCondicion) {
		IdentificadorCondicion = identificadorCondicion;
	}

	public String getDescripcionCondicion() {
		return DescripcionCondicion;
	}

	public void setDescripcionCondicion(String descripcionCondicion) {
		DescripcionCondicion = descripcionCondicion;
	}
	
	@Override
	public String toString() {
		return "CondicionDeCiudadano [ciudadano=" +TipoIdCiudadano +IdCiudadano + ", descripcion=" + DescripcionCondicion + "]";
	}
	
}
