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
 * Authors:: Jose Alfonso Riera (mailto:jariera@emergya.com)
 */
package com.emergya.ohiggins.cmis.bean;

import java.text.DateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.StringUtils;

import com.emergya.ohiggins.cmis.bean.ADocumentFilter;
import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.criteria.Criteria;
import com.emergya.ohiggins.cmis.criteria.EqualsCriteria;
import com.emergya.ohiggins.cmis.criteria.InDocumentCriteria;
import com.emergya.ohiggins.cmis.criteria.InValuesCriteria;
import com.emergya.ohiggins.cmis.criteria.JoinCriteria;
import com.emergya.ohiggins.cmis.criteria.LikeCriteria;

/**
 * Filtro para el buscador de la gestion de estudios
 * 
 * @author jariera
 * @version 1.0
 */
public class EstudioFilter implements ADocumentFilter, Cloneable {

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
	private String textoLibre;
	// Para indicar si borramos o no
	private String eliminar;
	private String ideliminar;

	private String[] nivelesTerritoriales;

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

	public String getNombre() {
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

	/**
	 * @return the identificador
	 */
	public String getIdentificador() {
		return identificador;
	}

	/**
	 * @param identificador
	 *            the identificador to set
	 */
	public void setIdentificador(String identificador) {
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
		res.setIdentifier(this.getIdentificador());
		res.setInstitucion(this.getInstitucion());
		res.setMaintCont(this.getMaintCont());
		res.setMaintFreq(this.getMaintFreq());
		res.setMaintNode(this.getMaintNode());
		res.setMaintScp(this.getMaintScp());
		res.setNivelTerritorial(this.getNivelTerritorial());
		res.setNombre(this.getNombre());
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

	public String getTextoLibre() {
		return textoLibre;
	}

	public void setTextoLibre(String textoLibre) {
		this.textoLibre = textoLibre;
	}

	public String getEliminar() {
		return eliminar;
	}

	public void setEliminar(String eliminar) {
		this.eliminar = eliminar;
	}

	public String getIdeliminar() {
		return ideliminar;
	}

	public void setIdeliminar(String id) {
		this.ideliminar = id;
	}

	public String[] getNivelesTerritoriales() {
		return nivelesTerritoriales;
	}

	public void setNivelesTerritoriales(String[] nivelesTerritoriales) {
		if (nivelesTerritoriales != null) {
			this.nivelesTerritoriales = Arrays.copyOf(nivelesTerritoriales,
					nivelesTerritoriales.length);
		} else {
			this.nivelesTerritoriales = null;
		}
	}

	@Override
	public void addToConditions(JoinCriteria conditions) {
		
		List<Criteria> criteria = new LinkedList<Criteria>();
		
		// año
		if (this.getAnyo() != null
				&& !"".equals(this.getAnyo().toString())) {
			
			conditions.add(new EqualsCriteria(PublicacionModel.ANYO, this.getAnyo().toString()));			
		}

		// autor
		if (!StringUtils.isEmpty(this.getAutor())) {			
			// condiciones.append(PREFIX + objectIdQueryNameAutor + " = '" +
			// estudioFilter.getAutor() + "'");
			
			conditions.add(new LikeCriteria(PublicacionModel.AUTOR, 
					this.getAutor()));
		}

		// sector
		if (!StringUtils.isEmpty(this.getSector())){
			conditions.add(new EqualsCriteria(PublicacionModel.SECTOR, 
					this.getSector()));			
		}

		// nivel Territorial
		if (this.getNivelesTerritoriales() != null
				&& this.getNivelesTerritoriales().length > 0) {			
			
			conditions.add(new InValuesCriteria(PublicacionModel.NIVEL_TERRITORIAL, this.getNivelesTerritoriales()));
			
		
		} else if (!StringUtils.isEmpty(this.getNivelTerritorial())) {				
			conditions.add(new LikeCriteria(PublicacionModel.NIVEL_TERRITORIAL, this.getNivelTerritorial()));			
		}

		// nombre
		if (!StringUtils.isEmpty(this.getNombre())) {
			conditions.add(new LikeCriteria(PublicacionModel.NOMBRE, this.getNombre()));
		}

		// institución
		if (!StringUtils.isEmpty(this.getInstitucion())) {
			conditions.add(new EqualsCriteria(PublicacionModel.INSTITUCION, this.getInstitucion()));			
		}

		// texto libre
		if (!StringUtils.isEmpty(this.getTextoLibre())) {
			String texto = this.getTextoLibre();
			

			
			JoinCriteria textCriteria = new JoinCriteria("OR");
			conditions.add(textCriteria);
			
			textCriteria.add(new LikeCriteria(PublicacionModel.AUTOR, texto));
			textCriteria.add(new LikeCriteria(PublicacionModel.SECTOR,texto));
			textCriteria.add(new LikeCriteria(PublicacionModel.NIVEL_TERRITORIAL, texto));			
			textCriteria.add(new LikeCriteria(PublicacionModel.NOMBRE, texto));
			textCriteria.add(new LikeCriteria(PublicacionModel.INSTITUCION, texto));
			textCriteria.add(new InDocumentCriteria(texto));			

			if (StringUtils.isNumeric(texto)) {
				textCriteria.add(new EqualsCriteria(PublicacionModel.ANYO, texto));
			}
		}		
	}

}
