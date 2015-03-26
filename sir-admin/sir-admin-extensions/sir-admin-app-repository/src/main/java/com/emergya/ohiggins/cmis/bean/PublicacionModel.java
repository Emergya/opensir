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

public class PublicacionModel {

    private PublicacionModel() {
        // Hides constructor
    }
    public static final String DOCUMENT_TYPE = "P:" + PublicacionModel.PREFIX
            + ":" + "publicacion";
    public static final String PREFIX = "ohg";
    public static final String MAINT_FREQ = "maintFreq";
    public static final String DATE_NEXT = "dateNext";
    public static final String USR_DEF_FREQ = "usrDefFreq";
    public static final String MAINT_SCP = "maintScp";
    public static final String UP_SCP_DESC = "upScpDesc";
    public static final String MAINT_CONT = "maintCont";
    public static final String MAINT_NODE = "maintNode";
    public static final String DIST_FORMAT = "distFormat";
    public static final String DISTRIBUTOR = "distributor";
    public static final String DIST_TRANS_OPS = "distTranOps";
    public static final String SECTOR = "sector";
    public static final String NIVEL_TERRITORIAL = "nivelTerritorial";
    public static final String NOMBRE = "nombre";
    public static final String AUTOR = "autor";
    public static final String ANYO = "anyo";
    public static final String INSTITUCION = "institucion";
    public static final String RESUMEN = "resumen";
    public static final String ESTADO = "estado";
    public static final String SOLICITADO = "solicitud";
    public static final String RESPUESTA = "respuesta";
    public static final String COMENTARIO = "comentario";
    public static final String USUARIO = "usuario";
}
