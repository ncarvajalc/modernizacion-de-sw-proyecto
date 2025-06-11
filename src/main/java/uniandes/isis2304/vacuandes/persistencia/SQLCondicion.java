package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Bar;
import uniandes.isis2304.vacuandes.negocio.Condicion;
import uniandes.isis2304.vacuandes.negocio.OficinaRegionalEPS;

public class SQLCondicion {
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
	public SQLCondicion (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una CONDICION a la base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @param IdentificadorCondicion - Identificador de la condicion
	 * @param DescripcionCondicion - Descripcion de la condicion
	 * @return El número de tuplas insertadas
	 */
	public long adicionarCondicion(PersistenceManager pm, long IdentificadorCondicion, String DescripcionCondicion) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCondicion() + "(IdentificadorCondicion, DescripcionCondicion) values (?, ?)");
		q.setParameters(IdentificadorCondicion, DescripcionCondicion);
		return (long) q.executeUnique();
	}

	public List<Condicion> darCondiciones(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCondicion());
		q.setResultClass(Condicion.class);
		return (List<Condicion>) q.executeList();
	}
}
