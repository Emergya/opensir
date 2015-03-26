-- Add published_folder column to gis_layer

ALTER TABLE ohiggins.gis_layer_publish_request ADD COLUMN "published_folder" varchar(255);

