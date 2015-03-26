package com.emergya.ohiggins.web;

import com.emergya.ohiggins.config.WorkspaceNamesConfig;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.util.Date;
import java.util.Map;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.emergya.ohiggins.dto.AuthorityDto;
import com.emergya.ohiggins.dto.LayerResourceDto;
import com.emergya.ohiggins.dto.LayerTypeDto;
import com.emergya.ohiggins.dto.UsuarioDto;
import com.emergya.ohiggins.service.InstitucionService;
import com.emergya.ohiggins.service.LayerResourceService;
import com.emergya.ohiggins.service.LayerTypeService;
import com.emergya.ohiggins.service.UserAdminService;
import com.emergya.persistenceGeo.service.GeoserverService;
import com.emergya.persistenceGeo.utils.GeoserverUtils;
import com.emergya.persistenceGeo.utils.GsRestApiConfiguration;
import com.google.common.collect.Maps;
import javax.annotation.Resource;

/**
 * Controller del proceso de subida e importación de archivos rasters.
 *
 * @author marcos
 *
 */
@Controller
public class RasterUploadController {

    private final static Log LOG = LogFactory
            .getLog(RasterUploadController.class);

    private final static String RESULTS = "results";
    private final static String ROOT = "data";
    private final static String SUCCESS = "success";
    private final static String LAYER_TYPE_GEOTIFF = "geotiff";
    private final static String LAYER_TYPE_IMAGE_WORLD = "imageworld";
    private final static String LAYER_TYPE_IMAGE_MOSAIC = "imagemosaic";

    @Resource
    private WorkspaceNamesConfig workspaceNamesConfig;

    public static enum FILE_TYPE_ENUM {

        GEOTIFF("geotiff"), IMAGE_WORLD("imageworld"), IMAGE_MOSAIC(
                "imagemosaic");
        private String name;

        private FILE_TYPE_ENUM(String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return this.name;
        }

    };

    @Autowired
    private GeoserverService geoserverService;
    @Autowired
    private UserAdminService userAdminService;
    @Autowired
    private LayerResourceService layerResourceService;
    @Autowired
    private LayerTypeService layerTypeService;
    @Autowired
    private InstitucionService institucionService;
    @Autowired
    private GsRestApiConfiguration gsConfiguration;

    @RequestMapping(value = "/uploadRasterFile", method = RequestMethod.POST)
    public @ResponseBody
    String uploadRasterFile(@RequestParam("name") String layerTitle,
            @RequestParam("nativeCRS") String crs,
            @RequestParam("file") MultipartFile uploadFile,
            @RequestParam("fileType") FILE_TYPE_ENUM fileType) {
        if (LOG.isInfoEnabled()) {
            LOG.info("Importando archivo raster de tipo " + fileType);
        }

        LayerResourceDto dto = new LayerResourceDto();
        String anonymousWSName = workspaceNamesConfig.getTmpWorkspace();
        String userName = null;
        File imageFile = null;

        // We create a unique sanitized name for the geoserver layer.
        String layerName = layerResourceService.generateNameNotYetUsed(
                GeoserverUtils.createName(layerTitle));

        // We clean quotes that break json response due to Ext stupidity. Fix for #83421.
        layerTitle = layerTitle.replaceAll("\"", "''");

        try {
            // This shouldn't happen as we create an unique name, but still...
            if (geoserverService.existsLayerInWorkspace(layerName, anonymousWSName)) {
                return prepareErrorResponse(
                        "6",
                        "Ya existe una capa con ese nombre. Por favor elija otro nombre.",
                        "uploadRasterFile. El nombre de la capa ya está en uso en el workspace");
            }
            // comprobamos que se ha recibido un fichero
            if (uploadFile == null) {
                return prepareErrorResponse("1",
                        "No se ha recibido ningún archivo",
                        "uploadRasterFile. No se ha recibido ningún archivo");
            }
            // Comprobamos que el archivo no sea mayor de 30 MB
            if (uploadFile.getSize() > 30000000) {
                return prepareErrorResponse(
                        "2",
                        "El tamaño del archivo recibido no es correcto, debe ser menor de 30 Mb.",
                        "uploadRasterFile. Archivo de tamaño excesivo");
            }

			// Si el archivo es un WorldImage o un ImageMosaic comprobamos que
            // el archivo recibido sea un ZIP
            if (fileType == FILE_TYPE_ENUM.IMAGE_MOSAIC
                    || fileType == FILE_TYPE_ENUM.IMAGE_WORLD) {
                InputStream is = uploadFile.getInputStream();
                boolean isZip = com.emergya.persistenceGeo.utils.FileUtils
                        .checkIfInputStreamIsZip(is);
                is.close();
                if (!isZip) {
                    return prepareErrorResponse(
                            "3",
                            "El archivo indicado no es un archivo ZIP. Por favor seleccione un archivo ZIP.",
                            "uploadRasterFile. El nombre de la capa ya está en uso en el workspace");

                }
            }
			// Comprobamos si el GeoTiff es correcto
            // if (!isGeotiff(uploadFile)) {
            // return prepareErrorResponse("1",
            // "El formato del archivo recibido no es correcto",
            // "uploadRasterFile. Archivo corrupto");
            // }
            // Publicamos capa en geoserver
            boolean creado = false;

            imageFile = createTempFile(uploadFile, fileType);
            creado = publishRasterFile(fileType, anonymousWSName,
                    layerName, imageFile, crs);

            if (!creado) {
                return prepareErrorResponse(
                        "10",
                        "No se ha podido crear la capa geográfica. ¿El fichero tiene el formato correcto?",
                        "publishGeoTiff. No se ha podido crear la capa "
                        + layerName + " en el workspace "
                        + anonymousWSName);
            } else {
                dto.setActive(true);
                dto.setCreateDate(new Date());
                dto.setUpdateDate(new Date());
                dto.setOriginalFileName(layerTitle);
                // We set the layer name
                dto.setTmpLayerName(layerName);
                dto.setWorkspaceName(anonymousWSName);
                LayerTypeDto layerType = layerTypeService
                        .getLayerTypeByName(fileType.toString());
                dto.setLayerType(layerType);
                UsuarioDto userDto = userAdminService
                        .obtenerUsuarioByUsername(userName);
                Long authorityID = null;
                AuthorityDto authority = null;
                if (userDto != null) {
                    authorityID = userDto.getAuthorityId();
                    authority = (AuthorityDto) institucionService
                            .getById(authorityID);
                }
                dto.setAuthority(authority);
                dto = (LayerResourceDto) layerResourceService.create(dto);
            }
        } catch (Exception exception) {
            LOG.error(exception);
        } finally {
            cleanupTempFile(imageFile);
        }
        return prepareSuccessResponse(anonymousWSName, layerName, layerTitle, dto);
    }

