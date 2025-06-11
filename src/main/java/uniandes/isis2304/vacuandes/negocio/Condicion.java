package uniandes.isis2304.vacuandes.negocio;

public class Condicion implements VOCondicion {
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	
	/**
	 * El identificador de la condicion
	 */
	private long IdentificadorCondicion;
	
	/**
	 * La descripcion de una condicion
	 */
	private String DescripcionCondicion;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	
	/**
	 * Constructor por defecto
	 */
	public Condicion() {
		this.IdentificadorCondicion = 0;
		this.DescripcionCondicion = "";
	}
	
	/**
	 * Constructor con valores
	 * @param IdentificadorCondicion - Identificador de la condicion
	 * @param DescripcionCondicion - Descripcion de la condicion
	 */
	public Condicion(long IdentificadorCondicion, String DescripcionCondicion) {
		this.IdentificadorCondicion = IdentificadorCondicion;
		this.DescripcionCondicion = DescripcionCondicion;
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
		return "Condicion [id=" + IdentificadorCondicion + ", descripcion=" + DescripcionCondicion + "]";
	}
	
}
