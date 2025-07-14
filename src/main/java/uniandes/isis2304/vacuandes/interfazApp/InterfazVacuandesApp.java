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

package uniandes.isis2304.vacuandes.interfazApp;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import javax.jdo.JDODataStoreException;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.UIManager;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;

import uniandes.isis2304.vacuandes.negocio.Ciudadano;
import uniandes.isis2304.vacuandes.negocio.OficinaRegionalEPS;

import uniandes.isis2304.vacuandes.negocio.PuntoVacunacion;
import uniandes.isis2304.vacuandes.negocio.VOCita;
import uniandes.isis2304.vacuandes.negocio.VOCiudadano;
import uniandes.isis2304.vacuandes.negocio.VOCondicion;
import uniandes.isis2304.vacuandes.negocio.VOCondicionPreservacion;
import uniandes.isis2304.vacuandes.negocio.VOEstadoVacunacion;
import uniandes.isis2304.vacuandes.negocio.VOEtapa;
import uniandes.isis2304.vacuandes.negocio.VOInfraestructura;
import uniandes.isis2304.vacuandes.negocio.VOLoteVacuna;
import uniandes.isis2304.vacuandes.negocio.VOOficinaRegionalEPS;
import uniandes.isis2304.vacuandes.negocio.VOPerteneceA;
import uniandes.isis2304.vacuandes.negocio.VOPuntoVacunacion;
import uniandes.isis2304.vacuandes.negocio.VOPuntoVacunacionVirtual;
import uniandes.isis2304.vacuandes.negocio.VOTalentoHumano;
import uniandes.isis2304.vacuandes.negocio.VOTieneCondPreservacion;
import uniandes.isis2304.vacuandes.negocio.VOVacuna;
import uniandes.isis2304.vacuandes.negocio.Vacuandes;

/**
 * Clase principal de la interfaz
 * @author Germán Bravo
 */
@SuppressWarnings("serial")

public class InterfazVacuandesApp extends JFrame implements ActionListener
{
	/* ****************************************************************
	 * 			Constantes
	 *****************************************************************/
	/**
	 * Logger para escribir la traza de la ejecución
	 */
	private static Logger log = Logger.getLogger(InterfazVacuandesApp.class.getName());

	/**
	 * Ruta al archivo de configuración de la interfaz
	 */
	private static final String CONFIG_INTERFAZ = "./src/main/resources/config/interfaceConfigApp.json"; 

	/**
	 * Ruta al archivo de configuración de los nombres de tablas de la base de datos
	 */
	private static final String CONFIG_TABLAS = "./src/main/resources/config/TablasBD_A.json"; 

	/* ****************************************************************
	 * 			Atributos
	 *****************************************************************/
	/**
	 * Objeto JSON con los nombres de las tablas de la base de datos que se quieren utilizar
	 */
	private JsonObject tableConfig;

	/**
	 * Asociación a la clase principal del negocio.
	 */
	private Vacuandes vacuandes;

	/**
	 * Usuario de la interfaz
	 */
	private int usuario;

	/* ****************************************************************
	 * 			Atributos de interfaz
	 *****************************************************************/
	/**
	 * Objeto JSON con la configuración de interfaz de la app.
	 */
	private JsonObject guiConfig;

	/**
	 * Panel de despliegue de interacción para los requerimientos
	 */
	private PanelDatos panelDatos;

	/**
	 * Menú de la aplicación
	 */
	private JMenuBar menuBar;

	/* ****************************************************************
	 * 			Métodos
	 *****************************************************************/
	/**
	 * Construye la ventana principal de la aplicación. <br>
	 * <b>post:</b> Todos los componentes de la interfaz fueron inicializados.
	 */
	public InterfazVacuandesApp( )
	{
		// Carga la configuración de la interfaz desde un archivo JSON
		guiConfig = openConfig ("Interfaz", CONFIG_INTERFAZ);

		// Configura la apariencia del frame que contiene la interfaz gráfica
		configurarFrame ( );
		if (guiConfig != null) 	   
		{
			crearMenu( guiConfig.getAsJsonArray("menuBar") );
		}

		tableConfig = openConfig ("Tablas BD", CONFIG_TABLAS);
		vacuandes = new Vacuandes(tableConfig);

		String path = guiConfig.get("bannerPath").getAsString();
		panelDatos = new PanelDatos ( );

		setLayout (new BorderLayout());
		add (new JLabel (new ImageIcon (path)), BorderLayout.NORTH );          
		add( panelDatos, BorderLayout.CENTER );  
		usuario = 0;
	}

	/* ****************************************************************
	 * 			Métodos de configuración de la interfaz
	 *****************************************************************/
	/**
	 * Lee datos de configuración para la aplicació, a partir de un archivo JSON o con valores por defecto si hay errores.
	 * @param tipo - El tipo de configuración deseada
	 * @param archConfig - Archivo Json que contiene la configuración
	 * @return Un objeto JSON con la configuración del tipo especificado
	 * 			NULL si hay un error en el archivo.
	 */
	private JsonObject openConfig (String tipo, String archConfig)
	{
		JsonObject config = null;
		try 
		{
			Gson gson = new Gson( );
			FileReader file = new FileReader (archConfig);
			JsonReader reader = new JsonReader ( file );
			config = gson.fromJson(reader, JsonObject.class);
			log.info ("Se encontró un archivo de configuración válido: " + tipo);
		} 
		catch (Exception e)
		{
			//			e.printStackTrace ();
			log.info ("NO se encontró un archivo de configuración válido");			
			JOptionPane.showMessageDialog(null, "No se encontró un archivo de configuración de interfaz válido: " + tipo, "Parranderos App", JOptionPane.ERROR_MESSAGE);
		}	
		return config;
	}

	/**
	 * Método para configurar el frame principal de la aplicación
	 */
	private void configurarFrame(  )
	{
		int alto = 0;
		int ancho = 0;
		String titulo = "";	

		if ( guiConfig == null )
		{
			log.info ( "Se aplica configuración por defecto" );			
			titulo = "Vacuandes APP Default";
			alto = 300;
			ancho = 500;
		}
		else
		{
			log.info ( "Se aplica configuración indicada en el archivo de configuración" );
			titulo = guiConfig.get("title").getAsString();
			alto= guiConfig.get("frameH").getAsInt();
			ancho = guiConfig.get("frameW").getAsInt();
		}

		setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
		setLocation (50,50);
		setResizable( true );
		setBackground( Color.WHITE );

		setTitle( titulo );
		setSize ( ancho, alto);        
	}

	/**
	 * Método para crear el menú de la aplicación con base em el objeto JSON leído
	 * Genera una barra de menú y los menús con sus respectivas opciones
	 * @param jsonMenu - Arreglo Json con los menùs deseados
	 */
	private void crearMenu(  JsonArray jsonMenu )
	{    	
		// Creación de la barra de menús
		menuBar = new JMenuBar();       
		for (JsonElement men : jsonMenu)
		{
			// Creación de cada uno de los menús
			JsonObject jom = men.getAsJsonObject(); 

			String menuTitle = jom.get("menuTitle").getAsString();        	
			JsonArray opciones = jom.getAsJsonArray("options");

			JMenu menu = new JMenu( menuTitle);

			for (JsonElement op : opciones)
			{       	
				// Creación de cada una de las opciones del menú
				JsonObject jo = op.getAsJsonObject(); 
				String lb =   jo.get("label").getAsString();
				String event = jo.get("event").getAsString();

				JMenuItem mItem = new JMenuItem( lb );
				mItem.addActionListener( this );
				mItem.setActionCommand(event);

				menu.add(mItem);
			}       
			menuBar.add( menu );
		}        
		setJMenuBar ( menuBar );	
	}