    private boolean publishRasterFile(FILE_TYPE_ENUM fileType,
            String workspaceName, String nameLayerPurged, File imageFile,
            String crs) {
        boolean creado = false;
        switch (fileType) {
            case GEOTIFF:
                creado = geoserverService.publishGeoTIFF(workspaceName,
                        nameLayerPurged, imageFile, crs);
                break;
            case IMAGE_MOSAIC:
                creado = geoserverService.publishImageMosaic(workspaceName,
                        nameLayerPurged, imageFile, crs);
                break;
            case IMAGE_WORLD:
                creado = geoserverService.publishWorldImage(workspaceName,
                        nameLayerPurged, imageFile, crs);
                break;
            default:
                break;
        }

        if (!creado) {
            return false;
        }

		// We are going to create and set a new style for the newly published
        // layer,
        // Basically, we are copying the default style (currently applied to the
        // published layer) into a new style named as the layer
        if (!geoserverService.copyLayerStyle(nameLayerPurged, nameLayerPurged)) {
            return false;
        }

		// We change the layer's style so its the new style we have just
        // created.
        if (!geoserverService.setLayerStyle(workspaceName, nameLayerPurged, nameLayerPurged)) {
            return false;
        }

        return true;
    }

    private File createTempFile(MultipartFile uploadFile,
            FILE_TYPE_ENUM fileType) throws IOException {
        if (fileType == null || uploadFile == null) {
            throw new IllegalArgumentException(
                    "uploadFile and fileType can not be null");
        }
        File imageFile = null;
        switch (fileType) {
            case GEOTIFF:
                imageFile = File.createTempFile(LAYER_TYPE_GEOTIFF, ".tiff");
                break;
            case IMAGE_MOSAIC:
                imageFile = File.createTempFile(LAYER_TYPE_IMAGE_MOSAIC, ".zip");
                break;
            case IMAGE_WORLD:
                imageFile = File.createTempFile(LAYER_TYPE_IMAGE_WORLD, ".zip");
                break;
            default:
                break;
        }

        uploadFile.transferTo(imageFile);
        return imageFile;
    }

    /**
     * Borra el archivo <code>rasterFile</code>
     *
     * @param rasterFile
     */
    private void cleanupTempFile(File rasterFile) {
        if (rasterFile != null) {
            FileUtils.deleteQuietly(rasterFile);
        }
    }

    /**
     * @return @throws IOException
     * @throws JsonGenerationException
     * @throws JsonMappingException
     */
    private String prepareErrorResponse(String codigo, String message,
            String logMessage) {
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, String> rootObject = Maps.newHashMap();
        ObjectMapper mapper = new ObjectMapper();
        rootObject.put("status", "error");
        rootObject.put("codigo", codigo);
        rootObject.put("message", StringEscapeUtils.escapeHtml4(message));

        if (LOG.isInfoEnabled()) {
            LOG.info(logMessage);
        }

        resultMap.put(ROOT, rootObject);
        resultMap.put(RESULTS, Integer.valueOf(1));
        resultMap.put(SUCCESS, true);
        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, resultMap);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }

    private String prepareSuccessResponse(String workspaceName,
            String layerName, String layerTitle, LayerResourceDto dto) {
        Map<String, Object> resultMap = Maps.newHashMap();
        Map<String, Object> rootObject = Maps.newHashMap();
        ObjectMapper mapper = new ObjectMapper();
        rootObject.put("status", "success");
        rootObject.put("layerName",
                StringEscapeUtils.escapeHtml4(workspaceName + ":" + layerName));
        rootObject.put("layerTitle", StringEscapeUtils.escapeHtml4(layerTitle));
        rootObject.put("layerResourceId", dto.getId());
        rootObject.put("layerTypeId", dto.getLayerType().getId());

        String geoserverRestUrl = gsConfiguration.getServerUrl();
        String geoserverUrl = geoserverRestUrl.replace("/rest", "") + "/wms";
        rootObject.put("serverUrl", geoserverUrl);

        resultMap.put(ROOT, rootObject);
        resultMap.put(RESULTS, Integer.valueOf(1));
        resultMap.put(SUCCESS, true);
        StringWriter sw = new StringWriter();
        try {
            mapper.writeValue(sw, resultMap);
        } catch (JsonGenerationException e) {
            e.printStackTrace();
        } catch (JsonMappingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sw.toString();
    }
}
