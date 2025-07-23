package uniandes.isis2304.vacuandes.persistencia;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.Properties;

import javax.jdo.JDODataStoreException;
import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.apache.log4j.Logger;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import uniandes.isis2304.vacuandes.negocio.Cita;
import uniandes.isis2304.vacuandes.negocio.Ciudadano;
import uniandes.isis2304.vacuandes.negocio.Condicion;
import uniandes.isis2304.vacuandes.negocio.CondicionPreservacion;
import uniandes.isis2304.vacuandes.negocio.EstadoVacunacion;
import uniandes.isis2304.vacuandes.negocio.Etapa;
import uniandes.isis2304.vacuandes.negocio.Infraestructura;
import uniandes.isis2304.vacuandes.negocio.LoteVacuna;
import uniandes.isis2304.vacuandes.negocio.OficinaRegionalEPS;
import uniandes.isis2304.vacuandes.negocio.PerteneceA;
import uniandes.isis2304.vacuandes.negocio.PuntoVacunacion;
import uniandes.isis2304.vacuandes.negocio.PuntoVacunacionVirtual;
import uniandes.isis2304.vacuandes.negocio.TalentoHumano;
import uniandes.isis2304.vacuandes.negocio.TieneCondPreservacion;
import uniandes.isis2304.vacuandes.negocio.Vacuna;

public class PersistenciaVacuandes {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(PersistenciaVacuandes.class.getName());
	
	/**
	 * Cadena para indicar el tipo de sentencias que se va a utilizar en una consulta
	 */
	public final static String SQL = "javax.jdo.query.SQL";

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Atributo privado que es el único objeto de la clase - Patrón SINGLETON
	 */
	private static PersistenciaVacuandes instance;
	
	/**
	 * Fábrica de Manejadores de persistencia, para el manejo correcto de las transacciones
	 */
	private PersistenceManagerFactory pmf;
	/**
	 * Arreglo de cadenas con los nombres de las tablas de la base de datos, en su orden:
	 * Secuenciador, OficinaRegionalEPS, PuntoVacunacionVirtual, LoteVacuna, Infraestructura,
	 * CondicionPreservacion, Vacuna, TieneCondicionPreservacion, PuntoVacunacion, TrabajaEn,
	 * Ciudadano, EstadoVacunacion, TalentoHumano, Cita, Etapa, Condicion, TieneInfraestructura y PerteneceA
	 */
	private List <String> tablas;
	/**
	 * Atributo para el acceso a las sentencias SQL propias a PersistenciaVacuandes
	 */
	private SQLUtil sqlUtil;
	
	private SQLCita sqlCita;
	
	private SQLOficinaRegionalEPS sqlOficinaRegionalEPS;
	
	private SQLCondicion sqlCondicion;
	
	private SQLEstadoVacunacion sqlEstadoVacunacion;
	
	private SQLPuntoVacunacion sqlPuntoVacunacion;
	
	private SQLEtapa sqlEtapa;
	
	private SQLCiudadano sqlCiudadano;
	
	private SQLPerteneceA sqlPerteneceA;
	
	private SQLTalentoHumano sqlTalentoHumano;
	
	private SQLVacuna sqlVacuna;
	
	private SQLLoteVacuna sqlLoteVacuna;
	
	private SQLInfraestructura sqlInfraestructura;
	
	private SQLPuntoVacunacionVirtual sqlPuntoVacunacionVirtual;
	
	private SQLCondicionPreservacion sqlCondicionPreservacion;
	
	private SQLTieneCondPreservacion sqlTieneCondPreservacion;
	
	private SQLTieneInfraestructura sqlTieneInfraestructura;

	
	private SQLTrabajaEn sqlTrabajaEn;
	
	//TODO Atributos acceso tablas

	
	

