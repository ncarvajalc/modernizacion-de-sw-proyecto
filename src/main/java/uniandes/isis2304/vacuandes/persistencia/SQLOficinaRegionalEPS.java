package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.OficinaRegionalEPS;

class SQLOficinaRegionalEPS {
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
	public SQLOficinaRegionalEPS (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una OFICINAREGIONAL a la base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @param NombreEPS - El nombre de la EPS
	 * @param RegionEPS - El numero de la region
	 * @return El número de tuplas insertadas
	 */
	public long adicionarOficinaRegionalEPS (PersistenceManager pm, String NombreEPS, int RegionEPS) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaOficinaRegionalEPS() + "(NombreEPS, RegionEPS) values (?, ?)");
		q.setParameters(NombreEPS, RegionEPS);
		return (long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UNA OFICINA de la 
	 * base de datos de Vacuandes, por su nombre y region
	 * @param pm - El manejador de persistencia
	 * @param NombreEPS - El nombre de la EPS
	 * @param RegionEPS - El numero de la region
	 * @return El objeto OficinaRegionalEPS que tiene el nombre y region dado
	 */
	public OficinaRegionalEPS darOficinaRegionalEPS (PersistenceManager pm, String NombreEPS, int RegionEPS) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOficinaRegionalEPS() + " WHERE NombreEPS = ? AND RegionEPS = ?");
		q.setResultClass(OficinaRegionalEPS.class);
		q.setParameters(NombreEPS, RegionEPS);
		return (OficinaRegionalEPS) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LAS OFICINAS de la 
	 * base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos OFICINA
	 */
	public List<OficinaRegionalEPS> darOficinasRegionalesEPS (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaOficinaRegionalEPS());
		q.setResultClass(OficinaRegionalEPS.class);
		return (List<OficinaRegionalEPS>) q.executeList();
	}
}
