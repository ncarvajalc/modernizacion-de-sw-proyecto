package uniandes.isis2304.vacuandes.negocio;

public class EstadoVacunacion implements VOEstadoVacunacion {
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * El identificador del estado
	 */
	private long IdentificadorEstado;
	
	/**
	 * La descripcion del estado
	 */
	private String DescripcionEstado;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto
	 */
	public EstadoVacunacion() {
		this.IdentificadorEstado = 0;
		this.DescripcionEstado = "";
	}
	
	/**
	 * Constructor con valores
	 * @param IdentificadorEstado - Identificador del estado
	 * @param DescripcionEstado - Descripcion del estado
	 */
	public EstadoVacunacion(long IdentificadorEstado, String DescripcionEstado) {
		this.IdentificadorEstado = IdentificadorEstado;
		this.DescripcionEstado = DescripcionEstado;
	}

	public long getIdentificadorEstado() {
		return IdentificadorEstado;
	}

	public void setIdentificadorEstado(long identificadorEstado) {
		IdentificadorEstado = identificadorEstado;
	}

	public String getDescripcionEstado() {
		return DescripcionEstado;
	}

	public void setDescripcionEstado(String descripcionEstado) {
		DescripcionEstado = descripcionEstado;
	}
	
	@Override
	public String toString() {
		return "EstadoVacunacion [id=" + IdentificadorEstado + ", descripcion=" + DescripcionEstado + "]";
	}
}
