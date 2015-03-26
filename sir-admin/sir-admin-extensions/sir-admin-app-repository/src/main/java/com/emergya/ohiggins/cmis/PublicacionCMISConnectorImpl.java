package com.emergya.ohiggins.cmis;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.emergya.ohiggins.cmis.bean.EstudioFilter;
import com.emergya.ohiggins.cmis.bean.Publicacion;

@Repository
public final class PublicacionCMISConnectorImpl extends CMISConnectorImpl<Publicacion, EstudioFilter>
	implements PublicacionCMISConnector {

}
