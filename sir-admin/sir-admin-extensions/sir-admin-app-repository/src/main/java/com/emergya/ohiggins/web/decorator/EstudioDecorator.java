package com.emergya.ohiggins.web.decorator;

import org.displaytag.decorator.TableDecorator;

import com.emergya.ohiggins.cmis.bean.ADocumentState;
import com.emergya.ohiggins.cmis.bean.Publicacion;


@Deprecated()
public class EstudioDecorator extends TableDecorator {

	public String getVer() {
		Publicacion p = (Publicacion) getCurrentRowObject();

		String href = "<a href='download/" + p.getIdentifier() + "'>...</a>";

		return href;
	}

	public String getVerDetalle() {
		Publicacion p = (Publicacion) getCurrentRowObject();

		String href = "<a href='verDetalle/" + p.getIdentifier()
				+ "' target=''><img src='../img/lupa.png' class='icono' /></a>";

		return href;
	}

	/**
	 * target='_blank' para abrir pestaña nueva en navegador
	 * 
	 * @return
	 */
	public String getEstado() {
		Publicacion p = (Publicacion) getCurrentRowObject();

		String href = "";
		
		String linkTpl = "<a href=\"javascript:void(0)\" onclick=\"showStudyStatus('%s', '%s')\">%s</a>";
		
		href= String.format(linkTpl,p.getEstado().name().toLowerCase(), p.getIdentifier(), p.getEstado());
		
		return href;
	}

	public String getEditar() {
		Publicacion p = (Publicacion) getCurrentRowObject();

		String href = "<a href='editar/" + p.getIdentifier()
				+ "' target=''>...</a>";

		return href;
	}

	public EstudioDecorator() {
	}

	public String getEliminar() {
		Publicacion p = (Publicacion) getCurrentRowObject();

		String href = "<a class='enlace-borrar' href='eliminar/"
				+ p.getIdentifier() + "' target=''>...</a>";

		return href;
	}
}
