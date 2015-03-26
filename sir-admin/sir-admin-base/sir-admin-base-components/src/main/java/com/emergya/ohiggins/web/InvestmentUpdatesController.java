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
package com.emergya.ohiggins.web;

import com.emergya.ohiggins.dao.impl.CustomSqlException;
import com.emergya.ohiggins.dto.FileColumnDto;
import com.emergya.ohiggins.dto.FileTypeDto;
import com.emergya.ohiggins.dto.InversionUpdateDto;
import com.emergya.ohiggins.exceptions.NotAllRequiredColumnsFound;
import com.emergya.ohiggins.importers.ShpImporter;
import com.emergya.ohiggins.security.ConstantesPermisos;
import com.emergya.ohiggins.security.OhigginsUserDetails;
import com.emergya.ohiggins.service.FileTypeService;
import com.emergya.ohiggins.service.InversionUpdateService;
import static com.emergya.ohiggins.web.AbstractController.MODULE_KEY;
import com.emergya.ohiggins.web.util.InversionShpValidator;
import com.emergya.ohiggins.web.util.InversionXlsValidator;
import com.emergya.ohiggins.web.util.MultiFileUploadForm;
import com.emergya.ohiggins.web.validators.MultiFileUploadValidator;
import com.emergya.persistenceGeo.exceptions.MultipleFilesWithSameExtension;
import com.emergya.persistenceGeo.exceptions.ShpImporterException;
import com.emergya.persistenceGeo.utils.FileUtils;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import javax.annotation.Resource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

/**
 * Controller for the functionality of updating investment projects information.
 *
 * @author lroman
 */
@Controller
public class InvestmentUpdatesController extends AbstractController {

    private static final Log LOG = LogFactory.getLog(InvestmentUpdatesController.class);
    public final static String MODULE = "cartografico";
    public final static String SUB_MODULE = "inversion";
    @Resource
    private InversionUpdateService inversionUpdateService;
    @Autowired
    private MultiFileUploadValidator fileUploadValidator;
    @Autowired
    private InversionXlsValidator inversionXlsValidator;
    @Autowired
    private FileTypeService fileTypeService;
    @Autowired
    private InversionShpValidator inversionShpValidator;
    @Autowired
    private ShpImporter shpImporter;

    /**
     * Muestra el formulario de envío de archivos para la actualización de las
     * iniciativas de inversión.
     *
     * @param model
     * @param webRequest
     * @return
     */
    @RequestMapping(value = "/cartografico/inversion")
    public ModelAndView updateInversionPaso1(Model model, WebRequest webRequest) {

        rolUser(model);
        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);

        ModelAndView mav = new ModelAndView();
        mav.addAllObjects(model.asMap());
        mav.addObject(MODULE_KEY, getActiveModule());
        mav.addObject(SUBMODULE_KEY, SUB_MODULE);

        updateLastUpdates(mav);

        mav.setViewName("cartografico/inversionStep1");

