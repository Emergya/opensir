SET search_path = ohiggins, pg_catalog, public;

INSERT INTO geometry_columns(
	f_table_catalog, f_table_schema, f_table_name, 
	f_geometry_column, coord_dimension, srid, "type") 
values ('', 'ohiggins', 'gis_shp_proyectos_georreferenciados', 'the_geom', 2, 32719, 'POINT');

INSERT INTO geometry_columns(
	f_table_catalog, f_table_schema, f_table_name, 
	f_geometry_column, coord_dimension, srid, "type") 
values ('', 'ohiggins', 'gis_zone', 'geom', 2, 32719, 'MULTIPOLYGON');