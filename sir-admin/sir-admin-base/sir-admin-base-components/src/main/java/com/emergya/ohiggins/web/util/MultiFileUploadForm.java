/* MultiFileUploadForm.java
 * 
 * Copyright (C) 2012
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

import org.springframework.web.multipart.MultipartFile;

/**
 * @author <a href="mailto:jlrodriguez@emergya.com">jlrodriguez</a>
 * 
 */
public class MultiFileUploadForm {
	public static enum FICHEROS_INVERSION {
		SHP_PROYECTOS_GEO, ACUERDOS_CORE, BASE_PREINVERSION_GORE, CHILEINDICA_EJECUCION, CHILEINDICA_EJECUCION_DETALLE, CHILEINDICA_PREINVERSION, PROYECTOS_DACG, PROYECTOS_GEORREFERENCIADOS_MIDESO, PROYECTOS_MIDESO

	};

	MultipartFile shpProyectosGeo, acuerdosCore, basePreGore, chileindicaEjec,
			chileindicaEjecDetalle, chileindicaPreinversion, proyectosDacg,
			projGeoMideso, projMideso;

	/**
	 * @return the shpProyectosGeo
	 */
	public MultipartFile getShpProyectosGeo() {
		return shpProyectosGeo;
	}

	/**
	 * @param shpProyectosGeo
	 *            the shpProyectosGeo to set
	 */
	public void setShpProyectosGeo(MultipartFile shpProyectosGeo) {
		this.shpProyectosGeo = shpProyectosGeo;
	}

	/**
	 * @return the acuerdosCore
	 */
	public MultipartFile getAcuerdosCore() {
		return acuerdosCore;
	}

	/**
	 * @param acuerdosCore
	 *            the acuerdosCore to set
	 */
	public void setAcuerdosCore(MultipartFile acuerdosCore) {
		this.acuerdosCore = acuerdosCore;
	}

	/**
	 * @return the basePreGore
	 */
	public MultipartFile getBasePreGore() {
		return basePreGore;
	}

	/**
	 * @param basePreGore
	 *            the basePreGore to set
	 */
	public void setBasePreGore(MultipartFile basePreGore) {
		this.basePreGore = basePreGore;
	}

	/**
	 * @return the chileindicaEjec
	 */
	public MultipartFile getChileindicaEjec() {
		return chileindicaEjec;
	}

	/**
	 * @param chileindicaEjec
	 *            the chileindicaEjec to set
	 */
	public void setChileindicaEjec(MultipartFile chileindicaEjec) {
		this.chileindicaEjec = chileindicaEjec;
	}

	/**
	 * @return the chileindicaEjecDetalle
	 */
	public MultipartFile getChileindicaEjecDetalle() {
		return chileindicaEjecDetalle;
	}

	/**
	 * @param chileindicaEjecDetalle
	 *            the chileindicaEjecDetalle to set
	 */
	public void setChileindicaEjecDetalle(MultipartFile chileindicaEjecDetalle) {
		this.chileindicaEjecDetalle = chileindicaEjecDetalle;
	}

	/**
	 * @return the chileindicaPreinversion
	 */
	public MultipartFile getChileindicaPreinversion() {
		return chileindicaPreinversion;
	}

	/**
	 * @param chileindicaPreinversion
	 *            the chileindicaPreinversion to set
	 */
	public void setChileindicaPreinversion(MultipartFile chileindicaPreinversion) {
		this.chileindicaPreinversion = chileindicaPreinversion;
	}

	/**
	 * @return the proyectosDacg
	 */
	public MultipartFile getProyectosDacg() {
		return proyectosDacg;
	}

	/**
	 * @param proyectosDacg
	 *            the proyectosDacg to set
	 */
	public void setProyectosDacg(MultipartFile proyectosDacg) {
		this.proyectosDacg = proyectosDacg;
	}

	/**
	 * @return the projGeoMideso
	 */
	public MultipartFile getProjGeoMideso() {
		return projGeoMideso;
	}

	/**
	 * @param projGeoMideso
	 *            the projGeoMideso to set
	 */
	public void setProjGeoMideso(MultipartFile projGeoMideso) {
		this.projGeoMideso = projGeoMideso;
	}

	/**
	 * @return the projMideso
	 */
	public MultipartFile getProjMideso() {
		return projMideso;
	}

