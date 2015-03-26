package com.emergya.ohiggins.web.util;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;

import com.emergya.email.sender.PropertiesEmail;

/**
 * Clase de utilidades generales
 * 
 * @author jariera
 * @version 1.0
 * 
 */
public class Utils {

	private static Log log = LogFactory.getLog(Utils.class);

	public static final String DIRECTORIO_TEMPLATES = "/WEB-INF/templates/";

	public static final String BARRA = "/";

	public static final String TEMPLATE_ENVIO_EMAIL = "envioEmail.html";

	public static final String TEMPLATE_ENVIO_PUBLICACION_EMAIL = "envioPublicacion.html";

	public static final String TEMPLATE_ENVIO_NUEVA_PUBLICACION_EMAIL = "envioNuevaPublicacion.html";

	public static final String ASUNTO_NUEVA_SOLICITUD_PUBLICACION = "Solicitud de Nueva Publicación";

	public static final String TEMPLATE_ENVIO_MENSAJE_CONTACTO_ADMIN = "envioMensajeContactoAAdmin.html";

	public static final String TEMPLATE_ENVIO_MENSAJE_CONTACTO_USER = "envioMensajeContactoAUsuario.html";

	public static final String ASUNTO_NUEVO_CONTACTO = "Solicitud Nuevo Contacto";

	public static final String ASUNTO_SOLCITUD_PUBLICACION_CAPA = "Solicitud Publicación de Capa";
	public static final String TEMPLATE_ENVIO_MENSAJE_SOLICITUD_PUBLICACION_CAPA = "envioPublicacionLayer.html";

	public static final String PUBLIC_KEY_CAPTCHA = "publicKeycaptcha";
	public static final String PRIVATE_KEY_CAPTCHA = "privateKeycaptcha";
	
	public static final String MAIL_SIR_ADDRESS_PROPERTY_KEY = "mail.sir.address";

	// String ruta =
	// request.getSession().getServletContext().getRealPath(Utils.DIRECTORIO_TEMPLATES
	// + "/"+"envioEmail.html");

	private static final int RETARDO = 6000;

	/**
	 * Cuerpo de la plantilla
	 * 
	 * @param encoding
	 * @param rutaPlantillas
	 * @param plantilla
	 * @param locale
	 * @return
	 */
	public static String getCuerpoPlantilla(String rutaPlantilla) {
		return readFile(rutaPlantilla);
	}

	/**
	 * Sustituir parametros en la plantilla
	 * 
	 * @param mensaje
	 * @param parametros
	 * @return
	 */
	public static String sustituirParametros(String mensaje,
			Map<String, String> parametros) {

		String mensajeAux = mensaje;

		if (parametros != null) {

			for (String clave : parametros.keySet()) {

				mensajeAux = mensajeAux
						.replaceAll(clave, parametros.get(clave));
			}
		}

		return mensajeAux;
	}

	/**
	 * 
	 * @param filePath
	 * @return
	 * @throws java.io.IOException
	 */
	public static String readFile(String filePath) {

		StringBuffer fileData = new StringBuffer(1000);
		try {
			BufferedReader reader = new BufferedReader(new FileReader(filePath));
			char[] buf = new char[1024];
			int numRead = 0;

			while ((numRead = reader.read(buf)) != -1) {
				String readData = String.valueOf(buf, 0, numRead);
				fileData.append(readData);
				buf = new char[1024];
			}

			reader.close();

		} catch (IOException e) {
			log.info("readFileAsString. : " + e);
			return null;
		}

		return fileData.toString();
	}

	/**
	 * Mensaje a enviar
	 * 
	 * @param ruta
	 * @param parametrosPlantilla
	 * @return
	 */
	public static String generaMensajeEnviar(String ruta,
			Map<String, String> parametrosPlantilla) {

		String mensajeAEnviar = new String();

		if (ruta == null || parametrosPlantilla == null)
			return mensajeAEnviar;

		mensajeAEnviar = Utils.sustituirParametros(Utils.readFile(ruta),
				parametrosPlantilla);
		return mensajeAEnviar;
	}

