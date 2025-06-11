package uniandes.isis2304.vacuandes.persistencia;

import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import org.apache.log4j.Logger;

import uniandes.isis2304.vacuandes.negocio.Cita;
import uniandes.isis2304.vacuandes.negocio.LoteVacuna;

class SQLLoteVacuna {
	
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Cadena que representa el tipo de consulta que se va a realizar en las sentencias de acceso a la base de datos
	 * Se renombra acá para facilitar la escritura de las sentencias
	 */
	private final static String SQL = PersistenciaVacuandes.SQL;
	
	private static Logger log = Logger.getLogger(SQLLoteVacuna.class.getName());
	
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
	public SQLLoteVacuna (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	
	public long adicionarLoteVacuna(PersistenceManager pm, long IdentificadorLote, String NombreOficina, int RegionOficina ) {
		
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaLoteVacuna() + "(IdentificadorLote, NombreOficina, RegionOficina) values (?, ?, ?)");
		q.setParameters(IdentificadorLote, NombreOficina, RegionOficina);
		
		return (long) q.executeUnique();
		
		
	}
	public List<LoteVacuna> darLotesVacunas(PersistenceManager pm)
	{
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaLoteVacuna());
		q.setResultClass(LoteVacuna.class);
		return (List<LoteVacuna>) q.executeList();
	}

	
	
	
	
	

}
