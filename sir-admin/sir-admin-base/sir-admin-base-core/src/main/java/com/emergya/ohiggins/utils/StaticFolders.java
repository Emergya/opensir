/* 
 * StaticFolders.java
 * 
 * Copyright (C) 2011
 * 
 * This file is part of Proyecto ohiggins
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.utils;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Valores precargados en base de datos Ohiggins para pruebas con canales y
 * capas de canales
 * 
 * @author <a href="mailto:adiaz@emergya.com">adiaz</a>
 * 
 */
public class StaticFolders {

	/**
	 * Canales precargados sin zona
	 */
	public static Set<Long> TEST_CHANNEL_ROOT_FOLDERS;
	/**
	 * Canales precargados con zona
	 */
	public static Set<Long> TEST_CHANNEL_ZONE_FOLDERS;
	/**
	 * Canales precargados deshabilitados
	 */
	public static Set<Long> TEST_CHANNEL_DISABLED_FOLDERS;
	/**
	 * Mapa de hijos de canales precargados
	 */
	public static Map<Long, Set<Long>> TEST_CHANNEL_CHILDREN;
	/**
	 * Id del canal precargado 'Agricultura'
	 */
	public static final Long AGRICULTURA = new Long(1);
	/**
	 * Id del canal precargado 'Censo'
	 */
	public static final Long CENSO = new Long(2);
	/**
	 * Id del canal precargado 'Educación'
	 */
	public static final Long EDUCACION = new Long(3);
	/**
	 * Id del canal precargado 'Educación infantil'
	 */
	public static final Long EDUCACION_INFANTIL = new Long(4);
	/**
	 * Id del canal precargado 'Educación media'
	 */
	public static final Long EDUCACION_MEDIA = new Long(5);
	/**
	 * Id del canal precargado 'Medio Ambiente'
	 */
	public static final Long MEDIO_AMBIENTE = new Long(6);
	/**
	 * Id del canal precargado 'Salud'
	 */
	public static final Long SALUD = new Long(7);
	/**
	 * Id del canal precargado 'Rancagua'
	 */
	public static final Long RANCAGUA = new Long(8);
	/**
	 * Id del canal precargado 'Machalí'
	 */
	public static final Long MACHALI = new Long(9);
	/**
	 * Id del canal precargado 'Deshabilitado'
	 */
	public static final Long DISABLED = new Long(10);

	static {
		TEST_CHANNEL_ROOT_FOLDERS = new HashSet<Long>();
		TEST_CHANNEL_ROOT_FOLDERS.add(AGRICULTURA);
		TEST_CHANNEL_ROOT_FOLDERS.add(CENSO);
		TEST_CHANNEL_ROOT_FOLDERS.add(EDUCACION);
		TEST_CHANNEL_ROOT_FOLDERS.add(MEDIO_AMBIENTE);
		TEST_CHANNEL_ROOT_FOLDERS.add(SALUD);
		TEST_CHANNEL_ZONE_FOLDERS = new HashSet<Long>();
		TEST_CHANNEL_ZONE_FOLDERS.add(RANCAGUA);
		TEST_CHANNEL_ZONE_FOLDERS.add(MACHALI);
		TEST_CHANNEL_DISABLED_FOLDERS = new HashSet<Long>();
		TEST_CHANNEL_DISABLED_FOLDERS.add(DISABLED);
		TEST_CHANNEL_CHILDREN = new HashMap<Long, Set<Long>>();
		Set<Long> children1 = new HashSet<Long>();
		children1.add(EDUCACION_INFANTIL);
		children1.add(EDUCACION_MEDIA);
		TEST_CHANNEL_CHILDREN.put(EDUCACION, children1);
	}

}
