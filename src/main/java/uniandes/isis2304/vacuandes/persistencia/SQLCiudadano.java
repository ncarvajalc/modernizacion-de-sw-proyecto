package uniandes.isis2304.vacuandes.persistencia;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import uniandes.isis2304.vacuandes.negocio.Ciudadano;

public class SQLCiudadano {
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
	public SQLCiudadano (PersistenciaVacuandes pp)
	{
		this.pp = pp;
	}

	/**
	 * Crea y ejecuta la sentencia SQL para adicionar un CIUDADANO a la base de datos de Vacuandes
	 * @param pm - El manejador de persistencia
	 * @param TipoDeIdentificacion - Tipo de identificacion ciudadano
	 * @param IdentificacionCiudadano - Identificacion del ciudadano
	 * @param NombreCiudadano - Nombre del ciudadano
	 * @param ApellidoCiudadano - Apellido del ciudadano
	 * @param EsVacunable - Indica si es vacunable o no
	 * @param FechaDeNacimiento - FechaDeNacimiento del ciudadano
	 * @param TelefonoDeContacto - TelefonoDeContacto del ciudadano
	 * @param EstadoVacunacion - EstadoVacunacion del ciudadano
	 * @param Etapa - Etapa del ciudadano
	 * @param NombreOficina - NombreOficina eps del ciudadano
	 * @param RegionOficina - RegionOficina eps del ciudadano
	 * @return El número de tuplas insertadas
	 */
	public long adicionarCiudadano(PersistenceManager pm, String TipoDeIdentificacion, String IdentificacionCiudadano, String NombreCiudadano, String ApellidoCiudadano, 
			String EsVacunable, Timestamp FechaDeNacimiento, long TelefonoDeContacto, long EstadoVacunacion, long Etapa, String NombreOficina, int RegionOficina, String genero, String rol, String ciudad, String localidad) 
	{
		Query q = pm.newQuery(SQL, "INSERT INTO " + pp.darTablaCiudadano() + "(TipoDeIdentificacion, IdentificacionCiudadano, NombreCiudadano, ApellidoCiudadano, EsVacunable, FechaDeNacimiento, TelefonoDeContacto, EstadoVacunacion, Etapa, NombreOficina, RegionOficina, genero,  rol,  ciudad,  localidad) values (?, ?, ?, ? ,? , ?, ?, ?, ?, ?, ?, ?, ?, ? ,?)");
		q.setParameters(TipoDeIdentificacion, IdentificacionCiudadano, NombreCiudadano, ApellidoCiudadano, EsVacunable, FechaDeNacimiento, TelefonoDeContacto, EstadoVacunacion, Etapa, NombreOficina, RegionOficina,  genero,  rol,  ciudad,  localidad);
		return (long) q.executeUnique();
	}

