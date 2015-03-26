SET search_path = ohiggins, pg_catalog;

-- SELECT para cambiarle el valor a la secuencia
SELECT setval('ohiggins.gis_layer_type_seq', 6, true);
-- Insert para añadir el tipo postgis a los tipos de capas disponibles 
insert into ohiggins.gis_layer_type (id, name, tipo) 
values(nextval('ohiggins.gis_layer_type_seq'), 'postgis', 'Vectorial');
-- Insert para añadir el tipo geotiff a los tipos de capas disponibles
insert into ohiggins.gis_layer_type (id, name, tipo) 
values(nextval('ohiggins.gis_layer_type_seq'), 'geotiff', 'Raster');
-- Sentencia para añadir una columna nueva a la tabla
ALTER TABLE ohiggins.gis_layer_resources ADD COLUMN
type_layer_id bigint;
-- Update para actualizar las capas que ya estén en layer y que sean shp
update gis_layer_resources 
set type_layer_id = (select id 
					from gis_layer_type 
					where name like '%postgis%') 
where table_name like '%shp%';
