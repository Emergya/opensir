/* MultiFileUploadValidatorLayerUpdate.java
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.web.validators;

import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.emergya.ohiggins.web.util.SimpleUploadFormLayerUpdate;
import com.emergya.ohiggins.web.util.SimpleUploadFormLayerUpdate.FICHEROS;
import com.emergya.persistenceGeo.utils.FileUtils;

/**
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
@Component
public class MultiFileUploadValidatorLayerUpdate implements Validator {
	private static final Log LOG = LogFactory
			.getLog(MultiFileUploadValidatorLayerUpdate.class);

	private static final String APPLICATION_VND_MS_EXCEL = "application/vnd.ms-excel";
	private static final String XLSX_MIME_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
	private static final String TEXT_CSV = "text/csv";

	private static final String SHP = "0";
	private static final String EXCEL_CSV = "1";

	/**
	 * Indica si el archivo a subir es de un tipo u otro: 0: SHP zip 1:
	 * csv/excel
	 * */

	private String metodo = "";

	/**
	 * 
	 */
	public MultiFileUploadValidatorLayerUpdate() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#supports(java.lang.Class)
	 */
	@Override
	public boolean supports(Class<?> clazz) {

		return SimpleUploadFormLayerUpdate.class.equals(clazz);
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see org.springframework.validation.Validator#validate(java.lang.Object,
	 * org.springframework.validation.Errors)
	 */
	@Override
	public void validate(Object target, Errors errors) {

		SimpleUploadFormLayerUpdate form = (SimpleUploadFormLayerUpdate) target;
		SimpleUploadFormLayerUpdate.FICHEROS fichero = form
				.compruebaArchivoRecibido();

		if (fichero == null) {
			errors.reject(errors.getObjectName()+".noFileUploaded",
					"Seleccione un fichero para actualizar.");
		} else {
			String contentType = form.getFile().getContentType();
			InputStream is = null;

			try {
				is = form.getFile().getInputStream();
			} catch (IOException ioe) {

			}

			if (FICHEROS.FILE.equals(fichero)) {
				if (getMetodo() != null) {

					if (SHP.equals(getMetodo())) {
						if (!FICHEROS.FILE.equals(fichero) || is == null
								|| !FileUtils.checkIfInputStreamIsZip(is)) {
							// Se sube desde el campo shpProyectosGeo pero no es
							// un ZIP
							errors.rejectValue(form.getFilePropertyName(),
									errors.getObjectName()+".shpIsNotAZip",
									"El fichero indicado no es un archivo ZIP.");
							errors.reject(
									errors.getObjectName()+".wrongShpFiletype",
									"Para subir un archivo SHP debe crear y seleccionar un "
											+ "archivo ZIP que contenga al menos los archivos .SHP, "
											+ ".DBF y .SHX que forman el shapefile.");
							if (LOG.isWarnEnabled()) {
								LOG.warn("Wrong Filetype found: " + contentType);
							}
						}
					} else if (EXCEL_CSV.equals(getMetodo())) {
						//Se ha eliminado la comprobación del mime-type por incompatibildad entre navegadores.
						if (!FICHEROS.FILE.equals(fichero) || is == null) {
							errors.rejectValue(form.getFilePropertyName(),
							errors.getObjectName()+".wrongMimeType",
							"El fichero indicado no debe estar vacío.");
//								|| !(APPLICATION_VND_MS_EXCEL
//										.equalsIgnoreCase(contentType)
//								|| XLSX_MIME_TYPE
//										.equalsIgnoreCase(contentType)
//								|| TEXT_CSV.equalsIgnoreCase(contentType))) {
							// Subida desde un campo que espera un XLS pero no
							// recibe un XLS
//							errors.rejectValue(form.getFilePropertyName(),
//									"inversion.wrongMimeType",
//									"El fichero indicado no es un XLS compatible");
							if (LOG.isWarnEnabled()) {
								LOG.warn("Wrong Filetype found: " + contentType);
							}
						}
					}
				}

			}// Fin metodo
		}
	}

	public String getMetodo() {
		return metodo;
	}

	public void setMetodo(String metodo) {
		this.metodo = metodo;
	}
}
