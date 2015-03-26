CREATE TABLE gis_analytics_category
(
  id bigint NOT NULL,
  name character varying(255) NOT NULL,
  enabled boolean,
  CONSTRAINT gis_analytics_category_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE gis_analytics_category
  OWNER TO subdere;
