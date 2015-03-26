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

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.chemistry.opencmis.commons.data.PropertyData;
import org.apache.log4j.Logger;


public class Publicacion implements ADocument, Cloneable {

	private String identificador;
	private String maintFreq;
	private Calendar dateNext;
	private String usrDefFreq;
	private String maintScp;
	private String upScpDesc;
	private String maintCont;
	private String maintNode;
	private String distFormat;
	private String distributor;
	private String distTranOps;
	private Integer anyo;
	private String sector;
	private String nivelTerritorial;
	private String nombre;
	private String autor;
	private String institucion;
	private String resumen;
	private ADocumentState estado;
	private String usuario;
	private Calendar respuesta;
	private Calendar solicitud;
	private String comentario;
	private DateFormat format = DateFormat.getDateTimeInstance();
	
	@Override
	public String getPrefix() {		
		return PublicacionModel.PREFIX;
	}

	public String getMaintFreq() {
		return maintFreq;
	}

	public void setMaintFreq(String maintFreq) {
		this.maintFreq = maintFreq;
	}

	public Calendar getDateNext() {
		return dateNext;
	}

	public void setDateNext(Calendar dateNext) {
		this.dateNext = dateNext;
	}

	public String getUsrDefFreq() {
		return usrDefFreq;
	}

	public void setUsrDefFreq(String usrDefFreq) {
		this.usrDefFreq = usrDefFreq;
	}

	public String getMaintScp() {
		return maintScp;
	}

	public void setMaintScp(String maintScp) {
		this.maintScp = maintScp;
	}

	public String getUpScpDesc() {
		return upScpDesc;
	}

	public void setUpScpDesc(String upScpDesc) {
		this.upScpDesc = upScpDesc;
	}

	public String getMaintCont() {
		return maintCont;
	}

	public void setMaintCont(String maintCont) {
		this.maintCont = maintCont;
	}

	public String getMaintNode() {
		return maintNode;
	}

	public void setMaintNode(String maintNode) {
		this.maintNode = maintNode;
	}

	public String getDistFormat() {
		return distFormat;
	}

	public void setDistFormat(String distFormat) {
		this.distFormat = distFormat;
	}

	public String getDistributor() {
		return distributor;
	}

	public void setDistributor(String distributor) {
		this.distributor = distributor;
	}

	public String getDistTranOps() {
		return distTranOps;
	}

	public void setDistTranOps(String distTranOps) {
		this.distTranOps = distTranOps;
	}

	public Integer getAnyo() {
		return anyo;
	}

	public void setAnyo(Integer anyo) {
		this.anyo = anyo;
	}

	public String getSector() {
		return sector;
	}

	public void setSector(String sector) {
		this.sector = sector;
	}

	public String getNivelTerritorial() {
		return nivelTerritorial;
	}

	public void setNivelTerritorial(String nivelTerritorial) {
		this.nivelTerritorial = nivelTerritorial;
	}

        @Override
	public String getName() {
		return nombre;
	}

	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	public String getAutor() {
		return autor;
	}

	public void setAutor(String autor) {
		this.autor = autor;
	}

	public String getInstitucion() {
		return institucion;
	}

	public void setInstitucion(String institucion) {
		this.institucion = institucion;
	}

	public String getResumen() {
		return resumen;
	}

	public void setResumen(String resumen) {
		this.resumen = resumen;
	}

	public ADocumentState getEstado() {
		if (estado == null) {
			estado = ADocumentState.PENDIENTE;
		}
		return estado;
	}

	public void setEstado(ADocumentState estado) {
		this.estado = estado;
	}

	public String getUsuario() {
		return usuario;
	}

	public void setUsuario(String usuario) {
		this.usuario = usuario;
	}

	public Calendar getRespuesta() {
		return respuesta;
	}

	public void setRespuesta(Calendar respuesta) {
		this.respuesta = respuesta;
	}

	public Calendar getSolicitud() {
		return solicitud;
	}

