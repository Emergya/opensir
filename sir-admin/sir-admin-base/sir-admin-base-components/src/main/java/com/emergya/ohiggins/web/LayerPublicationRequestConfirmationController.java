/*
 * SolicitudesPublicacionCapasAdminController
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.web;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.ModelAndView;

import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.emergya.ohiggins.dto.NodeLayerTree;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.FolderService;
import com.emergya.ohiggins.service.LayerPublishRequestService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.ohiggins.web.helper.PublicationRequestHelper;
import static com.emergya.ohiggins.web.helper.PublicationRequestHelper.ConfirmRequestResultStatus.FAILURE_GEOSERVER_LAYER_UPDATE;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Controller that handles confirmation or rejection of layer publication requests.
 *
 * @author jariera
 *
 */
@Controller
public class LayerPublicationRequestConfirmationController extends AbstractController {

    /**
     *
     */
    private static final long serialVersionUID = -9064198565598936131L;
    private static Log LOG = LogFactory
            .getLog(LayerPublicationRequestConfirmationController.class);
    public final static String MODULE = "cartografico";
    public final static String SUB_MODULE = "solicitudesPublicacion";
    private List<LayerPublishRequestDto> layerPublish = null;
    public static final String LAYERTREE = "layerTree";
    public static final int TAM_DEFAULT = 10000000;
    // Resources Services
    @Resource
    private LayerService layerService;
    @Resource
    private UserAdminService userAdminService;
    @Resource
    private LayerPublishRequestService layerPublishRequestService;
    @Resource
    private FolderService folderService;
    @Resource
    private PublicationRequestHelper publicationRequestHelper;

    /**
     * Solicitud de publicaciones pendientes para administrador
     *
     * @param model
     *
     * @return
     */
    @RequestMapping(value = "/admin/cartografico/solicitudesPublicacion")
    @SuppressWarnings({"rawtypes"})
    public String solicitudesPublicacion(
            @RequestParam(defaultValue = "10000000") int size,
            @RequestParam(defaultValue = "1") int page, Model model,
            WebRequest webRequest) {

        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        String username = getCurrentUsername(webRequest);
        UsuarioDto userLogado = userAdminService
                .obtenerUsuarioByUsername(username);

        calculatePagination(model);

        // Lista de solicitudes de capas pendientes del administrador
        List result = (List<LayerPublishRequestDto>) layerPublishRequestService
                .getFromToOrderByAdmin(0, Integer.MAX_VALUE,
                        LayerPublishRequestDto.NAME_PROPERTY,
                        userLogado.getId());

        model.addAttribute("layerPublish", result);

        rolUser(model);
        model.addAttribute("module", getActiveModule());
        model.addAttribute("submodule", getActiveSubModule());

        return "admin/cartografico/solicitudesPublicacion";
    }

    /**
     * Muestra los metadatos
     *
     * @param webRequest
     * @param request
     * @param model
     * @param response
     * @param ident
     * @param estado
     * @param action
     * @return
     */
    @RequestMapping(value = "/admin/cartografico/mostrarMetadatos/{estado}/{ident}", method = {
        RequestMethod.GET, RequestMethod.POST})
    public ModelAndView mostrarMetadatosSolicitudPublicacion(
            WebRequest webRequest, HttpServletRequest request, Model model,
            HttpServletResponse response, @PathVariable("ident") String ident,
            @PathVariable("estado") String estado) {

        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);
        rolUser(model);

        // Vista de retorno
        ModelAndView modelAndView = new ModelAndView(
                "admin/cartografico/metadatos");

        // Obtenemos la capa de publicacion
        LayerPublishRequestDto p = (LayerPublishRequestDto) layerPublishRequestService
                .getById(Long.decode(ident));

        // Añadimos la variable al modelo
        model.addAttribute("layerPublishRequestDto", p);

        model.addAttribute("module", getActiveModule());
        model.addAttribute("submodule", getActiveSubModule());

