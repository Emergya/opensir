ALTER TABLE ohiggins.gis_relacion_intrumentos
   ALTER COLUMN nombre_instrumento TYPE character varying(1024);
ALTER TABLE ohiggins.gis_relacion_intrumentos
   ALTER COLUMN especificacion TYPE text;
ALTER TABLE ohiggins.gis_relacion_intrumentos
   ALTER COLUMN relacion_principal_nombre TYPE character varying(1024);
ALTER TABLE ohiggins.gis_relacion_intrumentos
   ALTER COLUMN relacion_asociada_nombre TYPE character varying(1024);
ALTER TABLE ohiggins.gis_relacion_intrumentos
  DROP CONSTRAINT relacion_instrumentos_gis_chileindica_inversion_data_fk;
ALTER TABLE ohiggins.gis_relacion_intrumentos
  ADD CONSTRAINT relacion_instrumentos_gis_chileindica_inversion_data_fk FOREIGN KEY (id_gis_chileindica_inversion_data)
      REFERENCES ohiggins.gis_chileindica_inversion_data (id) MATCH SIMPLE
      ON UPDATE NO ACTION ON DELETE NO ACTION;