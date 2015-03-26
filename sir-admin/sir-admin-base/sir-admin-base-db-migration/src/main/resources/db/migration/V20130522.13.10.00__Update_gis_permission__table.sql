-- delete gis_permission_by_authtype
delete from ohiggins.gis_permission_by_authtype;
-- delete gis_permission
delete from ohiggins.gis_permission where id in (1, 25, 26,	27,	29,	2, 3, 4, 5, 6, 7, 8, 10, 28, 22);
-- Deleted permissions:
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (1, 'Layer Manager', 'pgeo_layermanager', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (25, 'Query Manager', 'pgeo_featuremanager', '{"id":"querymanager"}');
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (26, 'Feature Grid', 'gxp_featuregrid', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (27, 'Feature Editor', 'vw_featureeditor', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (29, 'Feature Manager', 'pgeo_featuremanager', '{"id":"featuremanager"}');
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (2, 'Borrar capa', 'pgeo_removelayer', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (3, 'Hacer capa persistente', 'pgeo_makelayerpersistent', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (4, 'Propiedades de la capa', 'vw_layerproperties', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (5, 'Editar estilos', 'vw_styler', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (6, 'Exportación de SHP', 'vw_exporttoshp', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (7, 'Exportación de KML', 'vw_exporttokml', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (8, 'Mostrar metadatos de la capa seleccionada', 'gxp_metadatainformation', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (10, 'Ver toda la la capa', 'gxp_zoomtolayerextent', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (28, 'Ver los elementos seleccionados', 'gxp_zoomtoselectedfeatures', null);
-- insert into ohiggins.gis_permission(id, name, ptype, config) values (22, 'Consultas Personalizadas', 'gxp_extendedtoolbar', '{"buttonText":"Consultas Personalizadas"}');