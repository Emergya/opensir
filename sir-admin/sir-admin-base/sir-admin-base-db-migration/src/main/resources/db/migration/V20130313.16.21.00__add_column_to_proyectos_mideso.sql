ALTER TABLE ohiggins.gis_proyectos_mideso
   ADD COLUMN fecha_ingreso_sni character varying(2048);
COMMENT ON COLUMN ohiggins.gis_proyectos_mideso.fecha_ingreso_sni IS ' Campo “FECHA INGRESO S.N.I” en la planilla “Proyectos_MIDESO”';


-- INSERT INTO ohiggins.gis_file_column(
--             id, create_date, dbname, name, type, update_date, filetype_id)
--     VALUES (8007, CURRENT_TIMESTAMP, 'fecha_ingreso_sni', 'FECHA INGRESO S.N.I', 'String', CURRENT_TIMESTAMP, 8000);
