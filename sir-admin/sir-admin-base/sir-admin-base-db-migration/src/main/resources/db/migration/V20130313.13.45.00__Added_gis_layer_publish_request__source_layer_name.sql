-- Readded source_layer_name to gis_layer_publish_request


ALTER TABLE ohiggins.gis_layer_publish_request ADD COLUMN source_layer_name varchar(255);

