/* CustomSqlException.java
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

import java.sql.SQLException;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class CustomSqlException extends SQLException {

	private static final long serialVersionUID = 4018348935191267103L;
	private int line = 0;

	/**
	 * 
	 */
	public CustomSqlException(int line) {
		this.line = line;
	}

	/**
	 * @param reason
	 */
	public CustomSqlException(int line, String reason) {
		super(reason);
		this.line = line;
	}

	/**
	 * @param cause
	 */
	public CustomSqlException(int line, Throwable cause) {
		super(cause);
		this.line = line;
	}

	/**
	 * @param reason
	 * @param SQLState
	 */
	public CustomSqlException(int line, String reason, String SQLState) {
		super(reason, SQLState);
		this.line = line;
	}

	/**
	 * @param reason
	 * @param cause
	 */
	public CustomSqlException(int line, String reason, Throwable cause) {
		super(reason, cause);
		this.line = line;
	}

	/**
	 * @param reason
	 * @param SQLState
	 * @param vendorCode
	 */
	public CustomSqlException(int line, String reason, String SQLState,
			int vendorCode) {
		super(reason, SQLState, vendorCode);
		this.line = line;
	}

	/**
	 * @param reason
	 * @param sqlState
	 * @param cause
	 */
	public CustomSqlException(int line, String reason, String sqlState,
			Throwable cause) {
		super(reason, sqlState, cause);
		this.line = line;
	}

	/**
	 * @param reason
	 * @param sqlState
	 * @param vendorCode
	 * @param cause
	 */
	public CustomSqlException(int line, String reason, String sqlState,
			int vendorCode, Throwable cause) {
		super(reason, sqlState, vendorCode, cause);
		this.line = line;
	}

	/**
	 * @return the line
	 */
	public int getLine() {
		return line;
	}

}