	/* ****************************************************************
	 * 			Métodos del MANEJADOR DE PERSISTENCIA
	 *****************************************************************/
	/**
	 * Constructor privado con valores por defecto - Patrón SINGLETON
	 */
	private PersistenciaVacuandes()
	{
		// Create JDO properties programmatically using environment variables
		Properties props = new Properties();
		
		//  // Get database configuration from system properties (set by Spring)
		//  String connectionUrl = System.getProperty("db.connection.url", "jdbc:oracle:thin:@localhost:1521/FREEPDB1");
		//  String username = System.getProperty("db.connection.username", "ISIS2304B05202110");
		//  String password = System.getProperty("db.connection.password", "MXJgcEkeOLBb");
		// Get database configuration from environment variables (set by Docker)
		String dbHost = System.getenv("DB_HOST") != null ? System.getenv("DB_HOST") : "localhost";
		String dbPort = System.getenv("DB_PORT") != null ? System.getenv("DB_PORT") : "1521";
		String dbSid = System.getenv("DB_SID") != null ? System.getenv("DB_SID") : "FREEPDB1";
		String username = System.getenv("DB_USERNAME") != null ? System.getenv("DB_USERNAME") : "ISIS2304B05202110";
		String password = System.getenv("DB_PASSWORD") != null ? System.getenv("DB_PASSWORD") : "MXJgcEkeOLBb";
		
		String connectionUrl = String.format("jdbc:oracle:thin:@%s:%s/%s", dbHost, dbPort, dbSid);
		
		// Set JDO properties
		props.setProperty("javax.jdo.PersistenceManagerFactoryClass", "org.datanucleus.api.jdo.JDOPersistenceManagerFactory");
		props.setProperty("javax.jdo.option.ConnectionURL", connectionUrl);
		props.setProperty("javax.jdo.option.ConnectionDriverName", "oracle.jdbc.driver.OracleDriver");
		props.setProperty("javax.jdo.option.ConnectionUserName", username);
		props.setProperty("javax.jdo.option.ConnectionPassword", password);
		props.setProperty("javax.jdo.option.Mapping", "oracle");
		props.setProperty("datanucleus.schema.autoCreateAll", "false");
		props.setProperty("datanucleus.query.sql.allowAll", "true");
		
		// Log connection details for debugging
		log.info("JDO Database Configuration:");
		log.info("Host: " + dbHost);
		log.info("Port: " + dbPort);
		log.info("SID: " + dbSid);
		log.info("URL: " + connectionUrl);
		log.info("Username: " + username);
		log.info("Password: " + (password != null ? "*****" : "null"));
		
		// Create persistence manager factory with properties
		pmf = JDOHelper.getPersistenceManagerFactory(props);
		crearClasesSQL ();
		
		// Initialize database if needed
		try {
			PersistenceManager pm = pmf.getPersistenceManager();
			SQLDatabaseInitializer.initializeDatabase(pm);
			pm.close();
		} catch (Exception e) {
			log.warn("Database initialization failed, continuing anyway: " + e.getMessage());
		}
		
		// Define los nombres por defecto de las tablas de la base de datos
		tablas = new LinkedList<String> ();
		tablas.add ("VACUANDES_SEQUENCE");
		tablas.add ("OFICINAREGIONALEPS");
		tablas.add ("PUNTOVACUNACIONVIRTUAL");
		tablas.add ("LOTEVACUNA");
		tablas.add ("INFRAESTRUCTURA");
		tablas.add ("CONDICIONPRESERVACION");
		tablas.add ("VACUNA");
		tablas.add ("TIENECONDICIONPRESERVACION");
		tablas.add ("PUNTOVACUNACION");
		tablas.add ("TRABAJAEN");
		tablas.add ("CIUDADANO");
		tablas.add ("ESTADOVACUNACION");
		tablas.add ("TALENTOHUMANO");
		tablas.add ("CITA");
		tablas.add ("ETAPA");
		tablas.add ("CONDICION");
		tablas.add ("TIENEINFRAESTRUCTURA");
		tablas.add ("PERTENECEA");
	}
	
	/**
	 * Constructor privado, que recibe los nombres de las tablas en un objeto Json - Patrón SINGLETON
	 * @param tableConfig - Objeto Json que contiene los nombres de las tablas y de la unidad de persistencia a manejar
	 */
	private PersistenciaVacuandes(JsonObject tableConfig)
	{
		crearClasesSQL();
		tablas = leerNombresTablas(tableConfig);
		
		String unidadPersistencia = tableConfig.get ("unidadPersistencia").getAsString ();
		log.trace ("Accediendo unidad de persistencia: " + unidadPersistencia);
		pmf = JDOHelper.getPersistenceManagerFactory (unidadPersistencia);
	}
	
	/**
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaVacuandes getInstance()
	{
		if (instance == null)
		{
			instance = new PersistenciaVacuandes();
		}
		return instance;
	}
	
	/**
	 * Constructor que toma los nombres de las tablas de la base de datos del objeto tableConfig
	 * @param tableConfig - El objeto JSON con los nombres de las tablas
	 * @return Retorna el único objeto PersistenciaParranderos existente - Patrón SINGLETON
	 */
	public static PersistenciaVacuandes getInstance (JsonObject tableConfig)
	{
		if (instance == null)
		{
			instance = new PersistenciaVacuandes (tableConfig);
		}
		return instance;
	}
	
	/**
	 * Cierra la conexión con la base de datos
	 */
	public void cerrarUnidadPersistencia ()
	{
		pmf.close ();
		instance = null;
	}
	
