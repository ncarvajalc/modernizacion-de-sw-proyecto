package uniandes.isis2304.vacuandes.negocio;

public class Infraestructura implements VOInfraestructura{
	
	private long IdentificadorInfraestructura;
	
	private String DescripcionInfraestructura;
	
	public Infraestructura() {
		IdentificadorInfraestructura = 0;
		DescripcionInfraestructura = "";
	}

	public Infraestructura(long identificadorInfraestructura, String descripcionInfraestructura) {
		IdentificadorInfraestructura = identificadorInfraestructura;
		DescripcionInfraestructura = descripcionInfraestructura;
	}

	public long getIdentificadorInfraestructura() {
		return IdentificadorInfraestructura;
	}

	public void setIdentificadorInfraestructura(long identificadorInfraestructura) {
		IdentificadorInfraestructura = identificadorInfraestructura;
	}

	public String getDescripcionInfraestructura() {
		return DescripcionInfraestructura;
	}

	public void setDescripcionInfraestructura(String descripcionInfraestructura) {
		DescripcionInfraestructura = descripcionInfraestructura;
	}

	@Override
	public String toString() {
		return "Infraestructura [IdentificadorInfraestructura=" + IdentificadorInfraestructura
				+ ", DescripcionInfraestructura=" + DescripcionInfraestructura + "]";
	}
	
	
	
	
	
	
	
	
	
	
	

}
