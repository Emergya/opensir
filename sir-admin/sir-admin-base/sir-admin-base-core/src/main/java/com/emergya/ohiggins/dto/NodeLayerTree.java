/*
 * NodeLayerTree.java
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
 * Authors:: Alejandro Díaz Torres (mailto:adiaz@emergya.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;

/**
 * Representa un nodo en el arbol de capas del modulo cartografico Cada Nodo del
 * arbol puede ser del tipo FolderDto o LayerDto. Se almacena en el atributo
 * node;
 * 
 * @author jariera
 * 
 */
public class NodeLayerTree implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 7355574617246435310L;

	/** Representa un nodo del arbol tipo Folder */
	private static final String FOLDER = "folder";

	/** Representa un nodo del arbol tipo Layer */
	private static final String LAYER = "layer";

	private static final String CHANNEL = "canal";

	/** Representa el nodo a pintar en el arbol */
	private Object node;

	/** Tipo de nodo : FOLDER, LAYER */
	private String tipo;

	/** Nivel en la jerarquia que representa en el arbol */
	private int nivel;

	public Object getNode() {
		return node;
	}

	public void setNode(Object node) {
		this.node = node;
	}

	public String getTipo() {
		return tipo;
	}

	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	public int getNivel() {
		return nivel;
	}

	public void setNivel(int nivel) {
		this.nivel = nivel;
	}

	public static String getFolder() {
		return FOLDER;
	}

	public static String getLayer() {
		return LAYER;
	}

	public static String getChannel() {
		return CHANNEL;
	}

}
