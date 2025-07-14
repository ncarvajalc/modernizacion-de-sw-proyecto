package uniandes.isis2304.vacuandes.persistencia;

import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Cita;

class SQLCita {

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
	public SQLCita (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una CITA a la base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @param identificadorCita - El identificador de la cita
	 * @param fechaCita - La fecha de la cita
	 * @param tipoIdCiudadano - El tipo de id del ciudadano que tiene la cita
	 * @param idCiudadano - El id del ciudadano que tiene la cita
	 * @return El número de tuplas insertadas
	 */
	public long adicionarCita (PersistenceManager pm, long identificadorCita, Timestamp fechaCita, String tipoIdCiudadano, String idCiudadano) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCita() + "(identificadorCita, fechaCita, tipoIdCiudadano, idCiudadano) values (?, ?, ?, ?)");
		q.setParameters(identificadorCita, fechaCita, tipoIdCiudadano, idCiudadano);
		return (long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para eliminar UNA CITA de la base de datos de Parranderos, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param identificadorCita - El identificador de la cita
	 * @return EL número de tuplas eliminadas
	 */
	public long eliminarCitaPorId (PersistenceManager pm, long identificadorCita)
	{
        Query q = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCita() + " WHERE identificadorCita = ?");
        q.setParameters(identificadorCita);
        return (long) q.executeUnique();
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de UNA CITA de la 
	 * base de datos de Vacuandes, por su identificador
	 * @param pm - El manejador de persistencia
	 * @param identificadorCita - El identificador de la cita
	 * @return El objeto CITA que tiene el identificador dado
	 */
	public Cita darCitaPorId (PersistenceManager pm, long identificadorCita) 
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCita () + " WHERE identificadorCita = ?");
		q.setResultClass(Cita.class);
		q.setParameters(identificadorCita);
		return (Cita) q.executeUnique();
	}
		
	/**
	 * Crea y ejecuta la sentencia SQL para encontrar la información de LAS CITAS de la 
	 * base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @return Una lista de objetos CITA
	 */
	public List<Cita> darCitas (PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCita());
		q.setResultClass(Cita.class);
		return (List<Cita>) q.executeList();
	}
}
