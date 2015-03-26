/*
 * DatabaseMetadata.java
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

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.HibernateException;
import org.hibernate.dialect.Dialect;
import org.hibernate.exception.JDBCExceptionHelper;
import org.hibernate.exception.SQLExceptionConverter;
import org.hibernate.mapping.Table;
import org.hibernate.util.StringHelper;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class DatabaseMetadata {

	private DatabaseMetaData meta;
	private final Map<Object, TableMetadata> tables = new HashMap<Object, TableMetadata>();
	private SQLExceptionConverter sqlExceptionConverter;
	private static final String[] TYPES = { "TABLE", "VIEW" };

	public DatabaseMetadata(Connection connection, Dialect dialect)
			throws SQLException {
		sqlExceptionConverter = dialect.buildSQLExceptionConverter();
		meta = connection.getMetaData();
	}

	public TableMetadata getTableMetadata(String name, String schema,
			String catalog, boolean isQuoted) throws HibernateException {

		Object identifier = identifier(catalog, schema, name);
		TableMetadata table = (TableMetadata) tables.get(identifier);
		if (table != null) {
			return table;
		} else {

			try {
				ResultSet rs = null;
				try {
					if ((isQuoted && meta.storesMixedCaseQuotedIdentifiers())) {
						rs = meta.getTables(catalog, schema, name, TYPES);
					} else if ((isQuoted && meta
							.storesUpperCaseQuotedIdentifiers())
							|| (!isQuoted && meta.storesUpperCaseIdentifiers())) {
						rs = meta.getTables(StringHelper.toUpperCase(catalog),
								StringHelper.toUpperCase(schema),
								StringHelper.toUpperCase(name), TYPES);
					} else if ((isQuoted && meta
							.storesLowerCaseQuotedIdentifiers())
							|| (!isQuoted && meta.storesLowerCaseIdentifiers())) {
						rs = meta.getTables(StringHelper.toLowerCase(catalog),
								StringHelper.toLowerCase(schema),
								StringHelper.toLowerCase(name), TYPES);
					} else {
						rs = meta.getTables(catalog, schema, name, TYPES);
					}

					while (rs.next()) {
						String tableName = rs.getString("TABLE_NAME");
						if (name.equalsIgnoreCase(tableName)) {
							table = new TableMetadata(rs, meta);
							tables.put(identifier, table);
							return table;
						}
					}

					return null;

				} finally {
					if (rs != null)
						rs.close();
				}
			} catch (SQLException sqle) {
				throw JDBCExceptionHelper.convert(sqlExceptionConverter, sqle,
						"could not get table metadata: " + name);
			}
		}
	}

	private Object identifier(String catalog, String schema, String name) {
		return Table.qualify(catalog, schema, name);
	}

}