	/**
	 * @param projMideso
	 *            the projMideso to set
	 */
	public void setProjMideso(MultipartFile projMideso) {
		this.projMideso = projMideso;
	}

	public FICHEROS_INVERSION compruebaArchivoRecibido() {
		FICHEROS_INVERSION result = null;
		if (shpProyectosGeo != null && !shpProyectosGeo.isEmpty()) {
			result = FICHEROS_INVERSION.SHP_PROYECTOS_GEO;
		} else if (acuerdosCore != null && !acuerdosCore.isEmpty()) {
			result = FICHEROS_INVERSION.ACUERDOS_CORE;
		} else if (basePreGore != null && !basePreGore.isEmpty()) {
			result = FICHEROS_INVERSION.BASE_PREINVERSION_GORE;
		} else if (chileindicaEjec != null && !chileindicaEjec.isEmpty()) {
			result = FICHEROS_INVERSION.CHILEINDICA_EJECUCION;
		} else if (chileindicaEjecDetalle != null
				&& !chileindicaEjecDetalle.isEmpty()) {
			result = FICHEROS_INVERSION.CHILEINDICA_EJECUCION_DETALLE;
		} else if (chileindicaPreinversion != null
				&& !chileindicaPreinversion.isEmpty()) {
			result = FICHEROS_INVERSION.CHILEINDICA_PREINVERSION;
		} else if (projGeoMideso != null && !projGeoMideso.isEmpty()) {
			result = FICHEROS_INVERSION.PROYECTOS_GEORREFERENCIADOS_MIDESO;
		} else if (projMideso != null && !projMideso.isEmpty()) {
			result = FICHEROS_INVERSION.PROYECTOS_MIDESO;
		} else if (proyectosDacg != null && !proyectosDacg.isEmpty()) {
			result = FICHEROS_INVERSION.PROYECTOS_DACG;
		}

		return result;
	}

	public MultipartFile getFile() {
		MultipartFile file = null;
		if (shpProyectosGeo != null && !shpProyectosGeo.isEmpty()) {
			file = shpProyectosGeo;
		} else if (acuerdosCore != null && !acuerdosCore.isEmpty()) {
			file = acuerdosCore;
		} else if (basePreGore != null && !basePreGore.isEmpty()) {
			file = basePreGore;
		} else if (chileindicaEjec != null && !chileindicaEjec.isEmpty()) {
			file = chileindicaEjec;
		} else if (chileindicaEjecDetalle != null
				&& !chileindicaEjecDetalle.isEmpty()) {
			file = chileindicaEjecDetalle;
		} else if (chileindicaPreinversion != null
				&& !chileindicaPreinversion.isEmpty()) {
			file = chileindicaPreinversion;
		} else if (projGeoMideso != null && !projGeoMideso.isEmpty()) {
			file = projGeoMideso;
		} else if (projMideso != null && !projMideso.isEmpty()) {
			file = projMideso;
		} else if (proyectosDacg != null && !proyectosDacg.isEmpty()) {
			file = proyectosDacg;
		}

		return file;
	}

	public String getFilePropertyName() {
		String propertyName = null;
		if (shpProyectosGeo != null && !shpProyectosGeo.isEmpty()) {
			propertyName = "shpProyectosGeo";
		} else if (acuerdosCore != null && !acuerdosCore.isEmpty()) {
			propertyName = "acuerdosCore";
		} else if (basePreGore != null && !basePreGore.isEmpty()) {
			propertyName = "basePreGore";
		} else if (chileindicaEjec != null && !chileindicaEjec.isEmpty()) {
			propertyName = "chileindicaEjec";
		} else if (chileindicaEjecDetalle != null
				&& !chileindicaEjecDetalle.isEmpty()) {
			propertyName = "chileindicaEjecDetalle";
		} else if (chileindicaPreinversion != null
				&& !chileindicaPreinversion.isEmpty()) {
			propertyName = "chileindicaPreinversion";
		} else if (projGeoMideso != null && !projGeoMideso.isEmpty()) {
			propertyName = "projGeoMideso";
		} else if (projMideso != null && !projMideso.isEmpty()) {
			propertyName = "projMideso";
		} else if (proyectosDacg != null && !proyectosDacg.isEmpty()) {
			propertyName = "proyectosDacg";
		}

		return propertyName;
	}

}