	public void setSolicitud(Calendar solicitud) {
		this.solicitud = solicitud;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((anyo == null) ? 0 : anyo.hashCode());
		result = prime * result + ((autor == null) ? 0 : autor.hashCode());
		result = prime * result
				+ ((dateNext == null) ? 0 : dateNext.hashCode());
		result = prime * result
				+ ((distFormat == null) ? 0 : distFormat.hashCode());
		result = prime * result
				+ ((distTranOps == null) ? 0 : distTranOps.hashCode());
		result = prime * result
				+ ((distributor == null) ? 0 : distributor.hashCode());
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result
				+ ((institucion == null) ? 0 : institucion.hashCode());
		result = prime * result
				+ ((maintCont == null) ? 0 : maintCont.hashCode());
		result = prime * result
				+ ((maintFreq == null) ? 0 : maintFreq.hashCode());
		result = prime * result
				+ ((maintNode == null) ? 0 : maintNode.hashCode());
		result = prime * result
				+ ((maintScp == null) ? 0 : maintScp.hashCode());
		result = prime
				* result
				+ ((nivelTerritorial == null) ? 0 : nivelTerritorial.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((respuesta == null) ? 0 : respuesta.hashCode());
		result = prime * result + ((resumen == null) ? 0 : resumen.hashCode());
		result = prime * result + ((sector == null) ? 0 : sector.hashCode());
		result = prime * result
				+ ((solicitud == null) ? 0 : solicitud.hashCode());
		result = prime * result
				+ ((upScpDesc == null) ? 0 : upScpDesc.hashCode());
		result = prime * result
				+ ((usrDefFreq == null) ? 0 : usrDefFreq.hashCode());
		result = prime * result + ((usuario == null) ? 0 : usuario.hashCode());
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof Publicacion)) {
			return false;
		}
		Publicacion other = (Publicacion) obj;
		if (anyo == null) {
			if (other.anyo != null) {
				return false;
			}
		} else if (!anyo.equals(other.anyo)) {
			return false;
		}
		if (autor == null) {
			if (other.autor != null) {
				return false;
			}
		} else if (!autor.equals(other.autor)) {
			return false;
		}
		if (dateNext == null) {
			if (other.dateNext != null) {
				return false;
			}
		} else if (dateNext.getTimeInMillis() != other.dateNext
				.getTimeInMillis()) {
			return false;
		}
		if (distFormat == null) {
			if (other.distFormat != null) {
				return false;
			}
		} else if (!distFormat.equals(other.distFormat)) {
			return false;
		}
		if (distTranOps == null) {
			if (other.distTranOps != null) {
				return false;
			}
		} else if (!distTranOps.equals(other.distTranOps)) {
			return false;
		}
		if (distributor == null) {
			if (other.distributor != null) {
				return false;
			}
		} else if (!distributor.equals(other.distributor)) {
			return false;
		}
		if (estado != other.estado) {
			return false;
		}
		if (institucion == null) {
			if (other.institucion != null) {
				return false;
			}
		} else if (!institucion.equals(other.institucion)) {
			return false;
		}
		if (maintCont == null) {
			if (other.maintCont != null) {
				return false;
			}
		} else if (!maintCont.equals(other.maintCont)) {
			return false;
		}
		if (maintFreq == null) {
			if (other.maintFreq != null) {
				return false;
			}
		} else if (!maintFreq.equals(other.maintFreq)) {
			return false;
		}
		if (maintNode == null) {
			if (other.maintNode != null) {
				return false;
			}
		} else if (!maintNode.equals(other.maintNode)) {
			return false;
		}
		if (maintScp == null) {
			if (other.maintScp != null) {
				return false;
			}
		} else if (!maintScp.equals(other.maintScp)) {
			return false;
		}
		if (nivelTerritorial == null) {
			if (other.nivelTerritorial != null) {
				return false;
			}
		} else if (!nivelTerritorial.equals(other.nivelTerritorial)) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (respuesta == null) {
			if (other.respuesta != null) {
				return false;
			}
		} else if (respuesta.getTimeInMillis() != other.respuesta
				.getTimeInMillis()) {
			return false;
		}
		if (resumen == null) {
			if (other.resumen != null) {
				return false;
			}
		} else if (!resumen.equals(other.resumen)) {
			return false;
		}
		if (sector == null) {
			if (other.sector != null) {
				return false;
			}
		} else if (!sector.equals(other.sector)) {
			return false;
		}
		if (solicitud == null) {
			if (other.solicitud != null) {
				return false;
			}
		} else if (solicitud.getTimeInMillis() != other.solicitud
				.getTimeInMillis()) {
			return false;
		}
		if (upScpDesc == null) {
			if (other.upScpDesc != null) {
				return false;
			}
		} else if (!upScpDesc.equals(other.upScpDesc)) {
			return false;
		}
		if (usrDefFreq == null) {
			if (other.usrDefFreq != null) {
				return false;
			}
		} else if (!usrDefFreq.equals(other.usrDefFreq)) {
			return false;
		}
		if (usuario == null) {
			if (other.usuario != null) {
				return false;
			}
		} else if (!usuario.equals(other.usuario)) {
			return false;
		}
		return true;
	}

	/**
	 * @return the identificador
	 */
        @Override
	public String getIdentifier() {
		return identificador;
	}

	/**
	 * @param identificador
	 *            the identificador to set
	 */
        @Override
	public void setIdentifier(String identificador) {
		this.identificador = identificador;
	}

	@Override
	public Publicacion clone() throws CloneNotSupportedException {
		Publicacion res = new Publicacion();

		res.setAnyo(this.getAnyo());
		res.setAutor(this.getAutor());
		res.setDateNext(this.getDateNext());
		res.setDistFormat(this.getDistFormat());
		res.setDistributor(this.getDistributor());
		res.setDistTranOps(this.getDistTranOps());
		res.setEstado(this.getEstado());
		res.setIdentifier(this.getIdentifier());
		res.setInstitucion(this.getInstitucion());
		res.setMaintCont(this.getMaintCont());
		res.setMaintFreq(this.getMaintFreq());
		res.setMaintNode(this.getMaintNode());
		res.setMaintScp(this.getMaintScp());
		res.setNivelTerritorial(this.getNivelTerritorial());
		res.setNombre(this.getName());
		res.setRespuesta(this.getRespuesta());
		res.setResumen(this.getResumen());
		res.setSector(this.getSector());
		res.setSolicitud(this.getSolicitud());
		res.setUpScpDesc(this.getUpScpDesc());
		res.setUsrDefFreq(this.getUsrDefFreq());
		res.setUsuario(this.getUsuario());

		return res;
	}

	public String getDateNextParsed() {
		if (this.dateNext == null) {
			return "";
		}
		return this.format.format(this.dateNext.getTime());
	}

	public String getRespuestaParsed() {
		if (this.respuesta == null) {
			return "";
		}
		return this.format.format(this.respuesta.getTime());
	}

	public String getSolicitudParsed() {
		if (this.solicitud == null) {
			return "";
		}
		return this.format.format(this.solicitud.getTime());
	}

	/**
	 * @return the comentario
	 */
	public String getComentario() {
		return comentario;
	}

	/**
	 * @param comentario
	 *            the comentario to set
	 */
	public void setComentario(String comentario) {
		this.comentario = comentario;
	}
	
        @Override
	public Map<String, Object> toAlfrescoProperties() {            
            	Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.MAINT_FREQ, this.getMaintFreq());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.DATE_NEXT, this.getDateNext());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.USR_DEF_FREQ, this.getUsrDefFreq());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.MAINT_SCP, this.getMaintScp());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.UP_SCP_DESC, this.getUpScpDesc());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.MAINT_CONT, this.getMaintCont());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.MAINT_NODE, this.getMaintNode());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.DIST_FORMAT, this.getDistFormat());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.DISTRIBUTOR, this.getDistributor());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.DIST_TRANS_OPS, this.getDistTranOps());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.ANYO, this.getAnyo());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.SECTOR, this.getSector());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.NIVEL_TERRITORIAL,
				this.getNivelTerritorial());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.NOMBRE, this.getName());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.AUTOR, this.getAutor());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.INSTITUCION, this.getInstitucion());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.RESUMEN, this.getResumen());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.ESTADO, this.getEstado().toString());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.USUARIO, this.getUsuario());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.SOLICITADO, this.getSolicitud());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.RESPUESTA, this.getRespuesta());
		properties.put(PublicacionModel.PREFIX+":"+PublicacionModel.COMENTARIO, this.getComentario());

		
		return properties;
	}

	@Override
	public String getDocumentType() {
		return PublicacionModel.DOCUMENT_TYPE;
	}

	@Override
	public String getStateProperty() {
		return "estado";
	}
	
	@Override
	public String getUserProperty() {
		return "usuario";
	}

	@Override
	public void copyFromProperties(List<PropertyData<?>> properties) {
		for (PropertyData<?> pd : properties) {
			String prop = pd.getId();

			if (prop == null) {
				break;
			} else if (pd.getFirstValue() == null) {
				Logger.getLogger(this.getClass()).trace("propiedad nula: " + prop);
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.MAINT_FREQ)) {
				this.setMaintFreq(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.DATE_NEXT)) {
				this.setDateNext((Calendar) pd.getFirstValue());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.USR_DEF_FREQ)) {
				this.setUsrDefFreq(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.MAINT_SCP)) {
				this.setMaintScp(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.UP_SCP_DESC)) {
				this.setUpScpDesc(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.MAINT_CONT)) {
				this.setMaintCont(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.MAINT_NODE)) {
				this.setMaintNode(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.DIST_FORMAT)) {
				this.setDistFormat(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.DISTRIBUTOR)) {
				this.setDistributor(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.DIST_TRANS_OPS)) {
				this.setDistTranOps(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.ANYO)) {
				this.setAnyo(Integer.valueOf(pd.getFirstValue().toString()));
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.SECTOR)) {
				this.setSector(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.NIVEL_TERRITORIAL)) {
				this.setNivelTerritorial(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.NOMBRE)) {
				this.setNombre(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.AUTOR)) {
				this.setAutor(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.INSTITUCION)) {
				this.setInstitucion(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.RESUMEN)) {
				this.setResumen(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.ESTADO)) {
				this.setEstado(ADocumentState.valueOf(pd.getFirstValue().toString().toUpperCase()));
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.USUARIO)) {
				this.setUsuario(pd.getFirstValue().toString());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.SOLICITADO)) {
				this.setSolicitud((Calendar) pd.getFirstValue());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.RESPUESTA)) {
				this.setRespuesta((Calendar) pd.getFirstValue());
			} else if (prop.equals(PublicacionModel.PREFIX+":"+PublicacionModel.COMENTARIO)) {
				this.setComentario(pd.getFirstValue().toString());
			} else
				Logger.getLogger(this.getClass()).trace("Unknown property: " + prop);
		}
		
	}
}
