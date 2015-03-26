/*
 * LayerPublishRequestDto.java
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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.dto;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.ReflectionUtils;

import com.emergya.persistenceGeo.dto.LayerDto;
import com.google.common.base.Strings;

/**
 * Dto Solicitud Publicacion Nueva Layer
 * 
 * @author jariera
 * @version 1.0
 */
public class LayerPublishRequestDto implements Serializable {

	private static final long serialVersionUID = -4222276053302896611L;
	public static final String ACTION_PUBLISH_NEW ="PUBLISH_NEW";
	public static final String ACTION_PUBLISH_UPDATE="PUBLISH_UPDATE";
	
	public static final String NAME_PROPERTY = "nombredeseado";

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

	private UsuarioDto user;
	private AuthorityDto auth;
	private Long sourceLayerId;
	private String updatedLayerPath;
	private LayerTypeDto sourceLayerType;
	private LayerMetadataTmpDto metadata;

	// Atributos para el form
	private String tipoCapaSeleccionada;
	private Long updatedLayerId;
	private String accionEjecutar;
	private String carpeta;

	private String tableName;

	private String publishedFolder;
	private String sourceLayerName;
	
	private List<LayerPropertyDto> properties;
	
	private List<StyleDto> styles;

	public LayerPublishRequestDto() {

	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Date getFechasolicitud() {
		return fechasolicitud;
	}

	public void setFechasolicitud(Date fechasolicitud) {
		this.fechasolicitud = fechasolicitud;
	}

	public Date getFecharespuesta() {
		return fecharespuesta;
	}

	public void setFecharespuesta(Date fecharespuesta) {
		this.fecharespuesta = fecharespuesta;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}

	public Boolean getActualizacion() {
		return actualizacion;
	}

	public void setActualizacion(Boolean actualizacion) {
		this.actualizacion = actualizacion;
	}

	public String getNombredeseado() {
		return nombredeseado;
	}

	public void setNombredeseado(String nombredeseado) {
		this.nombredeseado = nombredeseado;
	}

	public String getRecursoservidor() {
		return recursoservidor;
	}

	public void setRecursoservidor(String recursoservidor) {
		this.recursoservidor = recursoservidor;
	}

	public String getComentario() {
		return comentario;
	}

	public void setComentario(String comentario) {
		this.comentario = comentario;
	}

	public Boolean getLeida() {
		return leida;
	}

	public void setLeida(Boolean leida) {
		this.leida = leida;
	}

	public Date getFechacreacion() {
		return fechacreacion;
	}

	public void setFechacreacion(Date fechacreacion) {
		this.fechacreacion = fechacreacion;
	}

	public Date getFechaactualizacion() {
		return fechaactualizacion;
	}

	public void setFechaactualizacion(Date fechaactualizacion) {
		this.fechaactualizacion = fechaactualizacion;
	}

	public UsuarioDto getUser() {
		return user;
	}

	public void setUser(UsuarioDto user) {
		this.user = user;
	}

	public AuthorityDto getAuth() {
		return auth;
	}

	public void setAuth(AuthorityDto auth) {
		this.auth = auth;
	}

	public LayerMetadataTmpDto getMetadata() {
		return metadata;
	}

	public void setMetadata(LayerMetadataTmpDto metadata) {
		this.metadata = metadata;
	}

	public String getTipoCapaSeleccionada() {
		return tipoCapaSeleccionada;
	}

	public void setTipoCapaSeleccionada(String tipoCapaSeleccionada) {
		this.tipoCapaSeleccionada = tipoCapaSeleccionada;
	}

	public String getAccionEjecutar() {
		return accionEjecutar;
	}

	public void setAccionEjecutar(String accionEjecutar) {
		this.accionEjecutar = accionEjecutar;
	}

	public String getCarpeta() {
		return carpeta;
	}

	public void setCarpeta(String carpeta) {
		this.carpeta = carpeta;
	}

	public String getTmpLayerName() {
		return tableName;
	}

	public void setTmpLayerName(String tableName) {
		this.tableName = tableName;

	}

	public String getPublishedFolder() {
		return this.publishedFolder;
	}

	public void setPublishedFolder(String publishedFolder) {
		this.publishedFolder = publishedFolder;
	}

	public String getUpdatedLayerPath() {
		return updatedLayerPath;
	}

	public void setUpdatedLayerPath(String updatedLayerPath) {
		this.updatedLayerPath = updatedLayerPath;
	}

	public LayerTypeDto getSourceLayerType() {
		return sourceLayerType;
	}

	public void setSourceLayerType(LayerTypeDto sourceLayerType) {
		this.sourceLayerType = sourceLayerType;
	}

	public Long getSourceLayerId() {
		return sourceLayerId;
	}

	public void setSourceLayerId(Long sourceLayerId) {
		this.sourceLayerId = sourceLayerId;
	}

	public Long getUpdatedLayerId() {
		return updatedLayerId;
	}

	public void setUpdatedLayerId(Long updatedLayerId) {
		this.updatedLayerId = updatedLayerId;
	}

	public boolean isUpdate() {
		return this.updatedLayerId != null;
	}

	public String getSourceLayerName() {
		return this.sourceLayerName;
	}

	public void setSourceLayerName(String name) {
		this.sourceLayerName = name;

	}
	
	public List<LayerPropertyDto> getProperties() {
		return properties;
	}

	public void setProperties(List<LayerPropertyDto> properties) {
		this.properties = properties;
	}
	
	public List<StyleDto> getStyles() {
		return styles;
	}

	public void setStyles(List<StyleDto> styles) {
		this.styles = styles;
	}

	public LayerDto toPGLayerDto() {
		LayerDto layerDto = new LayerDto();
		layerDto.setAuthId(this.auth.getId());
		layerDto.setCreateDate(this.fechacreacion);
		layerDto.setEnabled(false);
		
		layerDto.setName(this.getTmpLayerName());
		
		layerDto.setTableName(this.getTmpLayerName());
		
		String srcLayerName="Desconocido";
		if(!Strings.isNullOrEmpty(this.getSourceLayerName())){
			srcLayerName = this.getSourceLayerName();
		}
		
		if(this.getUpdatedLayerId()==null) {
			// New layer request;
			layerDto.setLayerTitle(String.format(
					"'%s' a partir de '%s' de %s",
					this.getNombredeseado(),
					srcLayerName,
					this.auth.getAuthority()));
		} else {
			layerDto.setLayerTitle(String.format(
					"'%s' actualizada desde '%s' de %s",
					this.getUpdatedLayerPath(),
					srcLayerName,
					this.auth.getAuthority()));
		}
		
		
		layerDto.setPertenece_a_canal(false);
		layerDto.setPublicized(false);
		layerDto.setPublished(false);
		layerDto.setServer_resource(getRecursoservidor());
		layerDto.setType(this.sourceLayerType.getName());		
		layerDto.setTypeId(this.sourceLayerType.getId());
		layerDto.setUser(null);		
		
		Map<String, String> propsMap = new HashMap<String,String>();
		
		if(this.properties != null) {
			for(LayerPropertyDto propDto : properties) {
				propsMap.put(propDto.getName(), propDto.getValue());
			}
		}
		
		// Save properties of the publish request
		propsMap.putAll(toPropMap());
		
		layerDto.setProperties(propsMap);
		
		return layerDto;
	}
	
	public static final String PUBLISH_REQUEST_DATA_PREFIX = "PUBLISH_REQUEST_DATA_";
	
	/**
	 * Return a property <code>Map&lt;String, String&gt;</code> with the declared fields of this instance 
	 * with keys as PUBLISH_REQUET_DATA_PREFIX_${nameField} and value as String.
	 * Only save Boolean, Long or String fields
	 * 
	 * @return <code>Map&lt;String, String&gt;</code>
	 */
	private Map<String, String> toPropMap() {
		Map<String, String> propsMap = new HashMap<String,String>();
		for(Field field: LayerPublishRequestDto.class.getDeclaredFields()){
			try{
				ReflectionUtils.makeAccessible(field);
				String key = PUBLISH_REQUEST_DATA_PREFIX + field.getName().toUpperCase();
				Object value = ReflectionUtils.getField(field, this);
				//Only save long, boolean and string
				if(value instanceof Boolean
						|| value instanceof Long
						|| value instanceof String){
					propsMap.put(key, value.toString());
				}else{
					// Save collections or relations
				}
			}catch (Exception e){
				// TODO: Handle
			}
		}
		return propsMap;
	}

	
}
