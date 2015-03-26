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

package com.emergya.ohiggins.cmis;

import java.io.File;
import java.util.List;

import org.apache.chemistry.opencmis.client.api.ObjectId;

import com.emergya.ohiggins.cmis.bean.ADocument;
import com.emergya.ohiggins.cmis.bean.ADocumentFilter;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.criteria.Criteria;

public interface CMISConnector<DOCUMENT extends ADocument, FILTER extends ADocumentFilter> {

	/**
	 * el usuario podrá consultar el estado de las solicitudes de publicación
	 * realizadas por él mismo.
	 * 
	 * @param usuario
	 * @return
	 */
	public List<DOCUMENT> getPagedUserRequests(String usuario, Long from,
			Integer pageSize, String colOrder, ASortOrder order);

	/**
	 * Total
	 * 
	 * @param usuario
	 * @return
	 */
	public Long countUserRequests(String usuario);

	/**
	 * Desde esta pantalla los usuarios pueden solicitar la publicación de un
	 * estudio en el repositorio de estudios.
	 * 
	 * Una vez subido el documento, se contactará con el gestor documental (a
	 * través de CMIS) y se publicará el documento en la carpeta de documentos
	 * pendientes. Si todo ha ido bien, sebe crear una entrada en la tabla
	 * solicitudes de estudios de la base de datos con estado pendiente y enviar
	 * un correo electrónico a todos los usuarios administradores de la
	 * aplicación indicando que alguien ha solicitado una publicación de un
	 * estudio.
	 * 
	 */
	public ObjectId createDocument(DOCUMENT p, File f);

	/**
	 * el administrador podrá consultar las solicitudes de publicación de
	 * estudios pendientes de alguna actuación por su parte y acceder a la
	 * ventana de publicación o rechazo.
	 * 
	 * @return
	 */
	public List<DOCUMENT> getPagedPendingRequests(Long from, Integer max,
			String colOrder, ASortOrder ascDesc);

	/**
	 * Total
	 * 
	 * @return
	 */
	public Long countPendingRequests();

	/**
	 * Descarga el documento asociado a la publicación
	 * 
	 * @param identificador
	 * @return
	 */
	public File downloadDocument(String identificador);

	/**
	 * Devuelve los metadatos asociados al identificador
	 * 
	 * @param identificador
	 * @return
	 */
	public DOCUMENT getDocument(String identificador);

	/**
	 * Utilidad para saber el tipo de un documento por su identificador
	 * 
	 * @param identificador
	 * @return
	 */
	public String getMimeType(String identificador);

	/**
	 * Editar datos de la publicación
	 * 
	 * @param p
	 * @return
	 */
	public ObjectId updateDocument(DOCUMENT p);

	/**
	 * Borra la publicación
	 * 
	 * @param p
	 * @return
	 */
	public Boolean deleteDocument(DOCUMENT p);

	/**
	 * Busca publicaciones según #{@link Criteria}
	 * 
	 * @return
	 */
	public List<DOCUMENT> search(Criteria criteria, Long from, Integer max,
			String colOrder, ASortOrder ascDesc);

	/**
	 * Total
	 * 
	 * @return
	 */
	public Long countFilteredDocuments(FILTER filter, ADocumentState[] estados);

	public List<DOCUMENT> getFilteredDocuments(FILTER filter,
			Long from, Integer pageSize, String colOrder, ASortOrder ascDesc,
			ADocumentState[] estados);
}
