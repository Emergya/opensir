CREATE TABLE gis_analytics_downloads
(
  id bigint NOT NULL,
  identifier character varying(255) NOT NULL,
  downloads bigint,
  CONSTRAINT gis_analytics_downloads_pkey PRIMARY KEY (id)
)
WITH (
  OIDS=FALSE
);
ALTER TABLE gis_analytics_downloads
  OWNER TO subdere;
