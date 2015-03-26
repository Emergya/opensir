/*
 * Copyright (C) 2013 Emergya
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */
package com.emergya.ohiggins.web.helper;

import com.emergya.email.sender.PropertiesEmail;
import com.emergya.email.sender.SendEmailUtils;
import com.emergya.ohiggins.config.WorkspaceNamesConfig;
import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.EstadoPublicacionLayerRequestPublish;
import com.emergya.ohiggins.dto.FolderDto;
import com.emergya.ohiggins.dto.LayerDto;
import com.emergya.ohiggins.dto.LayerMetadataDto;
import com.emergya.ohiggins.dto.LayerMetadataTmpDto;
import com.emergya.ohiggins.dto.LayerPropertyDto;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;
import com.emergya.ohiggins.service.FolderService;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerPublishRequestService;
import com.emergya.ohiggins.service.LayerService;
import com.emergya.ohiggins.web.util.Utils;
import com.emergya.ohiggins.web.validators.LayerPublishRequestDtoValidator;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.service.GeoserverService.DuplicationResult;
import com.emergya.persistenceGeo.utils.GeoserverUtils;
import com.google.common.base.Strings;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import javax.annotation.Resource;
import javax.servlet.ServletContext;
import javax.validation.Valid;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.validation.BindingResult;

/**
 *
 * @author lroman
 */
@Repository
public class PublicationRequestHelperImpl implements PublicationRequestHelper {

    private static Log LOG = LogFactory
            .getLog(PublicationRequestHelperImpl.class);
    private static final String UNASSIGNED_LAYERS_FOLDER = "Otros";
    @Resource
    private LayerService layerService;

    @Resource
    private WorkspaceNamesConfig workspaceNamesConfig;

    @Resource
    private LayerPublishRequestService layerPublishRequestService;
    @Resource
    private FolderService folderService;
    @Resource
    private InstitucionService authorityService;
    @Resource
    private GeoserverService geoserverService;

    @Override
    public RejectRequestResult rejectPublicationRequest(Long publicationRequestId, String comment) {

        Date hoy = new Date();
        LayerPublishRequestDto requestDto
                = (LayerPublishRequestDto) layerPublishRequestService.getById(publicationRequestId);

        // Comentario y estado
        requestDto.setComentario(comment);
        requestDto.setFechaactualizacion(hoy);
        requestDto.setFecharespuesta(hoy);
        requestDto.setEstado(EstadoPublicacionLayerRequestPublish.RECHAZADA
                .toString());

        if (!geoserverService.deleteGeoServerLayer(
                workspaceNamesConfig.getRequestsWorspace(),
                requestDto.getTmpLayerName(),
                requestDto.getSourceLayerType().getNameAndTipo(),
                requestDto.getTmpLayerName())) {
            return RejectRequestResult.FAILURE;
        }

        layerPublishRequestService.update(requestDto);

        boolean enviado = sendUserMail(requestDto);

        if (enviado || requestDto.getUser() == null) {
            // We don't complain if the user was deleted.
            return RejectRequestResult.SUCCESS;
        } else {
            return RejectRequestResult.SUCCESS_NO_MAIL;
        }
    }
    //Comment autowired to testit!!
    @Autowired
    private ServletContext servletContext;

    /**
     * Envia email informado del rechazo de la solicitud de publicacion de capa al usuario
     *
     * @param layerRequestPublish
     */
    private boolean sendUserMail(
            LayerPublishRequestDto layerRequestPublish) {

        boolean enviado;

        try {
            // 1: Obtener fichero propiedades
            Properties p = Utils.getPropertiesEmailApplication();

            // 2: Properties email
            PropertiesEmail props = new PropertiesEmail();
            props = Utils.inicializaServidorEmail(props, p);// Valores iniciales
            props.setMAIL_ISSUE_VALUE(Utils.ASUNTO_SOLCITUD_PUBLICACION_CAPA);

            // 3: Plantilla envio correo
            String rutaPlantilla
                    = servletContext
                    .getRealPath(
                            Utils.DIRECTORIO_TEMPLATES
                            + Utils.TEMPLATE_ENVIO_MENSAJE_SOLICITUD_PUBLICACION_CAPA);
            // 4: Enviar
            // send email
            Map<String, String> parametrosPlantilla = new HashMap<String, String>();
            String nombre = layerRequestPublish.getUser() != null ? layerRequestPublish
                    .getUser().getNombreCompleto() : new String();
            String email = layerRequestPublish.getUser() != null ? layerRequestPublish
                    .getUser().getEmail() : new String();

            parametrosPlantilla.put("[\\$]NOMBRE", nombre);
            parametrosPlantilla.put("[\\$]ESTADO",
                    EstadoPublicacionLayerRequestPublish.RECHAZADA.toString());
            parametrosPlantilla.put("[\\$]ESTUDIO",
                    layerRequestPublish.getNombredeseado());
            parametrosPlantilla.put("[\\$]INSTITUCION", layerRequestPublish
                    .getAuth().getAuthority());

            String message = Utils.generaMensajeEnviar(rutaPlantilla,
                    parametrosPlantilla);

            // Sender email user single
            enviado = SendEmailUtils.sendEmailUser(props, message, nombre, email);

        } catch (Exception e) {
            LOG.info("Error al carga el fichero de propiedades:");
            enviado = false;
        }

        return enviado;

    }

