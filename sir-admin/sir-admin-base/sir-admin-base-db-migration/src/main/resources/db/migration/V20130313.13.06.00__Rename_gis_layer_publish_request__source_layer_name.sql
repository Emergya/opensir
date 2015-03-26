-- Rename source_layer_name from gis_layer_publish_request and add column updated_layer_id


ALTER TABLE ohiggins.gis_layer_publish_request DROP COLUMN source_layer_name;
ALTER TABLE ohiggins.gis_layer_publish_request ADD COLUMN updated_layer_id bigint;

