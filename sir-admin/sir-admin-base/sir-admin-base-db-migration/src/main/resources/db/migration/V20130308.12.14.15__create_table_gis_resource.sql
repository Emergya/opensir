SET search_path = ohiggins, pg_catalog;

-- table gis_resource
CREATE TABLE ohiggins.gis_resource (
    id bigint NOT NULL,
    data oid,
    name character varying(255),
    size bigint,
    type character varying(255),
    access_id bigint
);

--sequence
CREATE SEQUENCE ohiggins.gis_resource_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;

-- constrains
ALTER TABLE ONLY ohiggins.gis_resource
    ADD CONSTRAINT gis_resource_pkey PRIMARY KEY (id);
ALTER TABLE ONLY ohiggins.gis_resource
    ADD CONSTRAINT gis_resource_id_unique_pkey UNIQUE (id);
ALTER TABLE ONLY ohiggins.gis_resource
    ADD CONSTRAINT gis_resource_unique_access_id UNIQUE (access_id);