/* IterableJdbcTemplate.java
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
package com.emergya.ohiggins.dao.util;

import java.sql.BatchUpdateException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ParameterDisposer;
import org.springframework.jdbc.core.ParameterizedPreparedStatementSetter;
import org.springframework.jdbc.core.PreparedStatementCallback;
import org.springframework.jdbc.support.JdbcUtils;

import com.emergya.ohiggins.dao.impl.CustomSqlException;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class IterableJdbcTemplate extends JdbcTemplate {

	/**
	 * 
	 */
	public IterableJdbcTemplate() {
	}

	/**
	 * @param dataSource
	 */
	public IterableJdbcTemplate(DataSource dataSource) {
		super(dataSource);
	}

	/**
	 * @param dataSource
	 * @param lazyInit
	 */
	public IterableJdbcTemplate(DataSource dataSource, boolean lazyInit) {
		super(dataSource, lazyInit);
	}

	/**
	 * 
	 * @param sql
	 * @param batchArgs
	 * @param batchSize
	 * @param pss
	 *            * @return an array of the number of rows affected by each
	 *            statement
	 * @throws DataAccessException
	 *             if there is any problem executing the batch
	 */
	public <T> int[][] batchUpdate(String sql, final Iterator<T> batchArgs,
			final int batchSize,
			final ParameterizedPreparedStatementSetter<T> pss)
			throws DataAccessException {
		if (logger.isDebugEnabled()) {
			logger.debug("Executing SQL batch update [" + sql
					+ "] with a batch size of " + batchSize);
		}
		return execute(sql, new PreparedStatementCallback<int[][]>() {
			public int[][] doInPreparedStatement(PreparedStatement ps)
					throws SQLException {
				List<int[]> rowsAffected = new ArrayList<int[]>();
				try {
					boolean batchSupported = true;
					if (!JdbcUtils.supportsBatchUpdates(ps.getConnection())) {
						batchSupported = false;
						logger.warn("JDBC Driver does not support Batch updates; resorting to single statement execution");
					}
					int n = 0;
					while (batchArgs.hasNext()) {
						T obj = batchArgs.next();
						pss.setValues(ps, obj);
						n++;
						if (batchSupported) {
							ps.addBatch();
							if (n % batchSize == 0 || !batchArgs.hasNext()) {
								if (logger.isDebugEnabled()) {
									int batchIdx = (n % batchSize == 0) ? n
											/ batchSize : (n / batchSize) + 1;
									int items = n
											- ((n % batchSize == 0) ? n
													/ batchSize - 1
													: (n / batchSize))
											* batchSize;
									logger.debug("Sending SQL batch update #"
											+ batchIdx + " with " + items
											+ " items");
								}
								rowsAffected.add(ps.executeBatch());

							}
						} else {
							int i = ps.executeUpdate();
							rowsAffected.add(new int[] { i });
						}
					}
					int[][] result = new int[rowsAffected.size()][];
					for (int i = 0; i < result.length; i++) {
						result[i] = rowsAffected.get(i);
					}
					return result;
				} catch (SQLException oops) {
					// try to determine the line where the exceptios has
					// happened
					int line = 1 + rowsAffected.size() * batchSize;

					boolean found = false;
					if (oops instanceof BatchUpdateException) {
						int[] batchResult = (((BatchUpdateException) oops)
								.getUpdateCounts());
						for (int k = 0; k < batchResult.length && !found; k++) {
							if (batchResult[k] == Statement.EXECUTE_FAILED) {
								found = true;
							} else {
								line++;
							}
						}

						throw new CustomSqlException(line, oops.getMessage(),
								oops.getSQLState(), oops.getErrorCode());
					} else {
						throw oops;
					}

				} finally {
					if (pss instanceof ParameterDisposer) {
						((ParameterDisposer) pss).cleanupParameters();
					}
				}
			}
		});
	}

}
