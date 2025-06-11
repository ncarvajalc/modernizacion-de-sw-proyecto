package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.EstadoVacunacion;
import uniandes.isis2304.vacuandes.negocio.LoteVacuna;
import uniandes.isis2304.vacuandes.negocio.OficinaRegionalEPS;

class SQLEstadoVacunacion {
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
	public SQLEstadoVacunacion (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un ESTADO a la base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @param IdentificadorEstado - Identificador del estado
	 * @param DescripcionEstado - Descripcion del estado
	 * @return El número de tuplas insertadas
	 */
	public long adicionarEstadoVacunacion(PersistenceManager pm, long IdentificadorEstado, String DescripcionEstado) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaEstadoVacunacion() + "(IdentificadorEstado, DescripcionEstado) values (?, ?)");
		q.setParameters(IdentificadorEstado, DescripcionEstado);
		return (long) q.executeUnique();
	}

	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LOS ESTADOS de la 
	 * base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos ESTADO
	 */
	public List<EstadoVacunacion> darEstadosVacunacion(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaEstadoVacunacion());
		q.setResultClass(EstadoVacunacion.class);
		return (List<EstadoVacunacion>) q.executeList();
	}
	
	
	
	

}
