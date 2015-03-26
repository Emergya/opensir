/*
 * ContactoController.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
 * 
 * This software is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by the Free
 * Software Foundation; either version 2 of the License, or (at your option) any
 * later version.
 * 
 * This software is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA
 * 
 * As a special exception, if you link this library with other files to produce
 * an executable, this library does not by itself cause the resulting executable
 * to be covered by the GNU General Public License. This exception does not
 * however invalidate any other reasons why the executable file might be covered
 * by the GNU General Public License.
 * 
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.WebRequest;

import com.emergya.email.sender.PropertiesEmail;
import com.emergya.email.sender.SendEmailUtils;
import com.emergya.ohiggins.dto.ContactoDto;
import com.emergya.ohiggins.dto.FaqDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.ContactoService;
import com.emergya.ohiggins.service.RegionService;
import com.emergya.ohiggins.web.bean.RegionBean;
import com.emergya.ohiggins.web.util.PaginatedList;
import com.emergya.ohiggins.web.util.Utils;

/**
 * Simple index page controller for contacto
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 */
@Controller
public class ContactoController extends AbstractController {

	private static final long serialVersionUID = -1101152158714071663L;
	private static Log LOG = LogFactory.getLog(ContactoController.class);

	public final static String MODULE = "general";
	public final static String SUB_MODULE = "contacto";
	private List<ContactoDto> contactos = null;
	private static final String MODULOS_CONTACTO = "moduloscontacto";

	/** Lista de contactos usada en las vistas */
	private static final String LISTA_CONTACTOS = "listaContactos";

	private static final String TEXTO_ERROR = "textoError";

	@Resource
	private ContactoService contactoService;

	@Resource
	private RegionService regionService;

	@Autowired
	private RegionBean region;

