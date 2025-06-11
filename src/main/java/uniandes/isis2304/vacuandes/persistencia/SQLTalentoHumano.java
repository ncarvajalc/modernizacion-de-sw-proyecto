package uniandes.isis2304.vacuandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

public class SQLTalentoHumano {
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
	public SQLTalentoHumano (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un TALENTOHUMANO a la base de datos
	 * @param pm - El manejador de persistencia
	 * @param TipoDeIdentificacion - El tipo de identificacion del talento humano
	 * @param IdentificacionCiudadano - El numero de identificacion del talento humano
	 * @param FuncionTalentoHumano - La funcion del talento humano (Doctor, Enfermero)
	 * @return EL número de tuplas insertadas
	 */
	public long adicionarTalentoHumano(PersistenceManager pm, String TipoDeIdentificacion, String IdentificacionCiudadano, String FuncionTalentoHumano) 
	{
        Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaTalentoHumano() + "(TipoDeIdentificacion, IdentificacionCiudadano, FuncionTalentoHumano) values (?, ?, ?)");
        q.setParameters(TipoDeIdentificacion, IdentificacionCiudadano, FuncionTalentoHumano);
        return (long) q.executeUnique();
	}
}
