package uniandes.isis2304.vacuandes.negocio;

import java.sql.Timestamp;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.JsonObject;

import uniandes.isis2304.vacuandes.persistencia.PersistenciaVacuandes;

/**
 * Clase principal del negocio
 */
public class Vacuandes {
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(Vacuandes.class.getName());
	
	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * El manejador de persistencia
	 */
	private PersistenciaVacuandes pp;
	
	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * El constructor por defecto
	 */
	public Vacuandes ()
	{
		pp = PersistenciaVacuandes.getInstance ();
	}
	
	/**
	 * El constructor que recibe los nombres de las tablas en tableConfig
	 * @param tableConfig - Objeto Json con los nombres de las tablas y de la unidad de persistencia
	 */
	public Vacuandes (JsonObject tableConfig)
	{
		pp = PersistenciaVacuandes.getInstance (tableConfig);
	}
	
	/**
	 * Cierra la conexión con la base de datos (Unidad de persistencia)
	 */
	public void cerrarUnidadPersistencia ()
	{
		pp.cerrarUnidadPersistencia ();
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar las OficinaRegionalEPS
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente una oficina 
	 * Adiciona entradas al log de la aplicación
	 * @param NombreEPS - El nombre de la EPS
	 * @param RegionEPS - El numero de la region
	 * @return El objeto OficinaRegionalEPS adicionado. null si ocurre alguna Excepción
	 */
	public OficinaRegionalEPS adicionarOficinaRegionalEPS (String NombreEPS, int RegionEPS)
	{
		log.info ("Adicionando oficina " + NombreEPS + "-" + RegionEPS);
		OficinaRegionalEPS oficina = pp.adicionarOficinaRegionalEPS(NombreEPS, RegionEPS);
        log.info ("Adicionando oficina: " + oficina);
        return oficina;
	}

	/**
	 * Encuentra todas las oficinas en Vacuandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos OficinaRegionalEPS con todos las oficinas que conoce la aplicación, llenos con su información básica
	 */
	public List<OficinaRegionalEPS> darOficinasRegionalesEPS ()
	{
        log.info ("Consultando Oficinas");
        List<OficinaRegionalEPS> oficinas = pp.darOficinasRegionalesEPS();	
        log.info ("Consultando Bebidas: " + oficinas.size() + " oficinas existentes");
        return oficinas;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar las Condiciones
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente una condicion 
	 * Adiciona entradas al log de la aplicación
	 * @param DescripcionCondicion - La descripcion de la condicion
	 * @return El objeto Condicion adicionado. null si ocurre alguna Excepción
	 */
	public Condicion adicionarCondicion (long IdentificadorCondicion, String DescripcionCondicion)
	{
		log.info ("Adicionando condicion " + DescripcionCondicion );
		Condicion condicion = pp.adicionarCondicion(IdentificadorCondicion, DescripcionCondicion);
        log.info ("Adicionando condicion: " + condicion);
        return condicion;
	}
	
	public List<Condicion> darCondiciones() {
		log.info ("Consultando condiciones");
        List<Condicion> cond = pp.darCondiciones();	
        log.info ("Consultando Bebidas: " + cond.size() + " oficinas existentes");
        return cond;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los Estados
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente un estado de vacunacion
	 * Adiciona entradas al log de la aplicación
	 * @param DescripcionEstado - La descripcion del estado
	 * @return El objeto EstadoVacunacion adicionado. null si ocurre alguna Excepción
	 */
	public EstadoVacunacion adicionarEstadoVacunacion(String DescripcionEstado)
	{
		try {
			log.info ("Adicionando estado " + DescripcionEstado );
			EstadoVacunacion estado = pp.adicionarEstadoVacunacion(DescripcionEstado);
			log.info ("Adicionando estado: " + estado);
			return estado;
		} catch (Exception e) {
			log.error ("Error al adicionar estado " + DescripcionEstado, e);
			return null;
		}
	}
	public long cambiarEstadoVacunacion(String tipoIdCiudadano, String idCiudadano, long nuevoEstado)
	{
		log.info ("Cambiando estado " + nuevoEstado );
		long cambios = pp.cambiarEstadoVacunacion(tipoIdCiudadano, idCiudadano, nuevoEstado);
        log.info ("Adicionando estado: " + cambios);
        return cambios;
	}
	
	
	
	public List<EstadoVacunacion> darEstadosVacunacion() {
		log.info ("Consultando Estados");
        List<EstadoVacunacion> estados = pp.darEstadosVacunacion();	
        log.info ("Consultando Bebidas: " + estados.size() + " estados existentes");
        return estados;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los PuntoVacunacion
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente un punto de vacunacion
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
	public PuntoVacunacion adicionarPuntoVacunacion(String DireccionPuntoVacunacion, String NombrePuntoVacunacion, int CapacidadSimultanea, int CapacidadDiaria, int DisponibilidadDeDosis, String NombreOficina, int RegionOficina, String soloMayores, String soloSalud)
	{
		log.info ("Adicionando punto " + DireccionPuntoVacunacion );
		PuntoVacunacion punto = pp.adicionarPuntoVacunacion(DireccionPuntoVacunacion, NombrePuntoVacunacion, CapacidadSimultanea, CapacidadDiaria, DisponibilidadDeDosis, NombreOficina, RegionOficina, soloMayores, soloSalud);
        log.info ("Adicionando punto: " + punto);
        return punto;
	}
	
	/**
	 * Encuentra todos las puntos de vacunacion en Vacuandes
	 * Adiciona entradas al log de la aplicación
	 * @return Una lista de objetos PuntosVacunacion que conoce la aplicación, llenos con su información básica
	 */
	public List<PuntoVacunacion> darPuntosVacunacion() {
		log.info ("Consultando puntos");
        List<PuntoVacunacion> puntos = pp.darPuntosVacunacion();	
        log.info ("Consultando puntos: " + puntos.size() + " puntos existentes");
        return puntos;
	}
	
	public List<PuntoVacunacion> darPuntosVacunacionDeshabilitados() {
		log.info ("Consultando puntos");
        List<PuntoVacunacion> puntos = pp.darPuntosVacunacionDeshabilitados();	
        log.info ("Consultando puntos: " + puntos.size() + " puntos deshabilitados");
        return puntos;
	}
	
	public long deshabilitarPunto(String DireccionPuntoVacunacion) {
		log.info ("deshabilitando punto");
        long resp = pp.deshabilitarPuntoVacunacion(DireccionPuntoVacunacion);
        log.info ("Se modificaron " +resp + "tuplas");
        return resp;
	}
	
	public long habilitarPunto(String DireccionPuntoVacunacion) {
		log.info ("habilitando punto");
        long resp = pp.habilitarPuntoVacunacion(DireccionPuntoVacunacion);
        log.info ("Se modificaron " +resp + "tuplas");
        return resp;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar las Cita
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente una cita
	 * Adiciona entradas al log de la aplicación
	 * @param identificadorCita - El identificador de la cita
	 * @param fechaCita - La fecha de la cita
	 * @param tipoIdCiudadano - El tipo de id del ciudadano que tiene la cita
	 * @param idCiudadano - El id del ciudadano que tiene la cita
	 @return El objeto Cita adicionado. null si ocurre alguna Excepción
	 */
	public Cita adicionarCita(Timestamp fechaCita, String tipoIdCiudadano, String idCiudadano)
	{
		log.info ("Adicionando cita " + fechaCita );
		Cita cita = pp.adicionarCita(fechaCita, tipoIdCiudadano, idCiudadano);
        log.info ("Adicionando cita: " + cita);
        return cita;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar las Etapa
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente una etapa
	 * Adiciona entradas al log de la aplicación
	 * @param NumeroDeEtapa - Identificador de la etapa
	 * @param DescripcionEtapa - Descripcion de la etapa
	 * @return El objeto Etapa adicionado. null si ocurre alguna Excepción
	 */
	public Etapa adicionarEtapa(long NumeroDeEtapa, String DescripcionEtapa)
	{
		log.info ("Adicionando etapa " + NumeroDeEtapa );
		Etapa etapa = pp.adicionarEtapa(NumeroDeEtapa, DescripcionEtapa);
        log.info ("Adicionando etapa: " + etapa);
        return etapa;
	}
	
	public List<Etapa> darEtapas() {
		log.info ("Consultando etapas");
        List<Etapa> etapas = pp.darEtapas();	
        log.info ("Consultando etapas: " + etapas.size() + " etapas existentes");
        return etapas;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar los Ciudadano
	 *****************************************************************/
	
	/**
	 * Adiciona de manera persistente un punto de vacunacion
	 * Adiciona entradas al log de la aplicación
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
	 * @return El objeto Ciudadano adicionado. null si ocurre alguna Excepción
	 */
	public Ciudadano adicionarCiudadano(String TipoDeIdentificacion, String IdentificacionCiudadano, String NombreCiudadano, String ApellidoCiudadano, 
			String EsVacunable, Timestamp FechaDeNacimiento, long TelefonoDeContacto, long EstadoVacunacion, long Etapa, String NombreOficina, int RegionOficina, String genero, String rol, String ciudad, String localidad)
	{
		log.info ("Adicionando ciudadano " + IdentificacionCiudadano );
		Ciudadano c = pp.adicionarCiudadano(TipoDeIdentificacion, IdentificacionCiudadano, NombreCiudadano, ApellidoCiudadano, EsVacunable, FechaDeNacimiento, TelefonoDeContacto, EstadoVacunacion, Etapa, NombreOficina, RegionOficina,  genero,  rol,  ciudad,  localidad);
        log.info ("Adicionando ciudadno: " + c);
        return c;
	}
	
	public long asignarPuntoVacunacion(String TipoDeIdentificacion, String IdentificacionCiudadano, String PuntoVacunacion)
	{
        log.info ("Asignando punto vacunacion: " + PuntoVacunacion);
        long cambios = pp.asignarPuntoVacunacion(TipoDeIdentificacion, IdentificacionCiudadano, PuntoVacunacion);
        return cambios;
	}
	
	public Ciudadano darCiudadano(String tipoIdCiudadano, String idCiudadano) {
		log.info ("Dar información COMPLETA de un ciudadano por id: " + idCiudadano);
        Ciudadano c = pp.darCiudadano(tipoIdCiudadano,idCiudadano);
        log.info ("Buscando ciudadano por Id: " + c.toString() != null ? c : "NO EXISTE");
        return c;
	}
	
	public long actualizarEtapa(String TipoIdCiudadano, String IdCiudadano, long Etapa) {
		log.info ("Cambiando etapa de ciudadano: " + IdCiudadano);
        long cambios = pp.cambiarEtapa(TipoIdCiudadano, IdCiudadano, Etapa);
        return cambios;
	}
	
	public long darTotalVacunadosRegion(long regionEPS, Timestamp fechaInferior, Timestamp fechaSuperior) {
		log.info ("Dar cantidad vacunados por region");
		long total = pp.darVacunadosPorRegion(regionEPS, fechaInferior, fechaSuperior);
		log.info ("se encontraron: " + total);
		return total;
	}
	
	public long darTotalHabilitadosRegion(long regionEPS, Timestamp fechaInferior, Timestamp fechaSuperior) {
		log.info ("Dar cantidad habilitados por region");
		long total = pp.darHabilitadosPorRegion(regionEPS, fechaInferior, fechaSuperior);
		log.info ("se encontraron: " + total);
		return total;
	}
	
	public long darTotalVacunadosEstado(long estado, Timestamp fechaInferior, Timestamp fechaSuperior) {
		log.info ("Dar cantidad vacunados por estado");
		long total = pp.darVacunadosPorEstado(estado, fechaInferior, fechaSuperior);
		log.info ("se encontraron: " + total);
		return total;
	}
	
	public long darTotalHabilitadosEstado(long estado, Timestamp fechaInferior, Timestamp fechaSuperior) {
		log.info ("Dar cantidad habilitados por estado");
		long total = pp.darHabilitadosPorEstado(estado, fechaInferior, fechaSuperior);
		log.info ("se encontraron: " + total);
		return total;
	}
	
	public long darTotalVacunadosEtapa(long etapa, Timestamp fechaInferior, Timestamp fechaSuperior) {
		log.info ("Dar cantidad vacunados por etapa");
		long total = pp.darVacunadosPorEtapa(etapa, fechaInferior, fechaSuperior);
		log.info ("se encontraron: " + total);
		return total;
	}
	
	public long darTotalHabilitadosEtapa(long etapa, Timestamp fechaInferior, Timestamp fechaSuperior) {
		log.info ("Dar cantidad habilitados por etapa");
		long total = pp.darHabilitadosPorEtapa(etapa, fechaInferior, fechaSuperior);
		log.info ("se encontraron: " + total);
		return total;
	}
	
	public long darTotalVacunadosEPS(String eps, Timestamp fechaInferior, Timestamp fechaSuperior) {
		log.info ("Dar cantidad vacunados por eps");
		long total = pp.darVacunadosPorEPS(eps, fechaInferior, fechaSuperior);
		log.info ("se encontraron: " + total);
		return total;
	}
	
	public long darTotalHabilitadosEPS(String eps, Timestamp fechaInferior, Timestamp fechaSuperior) {
		log.info ("Dar cantidad habilitados por eps");
		long total = pp.darHabilitadosPorEPS(eps, fechaInferior, fechaSuperior);
		log.info ("se encontraron: " + total);
		return total;
	}
	
	public List<Ciudadano> consultarCohortesCiudadanos(String unificada) {
		log.info ("Consultando cohortes");
		List<Ciudadano> ciudadanos = pp.darCohortesCiudadanos(unificada);
		log.info ("Consultados cohortes");
		return ciudadanos;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar PerteneceA
	 *****************************************************************/
	
	public long agregarCondicionCiudadano(String tipoDeIdentificacion, String identificacionCiudadano,
			long identificadorCondicion, String descripcionCondicion) {
		log.info ("Asignando punto vacunacion: " + descripcionCondicion);
        long cambios = pp.agregarCondicionCiudadano(tipoDeIdentificacion, identificacionCiudadano, identificadorCondicion, descripcionCondicion);
        return cambios;
		
	}
	
	public PerteneceA adicionarCondicionCiudadano(String TipoIdCiudadano, String IdCiudadano,
			long IdentificadorCondicion, String DescripcionCondicion) 
	{
		log.info ("Adicionando condicion " + DescripcionCondicion + " a "+ TipoIdCiudadano+ IdCiudadano);
		PerteneceA c = pp.adicionarCondicionCiudadano(TipoIdCiudadano, IdCiudadano, IdentificadorCondicion, DescripcionCondicion);
        log.info ("Adicionando condicion: " + c);
        return c;
	}
	
	/* ****************************************************************
	 * 			Métodos para manejar TalentoHumano
	 *****************************************************************/
	
	public TalentoHumano adicionarTalentoHumano(String TipoDeIdentificacion, String IdentificacionCiudadano, String FuncionTalentoHumano) 
	{
		log.info ("Adicionando talento " + IdentificacionCiudadano+ "con rol" + FuncionTalentoHumano);
		TalentoHumano t = pp.adicionarTalentoHumano(TipoDeIdentificacion, IdentificacionCiudadano, FuncionTalentoHumano);
        log.info ("Adicionando talento: " + t);
        return t;
	}
	
	
	public Vacuna adicionarVacuna(String haSidoAplicada, long idLoteVacuna, String puntoVacunacion, String tipoIdentificacion, String identificacion, String tecnologia) 
	{
		log.info ("Adicionando vacuna con ciudadano" + identificacion);
		Vacuna v = pp.adicionarVacuna(haSidoAplicada, idLoteVacuna, puntoVacunacion, tipoIdentificacion, identificacion, tecnologia);
        log.info ("Adicionando vacuna: " + v);
        return v;
	}
	
	public LoteVacuna adicionarLoteVacuna(String nombreOficina, int RegionOficina) 
	{
		log.info ("Adicionando lote con oficina" + nombreOficina);
		LoteVacuna v = pp.adicionarLoteVacuna(nombreOficina, RegionOficina);
        log.info ("Adicionando Lotevacuna: " + v);
        return v;
	}
	public long agregarCondAVacuna(long idCond, long idVacuna) 
	{
		log.info ("Adicionando CondAVacuna con id" + idVacuna);
		long v = pp.agregarCondicionPreservacionVacuna(idCond, idVacuna);
        log.info ("Adicionando Lotevacuna: " + v);
        return v;
	}
	
	public long asociarSegundaDosis(long idPrincipal, long idSegundaDosis) 
	{
		log.info ("Adicionando segunda dosis " + idSegundaDosis +" con vacuna" + idPrincipal);
		long v = pp.asociarSegundaDosis(idPrincipal, idSegundaDosis);
        log.info ("Adicionando segunda dosis: " + v);
        return v;
	}
	
	public long agregarTalentoHumanoPunto( String tipoId, String identificacion, String direccion) 
	{
		log.info ("Adicionando TalentoHumanoPunto con id" + identificacion);
		long v = pp.agregarTalentoHumanoPunto(tipoId, identificacion, direccion);
        log.info ("Adicionando TalentoHumanoPunto: " + v);
        return v;
	}
	
	
	
	public CondicionPreservacion adicionarCondicionPreservacion(String descripcion) 
	{
		log.info ("Adicionando condicion con descripcion" + descripcion);
		CondicionPreservacion v = pp.adicionarCondicionPreservacion(descripcion);
        log.info ("Adicionando condicion con descripcion: " + v);
        return v;
	}
	
	public List<Ciudadano> darCiudadanosAtendidosFecha( String direccion, Timestamp fecha1,Timestamp fecha2){
		log.info ("Consultando ciudadanos por punto de vacunacion");
		List<Ciudadano> lotes = pp.darCiudadanosAtendidosFecha(direccion, fecha1, fecha2);
        log.info ("Consultando ciudadanos por punto de vacunacion");
        return lotes;
	}
	
	public List<PuntoVacunacion> darPuntosVacunacionEfectivos( Timestamp f1, Timestamp f2) {
		log.info ("Consultando PuntosVacunacionEfectivos");
        List<PuntoVacunacion> lotes = pp.darPuntosVacunacionEfectivos(f1, f2);
        log.info ("Consultando PuntosVacunacionEfectivos: " + lotes.size() + " puntos existentes");
        return lotes;
	}
	
	public List<LoteVacuna> darLotes() {
		log.info ("Consultando lotes");
        List<LoteVacuna> lotes = pp.darLotesVacuna();
        log.info ("Consultando lotes: " + lotes.size() + " lotes existentes");
        return lotes;
	}
	public List<CondicionPreservacion> darCondPreservacion() {
		log.info ("Consultando condsP");
        List<CondicionPreservacion> conds = pp.darCondicionPreservacion();
        log.info ("Consultando condsP: " + conds.size() + " condiciones existentes");
        return conds;
	}
	
	public Infraestructura adicionarInfraestructura(String descripcion) 
	{
		log.info ("Adicionando infraestructura con descripcion" + descripcion);
		Infraestructura v = pp.adicionarInfraestrucutra(descripcion);
        log.info ("Adicionando infraestructura: " + v);
        return v;
	}
	
	public long agregarTieneInfraestructura(long idInfra, String direccion) 
	{
		log.info ("Adicionando Tieneinfraestructura con id" + idInfra);
		long v = pp.agregarTieneInfraestructura(idInfra, direccion);
        log.info ("Adicionando infraestructura: " + v);
        return v;
	}
	
	public PuntoVacunacionVirtual adicionarPuntoVacunacionVirtual(String direccionVacunacion, String nombreOficina, int regionOficina, long idVacuna) 
	{
		log.info ("Adicionando punto vacunacion virtual con direccion" + direccionVacunacion);
		PuntoVacunacionVirtual v = pp.adicionarPuntoVacunacionVirtual(direccionVacunacion, nombreOficina, regionOficina, idVacuna);
        log.info ("Adicionando punto vacunacion virtual: " + v);
        return v;
	}
	
	public long aplicarVacuna(String tipoDeIdentificacion, String identificacionCiudadano) {
		log.info ("Aplicar vacuna");
		long v = pp.aplicarVacuna(tipoDeIdentificacion, identificacionCiudadano);
        log.info ("Aplicada vacuna");
        return v;
		
	}
	
	
	
	
	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	
	

	public long[] limpiarVacuandes() {
		log.info ("Limpiando la BD de Vacuandes");
        long [] borrrados = pp.limpiarVacuandes();	
        log.info ("Limpiando la BD de Vacuandes: Listo!");
        return borrrados;
	}

	
}
