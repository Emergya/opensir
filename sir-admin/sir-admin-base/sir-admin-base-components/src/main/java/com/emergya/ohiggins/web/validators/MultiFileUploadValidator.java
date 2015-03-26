/* MultiFileUploadValidator.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-app
 * 
 * This software is free software; you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation; either version 2 of the License, or
 * (at your option) any later version.
 * 
 * This software is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 *
 * As a special exception, if you link this library with other files to
 * produce an executable, this library does not by itself cause the
 * resulting executable to be covered by the GNU General Public License.
 * This exception does not however invalidate any other reasons why the
 * executable file might be covered by the GNU General Public License.
 * 
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.web.validators;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.emergya.ohiggins.web.util.MultiFileUploadForm;
import com.emergya.ohiggins.web.util.MultiFileUploadForm.FICHEROS_INVERSION;
import com.emergya.persistenceGeo.utils.FileUtils;
import java.io.BufferedInputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.poi.POIXMLDocument;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Component
public class MultiFileUploadValidator implements Validator {
	private static final Log LOG = LogFactory
			.getLog(MultiFileUploadValidator.class);

	private static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
	private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";

	/**
	 * 
	 */
	public MultiFileUploadValidator() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {

		return MultiFileUploadForm.class.equals(clazz);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {
		MultiFileUploadForm form = (MultiFileUploadForm) target;
		MultiFileUploadForm.FICHEROS_INVERSION fichero = form
				.compruebaArchivoRecibido();
		if (fichero == null) {
			errors.reject("inversion.noFileUploades",
					"Seleccione un fichero para actualizar");
		} else {
			String contentType = form.getFile().getContentType();
			InputStream is = null;

			try {
				is = form.getFile().getInputStream();
			} catch (IOException ioe) {

			}

			if (is == null){
                errors.rejectValue(form.getFilePropertyName(),
                        "inversion.emptyFile",
                        "No se ha recibido fichero.");
                if (LOG.isWarnEnabled()) {
                    LOG.warn("No file received.");
                }                
            } else if(!FICHEROS_INVERSION.SHP_PROYECTOS_GEO.equals(fichero)){
                BufferedInputStream bis = new BufferedInputStream(is);
                try {
                    if(!POIXMLDocument.hasOOXMLHeader(bis)  && !POIFSFileSystem.hasPOIFSHeader(bis)) {
                        // We don't check mime types due to different browsers sending a different mime type, see #83453
                        //					&& !APPLICATION_VND_MS_EXCEL.equalsIgnoreCase(contentType)
                        //					&& !XLSX_MIME_TYPE.equalsIgnoreCase(contentType)) {
                        // Checking directly with POI is much more precise given our use case.
                        // Subida desde un campo que espera un XLS pero no recibe un XLS
                        errors.rejectValue(form.getFilePropertyName(),
                                "inversion.wrongMimeType",
                                "El fichero indicado no es un XLS compatible");
                        if (LOG.isWarnEnabled()) {
                            LOG.warn("Wrong Filetype found: " + contentType);
                        }
                    }
                } catch (IOException ex) {
                    errors.rejectValue(form.getFilePropertyName(),
                            "inversion.wrongMimeType",
                            "El fichero indicado no es un XLS compatible");
                    if (LOG.isWarnEnabled()) {
                        LOG.warn("Wrong Filetype found: " + contentType);
                    }
                }
			} else if (FICHEROS_INVERSION.SHP_PROYECTOS_GEO.equals(fichero)
					&& !FileUtils.checkIfInputStreamIsZip(is)) {
				// Se sube desde el campo shpProyectosGeo pero no es un ZIP
				errors.rejectValue(form.getFilePropertyName(),
						"inversion.shpIsNotAZip",
						"El fichero indicado no es un archivo ZIP.");
				errors.reject(
						"inversion.wrongShpFiletype",
						"Para subir un archivo SHP debe crear y seleccionar un "
								+ "archivo ZIP que contenga al menos los archivos .SHP, "
								+ ".DBF y .SHX que forman el shapefile");
				if (LOG.isWarnEnabled()) {
					LOG.warn("Wrong Filetype found: " + contentType);
				}

			} else {
				if (LOG.isInfoEnabled()) {
					LOG.info("Filetype found: " + contentType);
				}
			}
		}

	}

}
