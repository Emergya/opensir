/*
 * LayerPublishRequestEntity.java
 * 
 * Copyright (C) 2011
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
 * Authors:: Jose Alfonso Riera(mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.model;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

/**
 * Entidad de capa
 * 
 * @author <a href="mailto:jariera@emergya.com">jariera</a>
 * 
 */
@SuppressWarnings("unchecked")
@Entity
@Table(name = "gis_layer_publish_request")
public class LayerPublishRequestEntity implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1267790121416214231L;

	public static final String PENDING_STATE = "PENDIENTE";

	private Long id;
	private Date fechasolicitud;
	private Date fecharespuesta;
	private String estado;
	private Boolean actualizacion;
	private String nombredeseado;
	private String recursoservidor;
	private String comentario;
	private Boolean leida;
	private Date fechacreacion;
	private Date fechaactualizacion;
	private String tmpLayerName;

	private UserEntity user;
	private AuthorityEntity auth;

	// We store the id (and the name and type)
	// directly because we DON'T want referencial integrity,
	// because we want to be able to delete the source layer with existings
	// requests.
	private Long sourceLayerId;
	private String sourceLayerName;
	private LayerTypeEntity sourceLayerType;
	private LayerMetadataTmpEntity metadata;

	private String publishedFolder;

	private Long updatedLayerId;
	
	private List<LayerPropertyEntity> properties;
	private List<StyleEntity> styles;

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * Constructor por defecto.
	 */
	public LayerPublishRequestEntity() {
	}

	@Column(name = "fecha_solicitud")
	public Date getFechasolicitud() {
		return fechasolicitud;
	}

	public void setFechasolicitud(Date fechasolicitud) {
		this.fechasolicitud = fechasolicitud;
	}

	@Column(name = "fecha_respuesta")
	public Date getFecharespuesta() {
		return fecharespuesta;
	}

	public void setFecharespuesta(Date fecharespuesta) {
		this.fecharespuesta = fecharespuesta;
	}

	@Column(name = "estado")
	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	@Column(name = "actualizacion")
	public Boolean getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Boolean actualizacion) {
		this.actualizacion = actualizacion;
	}

	@Column(name = "nombre_deseado")
	public String getNombredeseado() {
		return nombredeseado;
	}

	public void setNombredeseado(String nombredeseado) {
		this.nombredeseado = nombredeseado;
	}

	@Column(name = "recurso_servidor")
	public String getRecursoservidor() {
		return recursoservidor;
	}

	public void setRecursoservidor(String recursoservidor) {
		this.recursoservidor = recursoservidor;
	}

	@Column(name = "comentario")
	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	@Column(name = "leida")
	public Boolean getLeida() {
		return leida;
	}

	public void setLeida(Boolean leida) {
		this.leida = leida;
	}

	@Column(name = "create_date")
	public Date getFechacreacion() {
		return fechacreacion;
	}

	public void setFechacreacion(Date fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

	@Column(name = "update_date")
	public Date getFechaactualizacion() {
		return fechaactualizacion;
	}

	public void setFechaactualizacion(Date fechaactualizacion) {
		this.fechaactualizacion = fechaactualizacion;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "layer_publish_request_user_id")
	public UserEntity getUser() {
		return user;
	}

	public void setUser(UserEntity user) {
		this.user = user;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "layer_publish_request_auth_id")
	public AuthorityEntity getAuth() {
		return auth;
	}

	public void setAuth(AuthorityEntity auth) {
		this.auth = auth;
	}

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "layer_publish_request_metadata_tmp_id")
	public LayerMetadataTmpEntity getMetadata() {
		return metadata;
	}

	public void setMetadata(LayerMetadataTmpEntity metadata) {
		this.metadata = metadata;
	}
	
	@OneToMany(targetEntity = LayerPropertyEntity.class, orphanRemoval = true, cascade = { CascadeType.ALL }, fetch = FetchType.LAZY)
	public List<LayerPropertyEntity> getProperties() {
		return this.properties;
	}
	
	public void setProperties(List<LayerPropertyEntity> properties) {
		this.properties = properties;
	}

	//TODO: Update the database's column name to tmp_layer_name, when 
	// it doesn't breaks other people's work.
	@Column(name = "table_name")
	public String getTmpLayerName() {
		return tmpLayerName;
	}

	public void setTmpLayerName(String tmpLayerName) {
		this.tmpLayerName = tmpLayerName;
	}

	@Column(name = "published_folder")
	public String getPublishedFolder() {
		return publishedFolder;
	}

	public void setPublishedFolder(String publishedFolder) {
		this.publishedFolder = publishedFolder;
	}

	@Column(name = "source_layer_id")
	public Long getSourceLayerId() {
		return sourceLayerId;
	}

	public void setSourceLayerId(Long sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
	}

	@OneToOne
	@JoinColumn(name = "source_layer_type_id")
	public LayerTypeEntity getSourceLayerType() {
		return sourceLayerType;
	}

	public void setSourceLayerType(LayerTypeEntity layerType) {
		this.sourceLayerType = layerType;
	}

	@Column(name = "updated_layer_id")
	public Long getUpdatedLayerId() {
		return this.updatedLayerId;
	}

	public void setUpdatedLayerId(Long updatedLayerId) {
		this.updatedLayerId = updatedLayerId;
	}

	@Column(name = "source_layer_name")
	public String getSourceLayerName() {
		return this.sourceLayerName;
	}

	public void setSourceLayerName(String name) {
		this.sourceLayerName = name;
	}
}