	/**
	 * Genera una lista con los nombres de las tablas de la base de datos
	 * @param tableConfig - El objeto Json con los nombres de las tablas
	 * @return La lista con los nombres del secuenciador y de las tablas
	 */
	private List <String> leerNombresTablas (JsonObject tableConfig)
	{
		JsonArray nombres = tableConfig.getAsJsonArray("tablas") ;

		List <String> resp = new LinkedList<String>();
		for (JsonElement nom : nombres)
		{
			resp.add(nom.getAsString());
		}
		
		return resp;
	}
	
	/**
	 * Crea los atributos de clases de apoyo SQL
	 */
	private void crearClasesSQL ()
	{
		sqlCita = new SQLCita(this);
		sqlOficinaRegionalEPS = new SQLOficinaRegionalEPS(this);
		sqlCondicion = new SQLCondicion(this);
		sqlUtil = new SQLUtil(this);
		sqlEstadoVacunacion = new SQLEstadoVacunacion(this);
		sqlPuntoVacunacion = new SQLPuntoVacunacion(this);
		sqlEtapa = new SQLEtapa(this);
		sqlCiudadano = new SQLCiudadano(this);
		sqlPerteneceA = new SQLPerteneceA(this);
		sqlTalentoHumano = new SQLTalentoHumano(this);
		sqlVacuna = new SQLVacuna(this);
		sqlLoteVacuna = new SQLLoteVacuna(this);
		sqlInfraestructura = new SQLInfraestructura(this);
		sqlPuntoVacunacionVirtual = new SQLPuntoVacunacionVirtual(this);
		sqlCondicionPreservacion = new SQLCondicionPreservacion(this);
		sqlTieneCondPreservacion = new SQLTieneCondPreservacion(this);
		sqlTieneInfraestructura = new SQLTieneInfraestructura(this);

		sqlTrabajaEn = new SQLTrabajaEn(this);
		//TODO Constructores clases SQL

	}
	