    @Override
    public SaveRequestResult savePublicationRequest(
            LayerPublishRequestDto requestDto,
            boolean validateMetadata,
            BindingResult errors) {
        // We use it as an output parameter for the preSaveRequest method.
        PreSaveRequestData preSaveRequestData = new PreSaveRequestData();

        SaveRequestResult preSaveResult = preSaveLayerPublishRequest(requestDto, validateMetadata, preSaveRequestData, errors);

        if (!preSaveResult.isSuccess()) {
            return preSaveResult;
        }

        AuthorityDto institucion = preSaveRequestData.institucion;
        boolean editing = preSaveRequestData.editing;
        LayerDto sourceLayer = preSaveRequestData.sourceLayer;
        // Layer is duplicated in GeoServer
        DuplicationResult duplicationResult = geoserverService.duplicateGeoServerLayer(
                institucion.getWorkspaceName(),
                sourceLayer.getType().getNameAndTipo(),
                sourceLayer.getNameWithoutWorkspace(),
                sourceLayer.getTableName(),
                workspaceNamesConfig.getRequestsWorspace(),
                requestDto.getTmpLayerName(),
                requestDto.getNombredeseado());

        if (duplicationResult == GeoserverService.DuplicationResult.FAILURE) {
            return SaveRequestResult.FAILURE_GEOSERVER_ERROR;
        }

        if (editing) {
            layerPublishRequestService.update(requestDto);
            return SaveRequestResult.SUCCESS_REQUEST_MODIFICATION;
        } else {
            // We save the new entity in the database.
            layerPublishRequestService.create(requestDto);
            return SaveRequestResult.SUCCESS_REQUEST_CREATION;
        }
    }

