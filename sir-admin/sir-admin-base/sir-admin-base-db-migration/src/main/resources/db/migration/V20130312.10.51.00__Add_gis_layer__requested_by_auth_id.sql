
-- Add requested_by_auth_id column to gis_layer

ALTER TABLE ohiggins.gis_layer ADD COLUMN "requested_by_auth_id" bigint;
ALTER TABLE ohiggins.gis_layer ADD CONSTRAINT requested_by_auth_id_fk FOREIGN KEY (requested_by_auth_id) REFERENCES ohiggins.gis_authority (id) MATCH FULL;

