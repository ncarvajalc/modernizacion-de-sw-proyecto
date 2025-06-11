package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import uniandes.isis2304.vacuandes.negocio.Cita;
import uniandes.isis2304.vacuandes.negocio.LoteVacuna;
import uniandes.isis2304.vacuandes.negocio.TieneCondPreservacion;

public class SQLTieneCondPreservacion {
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaVacuandes.SQL;
	
	private static Logger log = Logger.getLogger(SQLTieneCondPreservacion.class.getName());
	
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
	public SQLTieneCondPreservacion (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	
	public long agregarCondPreservacionaVacuna(PersistenceManager pm, long IdentificadorCondicion, long idVacuna ) {
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaTieneCondicionPreservacion() + "(IDENTIFICADORCONDICION, IDENTIFICADORVACUNA) values (?, ?)");
		q.setParameters(IdentificadorCondicion, idVacuna);
		
		return (long) q.executeUnique();
		
		
	}
	

}
