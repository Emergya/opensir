/*
 * TableMetadata.java
 * 
 * Copyright (C) 2013
 * 
 * This file is part of ohiggins-core
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
 * Authors:: Juan Luis Rodríguez Ponce (mailto:jlrodriguez@emergya.com)
 */
package com.emergya.ohiggins.utils;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 *
 */
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import org.apache.commons.collections.map.LinkedMap;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * JDBC table metadata
 * 
 * @author Christoph Sturm
 * @author Max Rydahl Andersen
 */
public class TableMetadata {
	private static final Logger log = LoggerFactory
			.getLogger(TableMetadata.class);

	private final String catalog;
	private final String schema;
	private final String name;
	private final Map<String, ColumnMetadata> columns = new LinkedMap();

	TableMetadata(ResultSet rs, DatabaseMetaData meta) throws SQLException {
		catalog = rs.getString("TABLE_CAT");
		schema = rs.getString("TABLE_SCHEM");
		name = rs.getString("TABLE_NAME");
		initColumns(meta);

		String cat = catalog == null ? "" : catalog + '.';
		String schem = schema == null ? "" : schema + '.';
		log.info("table found: " + cat + schem + name);
		log.info("columns: " + columns.keySet());

	}

	public Map<String, ColumnMetadata> getColumns() {
		return columns;
	}

	public void addColumn(ResultSet rs) throws SQLException {
		String column = rs.getString("COLUMN_NAME");

		if (column == null) {
			return;
		}

		if (getColumnMetadata(column) == null) {
			ColumnMetadata info = new ColumnMetadata(rs);
			columns.put(info.getName(), info);
		}
	}

	public String getCatalog() {
		return catalog;
	}

	public ColumnMetadata getColumnMetadata(String columnName) {
		return (ColumnMetadata) columns.get(columnName);
	}

	public String getName() {
		return name;
	}

	public String getSchema() {
		return schema;
	}

	private void initColumns(DatabaseMetaData meta) throws SQLException {
		ResultSet rs = null;

		try {
			rs = meta.getColumns(catalog, schema, name, "%");
			while (rs.next()) {
				addColumn(rs);
			}
		} finally {
			if (rs != null) {
				rs.close();
			}
		}
	}

	@Override
	public String toString() {
		return "TableMetadata(" + name + ')';
	}

}
