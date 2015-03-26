/*
 * UserAdminController.java
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

import com.emergya.ohiggins.config.WorkspaceNamesConfig;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.web.util.ErrorMessage;
import com.emergya.ohiggins.web.util.PaginatedList;
import com.emergya.ohiggins.web.util.ValidationResponse;

/**
 * Simple index page controller for user admin
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 */
@Controller
public class UserAdminController extends AbstractController {

	private static final long serialVersionUID = -1291370778629362899L;

	private static Log LOG = LogFactory.getLog(UserAdminController.class);

	public final static String MODULE = "general";
	public final static String SUB_MODULE = "usuarios";
	
	private static final String ESADMIN = "esAdmin";

    @Resource
    private WorkspaceNamesConfig workspaceNamesConfig;

	@Resource
	private UserAdminService userAdminService;
	@Resource
	private InstitucionService institucionService;

	// @Resource
	// private Validator validator;

	private List<UsuarioDto> usuarios = null;

	// @InitBinder
	// protected void initBinder(WebDataBinder binder) {
	// binder.setValidator(validator);
	// }

	/**
	 * Obtiene el nombre del workspace asociado a un usuario
	 * 
	 * @param user
	 */
	public String getWorkspaceName(String user) {
		LayerResourceDto dto = new LayerResourceDto();
        String workspaceName = workspaceNamesConfig.getTmpWorkspace();
      		;
		if (user != null) {
			UsuarioDto userDto = userAdminService
					.obtenerUsuarioByUsername(user);
			dto.setAuthority((AuthorityDto) institucionService.getById(userDto
					.getAuthorityId()));
			workspaceName = dto.getAuthority().getWorkspaceName();
		}
		return workspaceName;
	}

	/**
	 * Pagina inicial de la administracion de usuarios
	 * 
	 * @param model
	 * 
	 * @return "usuarios"
	 */
	@RequestMapping(value = "/admin/usuarios", method = RequestMethod.GET)
	@SuppressWarnings({ "rawtypes" })
	public String getUsers(@RequestParam(defaultValue = "10") int size,
			@RequestParam(defaultValue = "1") int page, Model model) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		if (LOG.isInfoEnabled()) {
			LOG.info("Consultando los usuarios del sistema. Tamaño de página="
					+ size + ". Pagina=" + page);
		}
		calculatePagination(model);

		Long numElements = userAdminService.getResults();

		List userResult = (List<UsuarioDto>) userAdminService.getFromToOrderBy(
				(page - 1) * size, page * size, UsuarioDto.USERNAME_PROPERTY);

		PaginatedList result = new PaginatedList(userResult,
				(int) numElements.longValue(), size, page);
		model.addAttribute("listaUsuarios", result);
		if (LOG.isInfoEnabled()) {
			LOG.info("Redirigiendo a la vista admin/usuarios/usuarios");
		}

