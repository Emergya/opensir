SET search_path = ohiggins, pg_catalog;

ALTER TABLE ohiggins.gis_layer_resources ADD COLUMN
workspace_name CHARACTER VARYING (256);
