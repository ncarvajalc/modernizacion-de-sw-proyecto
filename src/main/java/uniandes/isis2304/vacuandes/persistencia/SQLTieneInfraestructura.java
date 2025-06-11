package uniandes.isis2304.vacuandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLTieneInfraestructura {
	
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
	public SQLTieneInfraestructura (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}

	public long agregarTieneInfraestructura(PersistenceManager pm, long idInfraestructura, String direccion) {
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaTieneInfraestructura() + "(IDENTIFICADORINFRAESTRUCTURA, DIRECCIONPUNTOVACUNACION) values (?, ?)");
		q.setParameters(idInfraestructura, direccion);
		return (long) q.executeUnique();
	}
	
	
	
	
	
	
	
	

}
