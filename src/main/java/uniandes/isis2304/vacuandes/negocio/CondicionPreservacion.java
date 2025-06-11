package uniandes.isis2304.vacuandes.negocio;

public class CondicionPreservacion implements VOCondicionPreservacion{
	
	private long IdentificadorCondPreservacion;
	
	private String DescripcionPreservacion;
	
	
	
	public CondicionPreservacion() {
		IdentificadorCondPreservacion = 0;
		DescripcionPreservacion = "";
	}

	public CondicionPreservacion(long identificadorCondPreservacion, String descripcionPreservacion) {
		IdentificadorCondPreservacion = identificadorCondPreservacion;
		DescripcionPreservacion = descripcionPreservacion;
	}

	public long getIdentificadorCondPreservacion() {
		return IdentificadorCondPreservacion;
	}

	public void setIdentificadorCondPreservacion(long identificadorCondPreservacion) {
		IdentificadorCondPreservacion = identificadorCondPreservacion;
	}

	public String getDescripcionPreservacion() {
		return DescripcionPreservacion;
	}

	public void setDescripcionPreservacion(String descripcionPreservacion) {
		DescripcionPreservacion = descripcionPreservacion;
	}

	@Override
	public String toString() {
		return "CondicionPreservacion [IdentificadorCondPreservacion=" + IdentificadorCondPreservacion
				+ ", DescripcionPreservacion=" + DescripcionPreservacion + "]";
	}
	
	
	
	
	
	
	
	
	

}
