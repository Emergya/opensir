package com.emergya.ohiggins.web.decorator;

import org.displaytag.decorator.TableDecorator;

import com.emergya.ohiggins.dto.EstadoPublicacionLayerRequestPublish;
import com.emergya.ohiggins.dto.LayerPublishRequestDto;

public class SolicitudPublicacionCapaDecorator extends TableDecorator {

	public String getVer() {
		return new String();
	}

	public String getVerDetalle() {
		return new String();
	}

	/**
	 * target='_blank' para abrir pestaña nueva en navegador
	 * 
	 * @return
	 */
	public String getEstado() {
		LayerPublishRequestDto p = (LayerPublishRequestDto) getCurrentRowObject();

		String href = "";

		if (p.getEstado().equals(
				EstadoPublicacionLayerRequestPublish.PENDIENTE.toString()))
			href = "<a href='estado/pendiente/" + p.getId() + "' target=''>"
					+ p.getEstado().toString() + "</a>";
		else if (p.getEstado().equals(
				EstadoPublicacionLayerRequestPublish.ACEPTADA.toString())
				|| p.getEstado().equals(
						EstadoPublicacionLayerRequestPublish.LEIDA.toString()))
			href = "<a href='estado/aceptada/" + p.getId() + "' target=''>"
					+ EstadoPublicacionLayerRequestPublish.ACEPTADA.toString()
					+ "</a>";
		else
			href = "<a href='estado/rechazada/" + p.getId() + "' target=''>"
					+ p.getEstado().toString() + "</a>";

		return href;
	}

	/**
	 * target='_blank' para abrir pestaña nueva en navegador
	 * 
	 * @return
	 */
	public String getEstadoAjax() {
		LayerPublishRequestDto p = (LayerPublishRequestDto) getCurrentRowObject();

		String href = "";

		if (p.getEstado().equals(
				EstadoPublicacionLayerRequestPublish.PENDIENTE.toString()))
			href = "<a href=\"#\" class=\"idAbrePopup\" onclick=\"mostrarEstado('estado/pendiente/"
					+ p.getId()
					+ "')\" target=\"\">"
					+ p.getEstado().toString() + "</a>";
		else if (p.getEstado().equals(
				EstadoPublicacionLayerRequestPublish.ACEPTADA.toString())
				|| p.getEstado().equals(
						EstadoPublicacionLayerRequestPublish.LEIDA.toString()))
			href = "<a href=\"#\" class=\"idAbrePopup\" onclick=\"mostrarEstado('estado/aceptada/"
					+ p.getId()
					+ "')\" target=\"\">"
					+ EstadoPublicacionLayerRequestPublish.ACEPTADA.toString()
					+ "</a>";
		else
			href = "<a href=\"#\" class=\"idAbrePopup\" onclick=\"mostrarEstado('estado/rechazada/"
					+ p.getId()
					+ "')\" target=\"\">"
					+ p.getEstado().toString() + "</a>";

		return href;
	}

	public String getEditar() {
		return new String();
	}

	public SolicitudPublicacionCapaDecorator() {
	}

	public String getEliminar() {
		return new String();
	}
}