	/**
	 * @return La cadena de caracteres con el nombre del secuenciador de Vacuandes
	 */
	public String darSeqVacuandes()
	{
		return tablas.get(0);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de OficinaRegionalEPS
	 */
	public String darTablaOficinaRegionalEPS()
	{
		return tablas.get (1);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de PuntoVacunacionVirtual
	 */
	public String darTablaPuntoVacunacionVirtual()
	{
		return tablas.get (2);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de LoteVacuna
	 */
	public String darTablaLoteVacuna()
	{
		return tablas.get (3);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Infraestructura
	 */
	public String darTablaInfraestructura()
	{
		return tablas.get (4);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de CondicionPreservacion
	 */
	public String darTablaCondicionPreservacion()
	{
		return tablas.get (5);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Vacuna
	 */
	public String darTablaVacuna()
	{
		return tablas.get (6);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de TieneCondicionPreservacion
	 */
	public String darTablaTieneCondicionPreservacion()
	{
		return tablas.get (7);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de PuntoVacunacion
	 */
	public String darTablaPuntoVacunacion()
	{
		return tablas.get (8);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de TrabajaEn
	 */
	public String darTablaTrabajaEn()
	{
		return tablas.get (9);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Ciudadano
	 */
	public String darTablaCiudadano()
	{
		return tablas.get (10);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de EstadoVacunacion
	 */
	public String darTablaEstadoVacunacion()
	{
		return tablas.get (11);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de TalentoHumano
	 */
	public String darTablaTalentoHumano()
	{
		return tablas.get (12);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Cita
	 */
	public String darTablaCita()
	{
		return tablas.get (13);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Etapa
	 */
	public String darTablaEtapa()
	{
		return tablas.get (14);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de Condicion
	 */
	public String darTablaCondicion()
	{
		return tablas.get (15);
	}
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de TieneInfraestructura
	 */
	public String darTablaTieneInfraestructura()
	{
		return tablas.get (16);
	}
	
	
	/**
	 * @return La cadena de caracteres con el nombre de la tabla de PerteneceA
	 */
	public String darTablaPerteneceA()
	{
		return tablas.get (17);
	}
	
	/**
	 * Transacción para el generador de secuencia de Vacuandes
	 * Adiciona entradas al log de la aplicación
	 * @return El siguiente número del secuenciador de Vacuandes
	 */
	private long nextval()
	{	
        long resp = sqlUtil.nextval(pmf.getPersistenceManager());
        log.trace ("Generando secuencia: " + resp);
        return resp;
    }
	
	/**
	 * Extrae el mensaje de la exception JDODataStoreException embebido en la Exception e, que da el detalle específico del problema encontrado
	 * @param e - La excepción que ocurrio
	 * @return El mensaje de la excepción JDO
	 */
	private String darDetalleException(Exception e) 
	{
		String resp = "";
		if (e.getClass().getName().equals("javax.jdo.JDODataStoreException"))
		{
			JDODataStoreException je = (javax.jdo.JDODataStoreException) e;
			return je.getNestedExceptions() [0].getMessage();
		}
		return resp;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar OficinaRegionalEPS
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla OficinaRegionalEPS
	 * Adiciona entradas al log de la aplicación
	 * @param NombreEPS - El nombre de la EPS
	 * @param RegionEPS - El numero de la region
	 * @return El objeto OficinaRegionalEPS adicionado. null si ocurre alguna Excepción
	 */
	public OficinaRegionalEPS adicionarOficinaRegionalEPS(String NombreEPS, int RegionEPS) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin();            
            long tuplasInsertadas = sqlOficinaRegionalEPS.adicionarOficinaRegionalEPS(pm, NombreEPS, RegionEPS);
            tx.commit();
            log.trace("Insercion oficina: " + NombreEPS  + "con region " + RegionEPS + ": " + tuplasInsertadas + " tuplas insertadas");
            return new OficinaRegionalEPS(NombreEPS, RegionEPS);
        }
        catch (Exception e) {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla OficinaRegionalEPS
	 * @return La lista de objetos OficinaRegionalEPS, construidos con base en las tuplas de la tabla OficinaRegionalEPS
	 */
	public List<OficinaRegionalEPS> darOficinasRegionalesEPS ()
	{
		return sqlOficinaRegionalEPS.darOficinasRegionalesEPS(pmf.getPersistenceManager());
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar Condicion
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla Condicion
	 * Adiciona entradas al log de la aplicación
	 * @param DescripcionCondicion - Descripcion de la condicion
	 * @return El objeto Condicion adicionado. null si ocurre alguna Excepción
	 */
	public Condicion adicionarCondicion(long IdentificadorCondicion, String DescripcionCondicion) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasInsertadas = sqlCondicion.adicionarCondicion(pm, IdentificadorCondicion, DescripcionCondicion);
            tx.commit();
            
            log.trace ("Inserción condicion: " + DescripcionCondicion + ": " + tuplasInsertadas + " tuplas insertadas");
            return new Condicion(IdentificadorCondicion, DescripcionCondicion);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	

	public List<Condicion> darCondiciones() {
		return sqlCondicion.darCondiciones(pmf.getPersistenceManager());
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar EstadoVacunacion
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla EstadoVacunacion
	 * Adiciona entradas al log de la aplicación
	 * @param DescripcionEstado - Descripcion de la condicion
	 * @return El objeto EstadoVacunacion adicionado. null si ocurre alguna Excepción
	 */
	public EstadoVacunacion adicionarEstadoVacunacion(String DescripcionEstado) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long IdentificadorEstado = nextval();
            long tuplasInsertadas = sqlEstadoVacunacion.adicionarEstadoVacunacion(pm, IdentificadorEstado, DescripcionEstado);
            tx.commit();
            
            log.trace ("Inserción estado vacunacion: " + DescripcionEstado + ": " + tuplasInsertadas + " tuplas insertadas");
            return new EstadoVacunacion(IdentificadorEstado, DescripcionEstado);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long cambiarEstadoVacunacion(String tipoIdCiudadano, String idCiudadano,  long nuevoEstado) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long resp = sqlCiudadano.cambiarEstado(pm, tipoIdCiudadano, idCiudadano, nuevoEstado);
            tx.commit();
            
            log.trace ("Cambio estado vacunacion: " + nuevoEstado );
            return resp;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<EstadoVacunacion> darEstadosVacunacion() {
		return sqlEstadoVacunacion.darEstadosVacunacion(pmf.getPersistenceManager());
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar PuntoVacunacion
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla PuntoVacunacion
	 * Adiciona entradas al log de la aplicación
	 * @param DireccionPuntoVacunacion - La direccion del punto de vacunacion
	 * @param NombrePuntoVacunacion - El nombre del punto de vacunacion
	 * @param CapacidadSimultanea - La capacidad de atencion simultanea del punto
	 * @param CapacidadDiaria - La capacidad diaria del punto
	 * @param DisponibilidadDeDosis - La disponibilidad de disis del punto
	 * @param NombreOficina - El nombre de la oficina que cordina el punto
	 * @param RegionOficina - La numero de la region de la oficina que cordina el punto
	 * @return El objeto PuntoVacunacion adicionado. null si ocurre alguna Excepción
	 */
	public PuntoVacunacion adicionarPuntoVacunacion(String DireccionPuntoVacunacion, String NombrePuntoVacunacion, int CapacidadSimultanea, int CapacidadDiaria, int DisponibilidadDeDosis, String NombreOficina, int RegionOficina , String soloMayores, String soloSalud) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasInsertadas = sqlPuntoVacunacion.adicionarPuntoVacunacion(pm, DireccionPuntoVacunacion, NombrePuntoVacunacion, CapacidadSimultanea, CapacidadDiaria, DisponibilidadDeDosis, NombreOficina, RegionOficina, soloMayores, soloSalud);
            tx.commit();
            
            log.trace ("Inserción estado vacunacion: " + DireccionPuntoVacunacion + ": " + tuplasInsertadas + " tuplas insertadas");
            return new PuntoVacunacion(DireccionPuntoVacunacion, NombrePuntoVacunacion, CapacidadSimultanea, CapacidadDiaria, DisponibilidadDeDosis, NombreOficina, RegionOficina, soloMayores, soloSalud, "1");
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Método que consulta todas las tuplas en la tabla Cita
	 * @return La lista de objetos Cita, construidos con base en las tuplas de la tabla Cita
	 */
	public List<PuntoVacunacion> darPuntosVacunacion() {
		return sqlPuntoVacunacion.darPuntosVacunacion(pmf.getPersistenceManager());
	}
	
	public List<PuntoVacunacion> darPuntosVacunacionDeshabilitados() {
		return sqlPuntoVacunacion.darPuntosVacunacionDeshabilitados(pmf.getPersistenceManager());
	}
	
	public long deshabilitarPuntoVacunacion(String DireccionPuntoVacunacion) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasModificadas = sqlPuntoVacunacion.deshabilitarPuntoVacunacion(pm, DireccionPuntoVacunacion);
            tx.commit();
            
            log.trace ("Deshabilitación: " + DireccionPuntoVacunacion + ": " + tuplasModificadas + " tuplas modificadas");
            return tuplasModificadas;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long habilitarPuntoVacunacion(String DireccionPuntoVacunacion) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasModificadas = sqlPuntoVacunacion.habilitarPuntoVacunacion(pm, DireccionPuntoVacunacion);
            tx.commit();
            log.trace ("Habilitación: " + DireccionPuntoVacunacion + ": " + tuplasModificadas + " tuplas modificadas");
            return tuplasModificadas;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar Cita
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla Cita
	 * Adiciona entradas al log de la aplicación
	 * @param identificadorCita - El identificador de la cita
	 * @param fechaCita - La fecha de la cita
	 * @param tipoIdCiudadano - El tipo de id del ciudadano que tiene la cita
	 * @param idCiudadano - El id del ciudadano que tiene la cita
	 * @return El objeto Cita adicionado. null si ocurre alguna Excepción
	 */
	public Cita adicionarCita(Timestamp fechaCita, String tipoIdCiudadano, String idCiudadano) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long identificadorCita = nextval();
            long tuplasInsertadas = sqlCita.adicionarCita(pm, identificadorCita, fechaCita, tipoIdCiudadano, idCiudadano);
            tx.commit();
            
            log.trace ("Inserción cita: " + fechaCita + ": " + tuplasInsertadas + " tuplas insertadas");
            return new Cita(identificadorCita, fechaCita, tipoIdCiudadano, idCiudadano);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar Etapa
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla Etapa
	 * Adiciona entradas al log de la aplicación
	 * @param NumeroDeEtapa - Identificador de la etapa
	 * @param DescripcionEtapa - Descripcion de la etapa
	 * @return El objeto Etapa adicionado. null si ocurre alguna Excepción
	 */
	public Etapa adicionarEtapa(long NumeroDeEtapa, String DescripcionEtapa) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasInsertadas = sqlEtapa.adicionarEtapa(pm, NumeroDeEtapa, DescripcionEtapa);
            tx.commit();
            
            log.trace ("Inserción etapa: " + NumeroDeEtapa + ": " + tuplasInsertadas + " tuplas insertadas");
            return new Etapa(NumeroDeEtapa, DescripcionEtapa);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<Etapa> darEtapas() {
    	return sqlEtapa.darEtapas(pmf.getPersistenceManager());
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar Ciudadano
	 *****************************************************************/
	
	/**
	 * Método que inserta, de manera transaccional, una tupla en la tabla Ciudadano
	 * Adiciona entradas al log de la aplicación
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
	 * @return El objeto Ciudadano adicionado. null si ocurre alguna Excepción
	 */
	public Ciudadano adicionarCiudadano(String TipoDeIdentificacion, String IdentificacionCiudadano, String NombreCiudadano, String ApellidoCiudadano, 
			String EsVacunable, Timestamp FechaDeNacimiento, long TelefonoDeContacto, long EstadoVacunacion, long Etapa, String NombreOficina, int RegionOficina, String genero, String rol, String ciudad, String localidad) 
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasInsertadas = sqlCiudadano.adicionarCiudadano(pm, TipoDeIdentificacion, IdentificacionCiudadano, NombreCiudadano, ApellidoCiudadano, EsVacunable, FechaDeNacimiento, TelefonoDeContacto, EstadoVacunacion, Etapa, NombreOficina, RegionOficina, genero,  rol,  ciudad,  localidad);
            tx.commit();
            
            log.trace ("Inserción etapa: " + IdentificacionCiudadano + ": " + tuplasInsertadas + " tuplas insertadas");
            return new Ciudadano(TipoDeIdentificacion, IdentificacionCiudadano, NombreCiudadano, ApellidoCiudadano, EsVacunable, FechaDeNacimiento, TelefonoDeContacto, EstadoVacunacion, Etapa, NombreOficina, null, RegionOficina,  genero,  rol,  ciudad,  localidad);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	static Timestamp convertStringToTimestamp(String strDate) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");
		return Optional.ofNullable(strDate) //
				.map(str -> LocalDateTime.parse(str, formatter))
				.map(Timestamp::valueOf) //
				.orElse(null);
	}
	
	public long asignarPuntoVacunacion(String TipoDeIdentificacion, String IdentificacionCiudadano, String PuntoVacunacion)
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
        	long resp = -1;
            tx.begin();
            Ciudadano c = darCiudadano(TipoDeIdentificacion, IdentificacionCiudadano);
            if(darPuntoVacunacion(PuntoVacunacion).getSoloMayores().equals("1") && c.getFechaDeNacimiento().after(convertStringToTimestamp("19610101 00:00"))) {
            	tx.rollback();
            }
            else if(!c.getRol().equals("Personal de salud") && darPuntoVacunacion(PuntoVacunacion).getSoloSalud().equals("1"))
            {
            	tx.rollback();
            }
            else { 
            	resp = sqlCiudadano.asignarPuntoVacunacion(pm, TipoDeIdentificacion, IdentificacionCiudadano, PuntoVacunacion);
            }
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public PuntoVacunacion darPuntoVacunacion(String direccion) {
		return (PuntoVacunacion) sqlPuntoVacunacion.darPuntoVacunacion(pmf.getPersistenceManager(), direccion);
	}
	
	public Ciudadano darCiudadano(String tipoIdCiudadano, String idCiudadano) {
		return (Ciudadano) sqlCiudadano.darCiudadano(pmf.getPersistenceManager(), tipoIdCiudadano,idCiudadano);
	}
	

	public long cambiarEtapa(String tipoIdCiudadano, String idCiudadano, long etapa) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlCiudadano.cambiarEtapa(pm, tipoIdCiudadano, idCiudadano, etapa);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long darVacunadosPorRegion(long regionEPS, Timestamp fechaInferior, Timestamp fechaSuperior) {
		return sqlCiudadano.darTotalVacunadosPorRegion(pmf.getPersistenceManager(), regionEPS, fechaInferior, fechaSuperior).longValue();
	}
	
	public long darHabilitadosPorRegion(long regionEPS, Timestamp fechaInferior, Timestamp fechaSuperior) {
		return sqlCiudadano.darHabilitadosPorRegion(pmf.getPersistenceManager(), regionEPS, fechaInferior, fechaSuperior).longValue();
	}
	
	public long darVacunadosPorEstado(long estado, Timestamp fechaInferior, Timestamp fechaSuperior) {
		return sqlCiudadano.darTotalVacunadosPorEstado(pmf.getPersistenceManager(), estado, fechaInferior, fechaSuperior).longValue();
	}
	
	public long darHabilitadosPorEstado(long estado, Timestamp fechaInferior, Timestamp fechaSuperior) {
		return sqlCiudadano.darTotalHabilitadosPorEstado(pmf.getPersistenceManager(), estado, fechaInferior, fechaSuperior).longValue();
	}
	
	public long darVacunadosPorEtapa(long etapa, Timestamp fechaInferior, Timestamp fechaSuperior) {
		return sqlCiudadano.darTotalVacunadosPorEtapa(pmf.getPersistenceManager(), etapa, fechaInferior, fechaSuperior).longValue();
	}
	
	public long darHabilitadosPorEtapa(long etapa, Timestamp fechaInferior, Timestamp fechaSuperior) {
		return sqlCiudadano.darTotalHabilitadosPorEtapa(pmf.getPersistenceManager(), etapa, fechaInferior, fechaSuperior).longValue();
	}
	
	public long darVacunadosPorEPS(String eps, Timestamp fechaInferior, Timestamp fechaSuperior) {
		return sqlCiudadano.darTotalVacunadosPorEPS(pmf.getPersistenceManager(), eps, fechaInferior, fechaSuperior).longValue();
	}
	
	public long darHabilitadosPorEPS(String eps, Timestamp fechaInferior, Timestamp fechaSuperior) {
		return sqlCiudadano.darHabilitadosPorEPS(pmf.getPersistenceManager(), eps, fechaInferior, fechaSuperior).longValue();
	}

	/* ****************************************************************
	 * 			Métodos para manejar PerteneceA
	 *****************************************************************/
	
	public long agregarCondicionCiudadano(String tipoDeIdentificacion, String identificacionCiudadano,
			long identificadorCondicion, String descripcionCondicion) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlPerteneceA.agregarCondicionCiudadano(pm, tipoDeIdentificacion, identificacionCiudadano, identificadorCondicion, descripcionCondicion);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public PerteneceA adicionarCondicionCiudadano(String TipoIdCiudadano, String IdCiudadano,
			long IdentificadorCondicion, String DescripcionCondicion) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasInsertadas = sqlPerteneceA.adicionarCondicionCiudadano(pm, TipoIdCiudadano, IdCiudadano, IdentificadorCondicion, DescripcionCondicion);
            tx.commit();
            
            log.trace ("Inserción condicion: " + DescripcionCondicion + ": " + tuplasInsertadas + " tuplas insertadas");
            return new PerteneceA(TipoIdCiudadano, IdCiudadano, IdentificadorCondicion, DescripcionCondicion);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar TalentoHumano
	 *****************************************************************/
	
	public TalentoHumano adicionarTalentoHumano(String TipoDeIdentificacion, String IdentificacionCiudadano, String FuncionTalentoHumano) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasInsertadas = sqlTalentoHumano.adicionarTalentoHumano(pm, TipoDeIdentificacion, IdentificacionCiudadano, FuncionTalentoHumano);
            tx.commit();
            
            log.trace ("Inserción talento: " + FuncionTalentoHumano + " con id " + IdentificacionCiudadano + ": " + tuplasInsertadas + " tuplas insertadas");
            return new TalentoHumano(TipoDeIdentificacion, IdentificacionCiudadano, FuncionTalentoHumano);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public LoteVacuna adicionarLoteVacuna( String nombre, int region) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long idLote = nextval();
            log.info("antes sql");
            long tuplasInsertadas = sqlLoteVacuna.adicionarLoteVacuna(pm, idLote, nombre, region);
            tx.commit();
            
            log.trace ("Inserción loteVacuna con id " + idLote + ": " + tuplasInsertadas + " tuplas insertadas");
            return new LoteVacuna(idLote, nombre, region);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<LoteVacuna> darLotesVacuna()
	{
		return sqlLoteVacuna.darLotesVacunas(pmf.getPersistenceManager());
	}
	public List<CondicionPreservacion> darCondicionPreservacion()
	{
		return sqlCondicionPreservacion.darcondPreservacion(pmf.getPersistenceManager());
	}
	
	
	
	public CondicionPreservacion adicionarCondicionPreservacion( String descripcion) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long idCond = nextval();
            long tuplasInsertadas = sqlCondicionPreservacion.adicionarCondPreservacion(pm,idCond, descripcion);
            tx.commit();
            
            log.trace ("Inserción CondicionPreservacion con id " + idCond + ": " + tuplasInsertadas + " tuplas insertadas");
            return new CondicionPreservacion(idCond, descripcion);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long agregarCondicionPreservacionVacuna(long idCond, long idVacuna) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
        
            long r = sqlTieneCondPreservacion.agregarCondPreservacionaVacuna(pm, idCond, idVacuna);
            tx.commit();
            
            log.trace ("Inserción TieneCondPreservacionVacuna con idVacuna " + idVacuna );
            return  r;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long agregarTieneInfraestructura(long idInfraestructura, String direccion) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
        
            long r = sqlTieneInfraestructura.agregarTieneInfraestructura(pm, idInfraestructura, direccion);
            tx.commit();
            
            log.trace ("Inserción TieneInfraestructura con id" + idInfraestructura );
            return  r;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public long agregarTalentoHumanoPunto(String tipoId, String identificacion, String direccion) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            log.info("Persistencia");
        
            long r = sqlTrabajaEn.adicionarTalentoHumanoPunto(pm, tipoId, identificacion, direccion);
            log.info("Persistencia despues sql");
            tx.commit();
            
            log.trace ("Inserción TalentoHumanoPunto con id" + identificacion );
            return  r;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	

	
	
	public Vacuna adicionarVacuna( String haSidoAplicada, long idLoteVacuna, String puntoVacunacion, String tipoIdentificacion, String identificacion, String tecnologia) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long idVacuna = nextval();
            long tuplasInsertadas = sqlVacuna.adicionarVacuna(pm, idVacuna, haSidoAplicada, idLoteVacuna, puntoVacunacion, tipoIdentificacion, identificacion, tecnologia);
            tx.commit();
            log.trace ("Inserción vacuna con id " + idVacuna + ": " + tuplasInsertadas + " tuplas insertadas");
            return new Vacuna(idVacuna, haSidoAplicada, idLoteVacuna,null, puntoVacunacion, tipoIdentificacion, identificacion, tecnologia);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
		
	public long aplicarVacuna(String tipoDeIdentificacion, String identificacionCiudadano) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long resp = sqlVacuna.aplicarVacuna(pm, tipoDeIdentificacion, identificacionCiudadano);
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public long asociarSegundaDosis(long vacunaPrincipal, long segundaDosis) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
        	long resp = -1;
            tx.begin();
            if(!sqlVacuna.darVacuna(pm, vacunaPrincipal).getTecnologia().equals(sqlVacuna.darVacuna(pm, segundaDosis).getTecnologia())) {
            	tx.rollback();
            }
            else {
            resp = sqlVacuna.asociarSegundaDosis(pm, vacunaPrincipal, segundaDosis);
            }
            tx.commit();
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
            return -1;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public Infraestructura adicionarInfraestrucutra(String descripcion ) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long idInfraestructura = nextval();
            long tuplasInsertadas = sqlInfraestructura.adicionarInfraestructura(pm, idInfraestructura, descripcion);
            tx.commit();
            log.trace ("Inserción infraestrcutra con id " + idInfraestructura + ": " + tuplasInsertadas + " tuplas insertadas");
            return new Infraestructura(idInfraestructura, descripcion);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	public PuntoVacunacionVirtual adicionarPuntoVacunacionVirtual(String direccionVacunacion, String nombreOficina, int regionOficina, long idVacuna) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            long tuplasInsertadas = sqlPuntoVacunacionVirtual.adicionarPuntoVacunacionVirtual(pm, direccionVacunacion, nombreOficina, regionOficina, idVacuna);
            tx.commit();
            log.trace ("Inserción punto vacunacion virtual con direccion" + direccionVacunacion + ": " + tuplasInsertadas + " tuplas insertadas");
            return new PuntoVacunacionVirtual(direccionVacunacion, nombreOficina, regionOficina, idVacuna);
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<Ciudadano> darCiudadanosAtendidosFecha( String direccion, Timestamp fecha1,Timestamp fecha2) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            List<Ciudadano> r = sqlCiudadano.darCiudadanosAtendidosFecha(pm, direccion, fecha1, fecha2);
            tx.commit();
            log.trace ("Dar ciudadanos de un puntodevacunacion" + direccion );
            return r;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<PuntoVacunacion> darPuntosVacunacionEfectivos( Timestamp f1, Timestamp f2) { 
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            List<PuntoVacunacion>  r = sqlPuntoVacunacion.darPuntosVacunacionEfectivos(pm, f1, f2);
            tx.commit();
            log.trace ("darPuntosVacunacionEfectivos"  );
            return r;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	public List<Ciudadano> darCohortesCiudadanos(String unificada) {
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        
        try
        {
            tx.begin(); 
            List<Ciudadano>  r = sqlCiudadano.darCohortes(pm, unificada);
            tx.commit();
            log.trace ("darCohortesCiudadanos" );
            return r;
            
        } 
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return null;
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
	}
	
	/**
	 * Elimina todas las tuplas de todas las tablas de la base de datos de Vacuandes
	 * Crea y ejecuta las sentencias SQL para cada tabla de la base de datos - EL ORDEN ES IMPORTANTE 
	 * @return Un arreglo con 17 números que indican el número de tuplas borradas en las tablas
	 */
	public long [] limpiarVacuandes ()
	{
		PersistenceManager pm = pmf.getPersistenceManager();
        Transaction tx=pm.currentTransaction();
        try
        {
            tx.begin();
            long [] resp = sqlUtil.limpiarVacuandes(pm);
            tx.commit ();
            log.info ("Borrada la base de datos");
            return resp;
        }
        catch (Exception e)
        {
//        	e.printStackTrace();
        	log.error ("Exception : " + e.getMessage() + "\n" + darDetalleException(e));
        	return new long[] {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
        }
        finally
        {
            if (tx.isActive())
            {
                tx.rollback();
            }
            pm.close();
        }
		
	}
}
