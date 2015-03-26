package com.emergya.ohiggins.web;

import java.util.Map;

import javax.annotation.Resource;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.IniciativaInversionService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.service.IniciativaInversionService.TIPO_PROYECTOS;
import com.google.common.collect.Maps;

@Controller
@RequestMapping("/fichaInversion")
public class FichaInversionController {

	/** Log */
	private static Log LOG = LogFactory.getLog(FichaInversionController.class);

	public final static String NAME = "name";
	public final static String CODIGOBIP = "codBip";
	public final static String INFO = "info";
	public final static String INFOINPI = "infoinpi";
	public final static String INFOINPIBLO1 = "infoinpiblo1";
	public final static String INFOINPIBLO2 = "infoinpiblo2";
	public final static String INFOINETE = "infoinete";
	public final static String INFOINARF = "infoinarf";
	public final static String INFOINPEI = "infoinpei";

	@Resource
	private IniciativaInversionService iniciativaInvService;
	@Resource
	private UserAdminService userService;

	// Booleanos que indican si hay informacion en los distintos bloques
	// private boolean infoInPI = false;
	// private boolean infoInPIBlo1 = false;
	// private boolean infoInPIBlo2 = false;
	// private boolean infoInETE = false;
	// private boolean infoInARF = false;
	// private boolean infoInPEI = false;

	@RequestMapping("/ficha")
	public ModelAndView getFichaInfo(@RequestParam("codBip") String codBip,
			@RequestParam("etapa") String etapa,
			@RequestParam("serRes") String serRes,
			@RequestParam("anyo") String anyo, 
			@RequestParam("tipoProyecto") String tipoProyecto, WebRequest webRequest) {

		ModelAndView modelAndView = new ModelAndView();
		rolUser(modelAndView, webRequest);

		Map<String, String> info;
		
		if(tipoProyecto.equalsIgnoreCase(TIPO_PROYECTOS.PREINVERSION.toString())) {
			info = iniciativaInvService.getInfoFichaInversion(
					codBip, etapa, serRes, anyo);
		} else {
			info = iniciativaInvService.getInfoFichaEjecucion(
					codBip, etapa, serRes, anyo);
		}

		Map<String, Boolean> existInfo = existInfo(info);
		String nameInversion = iniciativaInvService.getNameInversion(codBip);

		modelAndView.addObject(NAME, nameInversion);
		modelAndView.addObject(CODIGOBIP, codBip);
		modelAndView.addObject(INFO, info);
		modelAndView.addObject(INFOINPI, existInfo.get(INFOINPI));
		modelAndView.addObject(INFOINPIBLO1, existInfo.get(INFOINPIBLO1));
		modelAndView.addObject(INFOINPIBLO2, existInfo.get(INFOINPIBLO2));
		modelAndView.addObject(INFOINETE, existInfo.get(INFOINETE));
		modelAndView.addObject(INFOINARF, existInfo.get(INFOINARF));
		modelAndView.addObject(INFOINPEI, existInfo.get(INFOINPEI));

		modelAndView.setViewName("fichaInversion/ficha");

		return modelAndView;
	}

