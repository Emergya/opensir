/* InversionShpValidator.java
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
package com.emergya.ohiggins.web.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.geotools.data.FeatureSource;
import org.geotools.data.shapefile.ShapefileDataStore;
import org.opengis.feature.type.FeatureType;
import org.opengis.feature.type.Name;
import org.opengis.feature.type.PropertyDescriptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.emergya.ohiggins.dto.FileColumnDto;
import com.emergya.ohiggins.exceptions.NotAllRequiredColumnsFound;
import com.emergya.ohiggins.service.FileTypeService;
import com.emergya.ohiggins.web.util.MultiFileUploadForm.FICHEROS_INVERSION;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Component
public class InversionShpValidator {
	private final static Log LOG = LogFactory
			.getLog(InversionShpValidator.class);

	@Autowired
	private FileTypeService fileTypeService;

	@SuppressWarnings("rawtypes")
	public boolean validate(FICHEROS_INVERSION tipo, File shpFile,
			List<FileColumnDto> columns) {
		List<FileColumnDto> columnDefinitions = fileTypeService
				.getFileColumnsByFileType(tipo.toString());

		if (LOG.isWarnEnabled() && columnDefinitions.size() == 0) {
			LOG.warn("Se intentó validar un SHP de tipo "
					+ tipo.toString()
					+ ". Sin embargo, no existe definición de las columnas a importar en la base de datos. "
					+ "Compruebe que la definición de las columas para el tipo de arhivo "
					+ tipo.toString() + " existe en la tabla gis_file_column.");
		}
		// convert to Map
		Map<String, FileColumnDto> columMap = new HashMap<String, FileColumnDto>(
				columnDefinitions.size());
		for (FileColumnDto column : columnDefinitions) {
			columMap.put(column.getName().toUpperCase(), column);
		}

		ShapefileDataStore store = null;
		try {
			store = new ShapefileDataStore(shpFile.toURI().toURL());
			String typeName = store.getTypeNames()[0];

			FeatureSource source = store.getFeatureSource(typeName);
			FeatureType ft = source.getSchema();
			Collection<PropertyDescriptor> descriptors = ft.getDescriptors();
			Iterator<PropertyDescriptor> descriptorsIterator = descriptors
					.iterator();

			int numColumnasAEncontrar = columnDefinitions.size();
			int index = 0;

			while (descriptorsIterator.hasNext() && numColumnasAEncontrar > 0) {
				PropertyDescriptor property = descriptorsIterator.next();
				Name propertyName = property.getName();
				String name = propertyName.getLocalPart();
				if (LOG.isDebugEnabled()) {
					LOG.debug("SHP: column " + name);
				}

				FileColumnDto column = null;
				if (name != null
						&& (column = columMap.get(name.toUpperCase())) != null) {
					// Se ha encontrado una de las columnas necesarias para
					// interpretar el fichero
					column.setColumnIndex(index);
					columns.add(column);
					columnDefinitions.remove(column);
					numColumnasAEncontrar--;
				}
				index++;
			}
			if (numColumnasAEncontrar != 0) {
				throw new NotAllRequiredColumnsFound(
						"No se han encontrado todas las columnas necesarias para procesar el SHP",
						columnDefinitions);
			}

		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (store != null) {
				store.dispose();
			}
		}

		return true;
	}

}
