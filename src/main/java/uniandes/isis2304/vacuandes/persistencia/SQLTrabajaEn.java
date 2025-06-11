package uniandes.isis2304.vacuandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

public class SQLTrabajaEn {
	

	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaVacuandes.SQL;
	
	private static Logger log = Logger.getLogger(SQLTrabajaEn.class.getName());
	
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
	public SQLTrabajaEn (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	
	public long adicionarTalentoHumanoPunto(PersistenceManager pm, String tipoId, String identificacion, String direccion ) {
		log.info("En sql");
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaTrabajaEn() + "(TIPOIDTALENTOHUMANO, IDTALENTOHUMANO,DIRECCIONPUNTOVACUNACION) values (?, ?, ?)");
		q.setParameters(tipoId, identificacion, direccion);
		log.info("PASA sql");
		return (long) q.executeUnique();
		
		
	}


}
