-- Remove layer_publish_request_layer_id from gis_layer_publish_request and added substitutes.

ALTER TABLE ohiggins.gis_layer_publish_request DROP CONSTRAINT fk349dce83c8b4137;
ALTER TABLE ohiggins.gis_layer_publish_request DROP COLUMN layer_publish_request_layer_id;
ALTER TABLE ohiggins.gis_layer_publish_request ADD COLUMN source_layer_id bigint;
ALTER TABLE ohiggins.gis_layer_publish_request ADD COLUMN source_layer_name varchar(255);
ALTER TABLE ohiggins.gis_layer_publish_request ADD COLUMN source_layer_type_id bigint;
ALTER TABLE ohiggins.gis_layer_publish_request ADD CONSTRAINT source_layer_type_id_fk FOREIGN KEY (source_layer_type_id) REFERENCES ohiggins.gis_layer_type (id) MATCH FULL;

