package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import uniandes.isis2304.vacuandes.negocio.CondicionPreservacion;
import uniandes.isis2304.vacuandes.negocio.LoteVacuna;

class SQLCondicionPreservacion {
	
	
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaVacuandes.SQL;
	
	private static Logger log = Logger.getLogger(SQLCondicionPreservacion.class.getName());
	
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
	public SQLCondicionPreservacion (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	
	public long adicionarCondPreservacion(PersistenceManager pm, long IdentificadorCondicion, String descripcion ) {
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCondicionPreservacion() + "(IDENTIFICADORCONDPRESERVACION, DESCRIPCIONPRESERVACION) values (?, ?)");
		q.setParameters(IdentificadorCondicion, descripcion);
		
		return (long) q.executeUnique();
		
		
	}
	
	public List<CondicionPreservacion> darcondPreservacion(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCondicionPreservacion());
		q.setResultClass(CondicionPreservacion.class);
		return (List<CondicionPreservacion>) q.executeList();
	}
	
	
	

}
