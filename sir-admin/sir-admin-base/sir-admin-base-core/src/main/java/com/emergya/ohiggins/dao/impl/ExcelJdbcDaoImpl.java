/* ImportJdbcDaoImpl.java
 * 
 * Copyright (C) 2012
 * 
 * This file is part of project ohiggins-core
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
package com.emergya.ohiggins.dao.impl;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;

import com.emergya.ohiggins.dao.ExcelJdbDao;
import com.emergya.ohiggins.dao.util.IterableJdbcDaoSupport;
import com.emergya.ohiggins.dto.FileColumnDto;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class ExcelJdbcDaoImpl extends IterableJdbcDaoSupport implements
		ExcelJdbDao {

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.emergya.ohiggins.dao.ExcelJdbDao#insertBatchExcel(java.lang.String,
	 * java.util.List, org.apache.poi.ss.usermodel.Sheet)
	 */
	@Override
	public void insertBatchExcel(String tableName,
			List<FileColumnDto> columnModel, Sheet sheet, Long inversionUpdateId)
			throws DataAccessException {
		String sql = generateSql(tableName, columnModel);
		Iterator<Row> filas = sheet.iterator();

		if (filas.hasNext()) {
			filas.next();
			ParameterizedPreparedStatementSetter<Row> pps = new CustomParameterizedPreparedStatementSetter<Row>(
					columnModel, inversionUpdateId);

			getIterableJdbcTemplate().batchUpdate(sql, filas, 512, pps);
		}
	}

	/**
	 * Generate the SQL for insert a
	 * 
	 * @param tableName
	 * @param columnModel
	 * @return
	 */
	private String generateSql(String tableName, List<FileColumnDto> columnModel) {
		StringBuilder sb = new StringBuilder("INSERT INTO ");
		sb.append(tableName).append("(");
		int columnSize = columnModel.size();
		for (FileColumnDto column : columnModel) {
			sb.append(column.getDbName());
			sb.append(", ");
		}
		sb.append("investment_initiative_update_id");

		sb.append(") VALUES (");
		for (int j = 0; j < columnSize; j++) {
			sb.append("?, ");
		}

		sb.append("?"); // Placeholder for filetype_id
		sb.append(")");
		return sb.toString();
	}

	class CustomParameterizedPreparedStatementSetter<T> implements
			ParameterizedPreparedStatementSetter<Row> {
		private List<FileColumnDto> columnModel;
		private Long inversionUpdateId;
		private NumberFormat nf = NumberFormat.getInstance(new Locale("es",
				"CL"));

		NumberFormat noFormatFmt;

		public CustomParameterizedPreparedStatementSetter(
				List<FileColumnDto> columnModel, Long inversionUpdateId) {
			this.columnModel = columnModel;
			this.inversionUpdateId = inversionUpdateId;

			noFormatFmt = NumberFormat.getNumberInstance();
			noFormatFmt.setGroupingUsed(false);
			noFormatFmt.setMinimumFractionDigits(0);
		}

		@Override
		public void setValues(PreparedStatement ps, Row argument)
				throws SQLException {
			int i = 1;
			for (FileColumnDto column : columnModel) {
				Cell cell = argument.getCell(column.getColumnIndex());
				if (cell != null) {
					if ("String".equals(column.getType())) {
						ps.setString(i, getCellContentAsString(cell, false));
					} else if ("Double".equals(column.getType())) {
						double d = 0.0d;
						try {
							d = nf.parse(getCellContentAsString(cell, true))
									.doubleValue();
							ps.setDouble(i, d);
						} catch (NumberFormatException e) {
							if (logger.isTraceEnabled()) {
								logger.trace("The cell content is not a number :\""
										+ getCellContentAsString(cell, false)
										+ "\"");
							}
							ps.setNull(i, java.sql.Types.NUMERIC);
						} catch (ParseException e) {
							if (logger.isTraceEnabled()) {
								logger.trace("The cell content is not a number :\""
										+ getCellContentAsString(cell, false)
										+ "\"");
							}
							ps.setNull(i, java.sql.Types.NUMERIC);
						}
					} else {
						logger.warn("Type of Column \"" + column.getName()
								+ "\" not regconized");
					}
				} else {
					// cell is null
					if ("String".equals(column.getType())) {
						ps.setString(i, null);
					} else if ("Double".equals(column.getType())) {
						ps.setNull(i, java.sql.Types.NUMERIC);
					} else {
						logger.warn("Type of Column \"" + column.getName()
								+ "\" not regconized");
						ps.setNull(i, java.sql.Types.VARCHAR);
					}
				}

				// La última columna debería ser la foreing key a la tabla
				// gis_investment_project_update
				i++;
			}
			ps.setLong(i, this.inversionUpdateId);

		}

		private String getCellContentAsString(Cell cell, boolean formatNumber) {
			String result = null;

			switch (cell.getCellType()) {
			case Cell.CELL_TYPE_STRING:
				result = cell.getStringCellValue();
				break;
			case Cell.CELL_TYPE_NUMERIC:
				    if(DateUtil.isCellDateFormatted(cell)) { 
//				        Format f = new DataFormatter().getDefaultFormat(cell);
//				        result = f.format(cell.getNumericCellValue());
				    	Date fecha = DateUtil.getJavaDate(cell.getNumericCellValue());
				    	DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
				    	result = dateFormat.format(fecha);
				        
				    } else { 
				       //Numeric cell 
				    	if (formatNumber) {
				    		result = nf.format(cell.getNumericCellValue());
				    	} else {
				    		result = noFormatFmt.format(cell.getNumericCellValue());
				    	}
				    } 
				
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

}