		return "admin/usuarios/usuarios";
	}

	/**
	 * Pagina para la creacion de un nuevo usuario.
	 * 
	 * @param model
	 * 
	 * @return "nuevoUsuario"
	 */
	@RequestMapping(value = "/admin/nuevoUsuario", method = RequestMethod.GET)
	public String createUser(Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		calculatePagination(model);
		model.addAttribute("usuarioDto", new UsuarioDto());
		model.addAttribute(ESADMIN, false);

		populateAuthorities(model);

		return "admin/usuarios/nuevoUsuario";
	}

	/**
	 * Crea o actualiza un usuario, validando previamente el mismo.
	 * 
	 * @param usuarioDto
	 * @param result
	 * @param model
	 * @return
	 */
	@RequestMapping(value = "/admin/salvarUsuario", method = RequestMethod.POST)
	public String saveUser(@Valid UsuarioDto usuarioDto, BindingResult result,
			Model model) {

		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

                model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", getActiveSubModule());
                
		// Comprobamos que no exista el nombre de usuario si es un usuario nuevo
		if (usuarioDto != null && usuarioDto.getId() == null) {
			UsuarioDto existingUser = userAdminService
					.obtenerUsuarioByUsername(usuarioDto.getUsername());
			if (existingUser != null) {
				FieldError error = new FieldError(
						"usuarioDto",
						"username",
						existingUser.getUsername()
								+ " ya est\u00e1 en uso, elija otro nombre de usuario.");
				result.addError(error);
			}
		}

		if (usuarioDto != null && !usuarioDto.isAdmin() && usuarioDto.getAuthorityId() == null) {

			FieldError error = new FieldError("usuarioDto", "authorityId", "Elija una instituci\u00F3n.");
			result.addError(error);

		}

		if (result.hasErrors()) {
			populateAuthorities(model);
			if (usuarioDto.getId() == null) {
				return "admin/usuarios/nuevoUsuario";
			} else if (usuarioDto.getId() != null) {
				return "admin/usuarios/editarUsuario";
			}
		}

		Date fecha = Calendar.getInstance().getTime();
		usuarioDto.setFechaActualizacion(fecha);

		// Guardamos la institucion
		Long idAuth = usuarioDto.getAuthorityId();

		if (idAuth != null && !"".equals(idAuth)) {
			AuthorityDto auth = (AuthorityDto) institucionService
					.getById(idAuth);
			usuarioDto.setAuthorityId(idAuth);
			usuarioDto.setAuth(auth);

		}
		if (usuarioDto.isAdmin()) {
			usuarioDto.setAuth(null);
			usuarioDto.setAuthority(null);
			usuarioDto.setAuthorityId(null);

		}

		model.addAttribute(ESADMIN, usuarioDto.isAdmin());

		if (usuarioDto.getId() != null) {
			UsuarioDto old = userAdminService.obtenerUsuarioById(usuarioDto
					.getId());

			if (old.isAdmin() && !usuarioDto.isAdmin()
					&& this.userAdminService.obtenerUsuariosAdministradores().size() <= 1) {

				return "redirect:/admin/usuarios?msg=errorDeleteAdmin";

			} else {
				usuarioDto.setUsername(old.getUsername());
				userAdminService.update(usuarioDto);
			}
		} else {
			usuarioDto.setFechaCreacion(fecha);
			userAdminService.create(usuarioDto);
		}

		return "redirect:/admin/usuarios?msg=success";
	}

	private void populateAuthorities(Model model) {
		model.addAttribute(institucionService.getAllOrderedByName());
	}

	@RequestMapping(value = "/admin/user.json", method = RequestMethod.POST)
	public @ResponseBody
	ValidationResponse partialValidation(Model model, @Valid UsuarioDto user,
			BindingResult result) {
		ValidationResponse res = new ValidationResponse();
		if (!result.hasErrors()) {
			res.setStatus("SUCCESS");
		} else {
			res.setStatus("FAIL");
			List<FieldError> allErrors = result.getFieldErrors();
			List<ErrorMessage> errorMesages = new ArrayList<ErrorMessage>();
			for (FieldError objectError : allErrors) {
				errorMesages.add(new ErrorMessage(objectError.getField(),
						objectError.getField() + "  "
								+ objectError.getDefaultMessage()));
			}
			res.setErrorMessageList(errorMesages);

		}

		return res;
	}

	@RequestMapping(value = "/admin/userField.json", method = RequestMethod.POST)
	public @ResponseBody
	ValidationResponse partialFieldValidation(Model model,
			@Valid UsuarioDto user, BindingResult result) {
		ValidationResponse res = new ValidationResponse();
		String validatedField = (String) model.asMap().get("validatedField");
		if (!result.hasErrors() || validatedField == null) {
			res.setStatus("SUCCESS");
		} else {
			res.setStatus("FAIL");
			List<FieldError> allErrors = result.getFieldErrors();
			List<ErrorMessage> errorMesages = new ArrayList<ErrorMessage>();
			for (FieldError objectError : allErrors) {
				if (objectError.getField().equals(validatedField)) {
					errorMesages.add(new ErrorMessage(objectError.getField(),
							objectError.getField() + "  "
									+ objectError.getDefaultMessage()));
				}
			}
			res.setErrorMessageList(errorMesages);
		}
		return res;
	}

	@RequestMapping(value = "/admin/usuarios/checkUsernameAvailability", method = RequestMethod.GET)
	public @ResponseBody
	boolean validateUserExistence(String username) {
		return userAdminService.isUsernameAvailable(username);
	}

	@RequestMapping(value = "/admin/borrarUsuario/{id}")
	public String deleteUser(@PathVariable long id, Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		UsuarioDto usuario = (UsuarioDto) userAdminService
				.obtenerUsuarioById(id);

		if (usuario.isAdmin() && userAdminService.obtenerUsuariosAdministradores().size() <= 1)
			return "redirect:/admin/usuarios?msg=errorDeleteAdmin";
		else
			userAdminService.delete(usuario);

		return "redirect:/admin/usuarios?msg=success";
	}

	@RequestMapping(value = "/admin/editarUsuario/{id}")
	public String modifyUser(@PathVariable long id, Model model) {
		// Accede el rol ADMIN, en otro caso pagina error
		decideIfIsUserInRole(ConstantesPermisos.ADMIN);

		UsuarioDto usuario = (UsuarioDto) userAdminService
				.obtenerUsuarioById(id);
		usuario.setConfirmPassword(usuario.getPassword());
		populateAuthorities(model);
		model.addAttribute(usuario);
		model.addAttribute("module", getActiveModule());
		model.addAttribute("submodule", getActiveSubModule());
		model.addAttribute(ESADMIN, usuario.isAdmin());

		return "admin/usuarios/editarUsuario";
	}

	/**
	 * Referencia al enlace a la paginacion de usuarios
	 * 
	 * @return 'admin/usuarios'
	 */
	@Override
	protected String getDefaultPaginationUrl() {
		return "/admin/usuarios";
	}

	/**
	 * Incluye en el modelo los parametros por defecto de la administracion de
	 * usuarios/grupos
	 * 
	 * @param model
	 * @param update
	 *            si actualiza los parametros actualizables
	 */
	protected void copyDefaultModel(boolean update, Model model) {
		// model.addAttribute("grupoSeleccionado", grupoSeleccionado);
		// model.addAttribute("grupos", getGrupos(update));
		// model.addAttribute("adminGroup", getAdminGroup(update));
		calculatePagination(model);
		model.addAttribute("usuarios", usuarios);
	}

	@Override
	protected String getAllSubTabs() {
		return TabsByModule.GENERAL_SUBTABS;
	}

	@Override
	protected int getSelectedSubTab() {
		// indice 0 dentro de GENERAL_SUBTABS
		return 0;
	}

	@Override
	protected String getActiveModule() {
		return MODULE;
	}

	@Override
	protected String getActiveSubModule() {
		return SUB_MODULE;
	}

}
