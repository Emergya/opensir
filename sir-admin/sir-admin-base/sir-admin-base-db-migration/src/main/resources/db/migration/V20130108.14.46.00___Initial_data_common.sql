SET search_path = ohiggins, pg_catalog;


--
-- Data for Name: gis_authority_type; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--

INSERT INTO gis_authority_type VALUES (1, false, '2012-10-02 16:27:49.250453', 'Municipalidad', '2012-10-02 16:27:49.250453');
INSERT INTO gis_authority_type VALUES (2, false, '2012-10-02 16:27:49.250453', 'Servicio Público', '2012-10-02 16:27:49.250453');
INSERT INTO gis_authority_type VALUES (3, false, '2012-10-02 16:27:49.250453', 'Otros', '2012-10-02 16:27:49.250453');
INSERT INTO gis_authority_type VALUES (4, true, '2012-10-02 16:27:49.250453', 'Ciudadano', '2012-10-02 16:27:49.250453');

--
-- Data for Name: gis_layer_type; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--

INSERT INTO gis_layer_type VALUES (1, 'WMS', 'Raster');
INSERT INTO gis_layer_type VALUES (2, 'WFS', 'Raster');
INSERT INTO gis_layer_type VALUES (3, 'KML', 'Raster');
INSERT INTO gis_layer_type VALUES (4, 'WMS', 'Vectorial');
INSERT INTO gis_layer_type VALUES (5, 'WFS', 'Vectorial');
INSERT INTO gis_layer_type VALUES (6, 'KML', 'Vectorial');


--
-- Data for Name: gis_permission; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--

INSERT INTO gis_permission VALUES (1, '2012-10-02 16:27:49.250453', 'Permiso1', '2012-10-02 16:27:49.250453');
INSERT INTO gis_permission VALUES (2, '2012-10-02 16:27:49.250453', 'Permiso2', '2012-10-02 16:27:49.250453');
INSERT INTO gis_permission VALUES (3, '2012-10-02 16:27:49.250453', 'Permiso3', '2012-10-02 16:27:49.250453');
INSERT INTO gis_permission VALUES (4, '2012-10-02 16:27:49.250453', 'Permiso4', '2012-10-02 16:27:49.250453');
INSERT INTO gis_permission VALUES (5, '2012-10-02 16:27:49.250453', 'Permiso5', '2012-10-02 16:27:49.250453');


--
-- Data for Name: gis_permission_by_authtype; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--

INSERT INTO gis_permission_by_authtype VALUES (1, 2);
INSERT INTO gis_permission_by_authtype VALUES (1, 4);
INSERT INTO gis_permission_by_authtype VALUES (2, 5);
INSERT INTO gis_permission_by_authtype VALUES (2, 1);
INSERT INTO gis_permission_by_authtype VALUES (3, 5);
INSERT INTO gis_permission_by_authtype VALUES (4, 3);
INSERT INTO gis_permission_by_authtype VALUES (4, 1);