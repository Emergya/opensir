/* ExcelJdbDao.java
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
package com.emergya.ohiggins.dao;

import java.util.List;
import java.util.Map;

import com.emergya.ohiggins.utils.ColumnGeo;
import com.emergya.ohiggins.utils.ColumnMetadata;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public interface ImportGeoJdbDao {
	
	public List<ColumnGeo> createTable(String tableName, List<String> columnNames);
	
	public String getSQL(String tableName, List<ColumnGeo> fields, List<String> values);
	
	public Map<String, String> addData(String tableName, List<ColumnGeo> fields, List<String> values);
	
	public void batchUpdate(String sql, Map<String, String>[] values);
	
	public void updatePointColumn(String geoColumnName, String tableName, String lon, String lat, String srid,
			String columnX, String columnY);
	
	public void createGeoColumnDB(String tableName, String geoColumName, String srid);
	
	public List<Map<String,Object>> getTableCoordinates(String tableName, String colX, String colY);
	
	public List<Map<String,Object>> getTableDataById(String tableName, ColumnMetadata idColumnInfo, String idValue);
	
	public boolean createNewColumn(String tableName, String columnName, String columnType);
	
	public List<Map<String,Object>> getColumnsName(String tableName);
	
	public List<Map<String,Object>> getColumnsConstraints(String tableName);
	
	public void deleteTableColumns(String tableName, String columnName);
	
	public boolean existColumn(String tableName, String columnName);
	
	public void updateTableRow(
		String tableName, Map<String, ColumnMetadata> columnsInfo,
		List<String> columnValues, ColumnMetadata conditionColumnInfo, String condition);

	public boolean isFieldNumeric(ColumnMetadata columnInfo);
}
