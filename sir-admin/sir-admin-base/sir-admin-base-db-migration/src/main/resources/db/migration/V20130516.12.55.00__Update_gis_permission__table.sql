-- insert into gis_permission
insert into ohiggins.gis_permission(id, name, ptype, config) values (2, 'Borrar capa', 'pgeo_removelayer', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (3, 'Hacer capa persistente', 'pgeo_makelayerpersistent', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (4, 'Propiedades de la capa', 'vw_layerproperties', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (5, 'Editar estilos', 'vw_styler', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (6, 'Exportación de SHP', 'vw_exporttoshp', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (7, 'Exportación de KML', 'vw_exporttokml', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (8, 'Mostrar metadatos de la capa seleccionada', 'gxp_metadatainformation', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (9, 'Ver más', 'gxp_loadadditionallayers', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (10, 'Ver toda la la capa', 'gxp_zoomtolayerextent', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (11, 'Imprimir', 'gxp_print', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (12, 'Zoom inicial', 'gxp_zoomtoinitialvalues', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (13, 'Zoom', 'gxp_zoom', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (14, 'Historial de navegación', 'gxp_navigationhistory', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (15, 'Paneo', 'gxp_pancontrol', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (16, 'Gratícula', 'vw_graticuletool', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (19, 'Asistente para añadir capas', 'vw_addlayers', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (21, 'Información de elementos', 'vw_wmsgetfeatureinfo', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (23, 'Instrumentos de Planificación', 'gxp_planificationtools', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (24, 'Canales Temáticos', 'gxp_channeltools', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (28, 'Ver los elementos seleccionados', 'gxp_zoomtoselectedfeatures', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (17, 'Información', 'gxp_extendedtoolbar', '{"buttonText":"Información"}');
insert into ohiggins.gis_permission(id, name, ptype, config) values (18, 'Edición', 'gxp_extendedtoolbar', '{"buttonText":"Edición"}');
insert into ohiggins.gis_permission(id, name, ptype, config) values (20, 'Exportar', 'gxp_extendedtoolbar', '{"buttonText":"Exportar"}');
insert into ohiggins.gis_permission(id, name, ptype, config) values (22, 'Consultas Personalizadas', 'gxp_extendedtoolbar', '{"buttonText":"Consultas Personalizadas"}');
-- Really needed?
insert into ohiggins.gis_permission(id, name, ptype, config) values (1, 'Layer Manager', 'pgeo_layermanager', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (25, 'Query Manager', 'pgeo_featuremanager', '{"id":"querymanager"}');
insert into ohiggins.gis_permission(id, name, ptype, config) values (26, 'Feature Grid', 'gxp_featuregrid', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (27, 'Feature Editor', 'vw_featureeditor', null);
insert into ohiggins.gis_permission(id, name, ptype, config) values (29, 'Feature Manager', 'pgeo_featuremanager', '{"id":"featuremanager"}');
