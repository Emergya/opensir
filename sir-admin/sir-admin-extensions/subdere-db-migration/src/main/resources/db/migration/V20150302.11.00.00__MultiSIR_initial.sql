--
-- PostgreSQL database dump
--
SET search_path = ohiggins, public;

--
-- Data for Name: gis_region; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--
-- table gis_region
CREATE TABLE ohiggins.gis_region (
   id bigint NOT NULL,
   name_region character varying(255),
   prefix_wks character varying(255),
   node_analytics character varying(255),
   node_publicacion character varying(255)
);

-- Constrains
ALTER TABLE ONLY ohiggins.gis_region
   ADD CONSTRAINT gis_region_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ohiggins.gis_region
   ADD CONSTRAINT id_reg_unique_pkey UNIQUE (id);
ALTER TABLE ONLY ohiggins.gis_region
   ADD CONSTRAINT prefix_wks_unique UNIQUE (prefix_wks);
ALTER TABLE ONLY ohiggins.gis_region
   ADD CONSTRAINT node_analytics_unique UNIQUE (node_analytics);
ALTER TABLE ONLY ohiggins.gis_region
   ADD CONSTRAINT node_publicacion_unique UNIQUE (node_publicacion);

--
-- Data for Name: gis_region_zone; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--
-- table gis_region_zone
CREATE TABLE ohiggins.gis_region_zone (
   id_zone bigint NOT NULL,
   id_region bigint NOT NULL
);

-- Constrains
ALTER TABLE ONLY ohiggins.gis_region_zone
   ADD CONSTRAINT gis_region_zone_pkey PRIMARY KEY (id_region,id_zone);
ALTER TABLE ONLY ohiggins.gis_region_zone
   ADD CONSTRAINT id_reg_zone_region_fk FOREIGN KEY (id_region) REFERENCES gis_region (id) MATCH FULL;
ALTER TABLE ONLY ohiggins.gis_region_zone
   ADD CONSTRAINT id_reg_zone_zone_fk FOREIGN KEY (id_zone) REFERENCES gis_zone (id) MATCH FULL;

--
-- Data for Name: gis_region_zone; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--
-- table gis_region_zone
CREATE TABLE ohiggins.gis_region_user (
  id_user bigint NOT NULL,
  id_region bigint NOT NULL
);

-- Constrains
ALTER TABLE ONLY ohiggins.gis_region_user
  ADD CONSTRAINT gis_region_user_pkey PRIMARY KEY (id_region,id_user);
ALTER TABLE ONLY ohiggins.gis_region_user
  ADD CONSTRAINT id_reg_user_region_fk FOREIGN KEY (id_region) REFERENCES gis_region (id) MATCH FULL;
ALTER TABLE ONLY ohiggins.gis_region_user
  ADD CONSTRAINT id_reg_user_user_fk FOREIGN KEY (id_user) REFERENCES gis_user (id) MATCH FULL;

--
-- Data for Name: gis_authority_region; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--
-- table gis_authority_region
CREATE TABLE ohiggins.gis_authority_region (
  id_authority bigint NOT NULL,
  id_region bigint NOT NULL
);

-- Constrains
ALTER TABLE ONLY ohiggins.gis_authority_region
  ADD CONSTRAINT gis_authority_region_pkey PRIMARY KEY (id_authority,id_region);
ALTER TABLE ONLY ohiggins.gis_authority_region
  ADD CONSTRAINT id_aut_reg_region_fk FOREIGN KEY (id_region) REFERENCES gis_region (id) MATCH FULL;
ALTER TABLE ONLY ohiggins.gis_authority_region
  ADD CONSTRAINT id_aut_reg_authority_fk FOREIGN KEY (id_authority) REFERENCES gis_authority (id) MATCH FULL;

--
-- Data for Name: gis_region_layer; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--
-- table gis_region_layer
CREATE TABLE ohiggins.gis_region_layer (
  id_layer bigint NOT NULL,
  id_region bigint NOT NULL
);

-- Constrains
ALTER TABLE ONLY ohiggins.gis_region_layer
  ADD CONSTRAINT gis_region_layer_pkey PRIMARY KEY (id_region,id_layer);
ALTER TABLE ONLY ohiggins.gis_region_layer
  ADD CONSTRAINT id_reg_layer_region_fk FOREIGN KEY (id_region) REFERENCES gis_region (id) MATCH FULL;
ALTER TABLE ONLY ohiggins.gis_region_layer
  ADD CONSTRAINT id_reg_layer_layer_fk FOREIGN KEY (id_layer) REFERENCES gis_layer (id) MATCH FULL;

--
-- Data for Name: gis_region_contacto; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--
-- table gis_region_contacto
CREATE TABLE ohiggins.gis_region_contacto (
  id_contacto bigint NOT NULL,
  id_region bigint NOT NULL
);

-- Constrains
ALTER TABLE ONLY ohiggins.gis_region_contacto
  ADD CONSTRAINT gis_region_contacto_pkey PRIMARY KEY (id_region,id_contacto);
ALTER TABLE ONLY ohiggins.gis_region_contacto
  ADD CONSTRAINT id_reg_contacto_region_fk FOREIGN KEY (id_region) REFERENCES gis_region (id) MATCH FULL;
ALTER TABLE ONLY ohiggins.gis_region_contacto
  ADD CONSTRAINT id_reg_contacto_contacto_fk FOREIGN KEY (id_contacto) REFERENCES gis_contacto (id) MATCH FULL;

--
-- Data for Name: gis_region_faq; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--
-- table gis_region_faq
CREATE TABLE ohiggins.gis_region_faq (
  id_faq bigint NOT NULL,
  id_region bigint NOT NULL
);

