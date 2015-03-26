/*
 * 
 * Copyright (C) 2012
 * 
 * This file is part of Proyecto ohiggins
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
 * Authors:: María Arias de Reyna (mailto:delawen@gmail.com)
 */

package com.emergya.ohiggins.cmis.bean;

/**
 * <ul>
 * <li>Pendiente. Abre una ventana emergente en la que se indica que el
 * documento todavía a la espera de que el administrador realice la acción
 * correspondiente (publicación o denegación)</li>
 * emergente indicando que el documento ha sido aceptado por el administrador.
 * Cuando se cierre esta ventana se ha de eliminar la fila de la tabla y
 * actualizar la tabla de solicitudes de estudios en la base de datos con el
 * estado leída.</li>
 * <li>Rechazada. Al pulsar en un documento con este estado se abre una ventana
 * emergente indicando el motivo de rechazo escrito por el administrador. Cuando
 * se cierre esta ventana se ha de borrar la fila de la tabla de solicitudes y
 * marcarla en la tabla de solicitudes de estudios como leída.</li>
 * </ul>
 * 
 * @author marias
 * 
 */
public enum ADocumentState {
        PRIVATE,
	PENDIENTE,
        ACEPTADA, 
        RECHAZADA, 
        LEIDA;

		public boolean isPublished() {
			return this == ACEPTADA || this == LEIDA;
		}	
}