    /**
     * Encapsulate common code for the old method and use it in the new method
     *
     * @param layerPublishRequestDto
     * @param result
     * @param model
     * @param request
     * @param webRequest
     * @param sourceLayer
     *
     * @return PreSaveLayerPublishRequestResult
     */
    private SaveRequestResult preSaveLayerPublishRequest(
            @Valid LayerPublishRequestDto layerPublishRequestDto,
            boolean validateMetadata,
            // Output parameters:
            PreSaveRequestData preSaveData, BindingResult errors) {

        // We load the layer first as we need it for validation.
        LayerDto sourceLayer = (LayerDto) layerService.getById(layerPublishRequestDto.getSourceLayerId());
        if (sourceLayer == null) {
            return SaveRequestResult.FAILURE_SOURCE_LAYER_MISSING;
        }

        layerPublishRequestDto.setSourceLayerName(sourceLayer.getLayerLabel());
        layerPublishRequestDto.setSourceLayerType(sourceLayer.getType());
        layerPublishRequestDto.setTipoCapaSeleccionada(sourceLayer.getType().getNameAndTipo());

        // Validation
        LayerPublishRequestDtoValidator validator = new LayerPublishRequestDtoValidator(validateMetadata);

        validator.validate(layerPublishRequestDto, errors);

        if (errors.hasErrors()) {
            return SaveRequestResult.FAILURE_VALIDATION_ERRORS;
        }

        Date now = new Date();

        // Recogemos la informacion de user, layer y autoridad
        AuthorityDto institucion = (AuthorityDto) authorityService.getById(layerPublishRequestDto.getAuth().getId());

        // Comprobobamos que no hay una solucitud de publicación para la capa
        // por parte de la autoridad del usuario actual.
        if (layerPublishRequestService.existsRequest(institucion, sourceLayer,
                layerPublishRequestDto.getId())) {
            return SaveRequestResult.FAILURE_EXISTING_REQUEST;
        }

        layerPublishRequestDto.setAuth(institucion);

        if (layerPublishRequestDto.isUpdate()
                && Strings.isNullOrEmpty(layerPublishRequestDto.getNombredeseado())) {
            // We are updating a published layer, we use the name in the publication request
            // for display purposes.	    
            LayerDto updatedLayer = (LayerDto) layerService
                    .getById(layerPublishRequestDto.getUpdatedLayerId());
            layerPublishRequestDto.setNombredeseado(updatedLayer
                    .getLayerLabel());
        }

        // We create a  unique geoserver layer name for the publication request..
        String layerName;

        do {
            layerName = GeoserverUtils
                    .createUniqueName(layerPublishRequestDto.getNombredeseado());
        } while (geoserverService.existsLayerInWorkspace(
                layerName,
                workspaceNamesConfig.getRequestsWorspace()));

        // We save the tmp layer name so we can delete it if the publication
        // is not accepted.
        layerPublishRequestDto.setTmpLayerName(layerName);

        LayerMetadataTmpDto metadatos = layerPublishRequestDto.getMetadata();

        boolean editing = layerPublishRequestDto.getId() != null;

        if (editing) {
            // Actualizamos una solicitud de publicación
            LayerPublishRequestDto updatedRequest = (LayerPublishRequestDto) layerPublishRequestService
                    .getById(layerPublishRequestDto.getId());
            layerPublishRequestDto
                    .setEstado(EstadoPublicacionLayerRequestPublish.PENDIENTE
                            .toString());

            layerPublishRequestDto
                    .setFecharespuesta(updatedRequest.getFecharespuesta());
            layerPublishRequestDto
                    .setFechasolicitud(updatedRequest.getFechasolicitud());
            layerPublishRequestDto.setRecursoservidor(updatedRequest
                    .getRecursoservidor());
            layerPublishRequestDto.setFechacreacion(updatedRequest.getFechacreacion());
            layerPublishRequestDto
                    .setFechasolicitud(updatedRequest.getFechasolicitud());

            layerPublishRequestDto.setFechaactualizacion(now);
            metadatos.setFechaactualizacion(now);

        } else {
            // Nueva solicitud
            layerPublishRequestDto
                    .setEstado(EstadoPublicacionLayerRequestPublish.PENDIENTE
                            .toString());
            layerPublishRequestDto.setFechacreacion(now);
            layerPublishRequestDto.setFechasolicitud(now);
            metadatos.setFechacreacion(now);

            layerPublishRequestDto.setRecursoservidor(sourceLayer
                    .getServer_resource());
        }

        layerPublishRequestDto.setMetadata(metadatos);

        List<LayerPropertyDto> requestProperties = new ArrayList<LayerPropertyDto>();
        // We copy the source layer's properties.
        if (sourceLayer.getProperties() != null) {
            for (LayerPropertyDto propDto : sourceLayer.getProperties()) {
                LayerPropertyDto requestProp = propDto.clone();
                // We set the id to null so the entities are created new when
                // persisting.
                requestProp.setId(null);
                requestProperties.add(requestProp);
            }
        }

        // If we already have properties in the publication request, we keep them.
        if (layerPublishRequestDto.getProperties() != null) {
            layerPublishRequestDto.getProperties().addAll(requestProperties);
        } else {

            layerPublishRequestDto.setProperties(requestProperties);
        }

        preSaveData.institucion = institucion;
        preSaveData.editing = editing;
        preSaveData.sourceLayer = sourceLayer;

        // Not actually create, but we need to return a non fail value here,
        // and SUCCESS_CREATE is.
        return SaveRequestResult.SUCCESS_REQUEST_CREATION;
    }

