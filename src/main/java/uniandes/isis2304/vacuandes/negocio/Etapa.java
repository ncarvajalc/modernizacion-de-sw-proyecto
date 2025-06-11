package uniandes.isis2304.vacuandes.negocio;

public class Etapa implements VOEtapa{
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * El identificador de la etapa
	 */
	private long NumeroDeEtapa;
	
	/**
	 * La descripcion de una etapa
	 */
	private String DescripcionEtapa;

	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto
	 */
	public Etapa() {
		NumeroDeEtapa = 0;
		DescripcionEtapa = "";
	}
	
	
	/**
	 * Constructor con valores
	 * @param NumeroDeEtapa - Identificador de la etapa
	 * @param DescripcionEtapa - Descripcion de la etapa
	*/
	public Etapa(long numeroDeEtapa, String descripcionEtapa) {
		NumeroDeEtapa = numeroDeEtapa;
		DescripcionEtapa = descripcionEtapa;
	}


	public long getNumeroDeEtapa() {
		return NumeroDeEtapa;
	}


	public void setNumeroDeEtapa(long numeroDeEtapa) {
		NumeroDeEtapa = numeroDeEtapa;
	}


	public String getDescripcionEtapa() {
		return DescripcionEtapa;
	}


	public void setDescripcionEtapa(String descripcionEtapa) {
		DescripcionEtapa = descripcionEtapa;
	}
	
	@Override
	public String toString() {
		return "Etapa [id=" + NumeroDeEtapa + ", descripcion=" + DescripcionEtapa + "]";
	}
	
}
