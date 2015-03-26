ALTER TABLE ohiggins.gis_chileindica_inversion_data
ADD COLUMN  update_status character varying (16);
ALTER TABLE ohiggins.gis_chileindica_inversion_data
ADD COLUMN  last_update_try timestamp;