    @Override
    public ConfirmRequestResult confirmPublicationRequest(LayerPublishRequestDto requestFormDto) {
        Date hoy = new Date();
        String accion = requestFormDto.getAccionEjecutar();

        LayerPublishRequestDto existingRequest = (LayerPublishRequestDto) layerPublishRequestService
                .getById(requestFormDto.getId());

        if (existingRequest == null || existingRequest.getId() == null
                || accion == null) {
            return new ConfirmRequestResult(ConfirmRequestResultStatus.FAILURE_NO_REQUEST);
        }

        boolean publishAsNew = LayerPublishRequestDto.ACTION_PUBLISH_NEW.equals(accion);

        // Existing request is marked as accepted.
        existingRequest.setEstado(EstadoPublicacionLayerRequestPublish.ACEPTADA.toString());

        // Por defecto la capa no tiene carpeta (se verá en la capa virtual "otros");
        FolderDto folder = null;

        if (!StringUtils.isBlank(requestFormDto.getCarpeta())) {
            // El usuario ha seleccionado una carpeta, así que la cargamos.
            Long folderId = Long.decode(requestFormDto.getCarpeta());
            folder = (FolderDto) folderService.getById(folderId);
        }

        // Tratamos los metadatos
        LayerMetadataTmpDto metadataTmp = existingRequest.getMetadata();
        LayerMetadataDto metadata = LayerMetadataDto.fromTmpMetadata(metadataTmp);

        // We clean the id so we persist it as new.
        metadata.setId(null);

        // Setting the id to null will force the persistence of
        // the properties as new properties for the layer being
        // created/updated.
        List<LayerPropertyDto> properties = existingRequest.getProperties();
        List<LayerPropertyDto> newProperties = new ArrayList<LayerPropertyDto>();
        if (properties != null) {
            // Shouldn't be null, but still...
            for (LayerPropertyDto propDto : existingRequest.getProperties()) {
                LayerPropertyDto newPropDto = propDto.clone();
                newPropDto.setId(null);
                // We add a property so the published layer is tiled and cached.
                newProperties.add(newPropDto);
            }
        }
        // We add a property so the published layer is tiled and cached.
        newProperties.add(new LayerPropertyDto("tiled", "true"));

        ConfirmRequestResult result;
        if (publishAsNew) {
            result = publishNewLayer(requestFormDto, existingRequest, folder, newProperties, metadata);
        } else {
            result = publishUpdatedLayer(requestFormDto, existingRequest, newProperties, metadata);
        }

        if (!result.isSuccess()) {
            // There was some error.
            return result;
        }

        // Actualizamos la solicitud
        existingRequest.setFechaactualizacion(hoy);
        existingRequest.setNombredeseado(requestFormDto.getNombredeseado());

        layerPublishRequestService.update(existingRequest);

        if (!removeRequestLayerFromGeoserver(existingRequest)) {
            return new ConfirmRequestResult(ConfirmRequestResultStatus.SUCCESS_COULDNT_CLEAN_GEOSERVER, result.getPublishedLayer());
        }

        return result;

    }

    private boolean removeRequestLayerFromGeoserver(
            LayerPublishRequestDto requestDto) {
        return geoserverService.deleteGeoServerLayer(
                workspaceNamesConfig.getRequestsWorspace(),
                requestDto.getTmpLayerName(),
                requestDto.getSourceLayerType().getNameAndTipo(),
                requestDto.getTmpLayerName());
    }

    private DuplicationResult addLayerInGeoServer(LayerDto newLayer, LayerPublishRequestDto requestDto) {

        return geoserverService.duplicateGeoServerLayer(
                workspaceNamesConfig.getRequestsWorspace(),
                requestDto.getSourceLayerType().getNameAndTipo(),
                requestDto.getTmpLayerName(),
                requestDto.getTmpLayerName(),
                workspaceNamesConfig.getPublicWorkspaceName(),
                newLayer.getNameWithoutWorkspace(),
                newLayer.getLayerTitle());
    }

    private boolean updateLayerInGeoServer(
            LayerDto layerActualizar,
            LayerPublishRequestDto existingRequest) {

        if (StringUtils.isEmpty(existingRequest.getTmpLayerName())) {
            // If we dont have a tmp layer name the layer is remote and we don't need
            // to do anything in database or geoserver.
            return true;
        }

        // The current geoserver layer is deleted.
        if (!geoserverService.deleteGeoServerLayer(
                workspaceNamesConfig.getPublicWorkspaceName(),
                layerActualizar.getNameWithoutWorkspace(),
                layerActualizar.getType().getNameAndTipo(),
                layerActualizar.getTableName())) {
            return false;
        }

        // We add the layer again.
        return this.addLayerInGeoServer(layerActualizar, existingRequest)
                != DuplicationResult.FAILURE;

    }

