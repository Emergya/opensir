SET search_path = ohiggins, pg_catalog;

CREATE TABLE ohiggins.gis_layer_resources (
	id bigserial PRIMARY KEY,
	table_name character varying(256),
	original_file_name character varying(256),
	create_date timestamp without time zone,
	update_date timestamp without time zone,
	authority_id bigint,
	active boolean
);

ALTER TABLE ohiggins.gis_layer_resources OWNER TO ohiggins;