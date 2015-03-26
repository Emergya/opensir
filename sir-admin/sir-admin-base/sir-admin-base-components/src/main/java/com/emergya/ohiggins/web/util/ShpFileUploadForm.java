/* ShpFileUploadForm.java
 * 
 * Copyright (C) 2013
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

import org.springframework.web.multipart.MultipartFile;

import com.emergya.persistenceGeo.utils.FileUtils;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>.
 * 
 */
public class ShpFileUploadForm {
	// private static final String ZIP_MIME_TYPE = "application/zip";
	// private static final String PKZIP_MIME_TYPE = "multipart/x-zip";
	// private static final String X_ZIP_COPMRESSED =
	// "application/x-zip-compressed";

	MultipartFile file;
	String name;

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public MultipartFile getFile() {
		MultipartFile f = null;
		if (file != null && !file.isEmpty()) {
			f = file;
		}
		return f;
	}

	public void setFile(MultipartFile file) {
		this.file = file;
	}

	/**
	 * 
	 * @return <code>true</code> si el fichero es un ZIP, <code>false</code> en
	 *         otro caso.
	 * 
	 */
	public boolean validateFiletype() {
		InputStream in = null;
		boolean isZip = true;
		try {
			in = getFile().getInputStream();
			isZip = FileUtils.checkIfInputStreamIsZip(in);

		} catch (Exception e) {
			isZip = false;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
			} catch (IOException ioe) {

			}
		}

		return isZip;

		// String contentType = getFile().getContentType();
		//
		// boolean result = ZIP_MIME_TYPE.equalsIgnoreCase(contentType)
		// || PKZIP_MIME_TYPE.equalsIgnoreCase(contentType)
		// || X_ZIP_COPMRESSED.equalsIgnoreCase(contentType);
		//
		// return result;
	}
}
