package uniandes.isis2304.vacuandes.persistencia;



import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Ciudadano;
import uniandes.isis2304.vacuandes.negocio.Vacuna;



public class SQLVacuna {
	
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
	public SQLVacuna (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	
	public long adicionarVacuna(PersistenceManager pm, long idVacuna, String haSidoAplicada, long idLoteVacuna, String puntoVacunacion, String tipoIdentificacion, String identificacion, String tecnologia) {
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaVacuna() + "(IDENTIFICADORVACUNA, HASIDOAPLICADO, LOTEVACUNA, PUNTOVACUNACION, TIPOIDCIUDADANO, IDCIUDADANO, tecnologia ) values (?, ?, ?, ?, ?, ?, ?)");
		q.setParameters(idVacuna, haSidoAplicada, idLoteVacuna, puntoVacunacion,tipoIdentificacion, identificacion, tecnologia);
		return (long) q.executeUnique();
		
	}


	public long aplicarVacuna(PersistenceManager pm, String tipoDeIdentificacion, String identificacionCiudadano) {
		Query q = pm.newQuery(SQL, "UPDATE VACUNA\n" + 
				"SET hasidoaplicado = 1\n" + 
				"WHERE tipoidciudadano = ? AND\n" + 
				"idciudadano = ?");
		q.setParameters(tipoDeIdentificacion,identificacionCiudadano);
		return (long) q.executeUnique();
	}
	
	public long asociarSegundaDosis(PersistenceManager pm, long vacunaPrincipal, long segundaDosis) {
		Query q = pm.newQuery(SQL, "UPDATE VACUNA\n" + 
				"SET segundadosis = ?\n" + 
				"WHERE identificadorvacuna = ?");
		q.setParameters(segundaDosis, vacunaPrincipal);
		return (long) q.executeUnique();
	}


	public Vacuna darVacuna(PersistenceManager pm, long vacunaPrincipal) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaVacuna() + " WHERE identificadorvacuna = ?");
		q.setResultClass(Vacuna.class);
		q.setParameters(vacunaPrincipal);
		return (Vacuna) q.executeUnique();
	}


}