        return modelAndView;
    }

    /**
     * Obtiene el usuario logado
     *
     * @param webRequest
     * @return
     */
    private String getCurrentUsername(WebRequest webRequest) {
        SecurityContext context = (SecurityContext) webRequest.getAttribute(
                "SPRING_SECURITY_CONTEXT", RequestAttributes.SCOPE_SESSION);

        if (context != null && context.getAuthentication().isAuthenticated()
                && !context.getAuthentication().getAuthorities().isEmpty()) {
            return context.getAuthentication().getName();
        }
        return null;
    }

    /**
     * Rol del usuario conectado
     *
     * @param model
     */
    private void rolUser(Model model) {
        model.addAttribute("IS_ADMIN",
                OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
    }

    @Override
    protected void copyDefaultModel(boolean update, Model model) {
        calculatePagination(model);
        model.addAttribute("layerPublish", layerPublish);

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
        return "/admin/cartografico/solicitudesPublicacion";
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
     * Muestra el formulario con el estado de la solicitud de la publicacion de capa
     *
     * @param webRequest
     * @param request
     * @param model
     * @param response
     * @param ident
     * @param estado
     * @return
     */
    @RequestMapping(value = "/admin/cartografico/rechazarPublicacion/{ident}")
    public ModelAndView denegarPublicacion(WebRequest webRequest,
            HttpServletRequest request, Model model,
            HttpServletResponse response, @PathVariable("ident") String ident) {

        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        LayerPublishRequestDto layerRequest = (LayerPublishRequestDto) layerPublishRequestService
                .getById(Long.decode(ident));

        // Vista de retorno
        ModelAndView modelAndView = new ModelAndView(
                "admin/cartografico/denegar");
        model.addAttribute("cargaDatos", true);
        model.addAttribute("layerPublishRequestDto", layerRequest);

        return modelAndView;
    }

    /**
     * Guarda los comentarios realizados por el admin al rechazar la solicitud de publicacion
     *
     * @param layerPublishRequestDto
     * @param webRequest
     * @param request
     * @param model
     * @param response
     * @return
     */
    @RequestMapping(value = "/admin/cartografico/rechazarPublicacionRequestLayer")
    public String rechazarPublicacionRequestLayer(
            LayerPublishRequestDto layerPublishRequestDto,
            WebRequest webRequest, HttpServletRequest request, Model model,
            HttpServletResponse response) {

        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        PublicationRequestHelper.RejectRequestResult rejectPublicationResult
                = publicationRequestHelper.rejectPublicationRequest(layerPublishRequestDto.getId(), layerPublishRequestDto.getComentario());

        switch (rejectPublicationResult) {
            case FAILURE:
                return requestReturn(
                        "redirect:/admin/cartografico/solicitudesPublicacion",
                        "Ocurrió un error al eliminar la capa de GeoServer");
            case SUCCESS_NO_MAIL:
                return requestReturn("redirect:/admin/cartografico/solicitudesPublicacion",
                        "La solicitud se rechazó con éxito pero no se pudo notificar por correo al usuario.");
            case SUCCESS:
                return requestReturn(
                        "redirect:/admin/cartografico/solicitudesPublicacion", "success");
            default:
                throw new IllegalStateException("Unhandled rejection result state '" + rejectPublicationResult + "'!");
        }
    }

    /**
     * Muestra el formulario para la publicacion de capa
     *
     * @param webRequest
     * @param request
     * @param model
     * @param response
     * @param ident
     * @param estado
     * @return
     */
    @RequestMapping(value = "/admin/cartografico/publicarLayer/{ident}")
    public ModelAndView publicarPublicacion(WebRequest webRequest,
            HttpServletRequest request, Model model,
            HttpServletResponse response, @PathVariable("ident") String ident) {

        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

	// Usuario autenticado
        // Authentication u = getAuthentication(webRequest);
        LayerPublishRequestDto layerRequest = (LayerPublishRequestDto) layerPublishRequestService
                .getById(Long.decode(ident));

        // Vista de retorno
        ModelAndView modelAndView = new ModelAndView(
                "admin/cartografico/publicar");
        model.addAttribute("cargaDatos", true);

        // Seleccionamos la acción inicial a partir de los datos publicados.
        String defaultAction = LayerPublishRequestDto.ACTION_PUBLISH_NEW;
        if (layerRequest.isUpdate()) {

            LayerDto updatedLayer = (LayerDto) layerService.getById(layerRequest.getUpdatedLayerId());
            if (updatedLayer == null) {
		// The layer that the requesting user wanted to update has been deleted,
                // so we will show the dialog as if the request were for a new publication, but
                // informing the user.
                model.addAttribute("updatedLayerDeleted", true);
            } else {
                defaultAction = LayerPublishRequestDto.ACTION_PUBLISH_UPDATE;
                layerRequest.setNombredeseado(null);

		// We set the initial folder to the folder in which the updated
                // layer is in.
                FolderDto folder = updatedLayer.getFolder();
                if (folder != null) {
                    layerRequest.setCarpeta(folder.getId().toString());
                }
            }
        }
        layerRequest.setAccionEjecutar(defaultAction);

        model.addAttribute("layerPublishRequestDto", layerRequest);

        // Arbol de capas.
        List<NodeLayerTree> layerTree = folderService.getArbolCapas((1 - 1)
                * TAM_DEFAULT, 1 * TAM_DEFAULT, LayerDto.NAME_PROPERTY);
        model.addAttribute(LAYERTREE, layerTree);

	// Obtenemos lista de capas del mismo tipo (ráster o vectorial) ya
        // publicadas anteriormente por la institución.
        List<com.emergya.ohiggins.dto.LayerDto> layersActualizar = layerService
                .getLayersPubliciedByAuthIdByTypeId(layerRequest.getAuth()
                        .getId(), layerRequest.getSourceLayerType().getId());
        model.addAttribute("layersActualizar", layersActualizar);

        return modelAndView;
    }

    /**
     * Publica por parte del administrador la solicitud de capa del usuario
     *
     * @param webRequest
     * @param request
     * @param model
     * @param response
     * @param ident
     * @param estado
     * @return
     */
    @RequestMapping(value = "/admin/cartografico/publicarRequestLayer")
    public String publicarRequestLayer(
            @Valid LayerPublishRequestDto layerPublishRequestDto,
            BindingResult result, WebRequest webRequest,
            HttpServletRequest request, Model model,
            HttpServletResponse response) {

        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        if (layerPublishRequestDto.getAccionEjecutar().equals(LayerPublishRequestDto.ACTION_PUBLISH_UPDATE)) {
            // We want to keep the updated layer's title.
            layerPublishRequestDto.setNombredeseado(null);
        }

        PublicationRequestHelper.ConfirmRequestResult confirmationResult
                = publicationRequestHelper.confirmPublicationRequest(layerPublishRequestDto);

        switch (confirmationResult.getStatus()) {
            case FAILURE_NO_REQUEST:
                return requestReturn("redirect:/admin/cartografico/solicitudesPublicacion",
                        "Ocurrió un error al publicar.");
            case FAILURE_GEOSERVER_PUBLISH:
            case FAILURE_GEOSERVER_LAYER_UPDATE:
                return requestReturn("redirect:/admin/cartografico/solicitudesPublicacion",
                        "Ocurrió un error publicando la capa.");
            case FAILURE_MISSING_UPDATED_LAYER:
                return requestReturn("redirect:/admin/cartografico/solicitudesPublicacion",
                        "No se puede publicar la actualización, la capa a actualizar fue borrada.");
            case SUCCESS_COULDNT_CLEAN_GEOSERVER:
                return requestReturn("redirect:/admin/cartografico/solicitudesPublicacion",
                        "La capa se publicó con éxito pero ocurrió un error borrando los datos de la petición.");
            case SUCCESS:
                return requestReturn("redirect:/admin/cartografico/solicitudesPublicacion", "success");
            default:
                throw new IllegalStateException("No hanler for '" + confirmationResult + "' confirmation result type!");
        }
    }
}
