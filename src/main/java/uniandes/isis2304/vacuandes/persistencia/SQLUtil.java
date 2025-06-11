/**~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 * Universidad	de	los	Andes	(Bogotá	- Colombia)
 * Departamento	de	Ingeniería	de	Sistemas	y	Computación
 * Licenciado	bajo	el	esquema	Academic Free License versión 2.1
 * 		
 * Curso: isis2304 - Sistemas Transaccionales
 * Proyecto: Parranderos Uniandes
 * @version 1.0
 * @author Germán Bravo
 * Julio de 2018
 * 
 * Revisado por: Claudia Jiménez, Christian Ariza
 * ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
 */

package uniandes.isis2304.vacuandes.persistencia;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;



/**
 * Clase que encapsula los métodos que hacen acceso a la base de datos de Vacuandes
 * Nótese que es una clase que es sólo conocida en el paquete de persistencia
 * 
 */
class SQLUtil
{
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
	public SQLUtil (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para obtener un nuevo número de secuencia
	 * @param pm - El manejador de persistencia
	 * @return El número de secuencia generado
	 */
	public long nextval (PersistenceManager pm)
	{
        Query q = pm.newQuery(SQL, "SELECT "+ pp.darSeqVacuandes() + ".nextval FROM DUAL");
        q.setResultClass(Long.class);
        long resp = (long) q.executeUnique();
        return resp;
	}

	/**
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @param pm - El manejador de persistencia
	 * @return Un arreglo con 17 números que indican el número de tuplas borradas en las tablas 
	 */
	public long [] limpiarVacuandes (PersistenceManager pm)
	{
        Query qCita= pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCita());          
        Query qTalento = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTalentoHumano());
        Query qPertA = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPerteneceA());
        Query qCond = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCondicion());
        Query qTrabajaEn = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTrabajaEn());
        Query qTieneCondPres = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTieneCondicionPreservacion());
        Query qCondPres = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCondicionPreservacion());
        Query qTieneInf = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaTieneInfraestructura());
        Query qInfraestuctura = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaInfraestructura());
        Query qPuntoVacVirtual = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPuntoVacunacionVirtual());
        Query qVacuna = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaVacuna());
        Query qCiudadano = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaCiudadano());
        Query qPuntoVacunacion = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaPuntoVacunacion());
        Query qLoteVacuna = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaLoteVacuna());
        Query qOficinas = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaOficinaRegionalEPS());
        Query qEstadoVacunacion = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEstadoVacunacion());
        Query qEtapa = pm.newQuery(SQL, "DELETE FROM " + pp.darTablaEtapa());

        long citaEliminados = (long) qCita.executeUnique ();
        long talentoE = (long) qTalento.executeUnique ();
        long pertencenEliminadas = (long) qPertA.executeUnique ();
        long condEliminadas = (long) qCond.executeUnique ();
        long trabajaEnEliminados = (long) qTrabajaEn.executeUnique ();
        long tieneCondPresEliminados = (long) qTieneCondPres.executeUnique ();
        long condPresEliminados = (long) qCondPres.executeUnique ();
        long tieneInfEliminados = (long) qTieneInf.executeUnique ();
        long infraestructuraEliminados = (long) qInfraestuctura.executeUnique ();
        long puntoVacVirtEliminados = (long) qPuntoVacVirtual.executeUnique ();
        long vacunaEliminados = (long) qVacuna.executeUnique ();
        long ciudadanoEliminados = (long) qCiudadano.executeUnique ();
        long puntoVacEliminados = (long) qPuntoVacunacion.executeUnique ();
        long loteVacEliminados = (long) qLoteVacuna.executeUnique ();
        long oficinasEliminados = (long) qOficinas.executeUnique ();
        long estadovacunacionEliminados = (long) qEstadoVacunacion.executeUnique ();
        long etapaEliminados = (long) qEtapa.executeUnique ();
        return new long[] {citaEliminados, talentoE, pertencenEliminadas, condEliminadas,trabajaEnEliminados,tieneCondPresEliminados,condPresEliminados,tieneInfEliminados,
        		infraestructuraEliminados, puntoVacVirtEliminados, vacunaEliminados,ciudadanoEliminados,puntoVacEliminados,loteVacEliminados, oficinasEliminados,estadovacunacionEliminados,etapaEliminados};
	}

}
