--
-- PostgreSQL database dump
--
SET search_path = ohiggins, public;

--
-- Data for Name: gis_authority; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--

INSERT INTO gis_authority VALUES (1, current_timestamp , 'I. Municipalidad de Arica', current_timestamp, 1, NULL, NULL);
INSERT INTO gis_authority VALUES (2, current_timestamp , 'I. Municipalidad de Camarones', current_timestamp, 1, NULL, NULL);
INSERT INTO gis_authority VALUES (3, current_timestamp , 'I. Municipalidad de General Lagos', current_timestamp, 1, NULL, NULL);
INSERT INTO gis_authority VALUES (4, current_timestamp , 'I. Municipalidad de Putre', current_timestamp, 1, NULL, NULL);
INSERT INTO gis_authority VALUES (5, current_timestamp , 'GORE Arica y Parinacota', current_timestamp, 2, NULL, NULL);

--
-- Data for Name: gis_user; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--

INSERT INTO gis_user VALUES (1, true, 'Administrador', current_timestamp, 'admin@correoelectronico.com', 'Nombre Admin', 'padmin', '555111220', current_timestamp, 'admin', true, null);
INSERT INTO gis_user VALUES (2, false, 'Apellidos Test1', current_timestamp, 'test1@correoelectronico.com', 'Nombre Test1', 'ptest1', '555111221', current_timestamp, 'test1', true, 1);
INSERT INTO gis_user VALUES (3, false, 'Apellidos Test2', current_timestamp, 'test2@correoelectronico.com', 'Nombre Test2', 'ptest2', '555111222', current_timestamp, 'test2', true, 2);
INSERT INTO gis_user VALUES (4, false, 'Apellidos Test3', current_timestamp, 'test3@correoelectronico.com', 'Nombre Test3', 'ptest3', '555111223', current_timestamp, 'test3', true, 3);
INSERT INTO gis_user VALUES (5, false, 'Apellidos Test4', current_timestamp, 'test4@correoelectronico.com', 'Nombre Test4', 'ptest4', '555111224', current_timestamp, 'test4', true, 4);
INSERT INTO gis_user VALUES (6, false, 'Apellidos Test5', current_timestamp, 'test5@correoelectronico.com', 'Nombre Test5', 'ptest5', '555111225', current_timestamp, 'test5', true, 5);
INSERT INTO gis_user VALUES (7, false, 'Apellidos Test6', current_timestamp, 'test6@correoelectronico.com', 'Nombre Test6', 'ptest6', '555111226', current_timestamp, 'test6', true, 1);
INSERT INTO gis_user VALUES (8, true, 'Usado en tests', current_timestamp, 'test7@correoelectronico.com', 'No borrar', 'ptest7', '555111227', current_timestamp, 'test7', true, null);
INSERT INTO gis_user VALUES (9, true, 'Usado en tests', current_timestamp, 'test8@correoelectronico.com', 'No borrar', 'ptest8', '555111227', current_timestamp, 'test8', true, null);

--
-- Data for Name: gis_file_type; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--

INSERT INTO gis_file_type VALUES (1000, NULL, 'gis_acuerdos_core', 'ACUERDOS_CORE', NULL);
INSERT INTO gis_file_type VALUES (2000, NULL, 'gis_base_preinversion_gore', 'BASE_PREINVERSION_GORE', NULL);
INSERT INTO gis_file_type VALUES (3000, NULL, 'gis_chileindica_ejecucion', 'CHILEINDICA_EJECUCION', NULL);
INSERT INTO gis_file_type VALUES (4000, NULL, 'gis_chileindica_ejecucion_detalle', 'CHILEINDICA_EJECUCION_DETALLE', NULL);
INSERT INTO gis_file_type VALUES (5000, NULL, 'gis_chileindica_preinversion', 'CHILEINDICA_PREINVERSION', NULL);
INSERT INTO gis_file_type VALUES (6000, NULL, 'gis_proyectos_dacg', 'PROYECTOS_DACG', NULL);
INSERT INTO gis_file_type VALUES (7000, NULL, 'gis_proyectos_georreferenciados_mideso', 'PROYECTOS_GEORREFERENCIADOS_MIDESO', NULL);
INSERT INTO gis_file_type VALUES (8000, NULL, 'gis_proyectos_mideso', 'PROYECTOS_MIDESO', NULL);
INSERT INTO gis_file_type VALUES (9000, NULL, 'gis_shp_proyectos_georreferenciados', 'SHP_PROYECTOS_GEO', NULL);