	/**
	 * Get authentication
	 * 
	 * @param webRequest
	 *            .
	 * @return
	 */
	public static Authentication getAuthentication(WebRequest webRequest) {
		SecurityContext context = (SecurityContext) webRequest.getAttribute(
				"SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);

		Authentication u = null;
		if (context != null && context.getAuthentication().isAuthenticated()
				&& !context.getAuthentication().getAuthorities().isEmpty()) {
			u = context.getAuthentication();
		}
		return u;
	}

	/**
	 * Simula retardo para alfresco
	 */
	public static void simulaRetardo() {
		try {
			Thread.sleep(RETARDO);
		} catch (Exception e) {
		}
	}

	/**
	 * Simula retardo para alfresco
	 */
	public static void simulaRetardo(int segundos) {
		try {
			Thread.sleep(segundos * 1000);
		} catch (Exception e) {
		}
	}

	/**
	 * Obtener el properties de la configuracion del servidor correo
	 * 
	 * @return
	 */
	public static Properties getPropertiesEmailApplication() {
		Properties p = new Properties();

		try {
			InputStream inStream = Utils.class
					.getResourceAsStream("/email.properties");
			p.load(inStream);
		} catch (Exception e) {
			log.info("Error al obtener las propiedades del servidor de correo de la aplicacion : "
					+ e);
		}

		return p;
	}

	/**
	 * Inicializa el servidor de correos con los datos basicos de la conexion
	 * 
	 * @param props
	 *            Properties del modulo send-email
	 * @param p
	 *            Properties de la aplicacion
	 */
	public static PropertiesEmail inicializaServidorEmail(
			PropertiesEmail props, Properties p) {

		if (props == null)
			props = new PropertiesEmail();

		// Obtenemos las propiedades del fichero properties de la aplicacion,
		// para almacenarlas en el del servidor correo.
		if (p != null) {
			props.setMAIL_ACTIVATE_BCC_VALUE(Boolean.valueOf(p
					.getProperty("mail.activarbcc")));
			props.setMAIL_AUTENTICATION_VALUE(Boolean.valueOf(p
					.getProperty("mail.autenticacion")));
			props.setMAIL_SERVER_VALUE(p.getProperty("mail.servidor"));
			props.setMAIL_USER_VALUE(p.getProperty("mail.usuario"));
			props.setMAIL_PASSWORD_VALUE(p.getProperty("mail.clave"));
			props.setMAIL_PORT_VALUE(props.getMAIL_PORT_DEFAULT_VALUE());
			props.setMAIL_BCC_VALUE(p.getProperty("mail.bcc"));
			props.setMAIL_BCC_NAME_VALUE(p.getProperty("mail.bcc.name"));
			props.setMAIL_BCC_ADDRESS_VALUE(p.getProperty("mail.bcc"));
			props.setMAIL_ADDRESS_SENDER_VALUE(p
					.getProperty("mail.from.address"));
			props.setMAIL_NAME_SENDER_VALUE(p.getProperty("mail.from.name"));
		}

		return props;

		/**
		 * props.setMAIL_ACTIVATE_BCC_VALUE(new
		 * Boolean(p.getProperty("mail.activarbcc")));
		 * props.setMAIL_AUTENTICATION_VALUE(new
		 * Boolean(p.getProperty("mail.autenticacion")));
		 * props.setMAIL_SERVER_VALUE(p.getProperty("mail.servidor"));
		 * props.setMAIL_USER_VALUE(p.getProperty("mail.usuario"));
		 * props.setMAIL_PASSWORD_VALUE(p.getProperty("mail.clave"));
		 * props.setMAIL_PORT_VALUE(props.getMAIL_PORT_DEFAULT_VALUE());
		 * props.setMAIL_ISSUE_VALUE("Solicitud de Nueva Publicación");
		 * props.setMAIL_BCC_VALUE(p.getProperty("mail.bcc"));
		 * props.setMAIL_BCC_NAME_VALUE(p.getProperty("mail.bcc.name"));
		 * props.setMAIL_BCC_ADDRESS_VALUE(p.getProperty("mail.bcc"));
		 * props.setMAIL_ADDRESS_SENDER_VALUE
		 * (p.getProperty("mail.from.address"));
		 * props.setMAIL_NAME_SENDER_VALUE(p.getProperty("mail.from.name"));
		 */
	}

	public static final String ADMINISTRADOR_MAIL_NAME = "Administrador";
}
