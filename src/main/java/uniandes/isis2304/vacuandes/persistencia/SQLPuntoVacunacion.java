package uniandes.isis2304.vacuandes.persistencia;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Cita;
import uniandes.isis2304.vacuandes.negocio.Ciudadano;
import uniandes.isis2304.vacuandes.negocio.PuntoVacunacion;

public class SQLPuntoVacunacion {
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
	public SQLPuntoVacunacion (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}
	
	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un PUNTOVACUNACION a la base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @param DireccionPuntoVacunacion - La direccion del punto de vacunacion
	 * @param NombrePuntoVacunacion - El nombre del punto de vacunacion
	 * @param CapacidadSimultanea - La capacidad de atencion simultanea del punto
	 * @param CapacidadDiaria - La capacidad diaria del punto
	 * @param DisponibilidadDeDosis - La disponibilidad de disis del punto
	 * @param NombreOficina - El nombre de la oficina que cordina el punto
	 * @param RegionOficina - La numero de la region de la oficina que cordina el punto
	 * @return El número de tuplas insertadas
	 */
	public long adicionarPuntoVacunacion(PersistenceManager pm, String DireccionPuntoVacunacion, String NombrePuntoVacunacion, int CapacidadSimultanea, int CapacidadDiaria, int DisponibilidadDeDosis, String NombreOficina, int RegionOficina, String soloMayores, String soloSalud) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaPuntoVacunacion() + "(DireccionPuntoVacunacion, NombrePuntoVacunacion, CapacidadSimultanea, CapacidadDiaria, DisponibilidadDeDosis, NombreOficina, RegionOficina, soloMayores, soloSalud, estaHabilitado) values (?, ?, ?, ?, ?, ? ,?, ?, ?, 1)");
		q.setParameters(DireccionPuntoVacunacion, NombrePuntoVacunacion, CapacidadSimultanea, CapacidadDiaria, DisponibilidadDeDosis, NombreOficina, RegionOficina, soloMayores, soloSalud);
		return (long) q.executeUnique();
	}

	public PuntoVacunacion darPuntoVacunacion(PersistenceManager pm, String direccion) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPuntoVacunacion() + " WHERE DireccionPuntoVacunacion = ? ");
		q.setResultClass(PuntoVacunacion.class);
		q.setParameters(direccion);
		return (PuntoVacunacion) q.executeUnique();
	}
	
	
	
	
	public List<PuntoVacunacion> darPuntosVacunacionEfectivos(PersistenceManager pm, Timestamp f1, Timestamp f2) {
		Query q = pm.newQuery(SQL, "SELECT * \n"
				+ "FROM (SELECT  p.direccionpuntovacunacion, p.nombrepuntovacunacion, p.capacidadsimultanea, p.capacidaddiaria, p.disponibilidaddedosis, p.nombreoficina, p.regionoficina\n"
				+ "	             FROM PUNTOVACUNACION p, CITA ct, CIUDADANO c, VACUNA v\n"
				+ "	             WHERE ct.tipoidciudadano = c.tipodeidentificacion AND\n"
				+ "	             ct.idciudadano = c.identificacionciudadano AND \n"
				+ "	             c.puntovacunacion = p.direccionpuntovacunacion AND \n"
				+ "	             v.tipoidciudadano = c.tipodeidentificacion AND\n"
				+ "	             v.idciudadano = c.identificacionciudadano AND\n"
				+ "	             v.hasidoaplicado = 0 AND\n"
				+ "	             ct.fechacita BETWEEN ? AND ?\n"
				+ "	             GROUP BY p.direccionpuntovacunacion, p.nombrepuntovacunacion, p.capacidadsimultanea, p.capacidaddiaria, p.disponibilidaddedosis, p.nombreoficina, p.regionoficina\n"
				+ "              ORDER BY COUNT(ct.identificadorcita) DESC)\n"
				+ "WHERE ROWNUM <= 20\n"
				+ "");
		q.setResultClass(PuntoVacunacion.class);
		q.setParameters(f1, f2);
		return (List<PuntoVacunacion>) q.executeList();
	}
	public List<PuntoVacunacion> darPuntosVacunacion(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPuntoVacunacion() + " WHERE estahabilitado = 1");
		q.setResultClass(PuntoVacunacion.class);
		return (List<PuntoVacunacion>) q.executeList();
	}
	
	
	public BigDecimal darHabilitadosPorEPS(PersistenceManager pm, String regionEPS, Timestamp fechaInferior, Timestamp fechaSuperior) {
		Query q = pm.newQuery(SQL, "SELECT COUNT(distinct c.identificacionciudadano) \n" + 
				"FROM Ciudadano c, Oficinaregionaleps o, Cita cit\n" + 
				"WHERE c.regionoficina = o.regioneps AND\n" + 
				"      c.nombreoficina = o.nombreeps AND\n" + 
				"      c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = cit.idciudadano AND\n" + 
				"      o.nombreeps = ? AND\n" + 
				"      cit.fechacita BETWEEN ? AND ?");
		q.setParameters(regionEPS, fechaInferior, fechaSuperior);
		return (BigDecimal) q.executeUnique(); 
	}
		
	public long deshabilitarPuntoVacunacion(PersistenceManager pm, String DireccionPuntoVacunacion) {
		Query q = pm.newQuery(SQL, "UPDATE PUNTOVACUNACION\n" + 
				"SET estahabilitado = 0\n" + 
				"WHERE direccionpuntovacunacion = ? \n");
		q.setParameters(DireccionPuntoVacunacion);
		q.executeUnique();
			
		Query q2 = pm.newQuery(SQL, "UPDATE CIUDADANO c\n" + 
				"SET c.puntovacunacion = NULL\n" + 
				"WHERE c.puntovacunacion = ? AND\n" + 
				"     (c.tipodeidentificacion, c.identificacionciudadano) NOT IN\n" + 
				"     (SELECT tipodeidentificacion, identificacionciudadano\n" + 
				"      FROM Ciudadano c, Cita cit, Vacuna v\n" + 
				"      WHERE c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"            c.identificacionciudadano = cit.idciudadano AND\n" + 
				"            c.tipodeidentificacion = v.tipoidciudadano AND\n" + 
				"            c.identificacionciudadano = v.idciudadano AND\n" + 
				"            v.hasidoaplicado = '1')");
		q2.setParameters(DireccionPuntoVacunacion);
		
		long evacuados = (long) q2.executeUnique();
		
		Query q3 = pm.newQuery(SQL, "UPDATE PUNTOVACUNACION\n" + 
				"SET CapacidadSimultanea = CapacidadSimultanea + ?\n" + 
				"WHERE direccionpuntovacunacion = ?");
		q3.setParameters(evacuados, DireccionPuntoVacunacion);
		q3.executeUnique();
		
		return evacuados;
	}
	
	public long habilitarPuntoVacunacion(PersistenceManager pm, String DireccionPuntoVacunacion) {
		Query q = pm.newQuery(SQL, "UPDATE PUNTOVACUNACION\n" + 
				"SET estahabilitado = 1\n" + 
				"WHERE direccionpuntovacunacion = ? \n");
		q.setParameters(DireccionPuntoVacunacion);
		return (long) q.executeUnique();
	}
	
	public List<PuntoVacunacion> darPuntosVacunacionDeshabilitados(PersistenceManager pm) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaPuntoVacunacion() + " WHERE estahabilitado = 0");
		q.setResultClass(PuntoVacunacion.class);
		return (List<PuntoVacunacion>) q.executeList();
	}
}
