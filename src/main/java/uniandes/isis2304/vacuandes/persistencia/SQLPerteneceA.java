package uniandes.isis2304.vacuandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLPerteneceA {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaVacuandes.SQL;
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia general de la aplicación
	 */
	private PersistenciaVacuandes pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/

	/**
	 * Constructor
	 * @param pp - El Manejador de persistencia de la aplicación
	 */
	public SQLPerteneceA (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}

	public long agregarCondicionCiudadano(PersistenceManager pm, String TipoIdCiudadano, String IdCiudadano,
			long IdentificadorCondicion, String DescripcionCondicion) {
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPerteneceA() + "(TipoIdCiudadano, IdCiudadano, IdentificadorCondicion, DescripcionCondicion) values (?, ?, ?, ?)");
		q.setParameters(TipoIdCiudadano, IdCiudadano, IdentificadorCondicion, DescripcionCondicion);
		return (long) q.executeUnique();
	}
	
	public long adicionarCondicionCiudadano(PersistenceManager pm, String TipoIdCiudadano, String IdCiudadano,
			long IdentificadorCondicion, String DescripcionCondicion) {
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPerteneceA() + "(TipoIdCiudadano, IdCiudadano, IdentificadorCondicion, DescripcionCondicion) values (?, ?, ?, ?)");
		q.setParameters(TipoIdCiudadano, IdCiudadano, IdentificadorCondicion, DescripcionCondicion);
		return (long) q.executeUnique();
	}
}