	/* ****************************************************************
	 * 			CRUD de OficinaRegionalEPS
	 *****************************************************************/
	/**
	 * Adiciona una oficina regional con la información dada por el usuario
	 * Se crea una nueva tupla de OficinaRegionalEPS en la base de datos, si una EPS con ese nombre y region no existia
	 */
	public void adicionarOficinaRegionalEPS( )
	{
		if(usuario ==0) {
			try 
			{
				String nombreEps = JOptionPane.showInputDialog (this, "Nombre de la EPS?", "Adicionar nombre Oficina regional EPS", JOptionPane.QUESTION_MESSAGE);
				int RegionEPS = Integer.parseInt(JOptionPane.showInputDialog (this, "Region de la EPS", "Adicionar numero region Oficina regional EPS", JOptionPane.QUESTION_MESSAGE));
				if (nombreEps != null)
				{
					VOOficinaRegionalEPS or = vacuandes.adicionarOficinaRegionalEPS(nombreEps, RegionEPS);
					if (or == null)
					{
						throw new Exception ("No se pudo crear la oficina " + nombreEps + "-" + RegionEPS);
					}
					String resultado = "En adicionarOficinaRegionalEPS\n\n";
					resultado += "Oficina regional EPS adicionado exitosamente: " + or;
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			} 
			catch (Exception e) 
			{
				//			e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador del plan de vacunación.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}

	/* ****************************************************************
	 * 			CRUD de Condicion
	 *****************************************************************/

	/**
	 * Adiciona una condicion con la información dada por el usuario
	 * Se crea una nueva tupla de Condicion en la base de datos
	 */
	public void adicionarCondicion( )
	{
		if (usuario == 0) {
			try 
			{
				long IdentificadorCondicion = Long.parseLong(JOptionPane.showInputDialog (this, "Numero de condicion?", "Adicionar condicion de priorizacion", JOptionPane.QUESTION_MESSAGE));
				String descripcion = JOptionPane.showInputDialog (this, "Descripcion de la condicion?", "Adicionar condicion de priorizacion", JOptionPane.QUESTION_MESSAGE);
				if (descripcion != null)
				{
					VOCondicion c = vacuandes.adicionarCondicion(IdentificadorCondicion,descripcion);
					if (c == null)
					{
						throw new Exception ("No se pudo crear la condicion " + descripcion);
					}
					String resultado = "En adicionarCondicion\n\n";
					resultado += "Condocion adicionado exitosamente: " + c;
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			} 
			catch (Exception e) 
			{
				e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador del plan de vacunación.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}

	/* ****************************************************************
	 * 			CRUD de EstadoVacunacion
	 *****************************************************************/

	/**
	 * Adiciona un estado de vacuancion con la información dada por el usuario
	 * Se crea una nueva tupla de EstadoVacunacion en la base de datos.
	 */
	public void adicionarEstadoVacunacion( )
	{

		if(usuario == 0) {
			try 

			{
				String descripcion = JOptionPane.showInputDialog (this, "Descripcion del estado de vacunación?", "Adicionar estado de vacunación", JOptionPane.QUESTION_MESSAGE);
				if (descripcion != null)
				{
					VOEstadoVacunacion e = vacuandes.adicionarEstadoVacunacion(descripcion);
					if (e == null)
					{
						throw new Exception ("No se pudo crear el estado de vacunacion " + descripcion);
					}
					String resultado = "En  adicionarEstadoVacunacion\n\n";
					resultado += "Estado adicionado exitosamente: " + e;
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			} 
			catch (Exception e) 
			{
				//e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador del plan de vacunación.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}

	public void actualizarEstadoVacunacion() {

		String[] tiposDocumento = { "CC", "TI", "PermisoEspecial" };

		String TipoDeIdentificacion =  (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de documento del ciudadano...",
				"Actualizar estado de vacunacion", JOptionPane.QUESTION_MESSAGE, null, 
				tiposDocumento, // Arreglo de opciones
				tiposDocumento[0]); // Valor inicial
		String IdentificacionCiudadano = JOptionPane.showInputDialog (this, "Número Identificación?", "Actualizar estado de vacunacion", JOptionPane.QUESTION_MESSAGE);

		VOEstadoVacunacion estado = (VOEstadoVacunacion) JOptionPane.showInputDialog(this, "Seleccione estado de vacunacion del ciudadano",
				"Actualizar estado de vacunacion", JOptionPane.QUESTION_MESSAGE, null, 
				vacuandes.darEstadosVacunacion().toArray(), // Arreglo de opciones
				""); // Valor inicial

		int reply1 = JOptionPane.showConfirmDialog(this, "Se aplicó vacuna?", "Actualizar estado de vacunacion", JOptionPane.YES_NO_OPTION);		
		try 
		{
			long r = vacuandes.cambiarEstadoVacunacion(TipoDeIdentificacion, IdentificacionCiudadano, estado.getIdentificadorEstado());

			if(reply1 == 0) {
				vacuandes.aplicarVacuna(TipoDeIdentificacion,IdentificacionCiudadano);
			}
			
			if(r == -1) {
				throw new Exception ("No se pudo actuaizar el estado ");
			}
			String resultado = "En actualizarEstadoVacunacion\n\n";
			resultado += "Estado actualizada exitosamente: " + " con " +r + " dato actualizado\n";
			if(reply1 == 0) resultado += "Se actualizó en el inventario "+ reply1 + " vacuna\n";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
		}
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}


	/* ****************************************************************
	 * 			CRUD de PuntoVacunacion
	 *****************************************************************/

	/**
	 * Adiciona un punto de vacunacion con la información dada por el usuario
	 * Se crea una nueva tupla de PuntoVacunacion en la base de datos.
	 */
	public void adicionarPuntoVacunacion( )
	{
		if(usuario == 1) {
			try 
			{
				String DireccionPuntoVacunacion = JOptionPane.showInputDialog (this, "Dirección del punto de vacunación?", "Adicionar punto de vacunación", JOptionPane.QUESTION_MESSAGE);
				String NombrePuntoVacunacion = JOptionPane.showInputDialog (this, "Nombre del punto de vacunación?", "Adicionar punto de vacunación", JOptionPane.QUESTION_MESSAGE);
				int CapacidadSimultanea = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad simultanea del punto de vacunación?", "Adicionar punto de vacunación", JOptionPane.QUESTION_MESSAGE));
				int CapacidadDiaria = Integer.parseInt(JOptionPane.showInputDialog (this, "Capacidad diaria del punto de vacunación?", "Adicionar punto de vacunación", JOptionPane.QUESTION_MESSAGE));
				int DisponibilidadDeDosis = Integer.parseInt(JOptionPane.showInputDialog (this, "Disponibilidad de dosis del punto de vacunación?", "Adicionar punto de vacunación", JOptionPane.QUESTION_MESSAGE));
				VOOficinaRegionalEPS oficina =  (VOOficinaRegionalEPS) JOptionPane.showInputDialog(this, "Seleccione la oficina regional encargada del punto...",
						"Adicionar punto de vacunación", JOptionPane.QUESTION_MESSAGE, null, 
						vacuandes.darOficinasRegionalesEPS().toArray(), // Arreglo de opciones
						""); // Valor inicial
				int reply = JOptionPane.showConfirmDialog(this, "Exclusivo para el sector salud?", "Adicionar punto", JOptionPane.YES_NO_OPTION);
				String soloMayores = reply+"";

				int reply2 = JOptionPane.showConfirmDialog(this, "Exclusivo para adultos mayores?", "Adicionar punto", JOptionPane.YES_NO_OPTION);
				String soloSalud = reply2+"";

				if (DireccionPuntoVacunacion != null)
				{
					VOPuntoVacunacion e = vacuandes.adicionarPuntoVacunacion(DireccionPuntoVacunacion, NombrePuntoVacunacion, CapacidadSimultanea, CapacidadDiaria, DisponibilidadDeDosis, oficina.getNombreEPS(), oficina.getRegionEPS(), soloMayores, soloSalud);
					if (e == null)
					{
						throw new Exception ("No se pudo crear el punto de vacunacion " + DireccionPuntoVacunacion);
					}
					String resultado = "En adicionarPuntoVacunacion\n\n";
					resultado += "Punto adicionado exitosamente: " + e;
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			} 
			catch (Exception e) 
			{
				//e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador de EPS regional.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}

	public void asignarTieneInfraestructura( )
	{
		try 
		{
			String sidInfra= JOptionPane.showInputDialog (this, "Identificacion de la infraestructura?", "Asignar infraestructura", JOptionPane.QUESTION_MESSAGE);
			long idInfra = Integer.parseInt(sidInfra);
			String direccion = JOptionPane.showInputDialog (this, "Direccion del punto de vacunacion?", "Asignar infraestructura", JOptionPane.QUESTION_MESSAGE);

			if (direccion != null)
			{
				long r = vacuandes.agregarTieneInfraestructura(idInfra, direccion);
				if (r == -1)
				{
					throw new Exception ("No se pudo asignar la infraestructura");
				}
				String resultado = "En asignarTieneInfraestructura\n\n";
				resultado += "Infraestructura asignado exitosamente a " + idInfra;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		}
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void deshabilitarPunto() {
		if(usuario == 1) {
			try {
				VOPuntoVacunacion punto = (VOPuntoVacunacion) JOptionPane.showInputDialog(this, "Seleccione el punto de vacunacion que desea deshabilitar...",
						"Deshabilitar punto", JOptionPane.QUESTION_MESSAGE, null, 
						vacuandes.darPuntosVacunacion().toArray(), // Arreglo de opciones
						""); // Valor inicial
				if (punto != null) {
					long r = vacuandes.deshabilitarPunto(punto.getDireccionPuntoVacunacion());
					if (r == -1)
					{
						throw new Exception ("No se pudo deshabilitar el punto");
					}
					String resultado = "En deshabilitarPuntoDeVacunacion\n\n";
					resultado += "Se han retirado " + r + " ciudadanos del punto y se ha deshabilitado.\n"
							+ "Porfavor reasigne nuevamente las citas cuando el punto esté habilitado";
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				} else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}

			} catch (Exception e) 
			{
				//e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador de puntos.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}

	public void habilitarPunto() {
		if(usuario == 1) {
			try {
				VOPuntoVacunacion punto = (VOPuntoVacunacion) JOptionPane.showInputDialog(this, "Seleccione el punto de vacunacion que desea habilitar...",
						"Habilitar punto", JOptionPane.QUESTION_MESSAGE, null, 
						vacuandes.darPuntosVacunacionDeshabilitados().toArray(), // Arreglo de opciones
						""); // Valor inicial
				if (punto != null) {
					long r = vacuandes.habilitarPunto(punto.getDireccionPuntoVacunacion());
					if (r == -1)
					{
						throw new Exception ("No se pudo asignar la infraestructura");
					}
					String resultado = "En HabilitarPuntoDeVacunacion\n\n";
					resultado += "Se ha habilitado " +r + " punto de vacunación.\n"
							+ "Ya puede volver a asignar ciudadanos a este punto";
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				} else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}

			} catch (Exception e) 
			{
				//e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador de puntos.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}

	/* ****************************************************************
	 * 			CRUD de Cita
	 *****************************************************************/

	/**
	 * Convierte Sting a Timestamp
	 * Codigo recuperado de: https://stackoverflow.com/questions/18915075/java-convert-string-to-timestamp
	 */
	static Timestamp convertStringToTimestamp(String strDate) {
		final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm");
		return Optional.ofNullable(strDate) //
				.map(str -> LocalDateTime.parse(str, formatter))
				.map(Timestamp::valueOf) //
				.orElse(null);
	}

	/**
	 * Adiciona una Cita con la información dada por el usuario
	 * Se crea una nueva tupla de Cita en la base de datos.
	 */
	public void adicionarCita( )
	{
		if(usuario == 2) {
			String[] tiposDocumento = { "CC", "TI", "PermisoEspecial" };
			try 
			{
				String sFechaCita = JOptionPane.showInputDialog (this, "Fecha de la cita en formato yyyyMMdd HH:mm?", "Adicionar cita", JOptionPane.QUESTION_MESSAGE);
				Timestamp FechaCita = convertStringToTimestamp(sFechaCita);
				String TipoIdCiudadano =  (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de documento del ciudadano...",
						"Adicionar cita", JOptionPane.QUESTION_MESSAGE, null, 
						tiposDocumento, // Arreglo de opciones
						tiposDocumento[0]); // Valor inicial
				String IdCiudadano = JOptionPane.showInputDialog (this, "Identificacion del ciudadano?", "Adicionar cita", JOptionPane.QUESTION_MESSAGE);

				if (FechaCita != null)
				{
					VOCita e = vacuandes.adicionarCita(FechaCita, TipoIdCiudadano, IdCiudadano);
					if (e == null)
					{
						throw new Exception ("No se pudo crear la cita " + FechaCita);
					}
					String resultado = "En adicionarCita\n\n";
					resultado += "Cita adicionada exitosamente: " + e;
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			} 
			catch (Exception e) 
			{
				//e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador de puntos de atención.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}

	}

	/* ****************************************************************
	 * 			CRUD de Etapa
	 *****************************************************************/

	/**
	 * Adiciona una Etapa con la información dada por el usuario
	 * Se crea una nueva tupla de Etapa en la base de datos.
	 */
	public void adicionarEtapa( )
	{
		try 
		{
			String NumeroDeEtapaS = JOptionPane.showInputDialog (this, "Numero de etapa?", "Adicionar etapa", JOptionPane.QUESTION_MESSAGE);
			String DescripcionEtapa =JOptionPane.showInputDialog (this, "Descripcion de etapa?", "Adicionar etapa", JOptionPane.QUESTION_MESSAGE);

			if (NumeroDeEtapaS != null)
			{
				long NumeroDeEtapa = Long.parseLong(NumeroDeEtapaS);
				VOEtapa e = vacuandes.adicionarEtapa(NumeroDeEtapa, DescripcionEtapa);
				if (e == null)
				{
					throw new Exception ("No se pudo crear la etapa " + NumeroDeEtapa);
				}
				String resultado = "En adicionarEtapa\n\n";
				resultado += "Etapa adicionada exitosamente: " + e;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/* ****************************************************************
	 * 			CRUD de Ciudadano
	 *****************************************************************/

	/**
	 * Adiciona un ciudadano con la información dada por el usuario
	 * Se crea una nueva tupla de Ciudadano en la base de datos.
	 */
	public void adicionarCiudadano( )
	{
		if(usuario == 0) {
			String[] tiposDocumento = { "CC", "TI", "PermisoEspecial" };
			String[] generos = { "Masculino", "Femenino", "Otro" };
			String[] roles = { "Ciudadano", "Administrador eps regional","Administrador punto de atención", "Administrador puntos vacunación", "Personal de salud" };
			try 
			{
				String TipoDeIdentificacion =  (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de documento del ciudadano...",
						"Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE, null, 
						tiposDocumento, // Arreglo de opciones
						tiposDocumento[0]); // Valor inicial
				String IdentificacionCiudadano = JOptionPane.showInputDialog (this, "Número Identificación?", "Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE);
				String NombreCiudadano = JOptionPane.showInputDialog (this, "Nombre?", "Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE);
				String ApellidoCiudadano = JOptionPane.showInputDialog (this, "Apellido?", "Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE);

				int reply = JOptionPane.showConfirmDialog(this, "Tiene alguna restricción para no vacunarse?", "Adicionar ciudadano", JOptionPane.YES_NO_OPTION);
				String EsVacunable = reply+"";

				String sFechaDeNacimiento = (JOptionPane.showInputDialog (this, "Fecha de nacimiento en formato yyyyMMdd?", "Adicionar cita", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
				Timestamp FechaDeNacimiento = convertStringToTimestamp(sFechaDeNacimiento);

				long TelefonoDeContacto = Long.parseLong(JOptionPane.showInputDialog (this, "Telefono contacto?", "Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE));

				VOEstadoVacunacion estado = (VOEstadoVacunacion) JOptionPane.showInputDialog(this, "Seleccione estado de vacunacion del ciudadano",
						"Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE, null, 
						vacuandes.darEstadosVacunacion().toArray(), // Arreglo de opciones
						""); // Valor inicial

				VOCondicion condicion = (VOCondicion) JOptionPane.showInputDialog(this, "Seleccione una condición del ciudadano.",
						"Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE, null, 
						vacuandes.darCondiciones().toArray(), // Arreglo de opciones
						""); // Valor inicial


				VOOficinaRegionalEPS oficina = (VOOficinaRegionalEPS) JOptionPane.showInputDialog(this, "Seleccione la EPS del ciudadano",
						"Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE, null, 
						vacuandes.darOficinasRegionalesEPS().toArray(), // Arreglo de opciones
						""); // Valor inicial
				String genero =  (String) JOptionPane.showInputDialog(this, "Seleccione el género del ciudadano...",
						"Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE, null, 
						generos, // Arreglo de opciones
						generos[0]);
				
				String rol =  (String) JOptionPane.showInputDialog(this, "Seleccione el rol del ciudadano...",
						"Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE, null, 
						roles, // Arreglo de opciones
						roles[0]);
				
				String ciudad = JOptionPane.showInputDialog (this, "Ciudad?", "Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE);
				
				String localidad = JOptionPane.showInputDialog (this, "Localidad?", "Adicionar ciudadano", JOptionPane.QUESTION_MESSAGE);

				if (TipoDeIdentificacion != null)
				{
					VOCiudadano e = vacuandes.adicionarCiudadano(TipoDeIdentificacion, IdentificacionCiudadano, NombreCiudadano, ApellidoCiudadano, EsVacunable, FechaDeNacimiento, TelefonoDeContacto, estado.getIdentificadorEstado(), condicion.getIdentificadorCondicion(), oficina.getNombreEPS(), oficina.getRegionEPS(), genero , rol , ciudad ,localidad);
					long r = vacuandes.agregarCondicionCiudadano(TipoDeIdentificacion, IdentificacionCiudadano, condicion.getIdentificadorCondicion(), condicion.getDescripcionCondicion());
					if (e == null || r == -1)
					{
						throw new Exception ("No se pudo crear el ciudadano " + IdentificacionCiudadano);
					}
					String resultado = "En adicionarCiudadano\n\n";
					resultado += "Ciudadano adicionado exitosamente: " + e;
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			} 
			catch (Exception e) 
			{
				//e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador del plan de vacunación.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}


	public void asignarPuntoVacunacion( )
	{
		if(usuario == 1) {
			String[] tiposDocumento = { "CC", "TI", "PermisoEspecial" };
			try 
			{
				String TipoDeIdentificacion =  (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de documento del ciudadano...",
						"Adicionar cita", JOptionPane.QUESTION_MESSAGE, null, 
						tiposDocumento, // Arreglo de opciones
						tiposDocumento[0]); // Valor inicial
				String IdentificacionCiudadano = JOptionPane.showInputDialog (this, "Identificacion del ciudadano?", "Adicionar cita", JOptionPane.QUESTION_MESSAGE);

				VOPuntoVacunacion punto = (VOPuntoVacunacion) JOptionPane.showInputDialog(this, "Seleccione el punto de vacunacuion de la cita...",
						"Adicionar cita", JOptionPane.QUESTION_MESSAGE, null, 
						vacuandes.darPuntosVacunacion().toArray(), // Arreglo de opciones
						""); // Valor inicial

				if (TipoDeIdentificacion != null)
				{
					long e = vacuandes.asignarPuntoVacunacion(TipoDeIdentificacion, IdentificacionCiudadano, punto.getDireccionPuntoVacunacion());
					if (e == -1)
					{
						throw new Exception ("No se pudo asignar el punto " + punto.getDireccionPuntoVacunacion());
					}
					String resultado = "En asignarPuntoVacunacion\n\n";
					resultado += "Punto asignado exitosamente a " + TipoDeIdentificacion+IdentificacionCiudadano;
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);
				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			} 
			catch (Exception e) 
			{
				//e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador de EPS regional.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}

	/* ****************************************************************
	 * 			CRUD de PerteneceA
	 *****************************************************************/

	/**
	 * Adiciona una Condicion a un ciudadano con la información dada por el usuario
	 * Se crea una nueva tupla de PerteneceA en la base de datos.
	 */
	public void adicionarCondicionCiudadano( )
	{
		if(usuario == 0) {
			String[] tiposDocumento = { "CC", "TI", "PermisoEspecial" };
			try 
			{
				String TipoIdCiudadano =  (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de documento del ciudadano...",
						"Adicionar condicion", JOptionPane.QUESTION_MESSAGE, null, 
						tiposDocumento, // Arreglo de opciones
						tiposDocumento[0]); // Valor inicial
				String IdCiudadano = JOptionPane.showInputDialog (this, "Identificacion del ciudadano?", "Adicionar condicion", JOptionPane.QUESTION_MESSAGE);

				VOCondicion condicion = (VOCondicion) JOptionPane.showInputDialog(this, "Seleccione la condicion del ciudadano",
						"Adicionar condicion", JOptionPane.QUESTION_MESSAGE, null, 
						vacuandes.darCondiciones().toArray(), // Arreglo de opciones
						""); // Valor inicial

				VOCiudadano ciudadano = vacuandes.darCiudadano(TipoIdCiudadano, IdCiudadano);

				if (TipoIdCiudadano != null)
				{

					VOPerteneceA e = vacuandes.adicionarCondicionCiudadano(TipoIdCiudadano, IdCiudadano, condicion.getIdentificadorCondicion(), condicion.getDescripcionCondicion());
					if(ciudadano.getEtapa() > condicion.getIdentificadorCondicion()) {
						actualizarEtapaCiudadano(TipoIdCiudadano, IdCiudadano, condicion.getIdentificadorCondicion());
					}

					if (e == null )
					{
						throw new Exception ("No se pudo agregar la condicion " + condicion.getDescripcionCondicion());
					}
					String resultado = "En adicionarCondicionCiudadano\n\n";
					resultado += "Condicion adicionada exitosamente: " + e;
					resultado += "\n Operación terminada";
					panelDatos.actualizarInterfaz(resultado);

				}
				else
				{
					panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
				}
			} 
			catch (Exception e) 
			{
				//e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador del plan de vacunación.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}

	}

	private void actualizarEtapaCiudadano(String tipoIdCiudadano, String idCiudadano, long condicion) {
		try 
		{
			long r = vacuandes.actualizarEtapa(tipoIdCiudadano,idCiudadano, condicion);

			if(r == 0) {
				throw new Exception ("No se pudo agregar la condicion " + condicion);
			}
			String resultado = "En actualizarEtapaCiudadano\n\n";
			resultado += "Etapa actualizada exitosamente: " + " con " +r + " dato actualizado";
			resultado += "\n Operación terminada";
			panelDatos.actualizarInterfaz(resultado);
		}
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/* ****************************************************************
	 * 			CRUD de TalentoHumano
	 *****************************************************************/


	/**
	 * Adiciona un TalentoHumano con la información dada por el usuario
	 * Se crea una nueva tupla de TalentoHumano en la base de datos.
	 */
	public void adicionarTalentoHumano( )
	{
		String[] tiposDocumento = { "CC", "TI", "PermisoEspecial" };
		String[] funciones = { "Doctor", "Enfermero" };
		try 
		{
			String TipoIdCiudadano =  (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de documento del talento humano...",
					"Adicionar talento humano", JOptionPane.QUESTION_MESSAGE, null, 
					tiposDocumento, // Arreglo de opciones
					tiposDocumento[0]); // Valor inicial
			String IdCiudadano = JOptionPane.showInputDialog (this, "Identificacion del talento humano?", "Adicionar talento humano", JOptionPane.QUESTION_MESSAGE);

			String FuncionTalentoHumano =  (String) JOptionPane.showInputDialog(this, "Seleccione la funcion del talento humano...",
					"Adicionar talento humano", JOptionPane.QUESTION_MESSAGE, null, 
					funciones, // Arreglo de opciones
					funciones[0]); // Valor inicial

			if (TipoIdCiudadano != null)
			{
				VOTalentoHumano e = vacuandes.adicionarTalentoHumano(TipoIdCiudadano, IdCiudadano, FuncionTalentoHumano);
				if (e == null)
				{
					throw new Exception ("No se pudo agregar el talento " + IdCiudadano + FuncionTalentoHumano);
				}
				String resultado = "En adicionarTalentoHumano\n\n";
				resultado += "Talento humano adicionado exitosamente: " + e;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}


	public void adicionarVacuna() {
		String[] tiposDocumento = { "CC", "TI", "PermisoEspecial" };

		try 
		{

			int reply = JOptionPane.showConfirmDialog(this, "Ha sido aplicada?", "Agregando vacuna", JOptionPane.YES_NO_OPTION);
			String haSidoAplicada = reply+"";
			VOLoteVacuna idLote =  (VOLoteVacuna) JOptionPane.showInputDialog(this, "Seleccione el lote vacuna al que pertenerce","Adicionar Vacuna", JOptionPane.QUESTION_MESSAGE, null, vacuandes.darLotes().toArray(),"" );

			String direccionPunto = JOptionPane.showInputDialog (this, "Ingrese la direccion del punto de vacunacion", "Adicionar vacuna", JOptionPane.QUESTION_MESSAGE);
			String TipoIdCiudadano =  (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de documento del talento humano...",
					"Adicionar vacuna", JOptionPane.QUESTION_MESSAGE, null, 
					tiposDocumento, // Arreglo de opciones
					tiposDocumento[0]); // Valor inicial
			String IdCiudadano = JOptionPane.showInputDialog (this, "Identificacion del ciudadano", "Adicionar vacuna", JOptionPane.QUESTION_MESSAGE);
			String tecnologia = JOptionPane.showInputDialog (this, "Tecnlología vacuna", "Adicionar vacuna", JOptionPane.QUESTION_MESSAGE);

			if (TipoIdCiudadano != null)
			{
				VOVacuna v = vacuandes.adicionarVacuna(haSidoAplicada, idLote.getIdentificadorLote(), direccionPunto, TipoIdCiudadano, IdCiudadano, tecnologia);



				if (v == null )
				{
					throw new Exception ("No se pudo crear la vacuna ");
				}
				String resultado = "En adicionarVacuna\n\n";
				resultado += "Vacuna adicionada exitosamente: " + v;
				resultado += "Si desea adicionarle una segunda dosis, inserte otra vacuna y asocie su id con el de esta";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}

	}
	
	public void asociarSegundaDosis() {
		try 
		{
			String idVacunaPrincipal= JOptionPane.showInputDialog (this, "Identificacion de la vacuna principal?", "Asignar segunda dosis", JOptionPane.QUESTION_MESSAGE);
			String idVacunaSegundaDosis = JOptionPane.showInputDialog (this, "Identificacion de la segunda dosis?", "Asignar segunda dosis", JOptionPane.QUESTION_MESSAGE);
			
			if (idVacunaPrincipal != null && idVacunaSegundaDosis !=null)
			{
				long v = vacuandes.asociarSegundaDosis(Long.parseLong(idVacunaPrincipal), Long.parseLong(idVacunaSegundaDosis));

				if (v == -1 )
				{
					throw new Exception ("No se pudo asociar la segunda dosis");
				}
				String resultado = "En asociarSegundaDosis\n\n";
				resultado += v + " vacuna adicionada exitosamente" ;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}


	public void asignarCondicionPreservacion( )
	{
		try 
		{
			String sidVacuna= JOptionPane.showInputDialog (this, "Identificacion de la vacuna?", "Asignar condicion", JOptionPane.QUESTION_MESSAGE);
			long idVacuna = Integer.parseInt(sidVacuna);

			VOCondicionPreservacion cond =   (VOCondicionPreservacion) JOptionPane.showInputDialog(this, "Seleccione la condicion de preservacion de la vacuna",
					"Adicionar vacuna", JOptionPane.QUESTION_MESSAGE, null, 
					vacuandes.darCondPreservacion().toArray(), // Arreglo de opciones
					""); 
			if (cond == null)
			{
				long r = vacuandes.agregarCondAVacuna(cond.getIdentificadorCondPreservacion(), idVacuna);
				if (r == -1)
				{
					throw new Exception ("No se pudo asignar la condicion");
				}
				String resultado = "En asignarPuntoVacunacion\n\n";
				resultado += "Condicion asignado exitosamente a " + idVacuna;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		}
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}	

	public void adicionarLoteVacuna() {


		try 
		{

			String nombreO = JOptionPane.showInputDialog (this, "Ingrese el nombre de la EPS", "Adicionar loteVacuna", JOptionPane.QUESTION_MESSAGE);
			String sregionO = JOptionPane.showInputDialog (this, "Ingrese la region de la  EPS", "Adicionar loteVacuna", JOptionPane.QUESTION_MESSAGE);
			int regionO = Integer.parseInt(sregionO);


			if (nombreO != null)
			{
				VOLoteVacuna l = vacuandes.adicionarLoteVacuna(nombreO, regionO);
				if (l == null)
				{
					throw new Exception ("No se pudo crear el lote ");
				}
				String resultado = "En adicionarLote\n\n";
				resultado += "Lote adicionada exitosamente: " + l;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}

	}

	public void adicionarCondicionPreservacion( )
	{
		try 
		{
			String descripcion = JOptionPane.showInputDialog (this, "Descripcion de la condicion?", "Adicionar condicionPreservacion", JOptionPane.QUESTION_MESSAGE);


			if (descripcion != null)
			{
				VOCondicionPreservacion c = vacuandes.adicionarCondicionPreservacion(descripcion);
				if (c == null)
				{
					throw new Exception ("No se pudo crear la infraestructura ");
				}
				String resultado = "En adicionarCondicionPreservacion\n\n";
				resultado += "CondicionPreservacion adicionada exitosamente: " + c;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}






	public void adicionarInfraestructura( )
	{
		try 
		{
			String descripcion = JOptionPane.showInputDialog (this, "Descripcion de la infraestructura?", "Adicionar infraestructura", JOptionPane.QUESTION_MESSAGE);


			if (descripcion != null)
			{
				VOInfraestructura e = vacuandes.adicionarInfraestructura(descripcion);
				if (e == null)
				{
					throw new Exception ("No se pudo crear la infraestructura ");
				}
				String resultado = "En adicionarInfraestructura\n\n";
				resultado += "Infraestructura adicionada exitosamente: " + e;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}
	public void adicionarPuntoVacunacionVirtual( )
	{
		try 
		{
			String direccion = JOptionPane.showInputDialog (this, "Direccion de punto de vacunacion?", "Adicionar punto vacunacion virtual", JOptionPane.QUESTION_MESSAGE);
			String nombreEPS = JOptionPane.showInputDialog (this, "Nombre de la EPS?", "Adicionar punto vacunacion virtual", JOptionPane.QUESTION_MESSAGE);
			int regionEPS = Integer.parseInt(JOptionPane.showInputDialog (this, "Region de la EPS?", "Adicionar punto vacunacion virtual", JOptionPane.QUESTION_MESSAGE));
			long idVacuna = Long.parseLong( JOptionPane.showInputDialog (this, "Identificacion de vacuna asignada?", "Adicionar punto vacunacion virtual", JOptionPane.QUESTION_MESSAGE));

			if (direccion != null)
			{
				VOPuntoVacunacionVirtual e = vacuandes.adicionarPuntoVacunacionVirtual(direccion, nombreEPS, regionEPS, idVacuna);
				if (e == null)
				{
					throw new Exception ("No se pudo crear el punto de vacunacion ");
				}
				String resultado = "En adicionarpuntoDeVacunacion\n\n";
				resultado += "punto de vacunacion adicionada exitosamente: " + e;
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}


	public void asignarTalentoHumanoAPunto()
	{
		String[] tiposDocumento = { "CC", "TI", "PermisoEspecial" };
		try 
		{
			String TipoDeIdentificacion =  (String) JOptionPane.showInputDialog(this, "Seleccione el tipo de documento del ciudadano...",
					"Asignando talento humano a punto de vacunacion", JOptionPane.QUESTION_MESSAGE, null, 
					tiposDocumento, // Arreglo de opciones
					tiposDocumento[0]); // Valor inicial
			String identificacion = JOptionPane.showInputDialog (this, "Ingresar identificacion del ciudadano?", "Asignando talento humano a punto de vacunacion", JOptionPane.QUESTION_MESSAGE);
			VOPuntoVacunacion punto =  (VOPuntoVacunacion) JOptionPane.showInputDialog(this, "Seleccione punto vacunacion",
					"Asignando talento humano a punto de vacunacion", JOptionPane.QUESTION_MESSAGE, null, 
					vacuandes.darPuntosVacunacion().toArray(), // Arreglo de opciones
					""); // Valor inicial


			if (punto != null)
			{
				long e = vacuandes.agregarTalentoHumanoPunto(TipoDeIdentificacion, identificacion, punto.getDireccionPuntoVacunacion());
				if (e == -1)
				{
					throw new Exception ("No se pudo crear el punto de vacunacion ");
				}
				String resultado = "En asignarTalentoHumanoAPunto\n\n";
				resultado += "Talento Humano asignado exitosamente: ";
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}



	/* ****************************************************************
	 * 			Métodos de Consulta
	 *****************************************************************/

	public void consultarCohortesCiudadanos() {
		if(usuario == 0) {
			try {
				String[] consultasI = { "Edad puntual", "Rango de edad", "Género", "Condición", "Ciudad", "Localidad", "EPS", "Punto de vacunación", "Número de dosis aplicadas", "Tecnología"};
				List<String> consultas = new ArrayList<String>();
				Collections.addAll(consultas, consultasI);
				String consulta =  (String) JOptionPane.showInputDialog(this, "Seleccione la condición que desee consultar.",
						"Consultando cohortes", JOptionPane.QUESTION_MESSAGE, null, 
						consultas.toArray(), // Arreglo de opciones
						consultas.toArray());
				consultas.remove(consulta);

				int continuar = JOptionPane.showConfirmDialog(this, "Desea consultar para otra condicion?", "Consultar cohortes", JOptionPane.YES_NO_OPTION);
				while(continuar == 0 && !consultas.isEmpty()) {
					String consultaS =  (String) JOptionPane.showInputDialog(this, "Seleccione la condición que desee consultar.",
							"Consultando cohortes", JOptionPane.QUESTION_MESSAGE, null, 
							consultas.toArray(), // Arreglo de opciones
							consultas.toArray());
					consulta += ","+consultaS;
					consultas.remove(consultaS);
					continuar = JOptionPane.showConfirmDialog(this, "Desea consultar para otra condicion?", "Consultar cohortes", JOptionPane.YES_NO_OPTION);
				}
				
				String unificada = "";
				for (String c : consulta.split(",")) {
					if(c != null) {
						if(c.equals("Edad puntual")) {
							String edad = JOptionPane.showInputDialog (this, "Ingresar edad a consultar", "Consultar cohortes", JOptionPane.QUESTION_MESSAGE);
							unificada += " AND (SELECT TRUNC(TO_NUMBER(SYSDATE - c.fechadenacimiento) / 365.25) FROM DUAL) = " + edad;
						}
						else if(c.equals("Rango de edad")) {
							String rangoInf = JOptionPane.showInputDialog (this, "Ingresar edad inferior", "Consultar cohortes", JOptionPane.QUESTION_MESSAGE);
							String rangoSup = JOptionPane.showInputDialog (this, "Ingresar edad a consultar", "Consultar cohortes", JOptionPane.QUESTION_MESSAGE);
							unificada += " AND (SELECT TRUNC(TO_NUMBER(SYSDATE - c.fechadenacimiento) / 365.25) FROM DUAL) BETWEEN "+rangoInf+ " AND " + rangoSup;
						}
						else if(c.equals("Género")) {
							String[] generos = { "Masculino", "Femenino", "Otro"};
							String genero =  (String) JOptionPane.showInputDialog(this, "Seleccione la condición que desee consultar.",
									"Consultando cohortes", JOptionPane.QUESTION_MESSAGE, null, 
									generos, // Arreglo de opciones
									generos[0]);
							unificada += " AND c.genero = " + "'" +genero + "'";
						} else if(c.equals("Condición")) {
							VOCondicion condicion = (VOCondicion) JOptionPane.showInputDialog(this, "Seleccione la condicion del ciudadano",
									"Adicionar condicion", JOptionPane.QUESTION_MESSAGE, null, 
									vacuandes.darCondiciones().toArray(), // Arreglo de opciones
									""); // Valor inicial
							unificada += " AND pe.descripcioncondicion = " + "'"+ condicion.getDescripcionCondicion() + "'";
						} else if(c.equals("Ciudad")) {
							String ciudad = JOptionPane.showInputDialog (this, "Ingresar ciudad a consultar", "Consultar cohortes", JOptionPane.QUESTION_MESSAGE);
							unificada += " AND c.ciudad = " + "'" + ciudad + "'";
						} else if(c.equals("Localidad")) {
							String localidad = JOptionPane.showInputDialog (this, "Ingresar localidad a consultar", "Consultar cohortes", JOptionPane.QUESTION_MESSAGE);
							unificada += " AND c.localidad = " +"'" + localidad + "'";
						} else if(c.equals("EPS")) {
							VOOficinaRegionalEPS oficina = (VOOficinaRegionalEPS) JOptionPane.showInputDialog(this, "Seleccione EPS a consultar",
									"Consultar cohortes", JOptionPane.QUESTION_MESSAGE, null, 
									vacuandes.darOficinasRegionalesEPS().toArray(), // Arreglo de opciones
									""); // Valor inicial
							unificada += " AND o.nombreeps = '"+oficina.getNombreEPS()+"' AND\n" + 
									"o.regioneps = " +oficina.getRegionEPS();
						} else if(c.equals("Punto de vacunación")) {
							VOPuntoVacunacion punto =  (VOPuntoVacunacion) JOptionPane.showInputDialog(this, "Seleccione punto a consultar",
									"Asignando talento humano a punto de vacunacion", JOptionPane.QUESTION_MESSAGE, null, 
									vacuandes.darPuntosVacunacion().toArray(), // Arreglo de opciones
									""); // Valor inicial
							unificada += " AND pu.direccionpuntovacunacion = '"+ punto.getDireccionPuntoVacunacion() +"'";
						} else if(c.equals("Número de dosis aplicadas")) {
							String dosis = JOptionPane.showInputDialog (this, "Ingresar número de dosis a consultar", "Consultar cohortes", JOptionPane.QUESTION_MESSAGE);
							unificada += " AND (c.tipodeidentificacion, c.identificacionciudadano) IN\n" + 
									"      (SELECT c.tipodeidentificacion, c.identificacionciudadano\n" + 
									"        FROM ciudadano c, vacuna v\n" + 
									"        WHERE c.identificacionciudadano = v.idciudadano\n" + 
									"        GROUP BY c.tipodeidentificacion, c.identificacionciudadano\n" + 
									"        HAVING COUNT(*) = "+dosis+")";
						} else if(c.equals("Tecnología")) {
							String tecnologia = JOptionPane.showInputDialog (this, "Ingresar tacnología de vacunas a consultar", "Consultar cohortes", JOptionPane.QUESTION_MESSAGE);
							unificada += " AND v.tecnologia = '" + tecnologia + "'";
						}
					}
				}

				List<Ciudadano> e = vacuandes.consultarCohortesCiudadanos(unificada);

				if (e == null)
				{
					throw new Exception ("No se pudo conusltar los cohortes");
				}
				String resultado = "En consultarCohortesCiudadanos\n\n";
				resultado += "Los ciudadanos que cumplen las condiciones son " +e.size() + " y esta es su información:\n\n";
				for (Ciudadano ciudadano : e) {
					resultado += ciudadano.toString() + "\n";

				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);


			}catch (Exception e) 
			{
				e.printStackTrace();
				String resultado = generarMensajeError(e);
				panelDatos.actualizarInterfaz(resultado);
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador del plan de vacunación o el administrador regional.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}


	public void mostrarIndiceVacunacion() {
		if(usuario == 0 || usuario == 1) {
			String[] ops  = { "Una o varias regiones", "Una o varias EPS", "Por etapa del proceso", "Por grupo de priorización"};

			String opcion =  (String) JOptionPane.showInputDialog(this, "Seleccione lo que desee consultar",
					"Seleccionar opción", JOptionPane.QUESTION_MESSAGE, null, 
					ops, // Arreglo de opciones
					ops[0]); // Valor inicial
			if(opcion !=null) {
				if(opcion.equals("Una o varias regiones"))
					mostrarIndiceVacunacionRegion();
				if(opcion.equals("Por etapa del proceso"))
					mostrarIndiceVacunacionEstado();
				if(opcion.equals("Por grupo de priorización"))
					mostrarIndiceVacunacionEtapa();
				if(opcion.equals("Una o varias EPS"))
					mostrarIndiceVacunacionEPS();
			} else {
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} else {
			panelDatos.actualizarInterfaz("ERROR\n"
					+ "Esto solo lo puede realizar el administrador del plan de vacunación o el administrador regional.\n"
					+ "Cambie de usuario si desea realizar esta acción");
		}
	}

	public void mostrarIndiceVacunacionRegion() {
		try 
		{
			long regionEPS = Long.parseLong(JOptionPane.showInputDialog (this, "Region de la EPS a consultar?", "Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE));

			int reply = JOptionPane.showConfirmDialog(this, "Desea consultar un rango de fechas?", "Consultar indice vacunación", JOptionPane.YES_NO_OPTION);
			Timestamp fechaInferior = convertStringToTimestamp("20200101 00:00");

			if(reply == 0) {
				String fechaI = (JOptionPane.showInputDialog (this, "Fecha inferior en formato yyyyMMdd?", "Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
				fechaInferior = convertStringToTimestamp(fechaI);
			}
			String fechaS = (JOptionPane.showInputDialog (this, "Fecha superior en formato yyyyMMdd?", "Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
			Timestamp fechaSuperior = convertStringToTimestamp(fechaS);

			if (regionEPS != -1)
			{
				long totalVacunados = vacuandes.darTotalVacunadosRegion(regionEPS, fechaInferior, fechaSuperior);
				long totalHabilitados = vacuandes.darTotalHabilitadosRegion(regionEPS, fechaInferior, fechaSuperior);
				int continuar = JOptionPane.showConfirmDialog(this, "Desea consultar para otra región?", "Consultar indice vacunación", JOptionPane.YES_NO_OPTION);
				while(continuar == 0) {
					regionEPS =Long.parseLong(JOptionPane.showInputDialog (this, "Region de la EPS a consultar?", "Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE));
					totalVacunados += vacuandes.darTotalVacunadosRegion(regionEPS, fechaInferior, fechaSuperior);
					totalHabilitados += vacuandes.darTotalHabilitadosRegion(regionEPS, fechaInferior, fechaSuperior);
					continuar = JOptionPane.showConfirmDialog(this, "Desea consultar para otra región?", "Consultar indice vacunación", JOptionPane.YES_NO_OPTION);
				}
				if (totalHabilitados == 0)
				{
					throw new Exception ("No se pudo calcular el índice ya que no hay ciudadanos habilitados para esa región o rango de fechas");
				}

				String resultado = "En mostrarIndiceVacunacion\n\n";
				resultado += "Índice de vacunación: " + (double)(totalVacunados* 1.0/totalHabilitados);
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void mostrarIndiceVacunacionEstado() {
		try 
		{
			VOEstadoVacunacion estado = (VOEstadoVacunacion) JOptionPane.showInputDialog(this, "Seleccione estado de vacunación a consultar",
					"Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE, null, 
					vacuandes.darEstadosVacunacion().toArray(), // Arreglo de opciones
					""); // Valor inicial

			int reply = JOptionPane.showConfirmDialog(this, "Desea consultar un rango de fechas?", "Consultar índice vacunación", JOptionPane.YES_NO_OPTION);
			Timestamp fechaInferior = convertStringToTimestamp("20200101 00:00");

			if(reply == 0) {
				String fechaI = (JOptionPane.showInputDialog (this, "Fecha inferior en formato yyyyMMdd?", "Consultar índice vacunación", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
				fechaInferior = convertStringToTimestamp(fechaI);
			}
			String fechaS = (JOptionPane.showInputDialog (this, "Fecha superior en formato yyyyMMdd?", "Consultar índice vacunación", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
			Timestamp fechaSuperior = convertStringToTimestamp(fechaS);

			if (estado != null)
			{
				long totalVacunados = vacuandes.darTotalVacunadosEstado(estado.getIdentificadorEstado(), fechaInferior, fechaSuperior);
				long totalHabilitados = vacuandes.darTotalHabilitadosEstado(estado.getIdentificadorEstado(), fechaInferior, fechaSuperior);
				if (totalHabilitados == 0)
				{
					throw new Exception ("No se pudo calcular el índice ya que no hay ciudadanos habilitados para ese estado o rango de fechas");
				}
				String resultado = "En mostrarIndiceVacunacion\n\n";
				resultado += "Índice de vacunación: " + (double)(totalVacunados* 1.0/totalHabilitados);
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void mostrarIndiceVacunacionEtapa() {
		try 
		{
			VOEtapa etapa = (VOEtapa) JOptionPane.showInputDialog(this, "Seleccione etapa de vacunación a consultar",
					"Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE, null, 
					vacuandes.darEtapas().toArray(), // Arreglo de opciones
					""); // Valor inicial

			int reply = JOptionPane.showConfirmDialog(this, "Desea consultar un rango de fechas?", "Consultar indice vacunación", JOptionPane.YES_NO_OPTION);
			Timestamp fechaInferior = convertStringToTimestamp("20200101 00:00");

			if(reply == 0) {
				String fechaI = (JOptionPane.showInputDialog (this, "Fecha inferior en formato yyyyMMdd?", "Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
				fechaInferior = convertStringToTimestamp(fechaI);
			}
			String fechaS = (JOptionPane.showInputDialog (this, "Fecha superior en formato yyyyMMdd?", "Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
			Timestamp fechaSuperior = convertStringToTimestamp(fechaS);

			if (etapa != null)
			{
				long totalVacunados = vacuandes.darTotalVacunadosEtapa(etapa.getNumeroDeEtapa(), fechaInferior, fechaSuperior);
				long totalHabilitados = vacuandes.darTotalHabilitadosEtapa(etapa.getNumeroDeEtapa(), fechaInferior, fechaSuperior);
				if (totalHabilitados == 0)
				{
					throw new Exception ("No se pudo calcular el índice ya que no hay ciudadanos habilitados para esa etapa o rango de fechas");
				}
				String resultado = "En mostrarIndiceVacunacion\n\n";
				resultado += "Índice de vacunación: " + (double)(totalVacunados* 1.0/totalHabilitados);
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void mostrarIndiceVacunacionEPS() {
		try 
		{
			VOOficinaRegionalEPS eps = (VOOficinaRegionalEPS) JOptionPane.showInputDialog(this, "Seleccione eps a consultar",
					"Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE, null, 
					vacuandes.darOficinasRegionalesEPS().toArray(), // Arreglo de opciones
					""); // Valor inicial

			int reply = JOptionPane.showConfirmDialog(this, "Desea consultar un rango de fechas?", "Consultar índice vacunación", JOptionPane.YES_NO_OPTION);
			Timestamp fechaInferior = convertStringToTimestamp("20200101 00:00");

			if(reply == 0) {
				String fechaI = (JOptionPane.showInputDialog (this, "Fecha inferior en formato yyyyMMdd?", "Consultar índice vacunación", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
				fechaInferior = convertStringToTimestamp(fechaI);
			}
			String fechaS = (JOptionPane.showInputDialog (this, "Fecha superior en formato yyyyMMdd?", "Consultar índice vacunación", JOptionPane.QUESTION_MESSAGE).trim()) + " 00:00";
			Timestamp fechaSuperior = convertStringToTimestamp(fechaS);

			if (eps != null)
			{
				long totalVacunados = vacuandes.darTotalVacunadosEPS(eps.getNombreEPS(), fechaInferior, fechaSuperior);
				long totalHabilitados = vacuandes.darTotalHabilitadosEPS(eps.getNombreEPS(), fechaInferior, fechaSuperior);

				int continuar = JOptionPane.showConfirmDialog(this, "Desea consultar para otra región?", "Consultar indice vacunación", JOptionPane.YES_NO_OPTION);
				while(continuar == 0) {
					eps = (VOOficinaRegionalEPS) JOptionPane.showInputDialog(this, "Seleccione eps a consultar",
							"Consultar indice vacunación", JOptionPane.QUESTION_MESSAGE, null, 
							vacuandes.darOficinasRegionalesEPS().toArray(), // Arreglo de opciones
							""); // Valor inicial
					totalVacunados += vacuandes.darTotalVacunadosEPS(eps.getNombreEPS(), fechaInferior, fechaSuperior);
					totalHabilitados += vacuandes.darTotalHabilitadosEPS(eps.getNombreEPS(), fechaInferior, fechaSuperior);
					continuar = JOptionPane.showConfirmDialog(this, "Desea consultar para otra eps?", "Consultar indice vacunación", JOptionPane.YES_NO_OPTION);
				}

				if (totalHabilitados == 0)
				{
					throw new Exception ("No se pudo calcular el índice ya que no hay ciudadanos habilitados para esta eps o rango de fechas");
				}
				String resultado = "En mostrarIndiceVacunacion\n\n";
				resultado += "Índice de vacunación: " + (double)(totalVacunados* 1.0/totalHabilitados);
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void mostrarCiudadanosAtendidosFecha()
	{
		Timestamp fechaInferior = null;
		Timestamp fechaSuperior = null;
		try 
		{
			int reply = JOptionPane.showConfirmDialog(this, "Desea consultar un rango de fechas?", "Ciudadanos atentidos fecha", JOptionPane.YES_NO_OPTION);
			if (reply == 0) {
				String fechaI = (JOptionPane.showInputDialog (this, "Fecha inferior  en formato yyyyMMdd?", "Ciudadanos atentidos fecha", JOptionPane.QUESTION_MESSAGE).trim())+" 00:00";
				String fechaII = (JOptionPane.showInputDialog (this, "Fecha  superioren formato yyyyMMdd?", "Ciudadanos atentidos fecha", JOptionPane.QUESTION_MESSAGE).trim())+" 00:00" ;
				fechaInferior = convertStringToTimestamp(fechaI);
				fechaSuperior = convertStringToTimestamp(fechaII);
			}
			if (reply == 1) {
				String fechaI = (JOptionPane.showInputDialog (this, "Fecha  en formato yyyyMMdd?", "Ciudadanos atentidos fecha", JOptionPane.QUESTION_MESSAGE).trim()) ;
				String fechaII = fechaI+ " 00:00";
				fechaInferior = convertStringToTimestamp(fechaII);

				String fecha2 = fechaI + " 23:59";
				fechaSuperior = convertStringToTimestamp(fecha2);
			}

			VOPuntoVacunacion punto =  (VOPuntoVacunacion) JOptionPane.showInputDialog(this, "Seleccione punto vacunacion",
					"Ciudadanos atentidos fecha", JOptionPane.QUESTION_MESSAGE, null, 
					vacuandes.darPuntosVacunacion().toArray(), // Arreglo de opciones
					""); // Valor inicial



			if (punto != null)
			{

				List<Ciudadano> e = vacuandes.darCiudadanosAtendidosFecha(punto.getDireccionPuntoVacunacion(), fechaInferior, fechaSuperior);

				if (e == null)
				{
					throw new Exception ("No se pudo crear el punto de vacunacion ");
				}
				String resultado = "En mostrarCiudadanosAtendidosFecha\n\n";
				resultado += "Los ciudadanos atentidos en la fecha indicada y el punto de vacunacion: " +punto.getDireccionPuntoVacunacion()+"son ";
				for (Ciudadano ciudadano : e) {
					resultado += ciudadano.toString();

				}
				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	public void mostrarPuntosVacunacionEfectivos()
	{
		Timestamp fechaInferior = null;
		Timestamp fechaSuperior = null;
		try 
		{
			int reply = JOptionPane.showConfirmDialog(this, "Desea consultar un rango de fechas?", "Puntos vacunacion efectivos", JOptionPane.YES_NO_OPTION);
			if (reply == 0) {
				String fechaI = (JOptionPane.showInputDialog (this, "Fecha inferior  en formato yyyyMMdd?", "Puntos vacunacion efectivos", JOptionPane.QUESTION_MESSAGE).trim())+" 00:00";
				String fechaII = (JOptionPane.showInputDialog (this, "Fecha  superioren formato yyyyMMdd?", "Puntos vacunacion efectivos", JOptionPane.QUESTION_MESSAGE).trim())+" 00:00" ;
				fechaInferior = convertStringToTimestamp(fechaI);
				fechaSuperior = convertStringToTimestamp(fechaII);
			}
			if (reply == 1) {
				String fechaI = (JOptionPane.showInputDialog (this, "Fecha  en formato yyyyMMdd?", "Puntos vacunacion efectivos", JOptionPane.QUESTION_MESSAGE).trim()) ;
				String fechaII = fechaI+ " 00:00";
				fechaInferior = convertStringToTimestamp(fechaII);

				String fecha2 = fechaI + " 23:59";
				fechaSuperior = convertStringToTimestamp(fecha2);
			}




			if (fechaSuperior != null)
			{

				List<PuntoVacunacion> e = vacuandes.darPuntosVacunacionEfectivos(fechaInferior, fechaSuperior);

				if (e == null)
				{
					throw new Exception ("No se pudo crear el punto de vacunacion ");
				}
				String resultado = "En darPuntosVacunacionEfectivos\n\n";
				resultado += "Los 20 puntos de vacunacion mas efectivos en la fecha indicada son ";
				for (PuntoVacunacion puntoVacunacion : e) {
					resultado +=puntoVacunacion.toString();
				}

				resultado += "\n Operación terminada";
				panelDatos.actualizarInterfaz(resultado);
			}
			else
			{
				panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
			}
		} 
		catch (Exception e) 
		{
			//e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	/* ****************************************************************
	 * 			Métodos administrativos
	 *****************************************************************/
	/**
	 * Muestra el log de Vacuandes
	 */
	public void mostrarLogVacuandes ()
	{
		mostrarArchivo ("vacuandes.log");
	}

	/**
	 * Muestra el log de datanucleus
	 */
	public void mostrarLogDatanuecleus ()
	{
		mostrarArchivo ("datanucleus.log");
	}

	/**
	 * Limpia el contenido del log de parranderos
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogVacuandes ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("vacuandes.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de vacuandes ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}

	/**
	 * Limpia el contenido del log de datanucleus
	 * Muestra en el panel de datos la traza de la ejecución
	 */
	public void limpiarLogDatanucleus ()
	{
		// Ejecución de la operación y recolección de los resultados
		boolean resp = limpiarArchivo ("datanucleus.log");

		// Generación de la cadena de caracteres con la traza de la ejecución de la demo
		String resultado = "\n\n************ Limpiando el log de datanucleus ************ \n";
		resultado += "Archivo " + (resp ? "limpiado exitosamente" : "NO PUDO ser limpiado !!");
		resultado += "\nLimpieza terminada";

		panelDatos.actualizarInterfaz(resultado);
	}

	/**
	 * Limpia todas las tuplas de todas las tablas de la base de datos de parranderos
	 * Muestra en el panel de datos el número de tuplas eliminadas de cada tabla
	 */
	public void limpiarBD ()
	{
		try 
		{
			// Ejecución de la demo y recolección de los resultados
			long eliminados [] = vacuandes.limpiarVacuandes();

			// Generación de la cadena de caracteres con la traza de la ejecución de la demo
			String resultado = "\n\n************ Limpiando la base de datos ************ \n";
			resultado += eliminados [0] + " cita eliminados\n";
			resultado += eliminados [1] + " talentohumano eliminados\n";
			resultado += eliminados [2] + " perteneceA eliminados\n";
			resultado += eliminados [3] + " condicion eliminadas\n";
			resultado += eliminados [4] + " trabajaEnliminados\n";
			resultado += eliminados [5] + " tieneCondPreservacion eliminados\n";
			resultado += eliminados [6] + " condicionPreservacion eliminados\n";
			resultado += eliminados [7] + " tieneInfraestructura eliminados\n";
			resultado += eliminados [8] + " infraestructura eliminados\n";
			resultado += eliminados [9] + " puntoVacunacionVirtual eliminados\n";
			resultado += eliminados [10] + " vacuna eliminados\n";
			resultado += eliminados [11] + " ciudadano eliminados\n";
			resultado += eliminados [12] + " puntoVacunacion eliminados\n";
			resultado += eliminados [13] + " loteVacuna eliminados\n";
			resultado += eliminados [14] + " oficinaRegionalEps eliminados\n";
			resultado += eliminados [15] + " estadoVacunacion eliminados\n";
			resultado += eliminados [16] + " etapa eliminados\n";
			resultado += "\nLimpieza terminada";

			panelDatos.actualizarInterfaz(resultado);
		} 
		catch (Exception e) 
		{
			//			e.printStackTrace();
			String resultado = generarMensajeError(e);
			panelDatos.actualizarInterfaz(resultado);
		}
	}

	//TODO mostrar documentacion	
	/**
	 * Muestra el documento de reporte del proyecto
	 */
	public void mostrarDocumentoReporte() {
		mostrarArchivo ("data/DocumentoReporteVacuandes.pdf");
	}

	/**
	 * Muestra el modelo conceptual de Parranderos
	 */
	public void mostrarModeloConceptual ()
	{
		mostrarArchivo ("data/Modelo Conceptual Vacuandes.jpg");
	}

	/**
	 * Muestra el esquema de la base de datos de Parranderos
	 */
	public void mostrarEsquemaBD ()
	{
		mostrarArchivo ("data/Esquema BD Vacuandes.pdf");
	}

	/**
	 * Muestra el script de creación de la base de datos
	 */
	public void mostrarScriptBD ()
	{
		mostrarArchivo ("data/ConstruccionBD.sql");
	}

	/**
	 * Muestra la información acerca del desarrollo de esta apicación
	 */
	public void acercaDe ()
	{
		String resultado = "\n\n ************************************\n\n";
		resultado += " * Universidad	de	los	Andes	(Bogotá	- Colombia)\n";
		resultado += " * Departamento	de	Ingeniería	de	Sistemas	y	Computación\n";
		resultado += " * Licenciado	bajo	el	esquema	Academic Free License versión 2.1\n";
		resultado += " * \n";		
		resultado += " * Curso: isis2304 - Sistemas Transaccionales\n";
		resultado += " * Proyecto: Vacuandes Uniandes\n";
		resultado += " * @version 1.0\n";
		resultado += " * @author Gabriela García y Nicolas Carvajal\n";
		resultado += " * Abril de 2021\n";
		resultado += " * \n";
		resultado += "\n ************************************\n\n";

		panelDatos.actualizarInterfaz(resultado);		
	}

	public void seleccionarUsuario( ) {
		String[] ops  = { "Administrador plan de vacunación", "Administrador EPS regional", "Administrador punto de atención", "Administrador punto vacunación"};
		String sUsuario = (String) JOptionPane.showInputDialog(this, "Seleccione usuario",
				"Selección usario", JOptionPane.QUESTION_MESSAGE, null, 
				ops, // Arreglo de opciones
				""); // Valor inicial
		if (sUsuario != null) {
			if(sUsuario.equals("Administrador plan de vacunación"))
				usuario = 0;
			else if(sUsuario.equals("Administrador EPS regional"))
				usuario = 1;
			else if(sUsuario.equals("Administrador punto de atención"))
				usuario = 2;
			else if(sUsuario.equals("Administrador punto vacunación"))
				usuario = 3;
		} else {
			panelDatos.actualizarInterfaz("Operación cancelada por el usuario");
		}
	}


	/* ****************************************************************
	 * 			Métodos privados para la presentación de resultados y otras operaciones
	 *****************************************************************/
	/**
	 * Genera una cadena de caracteres con la descripción de la excepcion e, haciendo énfasis en las excepcionsde JDO
	 * @param e - La excepción recibida
	 * @return La descripción de la excepción, cuando es javax.jdo.JDODataStoreException, "" de lo contrario
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

	/**
	 * Genera una cadena para indicar al usuario que hubo un error en la aplicación
	 * @param e - La excepción generada
	 * @return La cadena con la información de la excepción y detalles adicionales
	 */
	private String generarMensajeError(Exception e) 
	{
		String resultado = "************ Error en la ejecución\n";
		resultado += e.getLocalizedMessage() + ", " + darDetalleException(e);
		resultado += "\n\nRevise datanucleus.log y vacuandes.log para más detalles";
		return resultado;
	}

	/**
	 * Limpia el contenido de un archivo dado su nombre
	 * @param nombreArchivo - El nombre del archivo que se quiere borrar
	 * @return true si se pudo limpiar
	 */
	private boolean limpiarArchivo(String nombreArchivo) 
	{
		BufferedWriter bw;
		try 
		{
			bw = new BufferedWriter(new FileWriter(new File (nombreArchivo)));
			bw.write ("");
			bw.close ();
			return true;
		} 
		catch (IOException e) 
		{
			//			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Abre el archivo dado como parámetro con la aplicación por defecto del sistema
	 * @param nombreArchivo - El nombre del archivo que se quiere mostrar
	 */
	private void mostrarArchivo (String nombreArchivo)
	{
		try
		{
			Desktop.getDesktop().open(new File(nombreArchivo));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/* ****************************************************************
	 * 			Métodos de la Interacción
	 *****************************************************************/
	/**
	 * Método para la ejecución de los eventos que enlazan el menú con los métodos de negocio
	 * Invoca al método correspondiente según el evento recibido
	 * @param pEvento - El evento del usuario
	 */
	@Override
	public void actionPerformed(ActionEvent pEvento)
	{
		String evento = pEvento.getActionCommand( );		
		try 
		{
			Method req = InterfazVacuandesApp.class.getMethod ( evento );			
			req.invoke ( this );
		} 
		catch (Exception e) 
		{
			e.printStackTrace();
		} 
	}

	/* ****************************************************************
	 * 			Programa principal
	 *****************************************************************/
	/**
	 * Este método ejecuta la aplicación, creando una nueva interfaz
	 * @param args Arreglo de argumentos que se recibe por línea de comandos
	 */
	public static void main( String[] args )
	{
		try
		{

			// Unifica la interfaz para Mac y para Windows.
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName( ) );
			InterfazVacuandesApp interfaz = new InterfazVacuandesApp( );
			interfaz.setVisible( true );
		}
		catch( Exception e )
		{
			e.printStackTrace( );
		}
	}
}
