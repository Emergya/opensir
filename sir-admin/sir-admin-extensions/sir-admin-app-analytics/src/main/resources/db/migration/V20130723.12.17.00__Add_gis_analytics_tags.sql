CREATE TABLE gis_analytics_tags
(
  id bigint NOT NULL,
  identifier character varying(255) NOT NULL,
  tagName character varying(255) NOT NULL,
  CONSTRAINT gis_analytics_tags_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE gis_analytics_tags
  OWNER TO subdere;
