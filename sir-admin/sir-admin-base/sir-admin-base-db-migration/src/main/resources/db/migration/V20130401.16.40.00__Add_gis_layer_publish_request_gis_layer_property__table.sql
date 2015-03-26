-- Add table gis_layer_publish_request_gis_layer_property

CREATE TABLE  gis_layer_publish_request_gis_layer_property
(
  gis_layer_publish_request_id bigint NOT NULL,
  properties_id bigint NOT NULL,
  CONSTRAINT fk_gis_layer_publish_request_id FOREIGN KEY (gis_layer_publish_request_id)
      REFERENCES gis_layer_publish_request (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT fk_properties_id FOREIGN KEY (properties_id)
      REFERENCES gis_layer_property (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION,
  CONSTRAINT gis_layer_publish_request_gis_layer_property_properties_id_key UNIQUE (properties_id )
)
WITH (
  OIDS=FALSE
);
ALTER TABLE gis_layer_publish_request_gis_layer_property
  OWNER TO ohiggins;

