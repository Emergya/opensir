-- Create table gis_source
CREATE TABLE ohiggins.gis_source (
	id bigserial PRIMARY KEY,
	url character varying(256),
	name character varying(256),
	ptype character varying(256),
	config character varying(256),
	create_date timestamp without time zone,
	update_date timestamp without time zone
);

ALTER TABLE ohiggins.gis_source OWNER TO ohiggins;