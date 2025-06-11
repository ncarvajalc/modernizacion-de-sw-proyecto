package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Etapa;
import uniandes.isis2304.vacuandes.negocio.OficinaRegionalEPS;

public class SQLEtapa {
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
	public SQLEtapa (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar una ETAPA a la base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @param NumeroDeEtapa - Identificador de la etapa
	 * @param DescripcionEtapa - Descripcion de la etapa
	 * @return El número de tuplas insertadas
	 */
	public long adicionarEtapa(PersistenceManager pm, long NumeroDeEtapa, String DescripcionEtapa) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaEtapa() + "(NumeroDeEtapa, DescripcionEtapa) values (?, ?)");
		q.setParameters(NumeroDeEtapa, DescripcionEtapa);
		return (long) q.executeUnique();
	}

	public List<Etapa> darEtapas(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaEtapa());
		q.setResultClass(Etapa.class);
		return (List<Etapa>) q.executeList();
	}
}