        return mav;
    }

    /**
     *
     * @param uploadForm
     * @param map
     * @param result
     * @return
     */
    @RequestMapping(value = "/cartografico/inversion2", method = RequestMethod.POST)
    public ModelAndView updateInversionPaso2(
            @ModelAttribute("uploadForm") MultiFileUploadForm uploadForm,
            Model map, BindingResult result) {
        String nextView = "redirect:inversion?msg=success";
        String errorView = "cartografico/inversionStep1";

        rolUser(map);
        // Accede el rol ADMIN, en otro caso pagina error
        decideIfIsUserInRole(ConstantesPermisos.ADMIN);
        ModelAndView mav = new ModelAndView();
        mav.addAllObjects(map.asMap());
        mav.addObject(MODULE_KEY, getActiveModule());
        mav.addObject(SUBMODULE_KEY, getActiveSubModule());

        // Validamos el fichero subido y si hay algún error redirigimos al
        // formulario de subida mostrando los mensajes de error
        fileUploadValidator.validate(uploadForm, result);
        if (result.hasErrors()) {
            if (LOG.isDebugEnabled()) {
                LOG.debug("Se han encontrados errores al validar el archivo enviado "
                        + "para actualizar iniciativas de inversión");

            }
            updateLastUpdates(mav);
            mav.setViewName(errorView);
            return mav;
        }

        InputStream is = null;
        InversionUpdateDto inversionDto = new InversionUpdateDto();
        File zipDir = null;
        File tempFile = null;
        try {
            MultipartFile mpFile = uploadForm.getFile();
            MultiFileUploadForm.FICHEROS_INVERSION tipoArchivo = uploadForm
                    .compruebaArchivoRecibido();
            if (LOG.isDebugEnabled()) {
                LOG.debug("Recibido el fichero " + tipoArchivo);
            }

            List<FileColumnDto> columns = new ArrayList<FileColumnDto>();
            File shpFile = null;
            try {
                if (MultiFileUploadForm.FICHEROS_INVERSION.SHP_PROYECTOS_GEO.equals(tipoArchivo)) {
                    tempFile = File.createTempFile("shp_inv", ".zip");
                    mpFile.transferTo(tempFile);
                    tempFile.deleteOnExit();
                    zipDir = FileUtils.unzipToTempDir(tempFile);
                    LOG.info("Temp dir: " + zipDir.getAbsolutePath());
                    shpFile = FileUtils.getUniqueFileWithExtensionInDir(zipDir,
                            "shp");
                    if (shpFile == null) {
                        result.rejectValue(uploadForm.getFilePropertyName(),
                                "inversion.noShpFile",
                                "El archivo ZIP no contiene ningún archivo SHP.");
                        if (LOG.isInfoEnabled()) {
                            LOG.info("El archivo ZIP no contiene ningún archivo SHP.");

                        }
                        updateLastUpdates(mav);
                        mav.setViewName(errorView);
                        return mav;
                    } else {
                        inversionShpValidator.validate(tipoArchivo, shpFile,
                                columns);
                    }

                } else {
                    inversionXlsValidator
                            .validate(tipoArchivo, mpFile, columns);
                    is = mpFile.getInputStream();

                }
            } catch (NotAllRequiredColumnsFound oops) {
                // Si falta alguna columna de las definidas en la base de datos,
                // se crea un error por cada una de las que falte y se vuelve
                // al formulario de envío de fichero.
                List<FileColumnDto> notFoundColumns = oops.getColumnsNotFound();
                for (FileColumnDto column : notFoundColumns) {
                    result.reject("inversion.columnNotFound",
                            new String[]{column.getName()},
                            "La columna \"{0}\" no se ha encontrado en el fichero enviado.");
                }
                result.rejectValue(uploadForm.getFilePropertyName(),
                        "inversion.wrongColumns",
                        "El fichero no tiene las columnas necesarias para la actualización.");
                if (LOG.isInfoEnabled()) {
                    LOG.info("Columnas no encontradas en el fichero de actualización de iniciativas de inversión.");

                }
                updateLastUpdates(mav);
                mav.setViewName(errorView);
                return mav;
            } catch (MultipleFilesWithSameExtension oops) {
                result.rejectValue(
                        uploadForm.getFilePropertyName(),
                        "inversion.multipleShpFiles",
                        "El ZIP enviado contiene más de un archivo .SHP. El archivo enviado no puede contener más de un archivo .SHP.");
                if (LOG.isInfoEnabled()) {
                    LOG.info("El ZIP contiene más de un archivo .SHP");
                }
                updateLastUpdates(mav);
                mav.setViewName(errorView);
                return mav;

            }

            FileTypeDto fileTypeDto = fileTypeService
                    .getFileTypeByFileType(tipoArchivo.toString());

            inversionDto.setLastUpdateDate(Calendar.getInstance().getTime());
            inversionDto.setFileType(fileTypeDto);
            inversionDto.setEnabled(false);
            inversionDto = (InversionUpdateDto) inversionUpdateService
                    .create(inversionDto);
            if (!MultiFileUploadForm.FICHEROS_INVERSION.SHP_PROYECTOS_GEO.equals(tipoArchivo)) {
                fileTypeService.updateDbTable(fileTypeDto.getTableName(),
                        columns, is, inversionDto.getId());
            } else {
                // Importar datos del shape
                shpImporter.importShapeToPosgis(fileTypeDto.getTableName(),
                        columns, shpFile, inversionDto.getId());
            }

	    // Si todo ha ido bien, marcamos todas las actualizaciones
            // anteriores
            // como deshabilitadas y la actual como habilitada.
            inversionUpdateService
                    .enableCurrentUpdateAndDisableOldOnes(inversionDto);

        } catch (ShpImporterException sie) {
            LOG.error("Falta algún fichero perteneciente al SHP", sie);
            LOG.error(
                    "Error de acceso a datos al actualizar proyectos de inversión",
                    sie);
            inversionUpdateService.delete(inversionDto);
            result.rejectValue(uploadForm.getFilePropertyName(),
                    "inversion.genericError",
                    "Falta alguno de los ficheros pertenecientes al SHP");

            updateLastUpdates(mav);
            mav.setViewName(errorView);
            return mav;
        } catch (DataAccessException dae) {
            LOG.error(
                    "Error de acceso a datos al actualizar proyectos de inversión",
                    dae);
            inversionUpdateService.delete(inversionDto);
            result.rejectValue(uploadForm.getFilePropertyName(),
                    "inversion.genericError",
                    "Se ha producido un error al procesar el archivo.");
            Throwable e = dae.getMostSpecificCause();
            if (e instanceof CustomSqlException) {
                CustomSqlException cse = (CustomSqlException) e;
                int line = cse.getLine() + 1;
                result.reject(
                        "inversion.batchError",
                        new Object[]{line},
                        "No se ha podido actualizar la información de iniciativas de inversión. "
                        + " Se produjo un error al procesar la línea {0} del archivo.");

            } else {
                result.reject(
                        "inversion.genericError",
                        "Se ha producido un error no "
                        + "determinado al actualizar las iniciativas de inversión. Consulte con "
                        + " el administrador para determinar la causa.");
            }

            updateLastUpdates(mav);
            mav.setViewName(errorView);
            return mav;

        } catch (Exception e) {
            LOG.error("Error actualizando proyectos de inversión", e);
            inversionUpdateService.delete(inversionDto);
            result.reject(
                    "inversion.genericError",
                    "Se ha producido un error no "
                    + "determinado al actualizar las iniciativas de inversión. Consulte con "
                    + " el administrador para determinar la causa.");
            result.rejectValue(uploadForm.getFilePropertyName(),
                    "inversion.genericError",
                    "Se ha producido un error al procesar el archivo.");
            updateLastUpdates(mav);
            mav.setViewName(errorView);
            return mav;

        } finally {
            // cleanup
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e) {
                    LOG.error(
                            "Error cerrando el InputStream del fichero temporal de inversión",
                            e);
                }
            }

            org.apache.commons.io.FileUtils.deleteQuietly(zipDir);
            org.apache.commons.io.FileUtils.deleteQuietly(tempFile);

        }

        updateLastUpdates(mav);
        mav.setViewName(nextView);

        return mav;
    }

    /**
     * @param mav
     */
    private void updateLastUpdates(ModelAndView mav) {
        List<InversionUpdateDto> lastUpdates = inversionUpdateService
                .getAllFileTypeLastUpdate();
        for (InversionUpdateDto dto : lastUpdates) {
            mav.addObject(dto.getFileType().getTypeName(),
                    dto.getLastUpdateDate());
        }
    }

    /**
     *
     * @param model
     */
    private void rolUser(Model model) {
        model.addAttribute("IS_ADMIN",
                OhigginsUserDetails.userHasAuthority(ConstantesPermisos.ADMIN));
    }

    @Override
    protected void copyDefaultModel(boolean update, Model model) {
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
        return "";
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