--METADATA FOR ACUERDOS_CORE
DELETE FROM ohiggins.gis_file_column WHERE filetype_id = 1000;
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (1001, CURRENT_TIMESTAMP, 'codigo_bip', 'Código Bip', 'String', CURRENT_TIMESTAMP, 1000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (1002, CURRENT_TIMESTAMP, 'servicio_responsable', 'Servicio Responsable', 'String', CURRENT_TIMESTAMP, 1000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (1003, CURRENT_TIMESTAMP, 'anyo', 'Año', 'String', CURRENT_TIMESTAMP, 1000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (1004, CURRENT_TIMESTAMP, 'etapa', 'Etapa', 'String', CURRENT_TIMESTAMP, 1000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (1005, CURRENT_TIMESTAMP, 'numero_acuerdo', 'Número de acuerdo', 'String', CURRENT_TIMESTAMP, 1000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (1006, CURRENT_TIMESTAMP, 'fecha_acuerdo', 'Fecha de acuerdo', 'String', CURRENT_TIMESTAMP, 1000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (1007, CURRENT_TIMESTAMP, 'costo_total_ms', 'Costo Total', 'Double', CURRENT_TIMESTAMP, 1000);

--METADATA FOR BASE_UNIDAD_PREINVERSION_GORE
DELETE FROM ohiggins.gis_file_column WHERE filetype_id = 2000;
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2001, CURRENT_TIMESTAMP, 'codigo_bip', 'Código Bip', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2002, CURRENT_TIMESTAMP, 'servicio_responsable', 'Servicio Responsable', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2003, CURRENT_TIMESTAMP, 'anyo', 'Año', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2004, CURRENT_TIMESTAMP, 'etapa', 'Etapa', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2005, CURRENT_TIMESTAMP, 'nro_of_institucion', 'N° Of Institucion', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2006, CURRENT_TIMESTAMP, 'carpeta_digital_bip', 'Carpeta BIP', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2007, CURRENT_TIMESTAMP, 'ingresado_mod_preinv_chilenindica', 'Ingresado a Preinversion Chileindica', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2008, CURRENT_TIMESTAMP, 'atributos_que_presenta', 'Atributos que presenta', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2009, CURRENT_TIMESTAMP, 'total_de_atributos', 'Total de atributos', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2010, CURRENT_TIMESTAMP, 'erd', 'ERD 2011-2020', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2011, CURRENT_TIMESTAMP, 'lineamiento_ude', 'Lineamiento UDE', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2012, CURRENT_TIMESTAMP, 'politica_regional_turismo', 'Politica Regional de Turismo', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2013, CURRENT_TIMESTAMP, 'politica_regional_cyt', 'Política Regional de Ciencia y Tecnología', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2014, CURRENT_TIMESTAMP, 'plan_ohiggins_2010_2014', 'Plan O´Higgins 2010-2014', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2015, CURRENT_TIMESTAMP, 'plan_reconstruc_27f', 'Plan Reconstrucción 27F', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2016, CURRENT_TIMESTAMP, 'convenio_programacion', 'Convenio Programación', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2017, CURRENT_TIMESTAMP, 'seleccionada_por_intendente', 'Seleccionada por Intendente', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2018, CURRENT_TIMESTAMP, 'mesa_tecnica', 'Mesa Técnica', 'String', CURRENT_TIMESTAMP, 2000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (2019, CURRENT_TIMESTAMP, 'notas_observaciones', 'Notas - Observaciones', 'String', CURRENT_TIMESTAMP, 2000);

--METADATA FOR CHILEINDICA_EJECUCION
DELETE FROM ohiggins.gis_file_column WHERE filetype_id = 3000;
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (3001, CURRENT_TIMESTAMP, 'codigo_bip', 'Código Bip', 'String', CURRENT_TIMESTAMP, 3000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (3002, CURRENT_TIMESTAMP, 'servicio_responsable', 'Servicio Responsable', 'String', CURRENT_TIMESTAMP, 3000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (3003, CURRENT_TIMESTAMP, 'anyo', 'Año', 'String', CURRENT_TIMESTAMP, 3000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (3004, CURRENT_TIMESTAMP, 'etapa', 'Etapa', 'String', CURRENT_TIMESTAMP, 3000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (3005, CURRENT_TIMESTAMP, 'gastado_anyos_anteriores', 'Gastado Años Anteriores', 'Double', CURRENT_TIMESTAMP, 3000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (3006, CURRENT_TIMESTAMP, 'total_pagado', 'Total Pagado', 'Double', CURRENT_TIMESTAMP, 3000);

--METADATA FOR CHILEINDICA_EJECUCION_DETALLE
DELETE FROM ohiggins.gis_file_column WHERE filetype_id = 4000;
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4001, CURRENT_TIMESTAMP, 'codigo_bip', 'Código Bip', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4002, CURRENT_TIMESTAMP, 'servicio_responsable', 'Servicio Responsable', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4003, CURRENT_TIMESTAMP, 'anyo', 'Año', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4004, CURRENT_TIMESTAMP, 'etapa', 'Etapa', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4005, CURRENT_TIMESTAMP, 'nombre_iniciativa', 'Nombre de la Iniciativa', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4006, CURRENT_TIMESTAMP, 'fuente_financiamiento', 'Fuente Financiamiento', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4007, CURRENT_TIMESTAMP, 'costo_ajustado', 'Costo Ajustado', 'Double', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4008, CURRENT_TIMESTAMP, 'sector', 'Sector', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4009, CURRENT_TIMESTAMP, 'descripcion', 'Descripción', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4010, CURRENT_TIMESTAMP, 'unidad_tecnica', 'Unidad Técnica', 'String', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4011, CURRENT_TIMESTAMP, 'gastado_anyos_anteriores', 'Gastado Años Anteriores', 'Double', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4012, CURRENT_TIMESTAMP, 'pagado', 'Pagado', 'Double', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4013, CURRENT_TIMESTAMP, 'solicitado', 'Solicitado año', 'Double', CURRENT_TIMESTAMP, 4000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (4014, CURRENT_TIMESTAMP, 'comuna', 'Comuna', 'String', CURRENT_TIMESTAMP, 4000);

--METADATA FOR CHILEINDICA_MODULO_PREINVERSION
DELETE FROM ohiggins.gis_file_column WHERE filetype_id = 5000;
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5001, CURRENT_TIMESTAMP, 'codigo_bip', 'Código Bip', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5002, CURRENT_TIMESTAMP, 'servicio_responsable', 'Servicio Responsable', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5003, CURRENT_TIMESTAMP, 'anyo', 'Año', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5005, CURRENT_TIMESTAMP, 'etapa', 'Etapa', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5006, CURRENT_TIMESTAMP, 'nombre_iniciativa', 'Nombre de la Iniciativa', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5007, CURRENT_TIMESTAMP, 'estado', 'Estado', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5008, CURRENT_TIMESTAMP, 'fuente_financiamiento', 'Fuente Financiamiento', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5009, CURRENT_TIMESTAMP, 'via_de_financiamiento', 'Vía de Financiamiento', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5010, CURRENT_TIMESTAMP, 'costo_total', 'Costo Total', 'Double', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5011, CURRENT_TIMESTAMP, 'solicitado_anyo', 'Solicitado Año', 'Double', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5012, CURRENT_TIMESTAMP, 'sector', 'Sector', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5013, CURRENT_TIMESTAMP, 'comuna', 'Comuna', 'String', CURRENT_TIMESTAMP, 5000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (5014, CURRENT_TIMESTAMP, 'observaciones', 'Observaciones', 'String', CURRENT_TIMESTAMP, 5000);

--METADATA FOR PROYECTOS DACG
DELETE FROM ohiggins.gis_file_column WHERE filetype_id = 6000;
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (6001, CURRENT_TIMESTAMP, 'codigo_bip', 'Código Bip', 'String', CURRENT_TIMESTAMP, 6000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (6002, CURRENT_TIMESTAMP, 'servicio_responsable', 'Servicio Responsable', 'String', CURRENT_TIMESTAMP, 6000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (6003, CURRENT_TIMESTAMP, 'anyo', 'Año', 'String', CURRENT_TIMESTAMP, 6000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (6004, CURRENT_TIMESTAMP, 'etapa', 'Etapa', 'String', CURRENT_TIMESTAMP, 6000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (6005, CURRENT_TIMESTAMP, 'observacion', 'Observación', 'String', CURRENT_TIMESTAMP, 6000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (6006, CURRENT_TIMESTAMP, 'fecha_observacion', 'Fecha Observación', 'String', CURRENT_TIMESTAMP, 6000);

-- METADATA FOR PROYECTOS MIDESO
DELETE FROM ohiggins.gis_file_column WHERE filetype_id = 8000;
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (8001, CURRENT_TIMESTAMP, 'codigo_bip', 'Código Bip', 'String', CURRENT_TIMESTAMP, 8000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (8002, CURRENT_TIMESTAMP, 'servicio_responsable', 'Servicio Responsable', 'String', CURRENT_TIMESTAMP, 8000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (8003, CURRENT_TIMESTAMP, 'anyo', 'Año', 'String', CURRENT_TIMESTAMP, 8000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (8004, CURRENT_TIMESTAMP, 'etapa', 'Etapa', 'String', CURRENT_TIMESTAMP, 8000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (8005, CURRENT_TIMESTAMP, 'rate', 'RATE', 'String', CURRENT_TIMESTAMP, 8000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (8006, CURRENT_TIMESTAMP, 'fecha_rate', 'FECHA RATE', 'String', CURRENT_TIMESTAMP, 8000);

--METADATA FOR SHP_PROYECTOS_GEO
DELETE FROM ohiggins.gis_file_column WHERE filetype_id = 9000;
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (9001, CURRENT_TIMESTAMP, 'cod_bip', 'COD_BIP', 'String', CURRENT_TIMESTAMP, 9000);
INSERT INTO ohiggins.gis_file_column(id, create_date, dbname, name, type, update_date, filetype_id) VALUES (9002, CURRENT_TIMESTAMP, 'nombre', 'NOMBRE', 'String', CURRENT_TIMESTAMP, 9000);





COMMIT;