-- Constrains
ALTER TABLE ONLY ohiggins.gis_region_faq
  ADD CONSTRAINT gis_region_faq_pkey PRIMARY KEY (id_region,id_faq);
ALTER TABLE ONLY ohiggins.gis_region_faq
  ADD CONSTRAINT id_reg_faq_region_fk FOREIGN KEY (id_region) REFERENCES gis_region (id) MATCH FULL;
ALTER TABLE ONLY ohiggins.gis_region_faq
  ADD CONSTRAINT id_reg_faq_faq_fk FOREIGN KEY (id_faq) REFERENCES gis_faq (id) MATCH FULL;

--
-- Data for Name: gis_region_folder; Type: TABLE DATA; Schema: ohiggins; Owner: ohiggins
--
-- table gis_region_folder
CREATE TABLE ohiggins.gis_region_folder (
  id_folder bigint NOT NULL,
  id_region bigint NOT NULL
);

-- Constrains
ALTER TABLE ONLY ohiggins.gis_region_folder
  ADD CONSTRAINT gis_region_folder_pkey PRIMARY KEY (id_region,id_folder);
ALTER TABLE ONLY ohiggins.gis_region_folder
  ADD CONSTRAINT id_reg_folder_region_fk FOREIGN KEY (id_region) REFERENCES gis_region (id) MATCH FULL;
ALTER TABLE ONLY ohiggins.gis_region_folder
  ADD CONSTRAINT id_reg_folder_folder_fk FOREIGN KEY (id_folder) REFERENCES gis_folder (id) MATCH FULL;

-- Región
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (1, 'Tarapacá', 'TRP', 'workspace://SpacesStore/4a184001-c6b7-4c55-899d-ec0890dc927d', 'workspace://SpacesStore/085d5b6f-9076-437e-8075-abc9a17c237a');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (2, 'Antofagasta', 'AFG', 'workspace://SpacesStore/be4bb116-988d-4200-8a04-7028676e0024', 'workspace://SpacesStore/f76cdec9-63d3-4db3-bc0a-27c27a19518e');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (3, 'Atacama', 'ATC', 'workspace://SpacesStore/83d64ace-abc1-49b3-aa19-a45d9891a808', 'workspace://SpacesStore/b270e1fd-c7c9-4506-9f77-1ac3651ff00a');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (4, 'Coquimbo', 'CQB', 'workspace://SpacesStore/ba4f920a-9200-4551-8fc4-0c166d95a966', 'workspace://SpacesStore/263a5061-1804-47ea-a832-4983caed4ce6');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (5, 'Valparaíso', 'VPR', 'workspace://SpacesStore/6fd1ff8c-dc1b-465f-8f6e-df43393fb6b0', 'workspace://SpacesStore/16af89a2-cb1e-4da5-af5f-680d13b8c8de');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (6, 'Región del Libertador Gral. Bernardo O’Higgins', 'OHG', 'workspace://SpacesStore/2e401a70-c850-43cf-b1cf-abe70d1cf36a', 'workspace://SpacesStore/59591670-2892-46ae-bdb2-c9eeba157400');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (7, 'Región del Maule', 'MAU', 'workspace://SpacesStore/f5f2c621-65a8-4b0e-b0cb-4766229e5101', 'workspace://SpacesStore/30b07680-1f10-486b-a389-c7ff4597a232');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (8, 'Región del Biobío', 'BBO', 'workspace://SpacesStore/3749500d-7bec-4fe7-804a-8a51bab6157b', 'workspace://SpacesStore/49101e37-538e-4067-a9a7-cb96307b5cf8');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (9, 'Región de la Araucanía', 'ARC', 'workspace://SpacesStore/e870c45d-ff31-4e53-9497-b8eba2d6848f', 'workspace://SpacesStore/cc846727-001d-4df1-a1e0-eed4470df466');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (10, 'Región de Los Lagos', 'LAG', 'workspace://SpacesStore/3704d6f4-ab2f-4182-9c2f-bbc0e14a441b', 'workspace://SpacesStore/3da32b73-8ece-49e4-82e0-25c9f47f8977');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (11, 'Región Aisén del Gral. Carlos Ibáñez del Campo', 'AIS', 'workspace://SpacesStore/92f34ab8-1f67-4745-9b32-c3d6d141fdba', 'workspace://SpacesStore/1aeabc4d-0ad5-4800-8800-b12b29e8a816');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (12, 'Región de Magallanes y de la Antártica Chilena', 'MAG', 'workspace://SpacesStore/4397b086-6f60-49ac-a32a-7a4bf5d78b9f', 'workspace://SpacesStore/10861518-1a4b-4fe3-b041-72f75921dd15');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (13, 'Región Metropolitana de Santiago', 'SNT', 'workspace://SpacesStore/5241ef42-4436-4134-aaf1-d601edf08ecc', 'workspace://SpacesStore/76f0fdf5-a991-4fc1-a7f1-fb9755791be3');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (14, 'Región de Los Ríos', 'RIO', 'workspace://SpacesStore/642f670e-01eb-4d11-a26a-a391c6737b83', 'workspace://SpacesStore/8030c88b-cb27-41bb-a829-15deffc6d343');
INSERT INTO gis_region(id, name_region, prefix_wks, node_analytics, node_publicacion)
	VALUES (15, 'Arica y Parinacota ', 'ARI', 'workspace://SpacesStore/9406b876-27fd-4ad0-b98f-2d2afa593c18', 'workspace://SpacesStore/bf541ad7-9296-4455-963d-fb95575f3a4f');

COMMIT;