    private ConfirmRequestResult publishNewLayer(
            LayerPublishRequestDto requestFormDto,
            LayerPublishRequestDto existingRequest,
            FolderDto folder,
            List<LayerPropertyDto> properties,
            LayerMetadataDto metadata) {

        LayerDto layerNew = new LayerDto();

        // Establecemos el id a null para que sea nuevo.
        layerNew.setId(null);
        layerNew.setType(existingRequest.getSourceLayerType());
        metadata.setFechacreacion(new Date());
        layerNew.setMetadata(metadata);
        layerNew.setCreateDate(new Date());
        layerNew.setUpdateDate(new Date());
        layerNew.setFolder(folder);
        layerNew.setEnabled(true);
        layerNew.setIsChannel(true);
        layerNew.setPublicized(true);

        // We set the server's resource.
        layerNew.setServer_resource(existingRequest.getRecursoservidor());

        String publicWorkspace = workspaceNamesConfig.getPublicWorkspaceName();

        // Establecemos el nombre de la capa.
        String sanitizedName;
        do {
            sanitizedName = GeoserverUtils.createUniqueName(requestFormDto.getNombredeseado());
        } while (geoserverService.existsLayerInWorkspace(sanitizedName, publicWorkspace));

        layerNew.setName(String.format("%s:%s", publicWorkspace, sanitizedName));
        layerNew.setLayerTitle(requestFormDto.getNombredeseado());

        // Establecemos la authority que solicitó la publicación para que se
        // pueda identificar
        // para las actualizaciones.
        layerNew.setRequestedByAuth(existingRequest.getAuth());

        // We set the properties of the layer. As the ids have been
        // nullified, we will persist new entities.
        layerNew.setProperties(properties);
        // Añadimos la capa y sus datos a geoserver.
        DuplicationResult duplicationResult
                = addLayerInGeoServer(layerNew, existingRequest);
        if (duplicationResult == DuplicationResult.FAILURE) {
            return new ConfirmRequestResult(ConfirmRequestResultStatus.FAILURE_GEOSERVER_PUBLISH);
        } else if (duplicationResult == DuplicationResult.SUCCESS_VECTORIAL) {
            // We save the table name for vectorial layers.
            layerNew.setTableName(sanitizedName);
        }

        existingRequest.setPublishedFolder(folder != null ? folder.getName() : UNASSIGNED_LAYERS_FOLDER);

        layerService.create(layerNew);

        return new ConfirmRequestResult(ConfirmRequestResultStatus.SUCCESS, layerNew);
    }

    private ConfirmRequestResult publishUpdatedLayer(
            LayerPublishRequestDto requestFormDto, LayerPublishRequestDto existingRequest,
            List<LayerPropertyDto> properties, LayerMetadataDto metadata) {
        // Actualizar
        Long layerId = requestFormDto.getUpdatedLayerId();
        LayerDto layerActualizar = (LayerDto) layerService.getById(layerId);
        if (layerActualizar == null) {
            return new ConfirmRequestResult(ConfirmRequestResultStatus.FAILURE_MISSING_UPDATED_LAYER);
        }

        if (!Strings.isNullOrEmpty(requestFormDto.getNombredeseado())) {
            // We allow changing the layer name if we recieve a non blank value.
            layerActualizar.setLayerTitle(requestFormDto.getNombredeseado());
        }

        layerActualizar.setUpdateDate(new Date());
        layerActualizar.setPublicized(true);

        // Establecemos los metadatos actualizados.
        layerActualizar.setMetadata(metadata);
        layerActualizar.setRequestedByAuth(existingRequest.getAuth());
        layerActualizar.setServer_resource(existingRequest
                .getRecursoservidor());

        // We set the properties of the layer. As the ids have been
        // nullified, we will persist new entities.
        layerActualizar.setProperties(properties);

        if (!updateLayerInGeoServer(layerActualizar, existingRequest)) {
            return new ConfirmRequestResult(ConfirmRequestResultStatus.FAILURE_GEOSERVER_LAYER_UPDATE);
        }

        // We inform the publication request with the updated layer's folder name.
        existingRequest.setPublishedFolder(layerActualizar.getFolder() != null ? layerActualizar.getFolder().getName() : UNASSIGNED_LAYERS_FOLDER);

        layerService.update(layerActualizar);

        return new ConfirmRequestResult(ConfirmRequestResultStatus.SUCCESS, layerActualizar);
    }

    /**
     * Wraps of preSaveLayerPublishRequest method result
     *
     * @author adiaz
     *
     */
    class PreSaveRequestData {

        AuthorityDto institucion;
        boolean editing;
        LayerDto sourceLayer;
        SaveRequestResult result;
    }
}