	public long asignarPuntoVacunacion(PersistenceManager pm, String TipoDeIdentificacion, String IdentificacionCiudadano, String PuntoVacunacion) {
		
		Query q1 = pm.newQuery(SQL,"UPDATE PUNTOVACUNACION\n" + 
				"SET CAPACIDADSIMULTANEA  = CAPACIDADSIMULTANEA - 1\n" + 
				"WHERE direccionpuntovacunacion = ?");
		q1.setParameters(PuntoVacunacion);
		q1.executeUnique();
		
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCiudadano() + " SET PuntoVacunacion = ? WHERE TipoDeIdentificacion = ? AND IdentificacionCiudadano = ?");
		q.setParameters(PuntoVacunacion,TipoDeIdentificacion, IdentificacionCiudadano);
		return (long) q.executeUnique();
	}
	
	

	public Ciudadano darCiudadano(PersistenceManager pm, String tipoIdCiudadano, String idCiudadano) {
		Query q = pm.newQuery(SQL, "SELECT * FROM " + pp.darTablaCiudadano() + " WHERE TipoDeIdentificacion = ? AND IdentificacionCiudadano = ?");
		q.setResultClass(Ciudadano.class);
		q.setParameters(tipoIdCiudadano,idCiudadano);
		return (Ciudadano) q.executeUnique();
	}

	public long cambiarEtapa(PersistenceManager pm, String tipoIdCiudadano, String idCiudadano, long etapa) {
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCiudadano() + " SET etapa = ? WHERE TipoDeIdentificacion = ? AND IdentificacionCiudadano = ?");
		q.setParameters(etapa, tipoIdCiudadano, idCiudadano);
		return (long) q.executeUnique(); 
	}
	
	public long cambiarEstado(PersistenceManager pm, String tipoIdCiudadano, String idCiudadano, long estado) {
		Query q = pm.newQuery(SQL, "UPDATE " + pp.darTablaCiudadano() + " SET ESTADOVACUNACION = ? WHERE TipoDeIdentificacion = ? AND IdentificacionCiudadano = ?");
		q.setParameters(estado, tipoIdCiudadano, idCiudadano);
		return (long) q.executeUnique(); 
	}

	public BigDecimal darTotalVacunadosPorRegion(PersistenceManager pm, long regionEPS, Timestamp fechaInferior, Timestamp fechaSuperior) {
		Query q = pm.newQuery(SQL, "SELECT COUNT(distinct c.identificacionciudadano) \n" + 
				"FROM Vacuna v, Ciudadano c, Oficinaregionaleps o, Cita cit\n" + 
				"WHERE v.idciudadano = c.identificacionciudadano AND\n" + 
				"      v.tipoidciudadano = c.tipodeidentificacion AND\n" + 
				"      c.regionoficina = o.regioneps AND\n" + 
				"      c.nombreoficina = o.nombreeps AND\n" + 
				"      c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = cit.idciudadano AND\n" + 
				"      v.hasidoaplicado = '1' AND\n" + 
				"      o.regioneps = ? AND\n" + 
				"      cit.fechacita BETWEEN ? AND ?\n" + 
				"      ");
		q.setParameters(regionEPS, fechaInferior, fechaSuperior);
		return (BigDecimal) q.executeUnique(); 
	}
	
	public BigDecimal darHabilitadosPorRegion(PersistenceManager pm, long regionEPS, Timestamp fechaInferior, Timestamp fechaSuperior) {
		Query q = pm.newQuery(SQL, "SELECT COUNT(distinct c.identificacionciudadano) \n" + 
				"FROM Ciudadano c, Oficinaregionaleps o, Cita cit\n" + 
				"WHERE c.regionoficina = o.regioneps AND\n" + 
				"      c.nombreoficina = o.nombreeps AND\n" + 
				"      c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = cit.idciudadano AND\n" + 
				"      o.regioneps = ? AND\n" + 
				"      cit.fechacita BETWEEN ? AND ?");
		q.setParameters(regionEPS, fechaInferior, fechaSuperior);
		return (BigDecimal) q.executeUnique(); 
	}
	
	public BigDecimal darTotalVacunadosPorEstado(PersistenceManager pm, long estado, Timestamp fechaInferior, Timestamp fechaSuperior) {
		Query q = pm.newQuery(SQL, "SELECT COUNT(distinct c.identificacionciudadano) \n" + 
				"FROM Vacuna v, Ciudadano c, Cita cit\n" + 
				"WHERE v.idciudadano = c.identificacionciudadano AND\n" + 
				"      v.tipoidciudadano = c.tipodeidentificacion AND\n" + 
				"      c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = cit.idciudadano AND\n" + 
				"      v.hasidoaplicado = '1' AND\n" + 
				"      c.estadovacunacion = ? AND\n" + 
				"      cit.fechacita BETWEEN ? AND ?" + 
				"      ");
		q.setParameters(estado, fechaInferior, fechaSuperior);
		return (BigDecimal) q.executeUnique(); 
	}
	
	public BigDecimal darTotalHabilitadosPorEstado(PersistenceManager pm, long estado, Timestamp fechaInferior, Timestamp fechaSuperior) {
		Query q = pm.newQuery(SQL, "SELECT COUNT(distinct c.identificacionciudadano) \n" + 
				"FROM Ciudadano c, Cita cit\n" + 
				"WHERE c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = cit.idciudadano AND\n" + 
				"      c.estadovacunacion = ? AND\n" + 
				"      cit.fechacita BETWEEN ? AND ?" + 
				"      ");
		q.setParameters(estado, fechaInferior, fechaSuperior);
		return (BigDecimal) q.executeUnique(); 
	}
	
	public BigDecimal darTotalVacunadosPorEtapa(PersistenceManager pm, long etapa, Timestamp fechaInferior, Timestamp fechaSuperior) {
		Query q = pm.newQuery(SQL, "SELECT COUNT(distinct c.identificacionciudadano) \n" + 
				"FROM Vacuna v, Ciudadano c, Cita cit\n" + 
				"WHERE v.idciudadano = c.identificacionciudadano AND\n" + 
				"      v.tipoidciudadano = c.tipodeidentificacion AND\n" + 
				"      c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = cit.idciudadano AND\n" + 
				"      v.hasidoaplicado = '1' AND\n" + 
				"      c.etapa = ? AND\n" + 
				"      cit.fechacita BETWEEN ? AND ?" + 
				"      ");
		q.setParameters(etapa, fechaInferior, fechaSuperior);
		return (BigDecimal) q.executeUnique(); 
	}
	
	public BigDecimal darTotalHabilitadosPorEtapa(PersistenceManager pm, long estado, Timestamp fechaInferior, Timestamp fechaSuperior) {
		Query q = pm.newQuery(SQL, "SELECT COUNT(distinct c.identificacionciudadano) \n" + 
				"FROM Ciudadano c, Cita cit\n" + 
				"WHERE c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = cit.idciudadano AND\n" + 
				"      c.etapa = ? AND\n" + 
				"      cit.fechacita BETWEEN ? AND ?" + 
				"      ");
		q.setParameters(estado, fechaInferior, fechaSuperior);
		return (BigDecimal) q.executeUnique(); 
	}
	

	public BigDecimal darTotalVacunadosPorEPS(PersistenceManager pm, String regionEPS, Timestamp fechaInferior, Timestamp fechaSuperior) {
		Query q = pm.newQuery(SQL, "SELECT COUNT(distinct c.identificacionciudadano) \n" + 
				"FROM Vacuna v, Ciudadano c, Oficinaregionaleps o, Cita cit\n" + 
				"WHERE v.idciudadano = c.identificacionciudadano AND\n" + 
				"      v.tipoidciudadano = c.tipodeidentificacion AND\n" + 
				"      c.regionoficina = o.regioneps AND\n" + 
				"      c.nombreoficina = o.nombreeps AND\n" + 
				"      c.tipodeidentificacion = cit.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = cit.idciudadano AND\n" + 
				"      v.hasidoaplicado = '1' AND\n" + 
				"      o.nombreeps = ? AND\n" + 
				"      cit.fechacita BETWEEN ? AND ?\n" + 
				"      ");
		q.setParameters(regionEPS, fechaInferior, fechaSuperior);
		return (BigDecimal) q.executeUnique(); 
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
	
	public  List<Ciudadano> darCiudadanosAtendidosFecha(PersistenceManager pm, String direccion, Timestamp fecha1,Timestamp fecha2 ) {
		Query q = pm.newQuery(SQL, "SELECT c.tipodeidentificacion, c.identificacionciudadano, c.nombreCiudadano, c.apellidociudadano, c.esvacunable, c.fechadenacimiento, c.telefonodecontacto, c.estadovacunacion, c.etapa, c.puntovacunacion, c.nombreoficina, c.regionoficina \n" + 
				"FROM Ciudadano c, Cita ct, Puntovacunacion p\n" + 
				"WHERE ct.tipoidciudadano = c.tipodeidentificacion AND\n" + 
				"      ct.idciudadano = c.identificacionciudadano AND\n" + 
				"      p.direccionpuntovacunacion = c.puntovacunacion AND\n" + 
				"      p.direccionpuntovacunacion = ? AND\n" + 
				"      ct.fechacita BETWEEN ? AND ?");
		q.setParameters(direccion, fecha1, fecha2);
		q.setResultClass(Ciudadano.class);
		return (List<Ciudadano>) q.executeList(); 
	}

	public List<Ciudadano> darCohortes(PersistenceManager pm, String unificada) {
		Query q = pm.newQuery(SQL, "SELECT distinct c.*\n" + 
				"FROM ciudadano c LEFT JOIN pertenecea pe ON\n" + 
				"     (c.tipodeidentificacion = pe.tipoidciudadano AND\n" + 
				"     c.identificacionciudadano = pe.idciudadano),\n" + 
				"     oficinaregionaleps o, puntovacunacion pu, vacuna v\n" + 
				"WHERE c.nombreoficina = o.nombreeps AND\n" + 
				"      c.regionoficina = o.regioneps AND\n" + 
				"      c.puntovacunacion = pu.direccionpuntovacunacion AND\n" + 
				"      c.tipodeidentificacion = v.tipoidciudadano AND\n" + 
				"      c.identificacionciudadano = v.idciudadano" + unificada);
		q.setResultClass(Ciudadano.class);
		return (List<Ciudadano>) q.executeList();
	}

	
	
	

}