	/**
	 * Pagina faqs para admin
	 * 
	 * @param model
	 * 
	 * @return "faqs"
	 */
	@RequestMapping(value = "/admin/contactos", method = RequestMethod.GET)
	@SuppressWarnings({ "rawtypes" })
	public String getContactos(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page, Model model,
			WebRequest webRequest) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando los contactos del sistema para la Región="
					+ region.getName_region() + ". Tamaño de página=" + size
					+ ". Pagina=" + page);
		}
		calculatePagination(model);

		Long numElements = contactoService.getResultsByRegionId(region.getId());

		List<ContactoDto> contactResult = contactoService
				.getFromToByRegionIdOrderBy((page - 1) * size, page * size,
						region.getId(), ContactoDto.ID_PROPERTY);

		PaginatedList<ContactoDto> result = new PaginatedList<ContactoDto>(
				contactResult, (int) numElements.longValue(), size, page);
		model.addAttribute(LISTA_CONTACTOS, result);

		if (LOG.isInfoEnabled()) {
			LOG.info("Redirigiendo a la vista contacto/contactos");
		}

		return "admin/contacto/contactos";
	}

	/**
	 * Pagina para la creacion de un nuevo Contacto Entra en la vista
	 * nuevoContacto, solo carga la vista
	 * 
	 * @param model
	 * 
	 * @return "nuevoContacto"
	 */
	@RequestMapping(value = "/admin/leerContacto/{id}", method = RequestMethod.GET)
	public String leerContacto(Model model, @PathVariable("id") String id) {

		calculatePagination(model);
		ContactoDto contacto = (ContactoDto) contactoService
				.getContactoById(Long.decode(id));
		if (contacto != null) {
			contacto.setLeido(Boolean.TRUE);
			contacto.setFechaActualizacion(new Date());
			contactoService.update(contacto);
		}

		return "redirect:/admin/contactos";
	}

	/**
	 * Pagina para la creacion de un nuevo Contacto Entra en la vista
	 * nuevoContacto, solo carga la vista
	 * 
	 * @param model
	 * 
	 * @return "nuevoContacto"
	 */
	@RequestMapping(value = "/contacto/nuevoContacto", method = RequestMethod.GET)
	public String createContacto(Model model) {

		calculatePagination(model);
		ContactoDto contacto = new ContactoDto();
		contacto.setId(null);

		model.addAttribute("contactoDto", contacto);

		cargaListasVista(model);

		cargaKeysCaptcha(model);

		return "contacto/nuevoContacto";
	}

	/**
	 * Carga las clave publicas y privadas del captcha
	 * 
	 * @param model
	 */
	private void cargaKeysCaptcha(Model model) {
		Properties p = new Properties();

		try {
			InputStream inStream = Utils.class
					.getResourceAsStream("/captcha.properties");
			p.load(inStream);
			// LOG.debug("" + p.getProperty(Utils.PUBLIC_KEY_CAPTCHA));
			// LOG.debug("" + p.getProperty(Utils.PRIVATE_KEY_CAPTCHA));
			model.addAttribute(Utils.PUBLIC_KEY_CAPTCHA,
					p.getProperty(Utils.PUBLIC_KEY_CAPTCHA));
			model.addAttribute(Utils.PRIVATE_KEY_CAPTCHA,
					p.getProperty(Utils.PRIVATE_KEY_CAPTCHA));
		} catch (Exception e) {
			LOG.info("Error al obtener las propiedades captcha : " + e);
		}
	}

	/**
	 * Carga las clave publicas y privadas del captcha
	 * 
	 * @param model
	 */
	private Properties cargaPropertiesKeysCaptcha() {
		Properties p = new Properties();

		try {
			InputStream inStream = Utils.class
					.getResourceAsStream("/captcha.properties");
			p.load(inStream);

		} catch (Exception e) {
			LOG.info("Error al obtener las propiedades captcha : " + e);
		}

		return p;
	}

	/**
	 * Carga las listas necesarios para la vista
	 * 
	 * @param model
	 */
	private void cargaListasVista(Model model) {
		// Tipos de autoridad
		List<String> modulos = new LinkedList<String>();
		model.addAttribute(MODULOS_CONTACTO, modulos);
	}

	/**
	 * Comprueba que el captcha introducido es correcto o no
	 * 
	 * @return
	 */
	private boolean compruebaCaptcha(HttpServletRequest request) {
		boolean correcto = false;

		// Comprueba catpcha
		Properties pcatpcha = cargaPropertiesKeysCaptcha();
		String remoteAddr = request.getRemoteAddr();
		ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
		// LOG.debug(pcatpcha.getProperty(Utils.PRIVATE_KEY_CAPTCHA));
		reCaptcha
				.setPrivateKey(pcatpcha.getProperty(Utils.PRIVATE_KEY_CAPTCHA));

		String challenge = request.getParameter("recaptcha_challenge_field");
		String uresponse = request.getParameter("recaptcha_response_field");
		ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr,
				challenge, uresponse);

		if (reCaptchaResponse.isValid()) {
			LOG.debug("Answer was entered correctly!");
			correcto = true;
		} else {
			LOG.debug("Answer is wrong");
			correcto = false;
		}
		// Fin captcha

		return correcto;
	}

	/**
	 * Guarda un nuevo Contacto
	 * 
	 * @param ContactoDto
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contacto/salvarContacto", method = RequestMethod.POST)
	public String saveContacto(@Valid ContactoDto contactoDto,
			BindingResult result, Model model, HttpServletRequest request,
			WebRequest webRequest) {

		model.addAttribute(MODULE_KEY, MODULE);
		model.addAttribute(SUBMODULE_KEY, SUB_MODULE);

		// Comprueba catpcha
		boolean captchaCorrecto = compruebaCaptcha(request);

		if (result.hasErrors() || captchaCorrecto == false) {
			cargaListasVista(model);
			cargaKeysCaptcha(model);
			setLoggedModelProps(request);
			if (contactoDto.getId() == null) {
				return "contacto/nuevoContacto";
			} else {
				return "contacto/editarContacto";
			}
		}

		// guardamos el contacto
		Date hoy = new Date();

		if (contactoDto.getId() != null) {// actualizar
			contactoDto.setFechaActualizacion(hoy);
			contactoService.update(contactoDto);
		} else { // Crear
			contactoDto.setRegion(RegionBean.getRegionDto(region));
			contactoDto.setFechaCreacion(hoy);
			contactoDto.setFechaActualizacion(hoy);
			contactoDto.setLeido(Boolean.FALSE);
			contactoService.create(contactoDto);
		}

		try {
			model.addAttribute("isEnviado", true);
			// Se envia correo al admin y al usuario
			enviaEmailContactoUser(contactoDto, request);
			enviaEmailContactoAdmin(contactoDto.getDescripcion(), request);
		} catch (Exception e) {
			LOG.error(
					"Error procesando nuevo contacto. Probablemente se deba a la configuración del correo.",
					e);
			model.addAttribute(TEXTO_ERROR,
					"El mensaje no ha sido enviado al administrador. Vuelva a intentarlo.");
			model.addAttribute("isEnviado", false);

		}

		return "contacto/mensajeEnviado";
	}

	/**
	 * Envia correcto al usuario del contacto
	 * 
	 * @param request
	 * @param webRequest
	 * @return
	 */
	private boolean enviaEmailContactoUser(ContactoDto contactoDto,
			HttpServletRequest request) {
		boolean enviado = true;

		try {
			// 1: Obtener fichero propiedades
			Properties p = Utils.getPropertiesEmailApplication();

			// 2: Properties email
			PropertiesEmail props = new PropertiesEmail();
			props = Utils.inicializaServidorEmail(props, p);// Valores iniciales
			props.setMAIL_ISSUE_VALUE(Utils.ASUNTO_NUEVO_CONTACTO);// Resto de
																	// Valores

			// 3: Plantilla envio correo
			String rutaPlantilla = request
					.getSession()
					.getServletContext()
					.getRealPath(
							Utils.DIRECTORIO_TEMPLATES
									+ Utils.TEMPLATE_ENVIO_MENSAJE_CONTACTO_USER);

			boolean e = false;
			// 4: Enviar
			// send email
			Map<String, String> parametrosPlantilla = new HashMap<String, String>();
			parametrosPlantilla.put("[\\$]NOMBRE",
					contactoDto != null ? contactoDto.getNombre()
							: new String());

			String message = Utils.generaMensajeEnviar(rutaPlantilla,
					parametrosPlantilla);

			// Sender email user single
			e = SendEmailUtils.sendEmailUser(props, message,
					contactoDto.getNombre(), contactoDto.getEmail());

			enviado = enviado & e;

		} catch (Exception e) {
			LOG.info("Error al carga el fichero de propiedades:");
			enviado = false;
		}

		return enviado;
	}

	/**
	 * Envia email de contacto al admin
	 * 
	 * @return
	 */
	private boolean enviaEmailContactoAdmin(String respuesta,
			HttpServletRequest request) {
		boolean enviado = true;

		try {

			// 1: Obtener fichero propiedades
			Properties p = Utils.getPropertiesEmailApplication();
			String toAddress = p
					.getProperty(Utils.MAIL_SIR_ADDRESS_PROPERTY_KEY);

			// 2: Properties email
			PropertiesEmail props = new PropertiesEmail();
			props = Utils.inicializaServidorEmail(props, p);// Valores
															// iniciales
			props.setMAIL_ISSUE_VALUE(Utils.ASUNTO_NUEVO_CONTACTO);// Resto
																	// de
																	// Valores

			// 3: Plantilla envio correo
			String rutaPlantilla = request
					.getSession()
					.getServletContext()
					.getRealPath(
							Utils.DIRECTORIO_TEMPLATES
									+ Utils.TEMPLATE_ENVIO_MENSAJE_CONTACTO_ADMIN);

			boolean e = false;
			// 4: Enviar
			// send email
			Map<String, String> parametrosPlantilla = new HashMap<String, String>();
			parametrosPlantilla.put("[\\$]NOMBRE",
					Utils.ADMINISTRADOR_MAIL_NAME);
			parametrosPlantilla.put("[\\$]RESPUESTA", respuesta);
			String message = Utils.generaMensajeEnviar(rutaPlantilla,
					parametrosPlantilla);

			// Sender email user single
			e = SendEmailUtils.sendEmailUser(props, message,
					Utils.ADMINISTRADOR_MAIL_NAME, toAddress);

			enviado = e;

		} catch (Exception e) {
			LOG.info("Error al carga el fichero de propiedades:");
			enviado = false;
		}

		return enviado;
	}

	/**
	 * Mensaje Enviado
	 * 
	 * @param ContactoDto
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/contacto/mensajeEnviado")
	public String mensajeEnviado(Model model) {
		return "contacto/mensajeEnviado?msg=success";
	}

	/**
	 * Elimina un contacto Solo se pueden borrar contacto
	 * 
	 * @param id
	 *            Identificador del contacto
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/borrarContacto/{id}")
	public String deleteContacto(@PathVariable long id, Model model) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		ContactoDto contacto = (ContactoDto) contactoService
				.getContactoById(id);

		contactoService.delete(contacto);

		return "redirect:/admin/contactos?msg=success";
	}

	/**
	 * Pagina para cargar los datos de un contacto, para su posterior
	 * modificacion
	 * 
	 * @param id
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/editarContacto/{id}")
	public String modifyContacto(@PathVariable long id, Model model,
			HttpServletRequest request) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		ContactoDto dto = (ContactoDto) contactoService.getContactoById(id);

		model.addAttribute("contactoDto", dto);

		// Se marca como leido
		if (dto != null) {
			dto.setFechaActualizacion(new Date());
			dto.setLeido(Boolean.TRUE);
			contactoService.update(dto);
		}

		setLoggedModelProps(request);

		cargaListasVista(model);
		calculatePagination(model);
		return "admin/contacto/editarcontacto";
	}

	private void setLoggedModelProps(HttpServletRequest request) {
		SecurityContext context = (SecurityContext) request.getSession()
				.getAttribute("SPRING_SECURITY_CONTEXT");
		String usuarioLogado = "";
		if (context != null && context.getAuthentication().isAuthenticated()
				&& !context.getAuthentication().getAuthorities().isEmpty()) {
			usuarioLogado = context.getAuthentication().getName();
			request.setAttribute("IS_ADMIN", context.getAuthentication()
					.getAuthorities().contains(OhigginsUserDetails.ADMIN_AUTH));
		}
		request.setAttribute("usuarioLogado", usuarioLogado);

	}

	@Override
	protected void copyDefaultModel(boolean update, Model model) {
		calculatePagination(model);
		model.addAttribute("contacto", contactos);
	}

	@Override
	protected String getAllSubTabs() {
		return TabsByModule.GENERAL_SUBTABS;
	}

	@Override
	protected int getSelectedSubTab() {
		return 0;
	}

	@Override
	protected String getDefaultPaginationUrl() {
		return "/contacto/contactos";
	}

	@Override
	protected String getActiveModule() {
		return MODULE;
	}

	@Override
	protected String getActiveSubModule() {
		return SUB_MODULE;
	}

	/**
	 * Pagina contactos para el resto de usuarios
	 * 
	 * @param model
	 * 
	 * @return "contactos"
	 */
	@RequestMapping(value = "/contacto/contactos", method = RequestMethod.GET)
	@SuppressWarnings({ "rawtypes" })
	public String getContactosUser(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page, Model model) {

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando los contactos del sistema para users. Tamaño de página="
					+ size + ". Pagina=" + page);
		}
		calculatePagination(model);

		List result = (List<ContactoDto>) contactoService
				.getFromToByRegionIdOrderBy(0, Integer.MAX_VALUE,
						region.getId(), FaqDto.TITULO_PROPERTY);

		model.addAttribute(LISTA_CONTACTOS, result);

		if (LOG.isInfoEnabled()) {
			LOG.info("Redirigiendo a la vista contacto/contactos");
		}

		return "contacto/contactos";
	}
}
