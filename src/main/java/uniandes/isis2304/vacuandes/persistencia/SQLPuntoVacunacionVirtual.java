package uniandes.isis2304.vacuandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

class SQLPuntoVacunacionVirtual {
	
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaVacuandes.SQL;
	
	private static Logger log = Logger.getLogger(SQLPuntoVacunacionVirtual.class.getName());
	
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
	public SQLPuntoVacunacionVirtual (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	
	public long adicionarPuntoVacunacionVirtual(PersistenceManager pm, String direccionVacunacion, String nombreOficina, int regionOficina, long idVacuna) {
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPuntoVacunacionVirtual() + "(DIRPUNTOVACUNACIONVIRTUAL, NOMBREOFICINA, REGIONOFICINA, VACUNAASIGNADA) values (?, ?, ?, ?)");
		q.setParameters(direccionVacunacion, nombreOficina, regionOficina, idVacuna  );
		
		return (long) q.executeUnique();
		
		
	}

}
