/* ExcelJdbcGeoDaoImpl.java
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
 * Authors:: Antonio J. Rodríguez (mailto:ajrodriguez@emergya.com)
 */
package com.emergya.ohiggins.dao.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.SessionFactory;
import org.hibernate.engine.SessionFactoryImplementor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.dao.ImportGeoJdbDao;
import com.emergya.ohiggins.utils.ColumnGeo;
import com.emergya.ohiggins.utils.ColumnMetadata;
import com.emergya.ohiggins.utils.DataBaseType;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import java.util.Arrays;
import java.util.Map.Entry;

/**
 * @author <a href="mailto:ajrodriguez@emergya.com">ajrodriguez</a>
 * 
 */
@Repository
public class ImportJdbcGeoDaoImpl extends HibernateDaoSupport implements
		ImportGeoJdbDao {

	private final static Log LOG = LogFactory.getLog(ImportJdbcGeoDaoImpl.class);
	private final static String [] POSTGRES_NUMBER_TYPES = new String[]{ "bigint", "int8", "double precision", "float8", "float,integer", "int", "int4","real", "float4","smallint", "int2","money","serial"};
	

	protected JdbcTemplate simpleJdbcTemplate = null;
	protected NamedParameterJdbcTemplate namedJdbcTemplate = null;

	@Autowired
	@Qualifier("sessionFactory")
	public void init(SessionFactory sessionFactory) {
		setSessionFactory(sessionFactory);
	}

	@Autowired
	@Qualifier("dataSourceHibernate")
	final public void setDataSource(final DataSource dataSource) {
		this.simpleJdbcTemplate = new JdbcTemplate(dataSource);
		this.namedJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}

	@Override
	public List<ColumnGeo> createTable(String tableName, List<String> columnNames) {
		List<ColumnGeo> columns = new ArrayList<ColumnGeo>();

		String schema = getSchemaName();

		String pk = "id_point";

		ColumnGeo column;
		for (String cl : columnNames) {
			column = new ColumnGeo(cl, DataBaseType.text);
			columns.add(column);
		}

		String columnsToTable = StringUtils.join(
				Lists.transform(columns, new Function<ColumnGeo, String>() {
					@Override
					public String apply(ColumnGeo column) {
						return column.toString();
					}
				}), ", ");

		String sqlCreateTable = String.format("CREATE TABLE %s.%s (\"%s\" SERIAL PRIMARY KEY, %s)",
				schema, tableName, pk, columnsToTable);

		this.simpleJdbcTemplate.execute(sqlCreateTable);

		return columns;
	}

	private String getSchemaName() {
		SessionFactoryImplementor sfi = (SessionFactoryImplementor) getSessionFactory();
		String schema = sfi.getSettings().getDefaultSchemaName();
		return schema;
	}

	@Override
	public void createGeoColumnDB(String tableName, String geoColumName, String srid) {
		ColumnGeo geoColumn = new ColumnGeo();
		geoColumn.setName(geoColumName);
		geoColumn.setType(DataBaseType.POINT);
		// Add the geometry field into DB
		String sqlGeometry = "Select AddGeometryColumn ('" + this.getSchemaName()
				+ "', '" + tableName.toLowerCase()
				+ "', '" + geoColumn.getName()
				+ "', " + String.valueOf(srid)
				+ ", '" + geoColumn.getType()
				+ "', " + String.valueOf(2) + ")";

		LOG.info(sqlGeometry);

		this.simpleJdbcTemplate.execute(sqlGeometry);
	}

	@Override
	public boolean createNewColumn(String tableName, String columnName, String columnType) {

		if (columnType.equalsIgnoreCase("String")) {
			columnType = "TEXT";
		} else if (columnType.equalsIgnoreCase("Date")) {
			columnType = "DATE";
		} else if (columnType.equalsIgnoreCase("Number")) {
			columnType = "INTEGER";
		} else {
			return false;
		}

		String query = String.format("ALTER TABLE %s.\"%s\" ADD COLUMN \"%s\" %s", getSchemaName(),
				tableName,
				columnName, columnType);

		LOG.info(query);
		this.simpleJdbcTemplate.execute(query);

		return true;
	}

	@Override
	public List<Map<String, Object>> getColumnsName(String tableName) {
		StringBuilder sqlColumnNames = new StringBuilder("select column_name from information_schema.columns where table_name='")
				.append(tableName)
				.append("'");
		LOG.info(sqlColumnNames);
		return this.simpleJdbcTemplate.queryForList(sqlColumnNames.toString());
	}

	@Override
	public boolean existColumn(String tableName, String columnName) {
		StringBuilder sqlColumnNames = new StringBuilder("select column_name from information_schema.columns where table_name='")
				.append(tableName)
				.append("'")
				.append(" and column_name='")
				.append(columnName)
				.append("'");
		LOG.info(sqlColumnNames);
		return (this.simpleJdbcTemplate.queryForList(sqlColumnNames.toString()) != null && this.simpleJdbcTemplate.queryForList(sqlColumnNames.toString()).size() > 0);
	}

	@Override
	public List<Map<String, Object>> getColumnsConstraints(String tableName) {
		StringBuilder sqlColumnConstraints = new StringBuilder("SELECT kcu.column_name ")
				.append(" FROM information_schema.table_constraints AS tc ")
				.append("JOIN information_schema.key_column_usage AS kcu ON tc.constraint_name = kcu.constraint_name ")
				.append("JOIN information_schema.constraint_column_usage AS ccu ON ccu.constraint_name = tc.constraint_name ")
				.append("WHERE (constraint_type = 'FOREING KEY' OR constraint_type = 'PRIMARY KEY') AND tc.table_name='")
				.append(tableName)
				.append("'");
		LOG.info(sqlColumnConstraints);
		return this.simpleJdbcTemplate.queryForList(sqlColumnConstraints.toString());
	}

	@Override
	public void deleteTableColumns(String tableName, String columnName) {
		String query = String.format("ALTER TABLE %s.\"%s\" DROP COLUMN \"%s\"",
				getSchemaName(),
				tableName,
				columnName);

		this.simpleJdbcTemplate.execute(query);

	}

	@Override
	public Map<String, String> addData(String tableName, List<ColumnGeo> fields, List<String> values) {

		Map<String, String> parameters = new HashMap<String, String>();

		for (int j = 0; j < values.size(); j++) {
			parameters.put(fields.get(j).getName(), values.get(j));
		}

		return parameters;

	}

	@Override
	public String getSQL(String tableName, List<ColumnGeo> fields, List<String> values) {

		String fieldNames = StringUtils.join(Lists.transform(fields,
				new Function<ColumnGeo, String>() {
					@Override
					public String apply(ColumnGeo column) {
						return "\"" + column.getName() + "\"";
					}
				}), ", ");

		String placeholders = StringUtils.join(Lists.transform(fields,
				new Function<ColumnGeo, String>() {
					@Override
					public String apply(ColumnGeo column) {
						return ":" + column.getName();
					}
				}), ", ");

		String sqlInsert = String.format("INSERT INTO %s.%s (%s) VALUES (%s)",
				this.getSchemaName(), tableName,
				fieldNames, placeholders);

		return sqlInsert;
	}

	@Override
	public void batchUpdate(String sql, Map<String, String>[] values) {
		this.namedJdbcTemplate.batchUpdate(sql, values);

	}

	@Override
	public List<Map<String, Object>> getTableCoordinates(String tableName, String colX, String colY) {
		if (StringUtils.isBlank(tableName) || StringUtils.isBlank(colX) || StringUtils.isBlank(colY)) {
			return null;
		}

		String hql = String.format(
				"SELECT tb.\"%s\", tb.\"%s\" FROM %s.%s tb",
				colX,colY, getSchemaName(), tableName);
		
		return this.simpleJdbcTemplate.queryForList(hql.toString());

	}

	@Override
	public List<Map<String, Object>> getTableDataById(String tableName, ColumnMetadata idColumnInfo, String idValue) {
		if (tableName != null && idColumnInfo != null && idValue != null) {

			String query = String.format("SELECT * FROM %s.\"%s\" WHERE \"%s\" = %s",
					getSchemaName(),
					tableName,
					idColumnInfo.getName(), getSQLValue(idValue, idColumnInfo));

			return this.simpleJdbcTemplate.queryForList(query);
			// return this.simpleJdbcTemplate.queryForList(hql.toString());
		} else {
			return null;
		}

	}

	@Override
	public void updatePointColumn(String geoColumnName, String tableName, String lon, String lat, String srid,
			String columnX, String columnY) {
		Map<String, Object> paramMap = new HashMap<String, Object>();
		
		String updateSQL = String.format(
				"UPDATE %s.%s SET \"%s\"=ST_GeomFromEWKT('SRID=%s;POINT(%s %s)') WHERE \"%s\"=:lon and \"%s\"=:lat",
				getSchemaName(), tableName, geoColumnName, srid, lon, lat, columnX, columnY);
		
		paramMap.put("lon", lon);
		paramMap.put("lat", lat);

		LOG.info(updateSQL);
		this.namedJdbcTemplate.update(updateSQL, paramMap);
	}

	@Override
	public void updateTableRow(
		String tableName, Map<String, ColumnMetadata> columnsInfo ,
		List<String> columnValues, ColumnMetadata conditionColumnInfo, String condition) {
		
		if (columnsInfo.size() != columnValues.size()) {
			throw new RuntimeException("Error: las columnas a actualizar difieren en número de las columnas en la tabla.");
		}

		List<String> updatePieces = new ArrayList<String>();
		
		// We need to trust that the map and the list have the columns in the same order
		// which is problably not safe to suppose...
		int columnIdx = 0;
		for(Entry<String,ColumnMetadata> columnInfo : columnsInfo.entrySet()) {
		    updatePieces.add(String.format("\"%s\" = %s", 
			    columnInfo.getKey(), 
			    getSQLValue(columnValues.get(columnIdx),columnInfo.getValue())));
		    columnIdx++;
		}
		
		String updateSQL =String.format("UPDATE %s.\"%s\" SET %s WHERE \"%s\" = %s",
			getSchemaName(),tableName,
			StringUtils.join(updatePieces, ", "),
			conditionColumnInfo.getName(),
			getSQLValue(condition, conditionColumnInfo));

		LOG.info(updateSQL);
		Map<String, Object> paramMap = new HashMap<String, Object>();
		this.namedJdbcTemplate.update(updateSQL.toString(), paramMap);
	}
	
	private String getSQLValue(String value, ColumnMetadata columnMetadata) {
	    
	    if(isFieldNumeric(columnMetadata)) {
		return value;
	    }
	    
	    return String.format("'%s'", value);
	}

	@Override
	public boolean isFieldNumeric(ColumnMetadata columnMetadata) {
	    return Arrays.asList(POSTGRES_NUMBER_TYPES).contains(columnMetadata.getTypeName());
	}
}
