-- Add table_name column to gis_layer

ALTER TABLE ohiggins.gis_layer_publish_request ADD COLUMN "table_name" varchar(255);