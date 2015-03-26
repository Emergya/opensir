/* InversionXlsValidator.java
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

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.emergya.ohiggins.dto.FileColumnDto;
import com.emergya.ohiggins.exceptions.NotAllRequiredColumnsFound;
import com.emergya.ohiggins.service.FileTypeService;
import com.emergya.ohiggins.web.util.MultiFileUploadForm.FICHEROS_INVERSION;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
@Component
public class InversionXlsValidator {
	private final static Log LOG = LogFactory
			.getLog(InversionXlsValidator.class);
	@Autowired
	private FileTypeService fileTypeService;

	public boolean validate(FICHEROS_INVERSION tipo, MultipartFile file,
			List<FileColumnDto> columns) throws IOException {
		List<FileColumnDto> columnDefinitions = fileTypeService
				.getFileColumnsByFileType(tipo.toString());
		if (LOG.isWarnEnabled() && columnDefinitions.size() == 0) {
			LOG.warn("Se intentó validar un Excel de tipo "
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

		InputStream inputStream = file.getInputStream();
		try {

			Workbook workbook = WorkbookFactory.create(inputStream);
			// Get the first sheet
			Sheet sheet = workbook.getSheetAt(0);
			Row row = sheet.getRow(0);
			Iterator<Cell> headerCells = row.cellIterator();
			int numCeldasAEncontrar = columnDefinitions.size();
			while (headerCells.hasNext() && numCeldasAEncontrar > 0) {
				Cell cell = headerCells.next();
				String cellContent = getCellContentAsString(cell);
				if (LOG.isDebugEnabled()) {
					LOG.debug("Cell (" + (cell.getRowIndex() + 1) + ", "
							+ (cell.getColumnIndex() + 1) + ") " + cellContent);
				}

				FileColumnDto column = null;
				if (cellContent != null
						&& (column = columMap.get(cellContent.toUpperCase())) != null) {
					// Se ha encontrado una de las columnas necesarias para
					// interpretar el fichero
					column.setColumnIndex(cell.getColumnIndex());
					columns.add(column);
					columnDefinitions.remove(column);
					numCeldasAEncontrar--;
				}
			}
			if (numCeldasAEncontrar != 0) {
				throw new NotAllRequiredColumnsFound(
						"No se han encontrado todas las columnas necesarias para procesar el XLS",
						columnDefinitions);
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		} catch (InvalidFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			if (inputStream != null) {
				inputStream.close();
			}
		}

		return true;
	}

	/**
	 * Devuelve el contenido de la celda como una cadena, independientemente de
	 * su tipo (cadena, numérico, booleano, ...). Si la celda contiene un error
	 * devuelte null.
	 * 
	 * @param cell
	 * @return
	 */
	private String getCellContentAsString(Cell cell) {
		String result = null;
		switch (cell.getCellType()) {
		case Cell.CELL_TYPE_STRING:
			result = cell.getStringCellValue();
			break;
		case Cell.CELL_TYPE_NUMERIC:
			double number = cell.getNumericCellValue();
			result = String
					.format(Locale.ENGLISH, "%f", Double.valueOf(number));
			break;
		case Cell.CELL_TYPE_BOOLEAN:
			boolean b = cell.getBooleanCellValue();
			result = Boolean.toString(b);
			break;
		case Cell.CELL_TYPE_FORMULA:
			result = cell.getCellFormula();
			break;
		case Cell.CELL_TYPE_BLANK:
			result = cell.getStringCellValue();
			break;
		default:
			break;
		}

		return result;
	}
}