	public Map<String, Boolean> existInfo(Map<String, String> info) {
		Map<String, Boolean> result = Maps.newHashMap();
		result.put(INFOINPI, false);
		result.put(INFOINPIBLO1, false);
		result.put(INFOINPIBLO2, false);
		result.put(INFOINETE, false);
		result.put(INFOINARF, false);
		result.put(INFOINPEI, false);

		boolean infoInPIBlo1 = false;
		boolean infoInPIBlo2 = false;

		String[] keysInfoInPIBlo1 = { "institucionPostula", "estadoPI", "anyo",
				"fuenteFinanciamiento", "lineaFinanciera", "montoTotal",
				"sector", "etapa", "ubicacion", "descripcion" };
		String[] keysInfoInPIBlo2 = { "oficio", "carpeta", "ingresado",
				"requisitos", "vinculacion", "lineamiento", "politicaTurismo",
				"politicaCiencia", "plan", "reconstruccion", "programacion",
				"autoridadRegional", "mesa", "observaciones" };
		String[] keysinfoInETE = { "institucionEvalua", "estadoETE",
				"fechaEstado", "fechaIngresoSNI" };
		String[] keysinfoInARF = { "institucionApruebaRecursos", "estadoRF",
				"nacuerdo", "fechaAcuerdo", "montoAprobado" };
		String[] keysinfoInPEI = { "institucionGasto", "unidad", "gastado",
				"pagado" };

		for (int i = 0; i < keysInfoInPIBlo1.length; i++) {
			if (info.containsKey(keysInfoInPIBlo1[i])) {
				result.put(INFOINPIBLO1, true);
				infoInPIBlo1 = true;
			}
		}

		for (int i = 0; i < keysInfoInPIBlo2.length; i++) {
			if (info.containsKey(keysInfoInPIBlo2[i])) {
				LOG.info("Existe info del bloque 2");
				result.put(INFOINPIBLO2, true);
				infoInPIBlo2 = true;
			}
		}

		if (infoInPIBlo1 || infoInPIBlo2) {
			result.put(INFOINPI, true);
		}

		for (int i = 0; i < keysinfoInETE.length; i++) {
			if (info.containsKey(keysinfoInETE[i])) {
				result.put(INFOINETE, true);
			}
		}

		for (int i = 0; i < keysinfoInARF.length; i++) {
			if (info.containsKey(keysinfoInARF[i])) {
				result.put(INFOINARF, true);
			}
		}

		for (int i = 0; i < keysinfoInPEI.length; i++) {
			if (info.containsKey(keysinfoInPEI[i])) {
				result.put(INFOINPEI, true);
			}
		}

		return result;
	}

	@RequestMapping("/fichaPopup")
	public ModelAndView getFichaPopup(@RequestParam("codBip") String codBip,
			@RequestParam("etapa") String etapa,
			@RequestParam("serRes") String serRes,
			@RequestParam("anyo") String anyo,
			@RequestParam("tipoProyecto") String tipoProyecto, WebRequest webRequest){

		ModelAndView modelAndView = new ModelAndView();
		rolUser(modelAndView, webRequest);

		Map<String, String> info;
		
		if(tipoProyecto.equalsIgnoreCase(TIPO_PROYECTOS.PREINVERSION.toString())) {
			info = iniciativaInvService.getInfoFichaInversion(
					codBip, etapa, serRes, anyo);
		} else {
			info = iniciativaInvService.getInfoFichaEjecucion(
					codBip, etapa, serRes, anyo);
		}

		Map<String, Boolean> existInfo = existInfo(info);
		String nameInversion = iniciativaInvService.getNameInversion(codBip);

		modelAndView.addObject(NAME, nameInversion);
		modelAndView.addObject(CODIGOBIP, codBip);
		modelAndView.addObject(INFO, info);
		modelAndView.addObject(INFOINPI, existInfo.get(INFOINPI));
		modelAndView.addObject(INFOINPIBLO1, existInfo.get(INFOINPIBLO1));
		modelAndView.addObject(INFOINPIBLO2, existInfo.get(INFOINPIBLO2));
		modelAndView.addObject(INFOINETE, existInfo.get(INFOINETE));
		modelAndView.addObject(INFOINARF, existInfo.get(INFOINARF));
		modelAndView.addObject(INFOINPEI, existInfo.get(INFOINPEI));

		modelAndView.setViewName("fichaInversion/fichaPopup");

		return modelAndView;
	}

	private void rolUser(ModelAndView model, WebRequest webRequest) {
		model.addObject("IS_ADMIN",
				OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));

//		SecurityContext context = (SecurityContext) webRequest.getAttribute(
//				"SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);
		SecurityContext context  = SecurityContextHolder.getContext();
		boolean isServicioPublico = false;

		if (context != null && context.getAuthentication().isAuthenticated()
				&& !context.getAuthentication().getAuthorities().isEmpty()) {
			String username = context.getAuthentication().getName();
			isServicioPublico = userService
					.isUserAuthorityPublicService(username);
		}
		model.addObject("IS_PUBLIC_SERVICE", isServicioPublico);

	}
}