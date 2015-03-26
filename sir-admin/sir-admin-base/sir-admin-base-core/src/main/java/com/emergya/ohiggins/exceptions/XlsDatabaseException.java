/* XlsDatabaseException.java
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
package com.emergya.ohiggins.exceptions;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class XlsDatabaseException extends RuntimeException {

	private static final long serialVersionUID = -1417441923529949842L;
	private int lineNumber;

	/**
	 * 
	 */
	public XlsDatabaseException(int lineNumber) {
		this.lineNumber = lineNumber;
	}

	/**
	 * @param message
	 */
	public XlsDatabaseException(int lineNumer, String message) {
		super(message);
		this.lineNumber = lineNumer;
	}

	/**
	 * @param cause
	 */
	public XlsDatabaseException(int lineNumer, Throwable cause) {
		super(cause);
		this.lineNumber = lineNumer;
	}

	/**
	 * @param message
	 * @param cause
	 */
	public XlsDatabaseException(int lineNumber, String message, Throwable cause) {
		super(message, cause);
		this.lineNumber = lineNumber;
	}

	/**
	 * @return the lineNumber
	 */
	public int getLineNumber() {
		return lineNumber;
	}

}
